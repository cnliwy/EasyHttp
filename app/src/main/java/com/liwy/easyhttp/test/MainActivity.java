package com.liwy.easyhttp.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.liwy.easyhttp.HttpUtils;
import com.liwy.easyhttp.R;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;
import com.liwy.easyhttp.okhttp.OkHttpService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    TextView contentTv;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginBtn = (Button)findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NextActivity.class);
                startActivity(intent);
            }
        });
        contentTv = (TextView)findViewById(R.id.tv_content);
        init();
        getByBuilder();
    }
    public void init(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        OkHttpService okHttpService = new OkHttpService().init(okHttpClient);
        HttpUtils.getInstance().setHttpService(okHttpService);
    }

    public void get(){
        Map<String,Object> params = new HashMap<>();
        params.put("ver","1");
        HttpUtils.getInstance().get("http://192.168.131.19:8886/login/update?ver=1", params, new SuccessCallback<TestResult>() {
            @Override
            public void success(TestResult result) {
                System.out.println(result.toString());
                contentTv.setText("转换成功" + result.toString());
            }
        }, new ErrorCallback() {
            @Override
            public void error(Object... values) {
                contentTv.setText("请求失败");
            }
        });
    }

    public void getByBuilder(){
        Map<String,Object> params = new HashMap<>();
        params.put("ver","1");
        new HttpUtils.Builder().setUrl("http://192.168.131.19:8886/login/update").setParams(params).setSuccessCallback(new SuccessCallback<String>() {
            @Override
            public void success(String result) {
                contentTv.setText("Build String = " + result);
            }
        }).setErrorCallback(new ErrorCallback() {
            @Override
            public void error(Object... values) {
                contentTv.setText("请求失败");
            }
        }).get();
    }
}
