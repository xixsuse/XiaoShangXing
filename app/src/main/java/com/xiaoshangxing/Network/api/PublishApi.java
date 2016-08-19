package com.xiaoshangxing.Network.api;

import com.xiaoshangxing.Network.BaseUrl;
import com.xiaoshangxing.Network.Bean.CheckCode;
import com.xiaoshangxing.Network.Bean.Publish;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by FengChaoQun
 * on 2016/8/6
 */
public interface PublishApi {
        @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
        @POST("http://192.168.0.129:8080/v1/moment/releaseMoment")
        Observable<ResponseBody> publish(@Body Publish publish);
}
