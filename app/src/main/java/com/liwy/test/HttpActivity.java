package com.liwy.test;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.liwy.easyhttp.EasyHttp;
import com.liwy.easyhttp.callback.DownloadCallback;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;
import com.liwy.easyhttp.common.EasyRequest;
import com.liwy.easyhttp.common.ProcessUtils;
import com.liwy.easyhttp.impl.RequestService;
import com.liwy.test.bean.Data;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.liwy.easyhttp.DataParse.DataParser.PARSE_GSON;
import static com.liwy.easyhttp.EasyHttp.getBuilder;
import static com.liwy.easyhttp.common.EasyRequest.VALUE_POST;


public class HttpActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvContent;
    Button syncBtn;
    Button getBtn;
    Button postBtn;
    Button downloadBtn;
    Button uploadBtn;
    Button cancelBtn;
    RequestService httpService;
    String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);
        tvContent = (TextView)findViewById(R.id.tv_content);
        getBtn = (Button)findViewById(R.id.btn_get);
        syncBtn = (Button)findViewById(R.id.btn_sync);
        postBtn = (Button)findViewById(R.id.btn_post);
        downloadBtn = (Button)findViewById(R.id.btn_download);
        uploadBtn = (Button)findViewById(R.id.btn_upload);
        cancelBtn = (Button)findViewById(R.id.btn_cancel);
        syncBtn.setOnClickListener(this);
        getBtn.setOnClickListener(this);
        postBtn.setOnClickListener(this);
        downloadBtn.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sync:
                syncHttp();
                break;
            case R.id.btn_get:
                get();
                break;
            case R.id.btn_post:
                post();
                break;
            case R.id.btn_download:
                download();
                break;
            case R.id.btn_upload:
                customerRequestBody();
                break;
            case R.id.btn_cancel:
                EasyHttp.getInstance().cancelHttp(tag);
                break;
        }
    }

    public void syncHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 参数
                Map<String, Object> params = new HashMap<>();
                params.put("identity", "40283c825d2bca81015d2bcabe850000");//debug
                params.put("jsonKey", "test");
                System.out.println("请求开始");
                final EasyRequest easyRequest = getBuilder()
                        .setUrl("http://192.168.131.19:8080/cnliwy/appdata/getTestData")
                        .setParams(params)
                        .setSync(true)
                        .setSuccessCallback(new SuccessCallback<String>() {
                            @Override
                            public void success(String result) {
                                System.out.println(result);
                            }
                        })
                        .build();
                EasyHttp.getInstance().get(easyRequest);
                System.out.println("请求完成");
            }
        }).start();

    }
    /**
     * 下载
     */
    public void download(){
        tag = "download";
        String url = "http://img5q.duitang.com/uploads/item/201506/23/20150623203928_HzBWU.jpeg";
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/img";
        String fileName = "moon.jpeg";
         EasyRequest easyRequest = getBuilder()
                .setUrl(url)
//                .setFileName(fileName)
                .setTag(tag)
                .setSaveDir(filePath)
                .setDownloadCallback(new DownloadCallback<File>() {
                    @Override
                    public void onSuccess(File o) {
                        System.out.println("---->下载成功" + o.getAbsolutePath());
                    }

                    @Override
                    public void onError(String err) {
                        System.out.println("---->下载失败");
                    }

                    @Override
                    public void onProgress(long total, int progress) {
                        tvContent.setText("已下载%" + progress);
                        if(progress == 100)tvContent.setText("下载完成");
                    }
                })
                .build();
        EasyHttp.getInstance().download(easyRequest);
    }

    public void get() {
        // 参数
        Map<String, Object> params = new HashMap<>();
//        params.put("identity","2c92e0315d2c6cd5015d2c6fb7fa0000");//release
        params.put("identity", "40283c825d2bca81015d2bcabe850000");//debug
        params.put("jsonKey", "test");
        final EasyRequest easyRequest = getBuilder()
                .setUrl("http://192.168.131.19:8080/cnliwy/appdata/getTestData")
                .setParams(params)
                .setSync(false)
                .setSuccessCallback(new SuccessCallback<String>() {
                    @Override
                    public void success(String result) {
                        System.out.println(result);
                        tvContent.setText(result);
                    }
                 })
                .build();
        EasyHttp.getInstance().get(easyRequest);
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
        EasyHttp.getInstance().post(easyRequest);
        System.out.println("getList请求结束");
    }

    public void post(){
        System.out.println("请求开始");
        EasyRequest easyRequest = getBuilder()
                .setUrl("http://192.168.131.19:8080/cnliwy/appdata/getTestData")
                .addParam("identity", "40283c825d2bca81015d2bcabe850000")
                .addParam("jsonKey", "getDatas")
                .setSuccessCallback(new SuccessCallback<List<Data>>() {
                    @Override
                    public void success(List<Data> result) {
                        for (Data data : result){
                            System.out.println(data.getName());
                        }
                        tvContent.setText("成功获取数据：" + result.toString());
                    }
                })
                .setErrorCallback(new ErrorCallback() {
                    @Override
                    public void error(String errorMsg) {
                        System.out.println("请求失败");
                    }
                })
                .build();
        EasyHttp.getInstance().post(easyRequest);
        System.out.println("请求完成");
    }

    /**
     * 自定义RequestBody
     */
    public void customerRequestBody(){
        // 参数
        Map<String, Object> params = new HashMap<>();
        params.put("identity", "40283c825d2bca81015d2bcabe850000");//debug
        params.put("jsonKey", "getDatas");
        String content = ProcessUtils.map2json(params);
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
//        RequestBody requestBody = RequestBody.create(mediaType,content);
        RequestBody requestBody = ProcessUtils.map2form(params);
        EasyRequest easyRequest =  EasyHttp.getBuilder().setUrl("http://192.168.131.19:8080/cnliwy/appdata/getTestData").requestBody(requestBody)
                .setSuccessCallback(new SuccessCallback() {
                    @Override
                    public void success(Object result) {
                        System.out.println(result.toString());
                    }
                })
                .build();
        EasyHttp.getInstance().post(easyRequest);

    }


}
