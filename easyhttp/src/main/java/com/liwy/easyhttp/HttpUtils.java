package com.liwy.easyhttp;

import android.content.Context;


import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;

import java.util.Map;

/**
 * Created by liwy on 2017/6/5.
 */

public class HttpUtils{
    private static HttpUtils instance;
    private IHttpService httpService;
    private Context context;

    public HttpUtils() {
    }

    /**
     * get the single instance of HttpUtils
     * @return
     */
    public static HttpUtils getInstance(){
        if (instance == null){
            synchronized (HttpUtils.class){
                if (instance == null){
                    instance = new HttpUtils();
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
    public <T> void get(String url, Map<String,Object> params, SuccessCallback<T> successCallback, ErrorCallback errorCallback){
        if (httpService != null)httpService.get(url,params,successCallback,errorCallback);
    }


    /**
     * post method
     * @param url
     * @param params
     */
    public void post(String url, Map<String,Object> params,SuccessCallback successCallback,ErrorCallback errorCallback){
        if (httpService != null)httpService.post(url,params,successCallback,errorCallback);
    }


    /**
     *  set the implementation of IHttpService
     * @param httpService OkHttpService or RetrofitService
     * @return
     */
    public HttpUtils setHttpService(IHttpService httpService) {
        this.httpService = httpService;
        return this;
    }

    /**
     * set the Context
     * @param context
     * @return
     */
    public HttpUtils setContext(Context context) {
        this.context = context;
        return this;
    }


    public static class Builder{
        private String url;
        private String tag;
        private boolean showDialog;
        private Map<String,Object> params;
        private SuccessCallback successCallback;
        private ErrorCallback errorCallback;

        public void get(){
            HttpUtils.getInstance().get(url,params,successCallback,errorCallback);
        }

        public void post(){
            HttpUtils.getInstance().post(url,params,successCallback,errorCallback);
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

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder setShowDialog(boolean showDialog) {
            this.showDialog = showDialog;
            return this;
        }
    }
}
