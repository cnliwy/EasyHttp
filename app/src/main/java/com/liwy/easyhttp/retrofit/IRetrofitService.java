package com.liwy.easyhttp.retrofit;

import com.google.gson.JsonObject;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by liwy on 2017/6/5.
 */

public interface IRetrofitService {
    @GET()
    Observable<JsonObject> get(@Url String url, @QueryMap Map<String, String> params);

    @POST()
    Observable<JsonObject> post(@Url String url, @QueryMap Map<String, String> params);
}
