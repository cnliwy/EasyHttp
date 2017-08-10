package com.liwy.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.liwy.easyhttp.EasyHttp;
import com.liwy.easyhttp.callback.SuccessCallback;
import com.liwy.easyhttp.common.EasyRequest;
import com.liwy.easyhttp.impl.RequestService;
import com.liwy.test.bean.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static com.liwy.easyhttp.DataParse.DataParser.PARSE_GSON;
import static com.liwy.easyhttp.common.EasyRequest.VALUE_POST;


public class HttpActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvContent;
    Button btn1;
    Button btn2;
    RequestService httpService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).build();
        httpService = new RequestService(okHttpClient);
        EasyHttp.getInstance().initHttpService(httpService,0,PARSE_GSON);
        tvContent = (TextView)findViewById(R.id.tv_content);
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
                http1();
                break;
            case R.id.btn2:
                getList2();
                break;
        }
    }

    public void http1() {
        // 参数
        Map<String, Object> params = new HashMap<>();
//        params.put("identity","2c92e0315d2c6cd5015d2c6fb7fa0000");//release
        params.put("identity", "40283c825d2bca81015d2bcabe850000");//debug
        params.put("jsonKey", "test");
        final EasyRequest easyRequest = new EasyRequest.Builder()
                .setUrl("http://192.168.131.19:8080/cnliwy/appdata/getTestData")
                .setParams(params)
                .setSync(true)
                .setSuccessCallback(new SuccessCallback<String>() {
                    @Override
                    public void success(String result) {
                        System.out.println(result);
//                        tvContent.setText(result);
                    }
                 })
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                httpService.http(easyRequest);
                System.out.println("http1请求结束");
            }
        }).start();

    }

    public void getList(){
        // 参数
        Map<String, Object> params = new HashMap<>();
//        params.put("identity","2c92e0315d2c6cd5015d2c6fb7fa0000");//release
        params.put("identity", "40283c825d2bca81015d2bcabe850000");//debug
        params.put("jsonKey", "getDatas");
        EasyRequest easyRequest = new EasyRequest.Builder()
                .setUrl("http://192.168.131.19:8080/cnliwy/appdata/getTestData")
                .setParams(params)
                .setRequestType(VALUE_POST)
                .setSuccessCallback(new SuccessCallback<List<Data>>() {
                    @Override
                    public void success(List<Data> result) {
                        System.out.println(result.size() + "个data");
                        for (Data data : result){
                            System.out.println(data.getName());
                        }
                        tvContent.setText("成功获取数据：" + result.size());
                    }
                })
                .build();
        httpService.http(easyRequest);
        System.out.println("getList请求结束");
    }

    public void getList2(){
        EasyRequest easyRequest =EasyHttp.getBuilder()
                .setUrl("http://192.168.131.19:8080/cnliwy/appdata/getTestData")
                .addParam("identity", "40283c825d2bca81015d2bcabe850000")
                .addParam("jsonKey", "getDatas")
                .setRequestType(VALUE_POST)
                .setSuccessCallback(new SuccessCallback<List<Data>>() {
                    @Override
                    public void success(List<Data> result) {
                        System.out.println(result.size() + "个data");
                        for (Data data : result){
                            System.out.println(data.getName());
                        }
                        tvContent.setText("成功获取数据：" + result.size());
                    }
                })
                .build();
        httpService.http(easyRequest);
    }

}
