package com.xiaoshangxing.Network.api.getAndPublish;

import com.xiaoshangxing.Network.BaseUrl;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by FengChaoQun
 * on 2016/9/7
 */
public interface FabuApi {
    @Multipart
//    @Headers({"Content-Type: multipart/form-data"})//需要添加头
    @POST(BaseUrl.SET_IMAGE)
    Call<ResponseBody> fabu(@Part("id") Integer id, @Part MultipartBody.Part photo, @Part("timeStamp") long time);
}
