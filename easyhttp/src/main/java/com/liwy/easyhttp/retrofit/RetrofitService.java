package com.liwy.easyhttp.retrofit;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.liwy.easyhttp.base.AbHttpService;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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
        if (tag != null)addCall(tag,call);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, final Response<JsonObject> response) {
                removeCall(tag);
                Observable.empty().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (successCallback != null){
                            if (responseClass == String.class){
                                successCallback.success((T)response.body().toString());
                            }else{
                                successCallback.success((T) new Gson().fromJson(response.body().toString(),responseClass));
                            }
                        }

                    }
                }).subscribe();
            }

            @Override
            public void onFailure(Call<JsonObject> call, final Throwable t) {
                System.out.println("失败了哦");
                removeCall(tag);
                Observable.empty().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (errorCallback != null)errorCallback.error(t);
                    }
                }).subscribe();
            }
        });
    }

    @Override
    public <T> void post(String url, Map<String, Object> params, final Object tag, final SuccessCallback<T> successCallback, final ErrorCallback errorCallback) {
        if (params == null) params = new HashMap<>();
        final Class<T> responseClass = getResultParameterClass(successCallback);
        Call<JsonObject> call = retrofitService.post(url,params);
        if (tag != null)addCall(tag,call);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, final Response<JsonObject> response) {
                removeCall(tag);
                Observable.empty().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (successCallback != null){
                            if (responseClass == String.class){
                                successCallback.success((T)response.body().toString());
                            }else{
                                successCallback.success((T) new Gson().fromJson(response.body().toString(),responseClass));
                            }
                        }

                    }
                }).subscribe();
            }

            @Override
            public void onFailure(Call<JsonObject> call, final Throwable t) {
                if (call.isCanceled()){

                }else{
                    removeCall(tag);
                    Observable.empty().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnComplete(new Action() {
                        @Override
                        public void run() throws Exception {
                            if (errorCallback != null)errorCallback.error(t);
                        }
                    }).subscribe();
                }
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
