package com.xiaoshangxing.network.api.InfoApi;

import com.xiaoshangxing.network.netUtil.BaseUrl;

import okhttp3.ResponseBody;
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
