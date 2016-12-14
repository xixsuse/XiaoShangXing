package com.xiaoshangxing.network.api.InfoApi;

import com.xiaoshangxing.network.netUtil.BaseUrl;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by FengChaoQun
 * on 2016/8/23
 */
public interface SetUserImage {
    @Multipart
    @POST(BaseUrl.SET_IMAGE)
    Observable<ResponseBody> setUserImage(@Part("id") Integer id, @Part MultipartBody.Part photo/*("*fields*")String path*/, @Part("timeStamp") long time);
}
