package com.liwy.easyhttp.callback;

import com.liwy.easyhttp.common.EasyRequest;

/**
 * Created by liwy on 2017/6/21.
 */

public interface DataProcessor {
//    public void onSuccess(String result,SuccessCallback successCallback,ErrorCallback errorCallback);
//    public void onError(Exception e,String errorMsg,ErrorCallback errorCallback);

    /**
     * 请求成功的回调
     * @param result 请求成功后的body内容
     * @param req   自定义请求体
     */
    public void onSuccess(String result,EasyRequest req);

    /**
     * 请求失败后的内容（请求失败及解析失败）
     * @param e 异常信息
//     * @param errorMsg 错误信息
     * @param req 自定义请求体
     */
    public void onError(Exception e,EasyRequest req);
}
