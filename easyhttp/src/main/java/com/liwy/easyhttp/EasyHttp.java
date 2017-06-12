package com.liwy.easyhttp;

import android.content.Context;

import com.liwy.easyhttp.base.IHttpService;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;

import java.util.Map;

/**
 * Created by liwy on 2017/6/5.
 */

public class EasyHttp implements IHttpService {
    private static EasyHttp instance;
    private IHttpService httpService;
    private Context context;

    public EasyHttp() {
    }

    /**
     * get the single instance of EasyHttp
     * @return
     */
    public static EasyHttp getInstance(){
        if (instance == null){
            synchronized (EasyHttp.class){
                if (instance == null){
                    instance = new EasyHttp();
                }
                return instance;
            }
        }
        return instance;
    }


    /**
     * get method
     * @param url
     * @param params
     */
    public <T> void get(String url, Map<String,Object> params,Object tag, SuccessCallback<T> successCallback, ErrorCallback errorCallback){
        if (httpService != null)httpService.get(url,params,tag,successCallback,errorCallback);
    }


    /**
     * post method
     * @param url
     * @param params
     */
    public <T> void post(String url, Map<String,Object> params,Object tag,SuccessCallback<T> successCallback,ErrorCallback errorCallback){
        if (httpService != null)httpService.post(url,params,tag,successCallback,errorCallback);
    }

    @Override
    public void cancelHttp(Object tag) {
        if (httpService != null)httpService.cancelHttp(tag);
    }

    /**
     *  set the implementation of IHttpService
     * @param httpService OkHttpService or RetrofitService
     * @return
     */
    public EasyHttp setHttpService(IHttpService httpService) {
        this.httpService = httpService;
        return this;
    }

    /**
     * set the Context
     * @param context
     * @return
     */
    public EasyHttp setContext(Context context) {
        this.context = context;
        return this;
    }


    public static class Builder{
        private String url;
        private Object tag;
        private Map<String,Object> params;
        private SuccessCallback successCallback;
        private ErrorCallback errorCallback;

        public void get(){
            EasyHttp.getInstance().get(url,params,tag,successCallback,errorCallback);
        }

        public void post(){
            EasyHttp.getInstance().post(url,params,tag,successCallback,errorCallback);
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setParams(Map<String, Object> params) {
            this.params = params;
            return this;
        }

        public Builder setSuccessCallback(SuccessCallback successCallback) {
            this.successCallback = successCallback;
            return this;
        }

        public Builder setErrorCallback(ErrorCallback errorCallback) {
            this.errorCallback = errorCallback;
            return this;
        }

        public Builder setTag(Object tag) {
            this.tag = tag;
            return this;
        }
    }
}
