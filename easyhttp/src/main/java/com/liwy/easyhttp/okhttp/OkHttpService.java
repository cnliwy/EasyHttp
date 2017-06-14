package com.liwy.easyhttp.okhttp;



import android.util.Log;

import com.google.gson.Gson;
import com.liwy.easyhttp.base.AbHttpService;
import com.liwy.easyhttp.callback.DownloadCallback;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
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

    public OkHttpService() {

    }

    public OkHttpService init(OkHttpClient okHttpClient) {
        OkHttpService okHttpService = new OkHttpService();
        okHttpService.okHttpClient = okHttpClient;
        if (okHttpClient == null)okHttpService = init();
        return okHttpService;
    }
    public OkHttpService init() {
        OkHttpService okHttpService = new OkHttpService();
        okHttpService.okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();;
        return okHttpService;
    }


    @Override
    public <T> void get(String url, Map<String, Object> params, final Object tag, final SuccessCallback<T> successCallback, final ErrorCallback errorCallback) {
        final Class<T> responseClass = getResultParameterClass(successCallback);
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
                Observable.empty().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (errorCallback != null)errorCallback.error(e);
                    }
                }).subscribe();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Observable.empty().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        removeCall(tag);
                        if (successCallback != null){
                            if (responseClass == String.class){
                                successCallback.success((T)response.body().string());
                            }else{
                                successCallback.success((T) new Gson().fromJson(response.body().string(),responseClass));
                            }
                        }

                    }
                }).subscribe();
            }
        });
    }


    private static final MediaType JSON=MediaType.parse("application/json;charset=utf-8");

    @Override
    public <T> void post(String url, Map<String, Object> params, final Object tag, final SuccessCallback<T> successCallback, final ErrorCallback errorCallback) {
        String content = map2json(params);
        RequestBody formBody = RequestBody.create(JSON,content);

        Request request = new Request.Builder().url(url).post(formBody).build();
        Call call = okHttpClient.newCall(request);
        addCall(tag,call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                removeCall(tag);
                if (call.isCanceled()){

                }else{
                    if (errorCallback != null)errorCallback.error(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                removeCall(tag);
                if (successCallback != null)successCallback.success((T) response.body().string());
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
        // set timeout for download task
        final Call call = okHttpClient.newBuilder().connectTimeout(1,TimeUnit.DAYS).readTimeout(1,TimeUnit.DAYS).writeTimeout(1,TimeUnit.DAYS).build().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                removeCall(tag);
                Log.e(TAG, e.toString());
                downloadCallback.onError("下载失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    long total = response.body().contentLength();
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                        downloadCallback.onProgress(total,current);
                    }
                    fos.flush();
                    downloadCallback.onSuccess((T)file);
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                    if (file.exists()){
                        file.delete();
                    }
                    downloadCallback.onError("下载异常");
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
