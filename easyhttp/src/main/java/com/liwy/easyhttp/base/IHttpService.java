package com.liwy.easyhttp.base;


import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.DownloadCallback;
import com.liwy.easyhttp.callback.SuccessCallback;

import java.util.List;
import java.util.Map;

/**
 * Created by liwy on 2017/6/5.
 */

public interface IHttpService {

    /**
     * get method
     * @param url
     * @param params
     * @param tag
     * @param parseType  PARSE_GSON  PARSE_XML
     * @param successCallback
     * @param errorCallback
     */
    public <T> void get(String url, Map<String, Object> params,Object tag,String parseType, SuccessCallback<T> successCallback, ErrorCallback errorCallback);

    /**
     * post method
     * @param url
     * @param params
     * @param tag
     * @param parseType
     * @param contentType
     * @param successCallback
     * @param errorCallback
     */
    public <T> void post(String url, Map<String, Object> params,Object tag, String parseType,int contentType, SuccessCallback<T> successCallback, ErrorCallback errorCallback);

    /**
     * upload files
     * @param url
     * @param params
     * @param files
     * @param tag
     * @param successCallback
     * @param errorCallback
     * @param <T>
     */
    public <T> void postFile(String url, Map<String, Object> params, List<EasyFile> files, final Object tag, String parseType,final SuccessCallback<T> successCallback, final ErrorCallback errorCallback);


    /**
     * download file
     * @param fileUrl              file download url
     * @param destFileDir          file save path
     * @param fileName              file name
     * @param tag                   request tag
     * @param downloadCallback      download callback
     * @param <T>
     */
    public <T> void download(String fileUrl, String destFileDir,String fileName,Object tag, DownloadCallback<T> downloadCallback);

    /**
     * cancel the request by tag
     * @param tag
     */
    public void cancelHttp(Object tag);

}
