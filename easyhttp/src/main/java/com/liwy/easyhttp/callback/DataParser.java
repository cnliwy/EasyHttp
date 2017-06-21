package com.liwy.easyhttp.callback;

import android.util.Xml;

import com.google.gson.Gson;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;
import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liwy on 2017/6/21.
 */

public class DataParser {
    public final static String PARSE_GSON = "gson";
    public final static String PARSE_XML = "xml";

    private static Map<String, Callback> callbackMap = new HashMap<>();
    private static String defaultParseType = PARSE_GSON;
    // 实现默认的两种解析方式（GSON和XML）
    static {
        // 添加GSON解析
        DataParser.addCallback(DataParser.PARSE_GSON,new Callback() {
            @Override
            public void onSuccess(String result,Class<?> responseClass, SuccessCallback successCallback) {
                if (successCallback != null){
                    if (responseClass == String.class || responseClass == null){
                        successCallback.success(result);
                    }else{
                        try {
                            successCallback.success( successCallback.adapter.fromJson(result));
                        } catch (IOException e) {
                            e.printStackTrace();
                            successCallback.success(null);
                        }
                    }
                }
            }

            @Override
            public void onError(String error, ErrorCallback errorCallback) {
                if (errorCallback != null)errorCallback.error(error);
            }
        });
        // 添加xml解析
        DataParser.addCallback(DataParser.PARSE_XML, new Callback() {
            @Override
            public void onSuccess(String result, Class<?> responseClass, SuccessCallback successCallback) {
                if (successCallback != null){
                    if (responseClass == String.class || responseClass == null){
                        if (successCallback != null)
                            successCallback.success(result);
                    }else{
                        if (successCallback != null) successCallback.success(getXMLObject(convertToEcrfContent(result),responseClass));
                    }
                }
            }

            @Override
            public void onError(String error, ErrorCallback errorCallback) {
                if (errorCallback != null)errorCallback.error(error);
            }
        });
    }

    public static Map<String, Callback> getCallbackMap() {
        return callbackMap;
    }

    public static String getDefaultParseType() {
        return defaultParseType;
    }

    /**
     * add the callback to map
     * @param typekey   a alias of data parse
     * @param callback
     */
    public static void addCallback(String typekey,com.liwy.easyhttp.callback.Callback callback) {
        callbackMap.put(typekey,callback);
    }

    public static void remove(String key){
        callbackMap.remove(key);
    }


    /**
     * set the default type to parse the data
     * @param typeKey
     */
    public static void setDefaultParseType(String typeKey){
        defaultParseType = typeKey;
    }


    public static <T> T getXMLObject(String result, Class<?> clazz) {
        if (clazz.equals(String.class)) {
            int startResult = result.indexOf(">") + 1;
            int endResult = result.indexOf("</");
            return (T)result.substring(startResult, endResult);
        }

        Object o = null;

        Strategy strategy = new AnnotationStrategy();
        Serializer serializer = new Persister(strategy);
        try {
            o = serializer.read(clazz, result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return (T)o;
    }

    //WebService返回的xml结果
    public static String convertToEcrfContent(String result) {
        String mResponseBody = "";
        try {
            InputStream xmlIs = new ByteArrayInputStream(result.getBytes("UTF-8"));
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setInput(xmlIs, "UTF-8");
            int eventType = xmlPullParser.getEventType();
            int i = 0;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // 实体字符没有转换所以只起到去掉头部作用
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        mResponseBody = xmlPullParser.nextText();
                        break;
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
            return mResponseBody;
        } catch (UnsupportedEncodingException exp) {
            throw new RuntimeException("解析数据过程出现错误:" + exp.toString());
        } catch (RuntimeException exp) {
            throw exp;
        } catch (Exception exp) {
            throw new RuntimeException(exp.toString());
        }
    }

}
