package com.liwy.easyhttp.service;


import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.DownloadCallback;
import com.liwy.easyhttp.callback.SuccessCallback;
import com.liwy.easyhttp.common.EasyFile;
import com.liwy.easyhttp.common.EasyRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by liwy on 2017/6/5.
 */

public interface IHttpService {
    public <T> void http(final EasyRequest req);
    public <T> void download(final EasyRequest req);
    public <T> void upload(final EasyRequest req);

//    public <T> void get(String url, Map<String, Object> params,Object tag,String parseType, SuccessCallback<T> successCallback, ErrorCallback errorCallback);
//    public <T> void post(String url, Map<String, Object> params,Object tag, String parseType,int contentType, SuccessCallback<T> successCallback, ErrorCallback errorCallback);
//    public <T> void postFile(String url, Map<String, Object> params, List<EasyFile> files, final Object tag, String parseType, final SuccessCallback<T> successCallback, final ErrorCallback errorCallback);
//    public <T> void download(String fileUrl, String destFileDir,String fileName,Object tag, DownloadCallback<T> downloadCallback);



    /**
     * cancel the request by tag
     * @param tag
     */
    public void cancelHttp(Object tag);
    public void addCall(Object tag,Object call);

}
