package com.liwy.easyhttp.DataParse;

import android.util.Xml;

import com.google.gson.Gson;
import com.liwy.easyhttp.callback.DataProcessor;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;
import com.liwy.easyhttp.interceptor.Interceptor;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;
import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liwy on 2017/6/21.
 */

public class DataParser {
    public final static String PARSE_GSON = "gson";
    public final static String PARSE_XML = "xml";
    private static List<Interceptor> interceptors = new ArrayList<Interceptor>();

    private static Map<String, DataProcessor> callbackMap = new HashMap<>();
    private static String defaultParseType = PARSE_GSON;
    // 实现默认的两种解析方式（GSON和XML）
    static {
        // 添加GSON解析
        DataParser.addCallback(DataParser.PARSE_GSON,new DataProcessor() {
            @Override
            public void onSuccess(String result,SuccessCallback successCallback) {
                if (successCallback != null){
                    if (successCallback.mType == String.class || successCallback.mType == null){
//                        successCallback.success(result);
                        executeSuccessInterceptor(result,successCallback);
                    }else{
                        try {
//                            successCallback.success(new Gson().fromJson(result,successCallback.mType));
                            executeSuccessInterceptor(new Gson().fromJson(result,successCallback.mType),successCallback);
                        } catch (Exception e) {
                            e.printStackTrace();
//                            successCallback.success(null);
                            executeSuccessInterceptor(null,successCallback);
                        }
                    }
                }
            }

            @Override
            public void onError(String error, ErrorCallback errorCallback) {
//                if (errorCallback != null)errorCallback.error(error);
                if (errorCallback != null)executeErrorInterceptor(error,errorCallback);
            }
        });
        // 添加xml解析
        DataParser.addCallback(DataParser.PARSE_XML, new DataProcessor() {
            @Override
            public void onSuccess(String result, SuccessCallback successCallback) {
                if (successCallback != null){
                    if (successCallback.rawType == String.class || successCallback.rawType == null){
                        if (successCallback != null)
//                            successCallback.success(result);
                        executeSuccessInterceptor(result,successCallback);
                    }else{
                        if (successCallback != null)
                            executeSuccessInterceptor(getXMLObject(convertToParseContent(result),successCallback.rawType),successCallback);
//                            successCallback.success(getXMLObject(convertToParseContent(result),successCallback.rawType));
                    }
                }
            }

            @Override
            public void onError(String error, ErrorCallback errorCallback) {
                if (errorCallback != null)executeErrorInterceptor(error,errorCallback);
            }
        });
    }

    /**
     * 添加拦截器
     * @param interceptor
     */
    public static void addInterceptor(Interceptor interceptor){
        interceptors.add(interceptor);
    }

    /**
     * 删除拦截器
     * @param interceptor
     */
    public static void removeInterceptor(Interceptor interceptor){
        interceptors.remove(interceptor);
    }

    /**
     * 请求成功后执行拦截器
     * @param obj
     * @param successCallback
     */
    private static void executeSuccessInterceptor(Object obj,SuccessCallback successCallback){
        if (interceptors != null && interceptors.size() > 0){
            for (Interceptor interceptor : interceptors){
                if (!interceptor.processSuccess(obj)){
                    successCallback.success(obj);
                }
            }
        }else{
            successCallback.success(obj);
        }
    }

    /**
     * 请求失败后执行拦截器
     * @param error
     * @param errorCallback
     */
    private static void executeErrorInterceptor(String error,ErrorCallback errorCallback){
        if (interceptors != null && interceptors.size() > 0){
            for (Interceptor interceptor : interceptors){
                if (!interceptor.processError(error)){
                    errorCallback.error(error);
                }
            }
        }else{
            errorCallback.error(error);
        }
    }

    public static Map<String, DataProcessor> getCallbackMap() {
        return callbackMap;
    }

    public static String getDefaultParseType() {
        return defaultParseType;
    }

    /**
     * add the dataProcessor to map
     * @param typekey   a alias of data parse
     * @param dataProcessor
     */
    public static void addCallback(String typekey,DataProcessor dataProcessor) {
        callbackMap.put(typekey, dataProcessor);
    }

    public static void remove(String key){
        callbackMap.remove(key);
    }


    /**
     * set the default type to parse the data
     * @param typeKey PARSE_GSON  PARSE_XML
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
    public static String convertToParseContent(String result) {
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
