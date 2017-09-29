package com.liwy.easyhttp.common;

import static com.liwy.easyhttp.DataParse.DataParser.PARSE_GSON;

/**
 * 全局配置类
 * Created by liwy on 2017/7/13.
 */

public class Constants {
    // post提交方式
    public final static int MEDIA_TYPE_FORM = 0;//默认post提交方式：表单提交
    public final static int MEDIA_TYPE_JSON = 1;// json方式提交

    // 默认的post请求MediaType
    public static int defaultMediaType = 0;
    // 默认的数据解析方式
    public static String defaultParseType = PARSE_GSON;
    // 是否打印日志，默认不打印
    public static boolean isLog = false;
    // 是否执行拦截器(全局),默认执行
    public static boolean isIntercept = true;
}
