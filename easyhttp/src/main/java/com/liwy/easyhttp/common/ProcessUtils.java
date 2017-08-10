package com.liwy.easyhttp.common;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;

/**
 * Created by liwy on 2017/8/9.
 */

public class ProcessUtils {
    /**
     * convert map to json string
     * @param params
     * @return
     */
    public static String map2json(Map<String,Object> params){
        JSONObject jsonObject = new JSONObject();
        if (params != null){
            Set<String> keys = params.keySet();
            if (!keys.isEmpty()){
                for (String key : keys){
                    try {
                        jsonObject.put(key,params.get(key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return jsonObject.toString();
            }
        }
        return jsonObject.toString();
    }

    /**
     * convert map to FormBody
     * @param params
     * @return
     */
    public static FormBody map2form(Map<String,Object> params){

        if (params != null){
            Set<String> keys = params.keySet();
            if (!keys.isEmpty()){
                FormBody.Builder builder = new FormBody.Builder();
                for (String key : keys){
                    builder.add(key,String.valueOf(params.get(key)));
                }
                return builder.build();
            }
        }
        return null;
    }

    /**
     * generate the url which is get request
     * @param url
     * @param params
     * @return
     */
    public static String makeGetUrl(String url,Map<String,Object> params){
        StringBuffer sb = new StringBuffer(url);
        if (params != null && params.size() > 0){
            sb.append("?");
            Set<String> keys = params.keySet();
            for (String key : keys){
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();
    }
}
