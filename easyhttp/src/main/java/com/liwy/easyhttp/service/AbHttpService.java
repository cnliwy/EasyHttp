package com.liwy.easyhttp.service;

import java.util.HashMap;
import java.util.Map;

/**
 * 可取消缓存的网络请求抽象类
 * cache the request call which can be used to cancel the request
 * Created by liwy on 2017/6/6.
 */

public abstract class AbHttpService implements IHttpService {
    protected Map<Object, Object> calls = new HashMap<>();

    /**
     * add the request to cache
     * @param tag
     * @param call Call
     */
    protected void addCall(Object tag,Object call){
        if (tag != null && call != null)calls.put(tag, call);
    }

    /**
     * remove the call when request is successful or failed
     * @param tag
     */
    protected void removeCall(Object tag) {
        if (tag != null)calls.remove(tag);
    }
}
