package com.liwy.easyhttp;

import android.content.Context;

import com.liwy.easyhttp.DataParse.DataParser;
import com.liwy.easyhttp.common.Constants;
import com.liwy.easyhttp.common.EasyFile;
import com.liwy.easyhttp.service.IHttpService;
import com.liwy.easyhttp.callback.DownloadCallback;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;

import java.util.List;
import java.util.Map;

import static android.R.attr.type;

/**
 * Created by liwy on 2017/6/5.
 */

public class EasyHttp  {
    private static EasyHttp instance;
    private HttpProxyService httpService;
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
     * 取消请求
     * @param tag
     */
    public void cancelHttp(Object tag){
        getHttpService().cancelHttp(tag);
    }


    private HttpProxyService getHttpService() {
        return httpService;
    }

    /**
     *  set the implementation of IHttpService
     * @param httpService OkHttpService 或者其他实现IHttpService的类
     * @param contentType CONTENT_TYPE_FORM   CONTENT_TYPE_JSON
     * @param parseType 数据解析方式 PARSE_GSON PARSE_XML
     * @return
     */
    public EasyHttp initHttpService(IHttpService httpService,int contentType,String parseType) {
        this.httpService = new HttpProxyService(httpService);
        Constants.defaultPostType = contentType;
        if(parseType != null && !"".equals(parseType) && DataParser.getCallbackMap().get(parseType) != null)DataParser.setDefaultParseType(parseType);
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
        private int contentType;
        private List<EasyFile> files;
        private Map<String,Object> params;
        private SuccessCallback successCallback;
        private ErrorCallback errorCallback;
        private DownloadCallback downloadCallback;

        public void get(){
            EasyHttp.getInstance().getHttpService().get(url,params,tag,parseType,successCallback,errorCallback);
        }

        public void post(){
            EasyHttp.getInstance().getHttpService().post(url,params,tag,parseType,contentType, successCallback,errorCallback);
        }
        public void postFile(){
            EasyHttp.getInstance().getHttpService().postFile(url,params,files,tag,parseType,successCallback,errorCallback);
        }
        public void download(){
            EasyHttp.getInstance().getHttpService().download(fileUrl,filePath,fileName,tag,downloadCallback);
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

        /**
         * 可选，不需要中途取消则不用设置
         * @param tag
         * @return
         */
        public Builder setTag(Object tag) {
            this.tag = tag;
            return this;
        }

        /**
         * 设置文件下载url
         * @param fileUrl
         * @return
         */
        public Builder setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
            return this;
        }

        /**
         * 设置文件的存储名称
         * @param fileName
         * @return
         */
        public Builder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder setDownloadCallback(DownloadCallback downloadCallback) {
            this.downloadCallback = downloadCallback;
            return this;
        }

        /**
         * 设置文件的下载路径
         * @param filePath
         * @return
         */
        public Builder setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        /**
         * 设置response数据的解析方式，gson/xml等等
         * @param parseType
         * @return
         */
        public Builder setParseType(String parseType) {
            this.parseType = parseType;
            return this;
        }

        public Builder setFiles(List<EasyFile> files) {
            this.files = files;
            return this;
        }

        /**
         * 设置post请求的content-type. form或者json
         * @param contentType
         * @return
         */
        public Builder setContentType(int contentType) {
            this.contentType = contentType;
            return this;
        }
    }
}
