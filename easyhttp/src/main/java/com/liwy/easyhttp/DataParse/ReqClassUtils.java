package com.liwy.easyhttp.DataParse;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by liwy on 2017/6/21.
 */

public class ReqClassUtils {

    public static TypeInfo getCallbackGenericType(Class<?> clazz) {
        //获得带有泛型的父类
        Type genericSuperclass = clazz.getGenericSuperclass();//Type是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型。
        TypeInfo type = getGetnericType(genericSuperclass);
        if (type == null) {
            Type[] genericInterfaces = clazz.getGenericInterfaces();
            if (genericInterfaces != null && genericInterfaces.length > 0) {
                type = getGetnericType(genericInterfaces[0]);
            }
        }
        return type;
    }

    private static TypeInfo getGetnericType(Type type) {
        if (type != null && type instanceof ParameterizedType) {
            //getActualTypeArguments获取参数化类型的数组，泛型可能有多个
            Type[] args = ((ParameterizedType) type).getActualTypeArguments();
            if (args != null && args.length > 0) {
                return new TypeInfo(args[0]);
            }
        }
        return null;
    }
}