package com.liwy.easyhttp.callback;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by liwy on 2017/6/5.
 */

public abstract class SuccessCallback<T> implements SuccessListener<T>{
    public Class<? super T> rawType;
    public Type mType;
    public Gson gson;
    public TypeAdapter<T> adapter;
    public SuccessCallback() {
        this.mType = getSuperclassTypeParameter(getClass());
        this.rawType = (Class<? super T>) $Gson$Types.getRawType(mType);
        gson = new Gson();
        adapter = (TypeAdapter<T>)gson.getAdapter(TypeToken.get(this.mType));
    }

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

}
