package com.liwy.easyhttp.base;


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
     * @param tag
     * @param successCallback
     * @param errorCallback
     */
    public <T> void get(String url, Map<String, Object> params,Object tag, SuccessCallback<T> successCallback, ErrorCallback errorCallback);

    /**
     * post method
     * @param url
     * @param params
     * @param tag
     * @param successCallback
     * @param errorCallback
     */
    public <T> void post(String url, Map<String, Object> params,Object tag,  SuccessCallback<T> successCallback, ErrorCallback errorCallback);

    /**
     * cancel the request by tag
     * @param tag
     */
    public void cancelHttp(Object tag);

}
