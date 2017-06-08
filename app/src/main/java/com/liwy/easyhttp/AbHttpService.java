package com.liwy.easyhttp;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.OkHttpClient;
import retrofit2.Call;

/**
 * 可取消缓存的网络请求抽象类
 * cache the request call which can be used to cancel the request
 * Created by liwy on 2017/6/6.
 */

public abstract class AbHttpService implements IHttpService {
    private OkHttpClient okHttpClient;

    public Map<Object, Call> currentCalls = new HashMap<>();

    /**
     * cancel the request call
     * @param tag
     */
    public void cancel(Object tag) {
        Call call = currentCalls.get(tag);
        if (call != null && !call.isCanceled()) {
            call.cancel();
        };
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


    /**
     * 获取SuccesscCallback的泛型类型
     * @param impl  SuccesscCallback的实现
     * @return
     */
    protected Class getResultParameterClass(Object impl){
        try {
            Type[] types = impl.getClass().getGenericInterfaces();
            String className;
            if (types.length > 0){
                Type type = types[0];
                if (type instanceof ParameterizedType){
                    ParameterizedType pType = (ParameterizedType)type;
                    System.out.println(pType.getRawType().toString());
                    //取得泛型类型的泛型参数
                    Type[] tArgs = pType.getActualTypeArguments();
                    if (tArgs.length > 0){
                        Type realType = tArgs[0];
                        String typeStr = realType.toString();
                        String[] strings = typeStr.split(" ");
                        className = strings[1];
                        Class clazz =  Class.forName(className);
                        return clazz;
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
