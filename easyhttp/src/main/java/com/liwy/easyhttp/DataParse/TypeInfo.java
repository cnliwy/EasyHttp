package com.liwy.easyhttp.DataParse;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by liwy on 2017/6/21.
 */

public class TypeInfo {
    //Type泛型对象类型
    private Class<?> componentType;
    //Type所属对象类型
    private Class<?> rawType;
    //type
    private Type type;

    private TypeInfo(Class<?> rawType, Class<?> componentType) {

        this.componentType = componentType;
        this.rawType = rawType;
    }

    public static TypeInfo createArrayType(Class<?> componentType) {
        return new TypeInfo(Array.class, componentType);
    }

    public static TypeInfo createNormalType(Class<?> componentType) {
        return new TypeInfo(null, componentType);
    }

    public static TypeInfo createParameterizedType(Class<?> rawType, Class<?> componentType) {
        return new TypeInfo(rawType, componentType);
    }

    public TypeInfo(Type type) {
        this.type = type;
        if (type instanceof ParameterizedType) {
            //返回 Type 对象，表示声明此类型的类或接口。
            this.rawType = (Class<?>) ((ParameterizedType) type).getRawType();
            //getActualTypeArguments()返回表示此类型实际类型参数的 Type 对象的数组。
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            this.componentType = (Class<?>) actualTypeArguments[0];
            // typeReference=new TypeReference<Map<componentType,componentType>>(){};

        } else if (type instanceof GenericArrayType) {
            //返回 Type 对象，表示声明此类型的类或接口。
            this.rawType = Array.class;
            // 表示一种元素类型是参数化类型或者类型变量的数组类型
            this.componentType = (Class<?>) ((GenericArrayType) type).getGenericComponentType();
        } else {
            this.componentType = (Class<?>) type;
        }
    }

    public Type getType() {
        return type;
    }

    public Class<?> getComponentType() {
        return componentType;
    }


    public Class<?> getRawType() {
        return rawType;
    }

}