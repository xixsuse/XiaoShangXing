package com.xiaoshangxing.network.api.IMApi;

import com.google.gson.JsonObject;
import com.xiaoshangxing.network.netUtil.BaseUrl;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by FengChaoQun
 * on 2016/9/9
 */
public interface CancleFavorApi {
    @POST(BaseUrl.CANCLE_FAVOR)
    Observable<ResponseBody> start(@Body JsonObject jsonObject);
}
