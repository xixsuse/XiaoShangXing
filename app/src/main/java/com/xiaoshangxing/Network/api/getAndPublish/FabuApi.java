package com.xiaoshangxing.Network.api.getAndPublish;

import android.support.annotation.Nullable;

import com.xiaoshangxing.Network.BaseUrl;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * Created by FengChaoQun
 * on 2016/9/7
 */
//public interface FabuApi {
//    @Multipart
//    @POST(BaseUrl.PUBLISH)
//    Observable<ResponseBody> fabu(@Part("userId") Integer id,
//                                  @Part("text") String text,
//                                  @Part("location") String location,
//                                  @Part("personLimit") Integer personLimit,
//                                  @Part("clientTime") long clientTime,
//                                  @Part("category") Integer category,
//                                  @Part("sight") Integer sight,
//                                  @Part("price") String price,
//                                  @Part("dorm") String dorm,
//                                  @Part("sightUserids") String sightUserids,
//                                  @Part("timeStamp") long time,
//                                  @PartMap @Nullable Map<String, RequestBody> photos
//    );
//}

public interface FabuApi {
    @Multipart
    @POST(BaseUrl.PUBLISH)
    Observable<ResponseBody> fabu(@Part("userId") Integer userId,
                                  @Part("text") String text,
                                  @Part("location") String location,
                                  @Part("personLimit") Integer personLimit,
                                  @Part("clientTime") long clientTime,
                                  @Part("category") Integer category,
                                  @Part("sight") Integer sight,
                                  @Part("price") String price,
                                  @Part("dorm") String dorm,
                                  @Part("sightUserids") String sightUserids,
                                  @Part("timeStamp") long timeStamp,
                                  @PartMap @Nullable Map<String, RequestBody> images
    );
}
