package com.liwy.easyhttp.impl;

import android.util.Log;

import com.liwy.easyhttp.DataParse.DataParser;
import com.liwy.easyhttp.common.Constants;
import com.liwy.easyhttp.common.EasyFile;
import com.liwy.easyhttp.common.EasyRequest;
import com.liwy.easyhttp.common.MainThread;
import com.liwy.easyhttp.common.ProcessUtils;
import com.liwy.easyhttp.service.IHttpService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by liwy on 2017/8/9.
 */

public class RequestService implements IHttpService{
    private final static String TAG = "EasyHttp";
    //MediaType 这个需要和服务端保持一致
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    private OkHttpClient okHttpClient;
    protected MainThread mainThread = new MainThread();

    protected Map<Object, Object> calls = new HashMap<>();

    public RequestService(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @Override
    public <T> void get(EasyRequest req) {
        req.setRequestType(EasyRequest.VALUE_GET);
        OkHttpClient okHttpClient = processOkHttpClient(req);
        RequestBody requestBody = processRequestBody(req);
        Request request = processRequest(req,requestBody);
        Call call = okHttpClient.newCall(request);
        processHttp(req,call);
    }

    @Override
    public <T> void post(EasyRequest req) {
        req.setRequestType(EasyRequest.VALUE_POST);
        OkHttpClient okHttpClient = processOkHttpClient(req);
        RequestBody requestBody = processRequestBody(req);
        Request request = processRequest(req,requestBody);
        Call call = processCall(req.getTag(),okHttpClient,request);
        processHttp(req,call);
    }

    /**
     * 上传
     * @param req
     * @param <T>
     */
    @Override
    public <T> void upload(final EasyRequest req){
        String uploadMediaType = "application/octet-stream";
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        List<EasyFile> files = req.getUploadFiles();
        // add file
        for (int i = 0; i <files.size() ; i++) {
            EasyFile easyFile = files.get(i);
            File f = easyFile.getFile();
//            MediaType mediaType = MediaType.parse(easyFile.getMediaType());
            MediaType mediaType = MediaType.parse(uploadMediaType);
            if (f!=null) {
                if (easyFile.getFileName() == null || "".equals(easyFile.getFileName()))easyFile.setFileName(f.getName());
                builder.addFormDataPart(easyFile.getRequestKey(), f.getName(), RequestBody.create(mediaType, f));
            }
        }
        //add the form data
        Map<String,Object> params = req.getParams();
        if (params != null && params.size() > 0){
            Set<String> keys = params.keySet();
            for (String key : keys){
                builder.addFormDataPart(key,String.valueOf(params.get(key)));
            }
        }

        MultipartBody requestBody = builder.build();
        //构建请求
        Request request = new Request.Builder()
                .url(req.getUrl())//地址
                .post(requestBody)//添加请求体
                .build();
        // 上传文件耗时较大，需加长超时时间
        okHttpClient.newBuilder().connectTimeout(1, TimeUnit.DAYS).readTimeout(1,TimeUnit.DAYS).writeTimeout(1,TimeUnit.DAYS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mainThread.execute(new Runnable() {
                    @Override
                    public void run() {
                        String type = "";
                        if (req.getParseType() != null && !"".equals(req.getParseType()))type = req.getParseType();
                        else type = DataParser.getDefaultParseType();
                        DataParser.getCallbackMap().get(type).onError(e.toString(),req.getErrorCallback());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String content = response.body().string();
                mainThread.execute(new Runnable() {
                    @Override
                    public void run() {
                        String type = "";
                        if (req.getParseType() != null && !"".equals(req.getParseType()))type = req.getParseType();
                        else type = DataParser.getDefaultParseType();
                        DataParser.getCallbackMap().get(type).onSuccess(content,req.getSuccessCallback());
                    }
                });
            }
        });
    }

    /**
     * 下载
     * @param req
     * @param <T>
     */
    @Override
    public <T> void download(final EasyRequest req){
        OkHttpClient okHttpClient = processOkHttpClient(req);
        String fileName = req.getFileName();
        if (fileName == null || "".equals(fileName))fileName = ProcessUtils.getNameFromUrl(req.getUrl());
        final File file = new File(req.getSaveDir(), fileName);
        if (file.exists()) {
            System.out.println("file has already exists!");
            req.getDownloadCallback().onSuccess((T)file);
            return;
        }
        final Request request = new Request.Builder().url(req.getUrl()).build();
        // 下载文件耗时较大，需加长超时时间
        // set timeout for download task
        final Call call = okHttpClient.newBuilder().connectTimeout(1,TimeUnit.DAYS).readTimeout(1,TimeUnit.DAYS).writeTimeout(1,TimeUnit.DAYS).build().newCall(request);
        addCall(req.getTag(),call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                removeCall(req.getTag());
                mainThread.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (req.getDownloadCallback() != null)req.getDownloadCallback().onError("download faied!");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    final long total = response.body().contentLength();
                    long current = 0;
                    int currentProgress = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                        final int progress = (int) (current * 1.0f / total * 100);
                        if (progress != currentProgress){
                            currentProgress = progress;
                            mainThread.execute(new Runnable() {
                                @Override
                                public void run() {
                                    if (req.getDownloadCallback() != null)req.getDownloadCallback().onProgress(total, progress);
                                }
                            });
                        }
                    }
                    fos.flush();
                    mainThread.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (req.getDownloadCallback() != null)req.getDownloadCallback().onSuccess((T)file);
                        }
                    });

                } catch (final IOException e) {
                    Log.e(TAG, e.toString());
                    if (file.exists()){
                        file.delete();
                    }
                    mainThread.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (req.getDownloadCallback() != null)req.getDownloadCallback().onError("下载异常:" + e.toString());
                        }
                    });

                } finally {
                    removeCall(req.getTag());
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }
        });
    }

    /**
     * add the request to cache
     * @param tag
     * @param call Call
     */
    @Override
    public void addCall(Object tag,Object call){
        if (tag != null && !"".equals(tag) && call != null)calls.put(tag, call);
    }

    @Override
    public void cancelHttp(Object tag) {
        System.out.println("点击了取消");
        if (calls.get(tag) != null){
            Call call = (Call)calls.get(tag);
            call.cancel();
            removeCall(tag);
        }
    }

    /**
     * remove the call when request is successful or failed
     * @param tag
     */
    protected void removeCall(Object tag) {
        if (tag != null)calls.remove(tag);
    }


    /**
     * 组装ReuqestBody，仅为post请求时。
     * @param req
     * @return
     */
    private RequestBody processRequestBody(EasyRequest req){
        RequestBody requestBody = req.getRequestBody();
        if (requestBody!=null) System.out.println("自定义请求内容");
        if (requestBody != null)return requestBody;
        if (req.getRequestType() == EasyRequest.VALUE_POST ){
            if (req.getMediaType() != null && !"".equals(req.getMediaType())){
                if ("json".equals(req.getMediaType())){
                    String content = ProcessUtils.map2json(req.getParams());
                    requestBody = RequestBody.create(JSON,content);
                }else if ("form".equals(req.getMediaType())){
                    requestBody = ProcessUtils.map2form(req.getParams());
                }else{
                    throw new NullPointerException("当前无"+ req.getMediaType() + "此类型的post提交");
                }
            }else{
                if ( Constants.defaultMediaType == Constants.MEDIA_TYPE_JSON){
                    String content = ProcessUtils.map2json(req.getParams());
                    requestBody = RequestBody.create(JSON,content);
                }else if( Constants.defaultMediaType == Constants.MEDIA_TYPE_FORM){
                    requestBody = ProcessUtils.map2form(req.getParams());
                }
            }
        }
        return requestBody;
    }

    /**
     * 组装Reuqest
     * @param req
     * @param requestBody
     * @return
     */
    private Request processRequest(EasyRequest req,RequestBody requestBody){
        Request request = null;
        Request.Builder builder = new Request.Builder();
        if (req.getRequestType() == EasyRequest.VALUE_GET){
            String getUrl = ProcessUtils.makeGetUrl(req.getUrl(),req.getParams());
            builder.url(getUrl).get();
        }else{
            builder.url(req.getUrl()).post(requestBody);
        }
        // 组装header
        Map<String,String> headers = req.getHeader();
        if (headers != null && headers.size() > 0){
            Set<String> keySet = headers.keySet();
            for (String key : keySet){
                builder.addHeader(key,headers.get(key));
            }
        }
        request = builder.build();
        return request;
    }

    /**
     * 组装Call
     * @param request
     * @return
     */
    private Call processCall(Object tag,OkHttpClient okHttpClient,Request request){
        Call call = okHttpClient.newCall(request);
        addCall(tag,call);
        return  call;
    }

    /**
     * 发起请求
     * @param req
     * @param call
     */
    private void processHttp(final EasyRequest req, Call call){
        String type = req.getParseType();
        if (type == null || "".equals(type))type = DataParser.getDefaultParseType();
        final String parseType = type;
        // 同步
        if (req.isSync()){
            try {
                final Response response = call.execute();
                removeCall(req.getTag());
                if (response != null && response.isSuccessful()){
                    final String content = response.body().string();
                    if (req.isLog())Log.e(TAG, "success---->" + content );
                    DataParser.getCallbackMap().get(type).onSuccess(content,req.getSuccessCallback());
                }else{
                    DataParser.getCallbackMap().get(parseType).onError("网络请求失败",req.getErrorCallback());
                }
            } catch (IOException e) {
                e.printStackTrace();
                removeCall(req.getTag());
                if (req.isLog())Log.e(TAG, "error---->" + e.getMessage() );
                DataParser.getCallbackMap().get(type).onError("网络请求失败",req.getErrorCallback());
            }
        }else{
            // 异步
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    removeCall(req.getTag());
                    if (req.isLog())Log.e(TAG, "error---->" + e.getMessage() );
                    mainThread.execute(new Runnable() {
                        @Override
                        public void run() {
                            DataParser.getCallbackMap().get(parseType).onError(e.toString(),req.getErrorCallback());
                        }
                    });
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    removeCall(req.getTag());
                    final String content = response.body().string();
                    if (req.isLog())Log.e(TAG, "success---->" + content );
                    mainThread.execute(new Runnable() {
                        @Override
                        public void run() {
                            DataParser.getCallbackMap().get(parseType).onSuccess(content,req.getSuccessCallback());
                        }
                    });
                }
            });
        }
    }

    /**
     * 组装OkHttpCliet
     * @param req
     * @return
     */
    private OkHttpClient processOkHttpClient(EasyRequest req){
        if (req.getOkHttpClient() != null){
            return req.getOkHttpClient();
        }
        return okHttpClient;
    }


}
