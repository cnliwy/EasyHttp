package com.liwy.easyhttp.interceptor;

import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;

/**
 * 拦截器
 * Created by liwy on 2017/8/10.
 */

public interface Interceptor {
    /**
     *
     * @param obj
     * @param successCallback
     * @return true拦截，不继续往下执行。false 不拦截，执行成功回调
     */
    public boolean processSuccess(Object obj,SuccessCallback successCallback);


    public boolean processError(String error,ErrorCallback errorCallback);
}
