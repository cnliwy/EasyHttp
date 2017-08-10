package com.liwy.test;

import android.app.Application;

import com.liwy.easyhttp.EasyHttp;
import com.liwy.easyhttp.callback.DataProcessor;
import com.liwy.easyhttp.DataParse.DataParser;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;
import com.liwy.easyhttp.common.Constants;
import com.liwy.easyhttp.impl.OkHttpService;
import com.liwy.easyhttp.impl.RequestService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by liwy on 2017/6/12.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initOkHttpService();
    }

    /**
     * OkHttpService,the implemention of IHttpService
     * initialize the EasyHttp with okHttpService
     */
    public void initOkHttpService() {
        // 自定义解析方式，比如解析html类型的数据
        DataParser.addCallback("html", new DataProcessor() {
            @Override
            public void onSuccess(String result,SuccessCallback successCallback) {
                // do parsing which you want to
            }

            @Override
            public void onError(String error, ErrorCallback errorCallback) {
                // http error
            }
        });
        // 实例化请求实现类
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).build();
//        OkHttpService okHttpService = new OkHttpService(okHttpClient);
        RequestService requestService = new RequestService(okHttpClient);
        // 设置EasyHttp的功能实现类为okHttpService，post提交方式为form表单，数据解析方式为GSON
        // 如果post提交数据的类型既不是form也不是json，则通过EasyRequest.Builder的requestBody()传入自定义的请求体
        EasyHttp.getInstance().initHttpService(requestService, Constants.MEDIA_TYPE_FORM,DataParser.PARSE_GSON);
    }
}