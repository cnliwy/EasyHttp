package com.liwy.easyhttp.interceptor;

/**
 * 拦截器
 * Created by liwy on 2017/8/10.
 */

public interface Interceptor {
    /**
     *
     * @param obj
     * @return true拦截，不继续往下执行。false 不拦截，执行成功回调
     */
    public boolean processSuccess(Object obj);


    public boolean processError(String error);
}
