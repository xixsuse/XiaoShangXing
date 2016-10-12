package com.xiaoshangxing.Network.api.IMApi;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.netUtil.BaseUrl;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by FengChaoQun
 * on 2016/9/9
 */
public interface SerchPersonApi {
    @POST(BaseUrl.SERCH_PERSON)
    Observable<ResponseBody> start(@Body JsonObject jsonObject);
}
