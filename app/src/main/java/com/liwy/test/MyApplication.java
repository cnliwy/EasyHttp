package com.liwy.test;

import android.app.Application;

import com.liwy.easyhttp.EasyHttp;
import com.liwy.easyhttp.callback.Callback;
import com.liwy.easyhttp.callback.DataParser;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;
import com.liwy.easyhttp.okhttp.OkHttpService;
import com.liwy.easyhttp.retrofit.RetrofitService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by liwy on 2017/6/12.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // there are two implementions of IHttpService,you can initialize one which you want
        initOkHttpService();
//        initRetrofitService();
    }

    /**
     * OkHttpService,the implemention of IHttpService
     * initialize the EasyHttp with okHttpService
     */
    public void initOkHttpService(){
        // 设置默认解析方式（已实现gson和xml的数据解析）
        DataParser.setDefaultParseType(DataParser.PARSE_GSON);
        // 也可以自定义解析方式，比如解析html类型的数据
        DataParser.addCallback("html", new Callback() {
            @Override
            public void onSuccess(String result, Class<?> clazz, SuccessCallback successCallback) {
                // do parsing which you want to
            }

            @Override
            public void onError(String error, ErrorCallback errorCallback) {
                // http error
            }
        });
        // 初始化回调实现
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).build();
        OkHttpService okHttpService = new OkHttpService(okHttpClient);

        EasyHttp.getInstance().setHttpService(okHttpService);
    }

    /**
     * RetrofitService,the implemention of IHttpService
     * initialize the EasyHttp with retrofitService
     */
    public void initRetrofitService(){
        RetrofitService retrofitService = new RetrofitService().init("http://192.168.131.19:8886/",10);
//        RetrofitService retrofitService = new RetrofitService().init("http://img5q.duitang.com/uploads/",5);
        EasyHttp.getInstance().setHttpService(retrofitService);
    }
}
