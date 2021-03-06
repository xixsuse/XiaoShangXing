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
 * on 2016/8/3
 */
public interface LoginApi {
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST(BaseUrl.LOGIN)
//    Observable<ResponseBody>login(@Body Login login);
    Observable<ResponseBody> login(@Body JsonObject string);
}
