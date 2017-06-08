package com.liwy.easyhttp;

import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;

import java.util.Map;

/**
 * Created by liwy on 2017/6/5.
 */

public interface IHttpService {
    /**
     * get method
     * @param url
     * @param params
     * @param successCallback
     * @param errorCallback
     */
    public <T> void get(String url, Map<String, Object> params, SuccessCallback<T> successCallback, ErrorCallback errorCallback);

    /**
     * post method
     * @param url
     * @param params
     * @param successCallback
     * @param errorCallback
     */
    public  void post(String url,Map<String, Object> params,SuccessCallback successCallback,ErrorCallback errorCallback);
}
