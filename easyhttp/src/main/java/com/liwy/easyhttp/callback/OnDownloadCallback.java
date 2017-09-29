package com.liwy.easyhttp.callback;

/**
 * Created by liwy on 2017/6/14.
 */

public interface OnDownloadCallback<T> {
    public void onSuccess(T t);
    public void onError(String err);
    public void onProgress(long total,int progress);
}
