package com.liwy.test;

import android.app.Application;

import com.liwy.easyhttp.EasyHttp;
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
//        initOkHttpService();
        initRetrofitService();
    }

    /**
     * OkHttpService,the implemention of IHttpService
     * initialize the EasyHttp with okHttpService
     */
    public void initOkHttpService(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).build();
        OkHttpService okHttpService = new OkHttpService().init(okHttpClient);
        EasyHttp.getInstance().setHttpService(okHttpService);
    }

    /**
     * RetrofitService,the implemention of IHttpService
     * initialize the EasyHttp with retrofitService
     */
    public void initRetrofitService(){
//        RetrofitService retrofitService = new RetrofitService().init("http://192.168.131.19:8886/",10);
        RetrofitService retrofitService = new RetrofitService().init("http://img5q.duitang.com/uploads/",5);
        EasyHttp.getInstance().setHttpService(retrofitService);
    }
}
