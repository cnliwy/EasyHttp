package com.liwy.easyhttp.service;


import com.liwy.easyhttp.common.EasyRequest;

/**
 * Created by liwy on 2017/6/5.
 */

public interface IHttpService {
    public <T> void get(final EasyRequest req);
    public <T> void post(final EasyRequest req);
    public <T> void download(final EasyRequest req);
    public <T> void upload(final EasyRequest req);

//    public <T> void get(String url, Map<String, Object> params,Object tag,String parseType, OnSuccessCallback<T> successCallback, OnErrorCallback errorCallback);
//    public <T> void post(String url, Map<String, Object> params,Object tag, String parseType,int contentType, OnSuccessCallback<T> successCallback, OnErrorCallback errorCallback);
//    public <T> void postFile(String url, Map<String, Object> params, List<EasyFile> files, final Object tag, String parseType, final OnSuccessCallback<T> successCallback, final OnErrorCallback errorCallback);
//    public <T> void download(String fileUrl, String destFileDir,String fileName,Object tag, OnDownloadCallback<T> downloadCallback);



    /**
     * cancel the request by tag
     * @param tag
     */
    public void cancelHttp(Object tag);
    public void addCall(Object tag,Object call);

}
