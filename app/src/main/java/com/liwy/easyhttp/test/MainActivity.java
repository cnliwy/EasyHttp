package com.liwy.easyhttp.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liwy.easyhttp.HttpUtils;
import com.liwy.easyhttp.R;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;
import com.liwy.easyhttp.okhttp.OkHttpService;
import com.liwy.easyhttp.retrofit.RetrofitService;

import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.NonNull;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    TextView contentTv;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RetrofitService retrofitService = new RetrofitService().init();
        HttpUtils.getInstance().setHttpService(retrofitService);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        OkHttpService okHttpService = new OkHttpService().init(okHttpClient);
        HttpUtils.getInstance().setHttpService(okHttpService);

        loginBtn = (Button)findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NextActivity.class);
                startActivity(intent);
            }
        });
        contentTv = (TextView)findViewById(R.id.tv_content);
        Map<String,String> params = new HashMap<>();
        params.put("ver","1");
//        HttpUtils.getInstance().get("/login/update", params, new SuccessCallback() {
//            @Override
//            public void success(String result) {
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    contentTv.setText(jsonObject.getString("releaseddate"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new ErrorCallback() {
//            @Override
//            public void error(Object... values) {
//                contentTv.setText(values[0].toString());
//            }
//        });

//        new HttpUtils.Builder().setUrl("http://192.168.131.19:8886/login/update").setParams(params).setSuccessCallback(new SuccessCallback() {
//            @Override
//            public void success(String result) {
//                contentTv.setText(result);
//            }
//        }).setErrorCallback(new ErrorCallback() {
//            @Override
//            public void error(Object... values) {
//                contentTv.setText(values[0].toString());
//            }
//        }).get();


//        HttpUtils.getInstance().getByClient("http://192.168.131.19:8886/login/update?ver=1", params, new SuccessCallback() {
//            @Override
//            public void success(String result) {
//                TestResult baseResult = new Gson().fromJson(result,TestResult.class);
//                contentTv.setText(baseResult.toString());
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        testFlowable();
    }
    public void testFlowable(){
        String[] strs = {"666","111","222"};
        Flowable.fromArray(strs).subscribe(new FlowableSubscriber<Object>() {
            @Override
            public void onSubscribe(@NonNull Subscription s) {
                Log.e(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(Object o) {
                Log.e(TAG, "onNext: " + o.toString());
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError: " );
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: " );
            }
        });
        testOkHttpGet();
    }
    public void testRetrofitGet(){
        Map map = new HashMap();
        HttpUtils.getInstance().get("/login/update?ver=1", map, new SuccessCallback() {
            @Override
            public void success(String result) {
                System.out.println(result);
                TestResult testResult = new Gson().fromJson(result,TestResult.class);
                contentTv.setText(testResult.getReleasedNote());
            }
        }, new ErrorCallback() {
            @Override
            public void error(Object... values) {
                contentTv.setText("请求失败");
            }
        });
    }

    public void testOkHttpGet(){
        HttpUtils.getInstance().get("http://192.168.131.19:8886/login/update?ver=1", null, new SuccessCallback() {
            @Override
            public void success(String result) {
                TestResult testResult = new Gson().fromJson(result,TestResult.class);
                contentTv.setText(testResult.getReleasedDate() + testResult.getReleasedNote());
            }
        }, new ErrorCallback() {
            @Override
            public void error(Object... values) {
                contentTv.setText("请求失败");
            }
        });
    }
}
