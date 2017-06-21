package com.liwy.easyhttp.callback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liwy on 2017/6/21.
 */

public class CallbackManager {
    public final static String PARSE_GSON = "gson";
    public final static String PARSE_XML = "xml";

    private static Map<String, Callback> callbackMap = new HashMap<>();
    private static String defaultParseType = PARSE_GSON;

    public static Map<String, Callback> getCallbackMap() {
        return callbackMap;
    }

    public static String getDefaultParseType() {
        return defaultParseType;
    }

    /**
     * add the callback to map
     * @param key
     * @param callback
     */
    public static void addCallback(String key,com.liwy.easyhttp.callback.Callback callback) {
        callbackMap.put(key,callback);
    }

    public static void remove(String key){
        callbackMap.remove(key);
    }


    /**
     * set the default type to parse the data
     * @param type
     */
    public static void setDefaultParseType(String type){
        defaultParseType = type;
    }

}
