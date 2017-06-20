package com.liwy.easyhttp.retrofit;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.liwy.easyhttp.base.AbHttpService;
import com.liwy.easyhttp.base.EasyFile;
import com.liwy.easyhttp.callback.DownloadCallback;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * Created by liwy on 2017/6/5.
 */

public class RetrofitService extends AbHttpService {
    private IRetrofitService retrofitService;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;

    public RetrofitService(IRetrofitService retrofitService, OkHttpClient okHttpClient, Retrofit retrofit) {
        this.retrofitService = retrofitService;
        this.okHttpClient = okHttpClient;
        this.retrofit = retrofit;
    }

    public RetrofitService() {
    }
    public RetrofitService init(Retrofit retrofit,OkHttpClient okHttpClient){
        if (retrofit == null)throw new NullPointerException("retrofit can't be null!");
        if (okHttpClient == null)throw new NullPointerException("okHttpClient can't be null");
        IRetrofitService retrofitService = retrofit.create(IRetrofitService.class);
        return new RetrofitService(retrofitService,okHttpClient,retrofit);
    }

    public RetrofitService init(String url,int timeOut) {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(timeOut, TimeUnit.SECONDS);
        OkHttpClient okHttpClient = httpClientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        IRetrofitService retrofitService = retrofit.create(IRetrofitService.class);
        return new RetrofitService(retrofitService,okHttpClient,retrofit);
    }

    @Override
    public <T> void get(String url, Map<String, Object> params, final Object tag, final SuccessCallback<T> successCallback, final ErrorCallback errorCallback) {
        if (params == null) params = new HashMap<>();
        final Class<T> responseClass = getResultParameterClass(successCallback);
        Call<JsonObject> call = retrofitService.get(url,params);
        addCall(tag,call);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, final Response<JsonObject> response) {
                removeCall(tag);
                        if (successCallback != null){
                            if (responseClass == String.class){
                                successCallback.success((T)response.body().toString());
                            }else{
                                successCallback.success((T) new Gson().fromJson(response.body().toString(),responseClass));
                            }
                        }
            }

            @Override
            public void onFailure(Call<JsonObject> call, final Throwable t) {
                System.out.println("失败了哦");
                removeCall(tag);
                if (errorCallback != null)errorCallback.error(t);
            }
        });
    }
    @Override
    public <T> void post(String url, Map<String, Object> params, final Object tag, final SuccessCallback<T> successCallback, final ErrorCallback errorCallback) {
        if (params == null) params = new HashMap<>();
        final Class<T> responseClass = getResultParameterClass(successCallback);
        Call<JsonObject> call = retrofitService.post(url,params);
        addCall(tag,call);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, final Response<JsonObject> response) {
                removeCall(tag);
                 if (successCallback != null && response != null){
                     if (responseClass == String.class){
                         successCallback.success((T)response.body());
                     }else{
                         successCallback.success((T) new Gson().fromJson(response.body(),responseClass));
                     }
                 }
            }

            @Override
            public void onFailure(Call<JsonObject> call, final Throwable t) {
                if (call.isCanceled()){

                }else{
                    removeCall(tag);
                    if (errorCallback != null)errorCallback.error(t);
                }
            }
        });
    }

    @Override
    public <T> void postFile(String url, Map<String, Object> params, List<EasyFile> files, Object tag, SuccessCallback<T> successCallback, ErrorCallback errorCallback) {

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
        OkHttpClient client = okHttpClient.newBuilder().connectTimeout(1,TimeUnit.DAYS).readTimeout(1,TimeUnit.DAYS).writeTimeout(1,TimeUnit.DAYS).build();
        Retrofit newRetrofit = retrofit.newBuilder().client(client).build();
        IRetrofitService retrofitService = newRetrofit.create(IRetrofitService.class);
        Call<ResponseBody> call = retrofitService.download(fileUrl);
        addCall(tag,call);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                removeCall(tag);
                downloadCallback.onError("下载失败");
            }
        });
    }

    @Override
    public void cancelHttp(Object tag) {
        Call call = (Call) calls.get(tag);
        if (call != null){
            if (!call.isCanceled())call.cancel();
            calls.remove(tag);
            System.out.println("retrofit 取消http请求");
        }
    }
}
