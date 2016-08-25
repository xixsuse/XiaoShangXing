package com.xiaoshangxing.Network.api;

import com.xiaoshangxing.Network.BaseUrl;
import com.xiaoshangxing.Network.Bean.Publish;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by FengChaoQun
 * on 2016/8/23
 */
public interface PublishApi {
    @POST(BaseUrl.PUBLISH)
    Observable<ResponseBody> publish(@Body Publish publish);
}
