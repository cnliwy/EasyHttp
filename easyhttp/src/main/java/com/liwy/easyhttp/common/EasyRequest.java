package com.liwy.easyhttp.common;


import android.content.Context;

import com.liwy.easyhttp.callback.DownloadCallback;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * Created by liwy on 2017/6/12.
 */

public class EasyRequest<T> {
    public static final int VALUE_GET = 1;
    public static final int VALUE_POST = 0;
    private OkHttpClient okHttpClient;
    private String url;// 请求url
    private Object tag;
    private Context context;
    Map<String, Object> params;//请求参数
    Map<String,String> header;// http header
    int mediaType;// mediaType  需与服务器保持一致
    List<EasyFile> uploadFiles;// 上传文件集合
    int requestType;//0post  1 get
    boolean isSync = true;//默认同步请求，false为异步请求

    String parseType;// 解析类型

    String destFileDir;// 文件存储路径
    String fileName;    // 文件存储名称

    SuccessCallback<T> successCallback;
    ErrorCallback errorCallback;
    DownloadCallback<T> downloadCallback;

    public EasyRequest(Builder builder) {
        this.okHttpClient = builder.okHttpClient;
        this.url = builder.url;
        this.tag = builder.url;
        this.params = builder.params;
        this.header = builder.headers;
        this.mediaType = builder.mediaType;
        this.uploadFiles = builder.uploadFiles;
        this.requestType = builder.requestType;
        this.isSync = builder.isSync;
        this.destFileDir = builder.destFileDir;
        this.fileName = builder.fileName;
        this.successCallback = builder.successCallback;
        this.errorCallback = builder.errorCallback;
        this.downloadCallback = builder.downloadCallback;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public String getUrl() {
        return url;
    }

    public Object getTag() {
        return tag;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public String getParseType() {
        return parseType;
    }

    public int getMediaType() {
        return mediaType;
    }

    public List<EasyFile> getUploadFiles() {
        return uploadFiles;
    }

    public int getRequestType() {
        return requestType;
    }

    public boolean isSync() {
        return isSync;
    }

    public String getDestFileDir() {
        return destFileDir;
    }

    public String getFileName() {
        return fileName;
    }

    public SuccessCallback<T> getSuccessCallback() {
        return successCallback;
    }

    public ErrorCallback getErrorCallback() {
        return errorCallback;
    }

    public DownloadCallback<T> getDownloadCallback() {
        return downloadCallback;
    }

    public static class Builder{
        public EasyRequest build(){
            return new EasyRequest(this);
        }

        private Context context;
        private OkHttpClient okHttpClient;
        private String url;// 请求url
        private Object tag;
        Map<String, Object> params;//请求参数
        Map<String,String> headers;// http header
        int mediaType;// mediaType  需与服务器保持一致
        List<EasyFile> uploadFiles;// 上传文件集合
        int requestType;// 0 post  1 get
        boolean isSync;//默认异步请求，为true时是同步

        String parseType;// 解析类型

        String destFileDir;// 文件存储路径
        String fileName;    // 文件存储名称

        SuccessCallback successCallback;
        ErrorCallback errorCallback;
        DownloadCallback downloadCallback;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder setOkHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setTag(Object tag) {
            this.tag = tag;return this;
        }

        public Builder setParams(Map<String, Object> params) {
            this.params = params;
            return this;
        }

        public Builder addParam(String key,Object value){
            if (this.params == null)this.params = new HashMap<String,Object>();
            this.params.put(key, value);
            return this;
        }

        public Builder addHeader(String key,String value) {
            if (headers == null)headers = new HashMap<String,String>();
            headers.put(key,value);
            return this;
        }

        public Builder setMediaType(int mediaType) {
            this.mediaType = mediaType;return this;
        }

        public Builder setUploadFiles(List<EasyFile> uploadFiles) {
            this.uploadFiles = uploadFiles;return this;
        }

        public Builder setRequestType(int requestType) {
            this.requestType = requestType;return this;
        }

        public Builder setSync(boolean sync) {
            isSync = sync;return this;
        }

        public Builder setParseType(String parseType) {
            this.parseType = parseType;return this;
        }

        public Builder setDestFileDir(String destFileDir) {
            this.destFileDir = destFileDir;return this;
        }

        public Builder setFileName(String fileName) {
            this.fileName = fileName;return this;
        }

        public Builder setSuccessCallback(SuccessCallback successCallback) {
            this.successCallback = successCallback;return this;
        }

        public Builder setErrorCallback(ErrorCallback errorCallback) {
            this.errorCallback = errorCallback;return this;
        }

        public Builder setDownloadCallback(DownloadCallback downloadCallback) {
            this.downloadCallback = downloadCallback;return this;
        }
    }
}
