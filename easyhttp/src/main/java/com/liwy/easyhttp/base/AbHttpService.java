package com.liwy.easyhttp.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
