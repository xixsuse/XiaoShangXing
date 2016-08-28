package com.xiaoshangxing.Network.api.InfoApi;

import com.xiaoshangxing.Network.BaseUrl;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Headers;
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
//    @Headers({"Content-Type: multipart/form-data"})//需要添加头
    @POST(BaseUrl.SET_IMAGE)
    Observable<ResponseBody> setUserImage(@Part("id") Integer id, @Part MultipartBody.Part photo/*("*fields*")String path*/, @Part("timeStamp") long time);
}
