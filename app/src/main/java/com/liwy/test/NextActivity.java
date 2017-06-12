package com.liwy.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liwy.easyhttp.EasyHttp;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;
import com.liwy.easyhttp.retrofit.RetrofitService;

import java.util.HashMap;
import java.util.Map;

public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        init();
//        getByBuilder();
        get();
    }

    /**
     * initialize the EasyHttp with retrofitService
     */
    public void init(){
        RetrofitService retrofitService = new RetrofitService().init();
        EasyHttp.getInstance().setHttpService(retrofitService);
    }

    /**
     * use the get request of EasyHttp
     */
    public void get(){
        Map<String,Object> params = new HashMap<>();
        params.put("ver","1");
        EasyHttp.getInstance().get("/login/update", params, new SuccessCallback<TestResult>() {
            @Override
            public void success(TestResult result) {
//                    JSONObject jsonObject = new JSONObject(result);
                    System.out.println("next result = " + result);
            }
        }, new ErrorCallback() {
            @Override
            public void error(Object... values) {
                System.out.println(values[0].toString());
            }
        });
    }

    /**
     *  use the get request of EasyHttp by Builder
     */
    public void getByBuilder(){
//        Map<String,String> params = new HashMap<>();
//        params.put("ver","1");
//        new EasyHttp.Builder().setUrl("/login/update").setParams(params).setSuccessCallback(new SuccessCallback() {
//            @Override
//            public void success(String result) {
//                System.out.println(result);
//            }
//        }).setErrorCallback(new ErrorCallback() {
//            @Override
//            public void error(Object... values) {
//                System.out.println(values[0].toString());
//            }
//        }).get();
    }
}
