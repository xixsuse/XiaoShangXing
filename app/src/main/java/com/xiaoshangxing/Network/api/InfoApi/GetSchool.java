package com.xiaoshangxing.Network.api.InfoApi;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.netUtil.BaseUrl;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by FengChaoQun
 * on 2016/8/24
 */
public interface GetSchool {
    @POST(BaseUrl.GET_SCHOOL)
    Observable<ResponseBody> start();
}