package com.liwy.easyhttp;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;

/**
 * 可取消缓存的网络请求抽象类
 * cache the request call which can be used to cancel the request
 * Created by liwy on 2017/6/6.
 */

public abstract class AbHttpService implements IHttpService {
    public Map<Object, Call> currentCalls = new HashMap<>();


    /**
     * cancel the request call
     * @param tag
     */
    public void cancel(Object tag) {
        Call call = currentCalls.get(tag);
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
        currentCalls.remove(tag);
    }

    /**
     * 将当前请求添加到缓存
     * add the request call to map
     * @param tag
     * @param call
     */
    public void add(Object tag, Call call) {
        currentCalls.put(tag, call);
    }

    /**
     * 结束所有请求，并清空缓存
     * finish all request and clear the map
     */
    public void clearAll(){
        Set<Object> keys = currentCalls.keySet();
        for (Object obj : keys){
            cancel(obj);
        }
    }
}
