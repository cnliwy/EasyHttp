package com.liwy.easyhttp.okhttp;


import android.util.Log;

import com.liwy.easyhttp.DataParse.ReqClassUtils;
import com.liwy.easyhttp.DataParse.TypeInfo;
import com.liwy.easyhttp.DataParse.TypeUtils;
import com.liwy.easyhttp.base.AbHttpService;
import com.liwy.easyhttp.base.EasyFile;
import com.liwy.easyhttp.base.MainThread;
import com.liwy.easyhttp.callback.DataParser;
import com.liwy.easyhttp.callback.DownloadCallback;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import static android.content.ContentValues.TAG;


/**
 * Created by liwy on 2017/6/5.
 */

public class OkHttpService extends AbHttpService {
    public OkHttpClient okHttpClient;
    private MainThread mainThread = new MainThread();

    public OkHttpService(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }



    @Override
    public <T> void get(String url, Map<String, Object> params, final Object tag, final String parseType, final SuccessCallback<T> successCallback, final ErrorCallback errorCallback) {
        final Class<?> responseClass = successCallback.rawType;
        System.out.println(responseClass.getName());
        String realUrl = makeGetUrl(url,params);//generate get url

        Request request = new Request.Builder().url(realUrl).get().build();
        Call call = okHttpClient.newCall(request);
        addCall(tag,call);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                System.out.println("okhttp service 失败了");
                removeCall(tag);
                mainThread.execute(new Runnable() {
                    @Override
                    public void run() {
                        String type = "";
                        if (parseType != null && !"".equals(parseType))type = parseType;
                        else type = DataParser.getDefaultParseType();
                        DataParser.getCallbackMap().get(type).onError(e.toString(),errorCallback);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                    removeCall(tag);
                    final String content = response.body().string();
                    mainThread.execute(new Runnable() {
                        @Override
                        public void run() {
                            String type = "";
                            if (parseType != null && !"".equals(parseType))type = parseType;
                            else type = DataParser.getDefaultParseType();
                            DataParser.getCallbackMap().get(type).onSuccess(content,responseClass,successCallback);
//                            if (successCallback != null){
//                                if (responseClass == String.class){
//                                    if (successCallback != null)
//                                        successCallback.success((T)content);
//                                }else{
//                                    if (successCallback != null) successCallback.success((T) new Gson().fromJson(content,responseClass));
//                                }
//                            }
                        }
                    });
            }
        });
    }


    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    @Override
    public <T> void post(String url, Map<String, Object> params, final Object tag, final String parseType, final SuccessCallback<T> successCallback, final ErrorCallback errorCallback) {
        final Class<T> responseClass = getResultParameterClass(successCallback);
        String content = map2json(params);
        RequestBody formBody = RequestBody.create(JSON,content);

        Request request = new Request.Builder().url(url).post(formBody).build();
        Call call = okHttpClient.newCall(request);
        addCall(tag,call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                removeCall(tag);
                if (!call.isCanceled()){
                    mainThread.execute(new Runnable() {
                        @Override
                        public void run() {
                            String type = "";
                            if (parseType != null && !"".equals(parseType))type = parseType;
                            else type = DataParser.getDefaultParseType();
                            DataParser.getCallbackMap().get(type).onError(e.toString(),errorCallback);
//                            if (errorCallback != null)errorCallback.error(e);
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                removeCall(tag);
                final String content = response.body().string();
                mainThread.execute(new Runnable() {
                    @Override
                    public void run() {
                        String type = "";
                        if (parseType != null && !"".equals(parseType))type = parseType;
                        else type = DataParser.getDefaultParseType();
                        DataParser.getCallbackMap().get(type).onSuccess(content,responseClass,successCallback);
//                        if (successCallback != null)successCallback.success((T) content);
                    }
                });

            }
        });
    }

    public <T> void postFile(String url, Map<String, Object> params, List<EasyFile> files, final Object tag, final String parseType, final SuccessCallback<T> successCallback, final ErrorCallback errorCallback){
        final Class<T> responseClass = getResultParameterClass(successCallback);

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        // add file
        for (int i = 0; i <files.size() ; i++) {
            EasyFile easyFile = files.get(i);
            File f = easyFile.getFile();
            MediaType mediaType = MediaType.parse(easyFile.getMediaType());
            if (f!=null) {
                if (easyFile.getFileName() == null || "".equals(easyFile.getFileName()))easyFile.setFileName(f.getName());
                builder.addFormDataPart(easyFile.getRequestKey(), f.getName(), RequestBody.create(mediaType, f));
            }
        }
        //add the form data
        if (params != null && params.size() > 0){
            Set<String> keys = params.keySet();
            for (String key : keys){
                builder.addFormDataPart(key,String.valueOf(params.get(key)));
            }
        }

        MultipartBody requestBody = builder.build();
        //构建请求
        Request request = new Request.Builder()
                .url(url)//地址
                .post(requestBody)//添加请求体
                .build();
        // 上传文件耗时较大，需加长超时时间
        okHttpClient.newBuilder().connectTimeout(1,TimeUnit.DAYS).readTimeout(1,TimeUnit.DAYS).writeTimeout(1,TimeUnit.DAYS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mainThread.execute(new Runnable() {
                    @Override
                    public void run() {
                        String type = "";
                        if (parseType != null && !"".equals(parseType))type = parseType;
                        else type = DataParser.getDefaultParseType();
                        DataParser.getCallbackMap().get(type).onError(e.toString(),errorCallback);
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
                        if (parseType != null && !"".equals(parseType))type = parseType;
                        else type = DataParser.getDefaultParseType();
                        DataParser.getCallbackMap().get(type).onSuccess(content,responseClass,successCallback);
                    }
                });
            }
        });
    }

