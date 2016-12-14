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
public interface RealName {
    @Multipart
    @POST(BaseUrl.REAL_NAME)
    Observable<ResponseBody> start(@Part("userId") Integer userId,
                                   @Part("name") String name,
                                   @Part("sex") String sex,
                                   @Part("studentNum") String studentNum,
                                   @Part("schoolName") String schoolName,
                                   @Part("college") String college,
                                   @Part("profession") String profession,
                                   @Part("admissionYear") String admissionYear,
                                   @Part("degree") String degree,
                                   @Part MultipartBody.Part left,
                                   @Part MultipartBody.Part right);
}
