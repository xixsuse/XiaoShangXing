package com.xiaoshangxing.network.api.Login_Register_Api;

import com.google.gson.JsonObject;
import com.xiaoshangxing.network.netUtil.BaseUrl;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by FengChaoQun
 * on 2016/8/23
 */
public interface SendCodeApi {
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST(BaseUrl.SEND_CODE)
    Observable<ResponseBody> sendCode(@Body JsonObject string);
}
