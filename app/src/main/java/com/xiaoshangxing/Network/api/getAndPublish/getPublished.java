package com.xiaoshangxing.Network.api.getAndPublish;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.BaseUrl;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by FengChaoQun
 * on 2016/9/9
 */
public interface GetPublished {
    @POST(BaseUrl.GET_PUBLISHED)
    Observable<ResponseBody> start(@Body JsonObject jsonObject);
}
