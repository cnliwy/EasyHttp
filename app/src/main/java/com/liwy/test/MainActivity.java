package com.liwy.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.liwy.easyhttp.EasyHttp;
import com.liwy.easyhttp.base.EasyFile;
import com.liwy.easyhttp.callback.DownloadCallback;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;
import com.liwy.easyhttp.okhttp.OkHttpService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static android.os.Build.VERSION_CODES.N;
import static android.os.Environment.getExternalStorageDirectory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    TextView contentTv;
    Button getBtn;
    Button postBtn;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getBtn = (Button)findViewById(R.id.btn_get);
        postBtn = (Button)findViewById(R.id.btn_post);
        nextBtn = (Button)findViewById(R.id.btn_next);
        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get();
            }
        });
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NextActivity.class);
                startActivity(intent);
            }
        });
        contentTv = (TextView)findViewById(R.id.tv_content);
    }

    /**
     * if the IHttpService Implement is RetrofitService
     * setUrl("/login/update")
     *
     * if OkHttpService
     * setUrl("http://192.168.131.192:8886/login/update")
     */
    public void get(){
        // 参数
        Map<String,Object> params = new HashMap<>();
        params.put("ver","1");
        EasyHttp.getBuilder()
//                .setUrl("/login/update")
                .setUrl("http://192.168.131.19:8886/login/update")
                .setTag("get")
                .setParams(params)
                .setSuccessCallback(new SuccessCallback<String>() {
                    @Override
                    public void success(String result) {
                        contentTv.setText("get = " + result);
                    }})
                .setErrorCallback(new ErrorCallback() {
                    @Override
                    public void error(Object... values) {
                        contentTv.setText("请求失败");
                    }})
                .get();
    }

    public void post(){
        // 参数
        Map<String,Object> params = new HashMap<>();
        params.put("name","tom");
        params.put("title","hello post");
        params.put("pwd","123456");
        EasyHttp.getBuilder()
//                .setUrl("/login/online")
                .setUrl("http://192.168.131.19:8886/login/online")
                .setTag("post")
                .setParams(params)
                .setSuccessCallback(new SuccessCallback<String>() {
                    @Override
                    public void success(String result) {
                        contentTv.setText("post = " + result);
                        System.out.println("post=" + result);
                    }})
                .setErrorCallback(new ErrorCallback() {
                    @Override
                    public void error(Object... values) {
                        contentTv.setText("请求失败");
                        System.out.println("请求失败");
                    }})
                .post();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 根据tag取消http请求
        // cancel the request with tag if activity will destroy or you want to close the request ,no matter if the http request completed or not.
        EasyHttp.getInstance().cancelHttp("get");
        EasyHttp.getInstance().cancelHttp("post");
    }
}
