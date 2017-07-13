package com.liwy.easyhttp;

import com.liwy.easyhttp.common.EasyFile;
import com.liwy.easyhttp.service.IHttpService;
import com.liwy.easyhttp.callback.DownloadCallback;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;

import java.util.List;
import java.util.Map;

/**
 * 代理类
 * 代理OkHttpSerivce,即IHttpService的实现类
 * Created by liwy on 2017/7/13.
 */

public class HttpProxyService implements IHttpService {
    private IHttpService httpService;

    public HttpProxyService(IHttpService httpService) {
        this.httpService = httpService;
    }

    /**
     * get method
     * @param url
     * @param params
     */
    public <T> void get(String url, Map<String,Object> params,Object tag,String parseType, SuccessCallback<T> successCallback, ErrorCallback errorCallback){
        if (httpService != null)httpService.get(url,params,tag,parseType,successCallback,errorCallback);
    }


    /**
     * post method
     * @param url
     * @param params
     */
    public <T> void post(String url, Map<String,Object> params,Object tag,String parseType,int contentType,SuccessCallback<T> successCallback,ErrorCallback errorCallback){
        if (httpService != null)httpService.post(url,params,tag,parseType,contentType, successCallback,errorCallback);
    }

    @Override
    public <T> void postFile(String url, Map<String, Object> params, List<EasyFile> files, Object tag,String parseType,  SuccessCallback<T> successCallback, ErrorCallback errorCallback) {
        if (httpService != null)httpService.postFile(url,params,files,tag,parseType,successCallback,errorCallback);
    }

    @Override
    public <T> void download(String fileUrl, String destFileDir,String fileName,Object tag, DownloadCallback<T> downloadCallback) {
        if (fileUrl == null)throw new NullPointerException("fileUrl can't be null!");
        if (httpService != null)httpService.download(fileUrl,destFileDir,fileName,tag,downloadCallback);
    }

    @Override
    public void cancelHttp(Object tag) {
        if (httpService != null)httpService.cancelHttp(tag);
    }

    public IHttpService getHttpService() {
        return httpService;
    }

    public void setHttpService(IHttpService httpService) {
        this.httpService = httpService;
    }
}
