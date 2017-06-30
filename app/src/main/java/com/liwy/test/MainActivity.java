package com.liwy.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.liwy.easyhttp.EasyHttp;
import com.liwy.easyhttp.callback.DataParser;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;
import com.liwy.test.bean.Data;
import com.liwy.test.bean.LoginResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    TextView contentTv;
    Button getGSONBtn;
    Button getXMLBtn;
    Button postBtn;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getGSONBtn = (Button)findViewById(R.id.btn_get_gson);
        getXMLBtn = (Button)findViewById(R.id.btn_get_xml);
        postBtn = (Button)findViewById(R.id.btn_post);
        nextBtn = (Button)findViewById(R.id.btn_next);
        getGSONBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGSONData();
            }

        });
        getXMLBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getXMLData();
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
    public void getGSONData(){
        List<Data> type = new ArrayList<Data>();
        System.out.println("集合：" + type.getClass());
        // 参数
        Map<String,Object> params = new HashMap<>();
        params.put("ver","1");
        EasyHttp.getBuilder()
//                .setUrl("/login/update")
                .setUrl("http://192.168.131.19:8886/login/datatest")
                .setTag("get")
                .setParams(params)
                .setSuccessCallback(new SuccessCallback<List<Data>>() {
                    @Override
                    public void success(List<Data> result) {
                        System.out.println(result.toString());
                        contentTv.setText("get = " + result.get(2).toString());
                    }
                })
//                .setSuccessCallback(new SuccessCallback<List<Data>>() {
//                    @Override
//                    public void success(List<Data> result) {
//                        System.out.println(result.toString());
//                        contentTv.setText("get = " + result.get(2).toString());
//                    }})
                .setErrorCallback(new ErrorCallback() {
                    @Override
                    public void error(String errorMsg) {
                        contentTv.setText("请求失败");
                    }})
                .get();
    }

    /**
     * the get request which parse xml data
     */
    public void getXMLData(){
        // 参数
        Map<String,Object> params = new HashMap<>();
        params.put("deviceNumber","52fb15e9-c0e7-30fd-b66e-fc9e8b834b0a");
        params.put("devicePin","a123456a");

        EasyHttp.getBuilder()
                .setUrl("http://122.224.205.147:8008/GCPServer.asmx/DeviceLogin")
                .setParams(params)
                .setParseType(DataParser.PARSE_XML)
                .setSuccessCallback(new SuccessCallback<LoginResponse>() {
                    @Override
                    public void success(LoginResponse result) {
                        contentTv.setText("get = " + result.toString());
                    }})
                .setErrorCallback(new ErrorCallback() {
                    @Override
                    public void error(String errorMsg) {
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
                    public void error(String errorMsg) {
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
