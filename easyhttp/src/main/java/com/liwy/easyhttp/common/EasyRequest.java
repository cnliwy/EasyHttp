package com.liwy.easyhttp.common;


import java.util.Map;

/**
 * Created by liwy on 2017/6/12.
 */

public class EasyRequest<T> {
    private String url;
    private Object tag;
    Map<String, Object> params;

    public static class Builder{
        private String url;
        private Object tag;
        Map<String, Object> params;
    }
}
