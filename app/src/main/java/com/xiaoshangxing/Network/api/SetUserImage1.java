package com.xiaoshangxing.Network.api;

import com.xiaoshangxing.Network.BaseUrl;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by FengChaoQun
 * on 2016/8/23
 */
public interface SetUserImage1 {
    @Multipart
    @POST(BaseUrl.SEND_CODE)
    Call<ResponseBody> setUserImage(@Part("id") Integer id, @Part /*MultipartBody.Part photo*/("*fields*") String path, @Part("timeStamp") long time);
}
