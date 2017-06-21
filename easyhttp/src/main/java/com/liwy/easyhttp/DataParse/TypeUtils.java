package com.liwy.easyhttp.DataParse;

/**
 * Created by liwy on 2017/6/21.
 */


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class TypeUtils {

    //基本类型映射关系Map
    private static final Map primitiveWrapperTypeMap = new HashMap(8);

    static {
        //添加基本类型
        primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
        primitiveWrapperTypeMap.put(Byte.class, byte.class);
        primitiveWrapperTypeMap.put(Character.class, char.class);
        primitiveWrapperTypeMap.put(Double.class, double.class);
        primitiveWrapperTypeMap.put(Float.class, float.class);
        primitiveWrapperTypeMap.put(Integer.class, int.class);
        primitiveWrapperTypeMap.put(Long.class, long.class);
        primitiveWrapperTypeMap.put(Short.class, short.class);
    }

    /**
     * 将JSON字符串转换成指定的用户返回值类型
     *
     * @param type
     * @param jsonData
     * @return
     * @throws JSONException
     */
    public static <T> T parseHttpResult(TypeInfo type, String jsonData) throws JSONException {
        // 处理Void类型的返回值
        if (Void.class.isAssignableFrom(type.getComponentType())) {
            return null;
        }
        //获取当前type的数据类型
        Class<?> rawType = type.getRawType();
        //是否是Array
        boolean isArray = rawType != null && Array.class.isAssignableFrom(rawType);
        //是否是Collection
        boolean isCollection = rawType != null && Collection.class.isAssignableFrom(rawType);
        //是否是Map
        boolean isMap = rawType != null && Map.class.isAssignableFrom(rawType);
        //获取泛型类型
        Class<?> componentType = type.getComponentType();
        //声明结果对象
        T result = null;
//        if (isCollection) {//处理collection
//            result = (T) JSON.parseArray(jsonData, componentType);
//        } else if (isArray) {//处理array
//            result = (T) JSON.parseArray(jsonData, componentType).toArray();
//        } else if (isMap) {//处理Map
//            result = (T) JSONObject.parseObject(jsonData, type.getType());
//        } else if (componentType.isAssignableFrom(String.class)) {//处理字符串返回值
//            return (T) jsonData;
//        } else {
//            // 接口的返回类型如果是简单类型，则会封装成为一个json对象，真正的对象存储在value属性上
//            if (isPrimitiveOrWrapper(componentType)) {
//                result = (T) parseObject(jsonData);
//            } else {
//                //处理自定义对象
//                result = (T) parseObject(jsonData, componentType);
//            }
//        }
        return result;
    }

    public static Class<?> getResponseClass(TypeInfo type){
        // 处理Void类型的返回值
        if (Void.class.isAssignableFrom(type.getComponentType())) {
            return null;
        }
        //获取当前type的数据类型
        Class<?> rawType = type.getRawType();
        //是否是Array
        boolean isArray = rawType != null && Array.class.isAssignableFrom(rawType);
        //是否是Collection
        boolean isCollection = rawType != null && Collection.class.isAssignableFrom(rawType);
        //是否是Map
        boolean isMap = rawType != null && Map.class.isAssignableFrom(rawType);
        //获取泛型类型
        Class<?> componentType = type.getComponentType();
        if (isArray || isCollection)componentType = rawType;
        return componentType;
    }

    /**
     * 判断是否是基本数据类型
     *
     * @param clazz
     * @return
     */
    public static boolean isPrimitiveOrWrapper(Class clazz) {
        return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
    }

    /**
     * 判断是否是基本数据类型
     *
     * @param clazz
     * @return
     */
    public static boolean isPrimitiveWrapper(Class clazz) {
        return primitiveWrapperTypeMap.containsKey(clazz);
    }
}