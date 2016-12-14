package com.xiaoshangxing.network.api.IMApi;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by FengChaoQun
 * on 2016/9/9
 */
public interface MyStarApi {
    @GET("friend/favorme/{param}")
    Observable<ResponseBody> start(@Path("param") String param);
}
