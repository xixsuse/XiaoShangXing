package com.xiaoshangxing.Network.api.InfoApi;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.BaseUrl;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import rx.Observable;

/**
 * Created by FengChaoQun
 * on 2016/8/28
 */
public interface ModifyInfoApi {
    @PUT(BaseUrl.MODIFT_INFO)
    Observable<ResponseBody> modify(@Body JsonObject string);
}
