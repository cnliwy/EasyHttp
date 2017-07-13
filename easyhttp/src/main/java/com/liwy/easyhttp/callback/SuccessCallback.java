package com.liwy.easyhttp.callback;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by liwy on 2017/6/5.
 */

public abstract class SuccessCallback<T> implements SuccessListener<T>{
    public Class<? super T> rawType;// T generic type
    public Type mType;//T type
    public SuccessCallback() {
        //GSON解析方式
//        this.mType = getSuperclassTypeParameter(getClass());
//        this.rawType = (Class<? super T>) $Gson$Types.getRawType(mType);
//        gson = new Gson();
//        adapter = (TypeAdapter<T>)gson.getAdapter(TypeToken.get(this.mType));

        // 自己解析方式
        this.mType = getParameterType(getClass());
        this.rawType = (Class<? super T>)getRawType(mType);
        if (mType != null)System.out.println("mType = " + mType.toString());
        if (rawType != null)System.out.println("rawType = " + rawType.toString());

    }
    static Type getParameterType(Class<?> subclass){
        Type superClass = subclass.getGenericSuperclass();
        if (superClass instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType)superClass;
            Type[] types = parameterizedType.getActualTypeArguments();
            if (types != null && types.length > 0){
                return types[0];
            }
        }
        return null;
    }

    static Type getRawType(Type type){
        if (type != null){
            if (type instanceof ParameterizedType){
                ParameterizedType parameterizedType = (ParameterizedType)type;
                return parameterizedType.getRawType();
            }else if (type instanceof Class<?>){
                // successCallback里不是类型化参数的清空下
             return (Class<?>)type;
            }
        }
        return null;
    }

    // GSON 解析方式
//    static Type getSuperclassTypeParameter(Class<?> subclass) {
//        System.out.println("successCallback subclass = " + subclass.toString());
//        Type superclass = subclass.getGenericSuperclass();
//        System.out.println("successCallback superclass = " + superclass.toString());
//        if (superclass instanceof Class) {
//            throw new RuntimeException("Missing type parameter.");
//        }
//        ParameterizedType parameterized = (ParameterizedType) superclass;
//        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
//    }
}
