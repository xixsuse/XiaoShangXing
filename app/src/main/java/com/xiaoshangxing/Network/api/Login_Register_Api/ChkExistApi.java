package com.xiaoshangxing.Network.api.Login_Register_Api;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.netUtil.BaseUrl;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by FengChaoQun
 * on 2016/8/3
 */
public interface ChkExistApi {
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST(BaseUrl.CHECK_PHONE)
    Observable<ResponseBody>check(@Body JsonObject string);
}
