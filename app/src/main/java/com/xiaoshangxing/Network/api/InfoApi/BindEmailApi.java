package com.xiaoshangxing.Network.api.InfoApi;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.BaseUrl;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by FengChaoQun
 * on 2016/8/6
 */
public interface BindEmailApi {
//        @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
        @POST(BaseUrl.BIND_EMAIL)
        Observable<ResponseBody> bindEmail(@Body JsonObject bindEmai);
}
