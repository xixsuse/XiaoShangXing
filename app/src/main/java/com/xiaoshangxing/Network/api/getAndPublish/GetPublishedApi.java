package com.xiaoshangxing.network.api.getAndPublish;

import com.google.gson.JsonObject;
import com.xiaoshangxing.network.netUtil.BaseUrl;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by FengChaoQun
 * on 2016/8/24
 */
public interface GetPublishedApi {
    @POST(BaseUrl.GET_PUBLISHED)
    Observable<ResponseBody> getPublished(@Body JsonObject string);
}