    @Override
    public <T> void download(String fileUrl, String destFileDir, String fileName, final Object tag, final DownloadCallback<T> downloadCallback) {
        if (fileName == null || "".equals(fileName))fileName = fileUrl;
        final File file = new File(destFileDir, fileName);
        if (file.exists()) {
            System.out.println("file has already exists!");
            downloadCallback.onSuccess((T)file);
            return;
        }
        final Request request = new Request.Builder().addHeader("","1000").url(fileUrl).build();
        // 下载文件耗时较大，需加长超时时间
        // set timeout for download task
        final Call call = okHttpClient.newBuilder().connectTimeout(1,TimeUnit.DAYS).readTimeout(1,TimeUnit.DAYS).writeTimeout(1,TimeUnit.DAYS).build().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                removeCall(tag);
                mainThread.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (downloadCallback != null)downloadCallback.onError("download faied!");
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
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                        final long finalCurrent = current;
                        mainThread.execute(new Runnable() {
                            @Override
                            public void run() {
                                if (downloadCallback != null)downloadCallback.onProgress(total, finalCurrent);
                            }
                        });

                    }
                    fos.flush();
                    mainThread.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (downloadCallback != null)downloadCallback.onSuccess((T)file);
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
                            if (downloadCallback != null)downloadCallback.onError("下载异常:" + e.toString());
                        }
                    });

                } finally {
                    removeCall(tag);
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

    @Override
    public void cancelHttp(Object tag) {
        Call call = (Call)calls.get(tag);
        if (tag != null && call != null){
            if (!call.isCanceled())call.cancel();
            calls.remove(tag);
        }
    }

    /**
     * convert map to json string
     * @param params
     * @return
     */
    private static String map2json(Map<String,Object> params){
        JSONObject jsonObject = new JSONObject();
        if (params != null){
            Set<String> keys = params.keySet();
            if (!keys.isEmpty()){
                for (String key : keys){
                    try {
                        jsonObject.put(key,params.get(key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return jsonObject.toString();
            }
        }
        return jsonObject.toString();
    }

    /**
     * generate the url which is get request
     * @param url
     * @param params
     * @return
     */
    private String makeGetUrl(String url,Map<String,Object> params){
        StringBuffer sb = new StringBuffer(url);
        if (params != null && params.size() > 0){
            sb.append("?");
            Set<String> keys = params.keySet();
            for (String key : keys){
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();
    }
}
