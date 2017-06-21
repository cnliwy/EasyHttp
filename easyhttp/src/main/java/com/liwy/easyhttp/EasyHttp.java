package com.liwy.easyhttp;

import android.content.Context;

import com.liwy.easyhttp.base.EasyFile;
import com.liwy.easyhttp.base.IHttpService;
import com.liwy.easyhttp.callback.DownloadCallback;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;

import java.util.List;
import java.util.Map;

/**
 * Created by liwy on 2017/6/5.
 */

public class EasyHttp implements IHttpService {
    private static EasyHttp instance;
    private IHttpService httpService;
    private Context context;

    protected EasyHttp() {

    }

    public static Builder getBuilder(){
        return new Builder();
    }

    /**
     * get the single instance of EasyHttp
     * @return
     */
    public static EasyHttp getInstance(){
        if (instance == null){
            synchronized (EasyHttp.class){
                if (instance == null){
                    instance = new EasyHttp();
                }
                return instance;
            }
        }
        return instance;
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
    public <T> void post(String url, Map<String,Object> params,Object tag,String parseType, SuccessCallback<T> successCallback,ErrorCallback errorCallback){
        if (httpService != null)httpService.post(url,params,tag,parseType,successCallback,errorCallback);
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

    /**
     *  set the implementation of IHttpService
     * @param httpService OkHttpService or RetrofitService
     * @return
     */
    public EasyHttp setHttpService(IHttpService httpService) {
        this.httpService = httpService;
        return this;
    }

    /**
     * set the Context
     * @param context
     * @return
     */
    public EasyHttp setContext(Context context) {
        this.context = context;
        return this;
    }


    public static class Builder{
        private String url;         // request url
        private Object tag;
        private String fileUrl;     // download url
        private String filePath;
        private String fileName;
        private String parseType;
        private List<EasyFile> files;
        private Map<String,Object> params;
        private SuccessCallback successCallback;
        private ErrorCallback errorCallback;
        private DownloadCallback downloadCallback;

        public void get(){
            EasyHttp.getInstance().get(url,params,tag,parseType,successCallback,errorCallback);
        }

        public void post(){
            EasyHttp.getInstance().post(url,params,tag,parseType,successCallback,errorCallback);
        }
        public void postFile(){
            EasyHttp.getInstance().postFile(url,params,files,tag,parseType,successCallback,errorCallback);
        }
        public void download(){
            EasyHttp.getInstance().download(fileUrl,filePath,fileName,tag,downloadCallback);
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setParams(Map<String, Object> params) {
            this.params = params;
            return this;
        }

        public Builder setSuccessCallback(SuccessCallback successCallback) {
            this.successCallback = successCallback;
            return this;
        }

        public Builder setErrorCallback(ErrorCallback errorCallback) {
            this.errorCallback = errorCallback;
            return this;
        }

        public Builder setTag(Object tag) {
            this.tag = tag;
            return this;
        }

        public Builder setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
            return this;
        }

        public Builder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder setDownloadCallback(DownloadCallback downloadCallback) {
            this.downloadCallback = downloadCallback;
            return this;
        }

        public Builder setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder setParseType(String parseType) {
            this.parseType = parseType;
            return this;
        }

        public Builder setFiles(List<EasyFile> files) {
            this.files = files;
            return this;
        }
    }
}
