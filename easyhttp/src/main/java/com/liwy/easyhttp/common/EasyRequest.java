package com.liwy.easyhttp.common;


import android.content.Context;

import com.liwy.easyhttp.callback.DownloadCallback;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by liwy on 2017/6/12.
 */

public class EasyRequest<T> {
    public static final int VALUE_POST = 0;
    public static final int VALUE_GET = 1;
    public static final int VALUE_DOWNLOAD = 2;

    private OkHttpClient okHttpClient;
    RequestBody requestBody;
    private String url;// 请求url
    private Object tag;
    private Context context;
    Map<String, Object> params;//请求参数
    Map<String,String> header;// http header
    String mediaType;// mediaType  需与服务器保持一致
    List<EasyFile> uploadFiles;// 上传文件集合
    int requestType;//0post  1 get
    boolean isSync = true;//默认同步请求，false为异步请求

    String parseType;// 解析类型

    String saveDir;// 文件存储路径
    String fileName;    // 文件存储名称

    SuccessCallback<T> successCallback;
    ErrorCallback errorCallback;
    DownloadCallback<T> downloadCallback;

    public EasyRequest(Builder builder) {
        this.okHttpClient = builder.okHttpClient;
        this.url = builder.url;
        this.tag = builder.tag;
        this.params = builder.params;
        this.header = builder.headers;
        this.mediaType = builder.mediaType;
        this.uploadFiles = builder.uploadFiles;
        this.requestType = builder.requestType;
        this.isSync = builder.isSync;
        this.saveDir = builder.saveDir;
        this.fileName = builder.fileName;
        this.successCallback = builder.successCallback;
        this.errorCallback = builder.errorCallback;
        this.downloadCallback = builder.downloadCallback;
        this.requestBody = builder.requestBody;
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

    public String getMediaType() {
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

    public String getSaveDir() {
        return saveDir;
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

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestType(int type){
        this.requestType = type;
    }
    public static class Builder{
        public EasyRequest build(){
            return new EasyRequest(this);
        }
        Context context;
        OkHttpClient okHttpClient;
        RequestBody requestBody;
        String url;// 请求url
        Object tag;
        Map<String, Object> params;//请求参数
        Map<String,String> headers;// http header
        String mediaType;// mediaType  需与服务器保持一致
        List<EasyFile> uploadFiles;// 上传文件集合
        int requestType;// 0 post  1 get 2 download
        boolean isSync;//默认异步请求，为true时是同步

        String parseType;// 解析类型

        String saveDir;// 文件存储路径
        String fileName;    // 文件存储名称

       // 请求结果回调
        SuccessCallback successCallback;
        ErrorCallback errorCallback;
        DownloadCallback downloadCallback;

        /**
         * 传入context对象
         * @param context
         * @return
         */
        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        /**
         * 添加http的header信息
         * @param headers
         * @return
         */
        public Builder setHeaders(Map<String, String> headers) {
            if (this.headers == null){
                this.headers = headers;
            }else{
                this.headers.putAll(headers);
            }
            return this;
        }

        /**
         * 传入自定义的okhttpclient
         * @param okHttpClient
         * @return
         */
        public Builder setOkHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        /**
         * 设置本次请求的url
         * @param url
         * @return
         */
        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        /**
         * 设置用于取消本次请求的tag
         * @param tag
         * @return
         */
        public Builder setTag(Object tag) {
            this.tag = tag;return this;
        }

        /**
         * 添加请求参数和值
         * @param params
         * @return
         */
        public Builder setParams(Map<String, Object> params) {
            if (this.params == null){
                this.params = params;
            }else {
                this.params.putAll(params);
            }
            return this;
        }

        /**
         * 添加请求参数和值
         * @param key
         * @param value
         * @return
         */
        public Builder addParam(String key,Object value){
            if (this.params == null)this.params = new HashMap<String,Object>();
            this.params.put(key, value);
            return this;
        }

        /**
         * 添加http的header信息
         * @param key
         * @param value
         * @return
         */
        public Builder addHeader(String key,String value) {
            if (headers == null)headers = new HashMap<String,String>();
            headers.put(key,value);
            return this;
        }

        /**
         * 传入要上传的文件
         * @param uploadFiles
         * @return
         */
        public Builder setUploadFiles(List<EasyFile> uploadFiles) {
            this.uploadFiles = uploadFiles;
            return this;
        }

//        private Builder setRequestType(int requestType) {
//            this.requestType = requestType;
//            return this;
//        }

//        /**
//         * 设置本次请求为get类型
//         * @return
//         */
//        public Builder get(){
//            this.requestType = VALUE_GET;
//            return this;
//        }
//
//        /**
//         * 设置本次请求为post类型
//         * @return
//         */
//        public Builder post(){
//            this.requestType = VALUE_POST;
//            return this;
//        }
//
//        /**
//         * 本次请求为download
//         * @return
//         */
//        public Builder download(){
//            this.requestType = VALUE_DOWNLOAD;
//            return this;
//        }

        /**
         * 自定义requestbody数据，若用此方法，则setMediaTpe和setParam\setParams则不需设置
         * @param requestBody
         * @return
         */
        public Builder requestBody(RequestBody requestBody){
            this.requestBody = requestBody;
            return this;
        }

        /**
         * 设置post提交数据的mediaType类型，默认为form。需与服务器保持一致。
         * @param mediaType  form或json
         * @return
         */
        public Builder setMediaType(String mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        /**
         * 设置本次请求为同步或异步。默认为异步。
         * @param sync
         * @return
         */
        public Builder setSync(boolean sync) {
            isSync = sync;return this;
        }

        /**
         * 设置本次的数据解析方式，xml解析或者gson解析，默认gson解析
         * @param parseType  xml或者gson,也可是自定义的解析类型。
         * @return
         */
        public Builder setParseType(String parseType) {
            this.parseType = parseType;return this;
        }

        /**
         * 设置文件下载的存储路径
         * @param saveDir
         * @return
         */
        public Builder setSaveDir(String saveDir) {
            this.saveDir = saveDir;
            return this;
        }

        /**
         * 设置文件的存储清楚，不传则默认采用下载链接里的文件名称。
         * @param fileName
         * @return
         */
        public Builder setFileName(String fileName) {
            this.fileName = fileName;return this;
        }

        /**
         * 请求成功的结果回调
         * @param successCallback
         * @return
         */
        public Builder setSuccessCallback(SuccessCallback successCallback) {
            this.successCallback = successCallback;return this;
        }

        /**
         * 请求失败的结果回调
         * @param errorCallback
         * @return
         */
        public Builder setErrorCallback(ErrorCallback errorCallback) {
            this.errorCallback = errorCallback;return this;
        }

        /**
         * 下载的结果回调
         * @param downloadCallback
         * @return
         */
        public Builder setDownloadCallback(DownloadCallback downloadCallback) {
            this.downloadCallback = downloadCallback;return this;
        }
    }
}
