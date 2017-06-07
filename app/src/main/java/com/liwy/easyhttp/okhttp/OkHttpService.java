package com.liwy.easyhttp.okhttp;

import com.liwy.easyhttp.AbHttpService;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by liwy on 2017/6/5.
 */

public class OkHttpService extends AbHttpService {
    public OkHttpClient okHttpClient;

    public OkHttpService() {

    }

    public OkHttpService init(OkHttpClient okHttpClient) {
        OkHttpService okHttpService = new OkHttpService();
        okHttpService.okHttpClient = okHttpClient;
        if (okHttpClient == null)okHttpService = init();
        return okHttpService;
    }
    public OkHttpService init() {
        OkHttpService okHttpService = new OkHttpService();
        okHttpService.okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();;
        return okHttpService;
    }

    public static final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
    @Override
    public void get(String url, Map<String, String> params, final SuccessCallback successCallback, final ErrorCallback errorCallback) {
        String content = map2json(params);
        RequestBody formBody = RequestBody.create(JSON,content);

        Request request = new Request.Builder().url(url).get().build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Observable.empty().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (errorCallback != null)errorCallback.error(e);
                    }
                }).subscribe();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Observable.empty().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (successCallback != null)successCallback.success(response.body().string());
                    }
                }).subscribe();
            }
        });
    }

    public static String map2json(Map<String,String> params){
        JSONObject jsonObject = new JSONObject();
        if (params != null){
            Set<String> keys = params.keySet();
            if (!keys.isEmpty()){
                for (String key : keys){
                    try {
                        jsonObject.put(key,params.get(key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return jsonObject.toString();
            }
        }
        return jsonObject.toString();
    }

    @Override
    public void post(String url, Map<String, String> params, final SuccessCallback successCallback, final ErrorCallback errorCallback) {
        String content = map2json(params);
        RequestBody formBody = RequestBody.create(JSON,content);

        Request request = new Request.Builder().url(url).post(formBody).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (errorCallback != null)errorCallback.error(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (successCallback != null)successCallback.success(response.body().string());
            }
        });
    }
}
