package com.xiaoshangxing.Network.api.InfoApi;

import com.xiaoshangxing.Network.netUtil.BaseUrl;

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
public interface RealNameTest {
    @Multipart
    @POST(BaseUrl.REAL_NAME)
    Observable<ResponseBody> start(@Part("userId") Integer userId, @Part("name") String name,
                                   @Part MultipartBody.Part left,
                                   @Part MultipartBody.Part right);
}
