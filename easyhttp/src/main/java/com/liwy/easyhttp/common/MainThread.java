package com.liwy.easyhttp.common;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * it's used to process ui when http response has been received
 * Created by liwy on 2017/6/16.
 */

public class MainThread implements Executor{
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(@NonNull Runnable r) {
        handler.post(r);
    }
}
