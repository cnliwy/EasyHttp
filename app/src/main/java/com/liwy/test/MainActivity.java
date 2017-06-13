package com.liwy.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.liwy.easyhttp.EasyHttp;
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
        getByBuilder();
    }

    /**
     * if the IHttpService Implement is RetrofitService
     * setUrl("/login/update")
     *
     * if OkHttpService
     * setUrl("http://192.168.131.192:8886/login/update")
     */
    public void getByBuilder(){
        // 参数
        Map<String,Object> params = new HashMap<>();
        params.put("ver","1");
        new EasyHttp.Builder()
                .setUrl("/login/update")
                .setTag("update")
                .setParams(params)
                .setSuccessCallback(new SuccessCallback<String>() {
                    @Override
                    public void success(String result) {
                        contentTv.setText("Build String = " + result);
                    }})
                .setErrorCallback(new ErrorCallback() {
                    @Override
                    public void error(Object... values) {
                        contentTv.setText("请求失败");
                    }})
                .get();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // cancel the request if activity will destroy ,no matter if the http request completed or not.
        EasyHttp.getInstance().cancelHttp("update");
    }
}
