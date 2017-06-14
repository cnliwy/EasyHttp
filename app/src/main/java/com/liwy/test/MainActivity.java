package com.liwy.test;

import android.content.Intent;
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
import com.liwy.easyhttp.okhttp.OkHttpService;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static android.os.Environment.getExternalStorageDirectory;

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
//        get();
        download();
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
                .setUrl("http://192.168.131.192:8886/login/update")
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

    public void download(){
//        String url = "http://img5q.duitang.com/uploads/item/201506/23/20150623203928_HzBWU.jpeg";
        String url = "item/201506/23/20150623203928_HzBWU.jpeg";
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/easyhttp";
        String fileName = "20150623203928_HzBWU.jpeg";
        System.out.println("save path :" + filePath);
        EasyHttp.getBuilder()
                .setFileUrl(url)
                .setFileName(fileName)
                .setFilePath(filePath)
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
            public void onProgress(long total, long current) {
                System.out.println("---->total=" + total + ",current=" + current);
            }
        }).download();
    }
}
