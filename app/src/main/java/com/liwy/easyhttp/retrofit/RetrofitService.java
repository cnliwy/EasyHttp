package com.liwy.easyhttp.retrofit;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.liwy.easyhttp.AbHttpService;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.os.Build.VERSION_CODES.N;

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

    public RetrofitService init() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(10, TimeUnit.SECONDS);
        OkHttpClient okHttpClient = httpClientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.131.19:8886/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        IRetrofitService retrofitService = retrofit.create(IRetrofitService.class);
        return new RetrofitService(retrofitService,okHttpClient,retrofit);
    }

    @Override
    public <T> void get(String url, Map<String, Object> params, final SuccessCallback<T> successCallback,final ErrorCallback errorCallback) {
        if (params == null) params = new HashMap<>();
        final Class<T> tClass = getResultParameterClass(successCallback);
        retrofitService.get(url,params).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<JsonObject>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull JsonObject s) {
                System.out.println("retrofit请求成功" + s);
                if (successCallback != null)successCallback.success((T)new Gson().fromJson(s,tClass));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("请求onError" + e.toString());
                if (errorCallback != null)errorCallback.error(e);
            }

            @Override
            public void onComplete() {
            }
        });
    }

    @Override
    public void post(String url, Map<String, Object> params, final SuccessCallback successCallback, final ErrorCallback errorCallback) {
        if (params == null) params = new HashMap<>();
        retrofitService.post(url,params).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<JsonObject>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull JsonObject s) {
                System.out.println("请求成功" + s.toString());
                if (successCallback != null)successCallback.success(s.toString());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("请求onError" + e.toString());
                if (errorCallback != null)errorCallback.error(e);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public OkHttpClient okHttpClient() {
        return okHttpClient;
    }
}
