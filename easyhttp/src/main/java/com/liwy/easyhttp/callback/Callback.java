package com.liwy.easyhttp.callback;

/**
 * Created by liwy on 2017/6/21.
 */

public interface Callback {
    public void onSuccess(String result,Class<?> clazz,SuccessCallback successCallback);
    public void onError(String error,ErrorCallback errorCallback);
}
