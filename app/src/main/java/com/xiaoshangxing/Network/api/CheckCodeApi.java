package com.xiaoshangxing.Network.api;

import com.xiaoshangxing.Network.BaseUrl;
import com.xiaoshangxing.Network.Bean.CheckCode;
import com.xiaoshangxing.Network.Bean.Login;
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by FengChaoQun
 * on 2016/8/6
 */
public interface CheckCodeApi {
        @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
        @POST(BaseUrl.CHECK_CODE)
        Observable<ResponseBody> login(@Body CheckCode checkCode);
}
