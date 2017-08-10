package com.liwy.easyhttp.service;

import com.liwy.easyhttp.common.MainThread;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * 可取消缓存的网络请求抽象类
 * cache the request call which can be used to cancel the request
 * Created by liwy on 2017/6/6.
 */

public abstract class AbHttpService implements IHttpService {
    public OkHttpClient okHttpClient;
    protected MainThread mainThread = new MainThread();

    protected Map<Object, Object> calls = new HashMap<>();

    /**
     * add the request to cache
     * @param tag
     * @param call Call
     */
    public void addCall(Object tag,Object call){
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
