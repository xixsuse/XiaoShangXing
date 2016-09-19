package com.xiaoshangxing.Network.api.getAndPublish;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.netUtil.BaseUrl;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by FengChaoQun
 * on 2016/9/9
 */
public interface Operate {
    @POST(BaseUrl.OPERATE)
    Observable<ResponseBody> start(@Body JsonObject jsonObject);
}
