package com.liwy.test;

import android.app.Application;
import android.util.Xml;

import com.google.gson.Gson;
import com.liwy.easyhttp.EasyHttp;
import com.liwy.easyhttp.callback.Callback;
import com.liwy.easyhttp.callback.CallbackManager;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;
import com.liwy.easyhttp.okhttp.OkHttpService;
import com.liwy.easyhttp.retrofit.RetrofitService;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;
import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
        // 初始化回调实现
        // 添加GSON解析
        CallbackManager.addCallback(CallbackManager.PARSE_GSON,new Callback() {
            @Override
            public void onSuccess(String result,Class<?> responseClass, SuccessCallback successCallback) {
                if (successCallback != null){
                    if (responseClass == String.class){
                        if (successCallback != null)
                            successCallback.success(result);
                    }else{
                        if (successCallback != null) successCallback.success( new Gson().fromJson(result,responseClass));
                    }
                }
            }

            @Override
            public void onError(String error, ErrorCallback errorCallback) {
                if (errorCallback != null)errorCallback.error(error);
            }
        });
        // 添加xml解析
        CallbackManager.addCallback(CallbackManager.PARSE_XML, new Callback() {
            @Override
            public void onSuccess(String result, Class<?> responseClass, SuccessCallback successCallback) {
                if (successCallback != null){
                    if (responseClass == String.class){
                        if (successCallback != null)
                            successCallback.success(result);
                    }else{
                        if (successCallback != null) successCallback.success(getXMLObject(convertToEcrfContent(result),responseClass));
                    }
                }
            }

            @Override
            public void onError(String error, ErrorCallback errorCallback) {
                if (errorCallback != null)errorCallback.error(error);
            }
        });
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

    public static <T> T getXMLObject(String result, Class<?> clazz) {
        if (clazz.equals(String.class)) {
            int startResult = result.indexOf(">") + 1;
            int endResult = result.indexOf("</");
            return (T)result.substring(startResult, endResult);
        }

        Object o = null;

        Strategy strategy = new AnnotationStrategy();
        Serializer serializer = new Persister(strategy);
        try {
            o = serializer.read(clazz, result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return (T)o;
    }

    //WebService返回的结果
    public String convertToEcrfContent(String result) {
        String mResponseBody = "";
        try {
            InputStream xmlIs = new ByteArrayInputStream(result.getBytes("UTF-8"));
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setInput(xmlIs, "UTF-8");
            int eventType = xmlPullParser.getEventType();
            int i = 0;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // 实体字符没有转换所以只起到去掉头部作用
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        mResponseBody = xmlPullParser.nextText();
                        break;
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
            return mResponseBody;
        } catch (UnsupportedEncodingException exp) {
            throw new RuntimeException("解析数据过程出现错误:" + exp.toString());
        } catch (RuntimeException exp) {
            throw exp;
        } catch (Exception exp) {
            throw new RuntimeException(exp.toString());
        }
    }
}
