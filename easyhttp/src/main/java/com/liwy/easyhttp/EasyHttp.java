package com.liwy.easyhttp;

import com.liwy.easyhttp.DataParse.DataParser;
import com.liwy.easyhttp.callback.DataProcessor;
import com.liwy.easyhttp.common.Constants;
import com.liwy.easyhttp.common.EasyRequest;
import com.liwy.easyhttp.interceptor.Interceptor;
import com.liwy.easyhttp.service.IHttpService;

/**
 * Created by liwy on 2017/6/5.
 */

public class EasyHttp  implements IHttpService{
    private static EasyHttp instance;
    private IHttpService httpService;

    protected EasyHttp() {

    }

    public static EasyRequest.Builder getBuilder(){
        return new EasyRequest.Builder();
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
    @Override
    public void cancelHttp(Object tag){
        getHttpService().cancelHttp(tag);
    }

    @Override
    public void addCall(Object tag,Object call){
        getHttpService().addCall(tag,call);
    }

    @Override
    public <T> void get(EasyRequest req) {
        if (httpService != null)httpService.get(req);
    }

    @Override
    public <T> void post(EasyRequest req) {
        if (httpService != null)httpService.post(req);
    }

    @Override
    public <T> void download(EasyRequest req) {
        if (httpService != null)httpService.download(req);
    }

    @Override
    public <T> void upload(EasyRequest req) {
        if (httpService != null)httpService.upload(req);
    }

    public IHttpService getHttpService() {
        return httpService;
    }

    /**
     *  set the implementation of IHttpService
     * @param httpService OkHttpService 或者其他实现IHttpService的类
     * @param mediaType 数据提交方式：MEDIA_TYPE_FORM  MEDIA_TYPE_JSON
     * @param parseType 数据解析方式 PARSE_GSON PARSE_XML
     * @return
     */
    public EasyHttp initHttpService(IHttpService httpService,int mediaType,String parseType) {
        this.httpService = httpService;
        Constants.defaultMediaType = mediaType;
        if(parseType != null && !"".equals(parseType) && DataParser.getCallbackMap().get(parseType) != null)DataParser.setDefaultParseType(parseType);
        return this;
    }

    /**
     * 初始化是否打印日志
     * @param isLog
     * @return
     */
    public EasyHttp isLog(boolean isLog){
        Constants.isLog = isLog;
        return this;
    }

    /**
     * 初始化是否执行拦截器
     * @param isIntercept
     * @return
     */
    public EasyHttp isIntercept(boolean isIntercept){
        Constants.isIntercept = isIntercept;
        return this;
    }

    // 新增自定义数据解析
    public void addDataParse(String typekey,DataProcessor dataProcessor){
        DataParser.addCallback(typekey,dataProcessor);
    }
    // 新增拦截器
    public EasyHttp addIntercepor(Interceptor interceptor){
        DataParser.addInterceptor(interceptor);
        return this;
    }

    // 删除拦截器
    public  void removeInterceptor(Interceptor interceptor){
        DataParser.removeInterceptor(interceptor);
    }

}
