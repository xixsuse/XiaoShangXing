package com.xiaoshangxing.Network;

import android.content.Context;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.api.InfoApi.BindEmailApi;
import com.xiaoshangxing.Network.api.InfoApi.CheckPassword;
import com.xiaoshangxing.Network.api.InfoApi.GetUser;
import com.xiaoshangxing.Network.api.InfoApi.ModifyInfoApi;
import com.xiaoshangxing.Network.api.InfoApi.ModifyPassword;
import com.xiaoshangxing.Network.api.InfoApi.RealName;
import com.xiaoshangxing.Network.api.InfoApi.RealNameTest;
import com.xiaoshangxing.Network.api.InfoApi.SetUserImage;
import com.xiaoshangxing.Network.api.InfoApi.UnBindEmailApi;
import com.xiaoshangxing.Network.netUtil.NS;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by FengChaoQun
 * on 2016/8/28
 */
public class InfoNetwork {
    private GetUser getUser;
    private ModifyInfoApi modifyInfoApi;
    private CheckPassword checkPassword;
    private ModifyPassword modifyPassword;
    private BindEmailApi bindEmailApi;
    private UnBindEmailApi unBindEmailApi;
    private SetUserImage setUserImage;
    private RealName realName;


    private InfoNetwork() {

    }

    //在访问InfoNetwork时创建单例
    private static class SingletonHolder {
        private static final InfoNetwork INSTANCE = new InfoNetwork();
    }

    //获取单例
    public static InfoNetwork getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void GetUser(Subscriber<ResponseBody> subscriber, JsonObject user, Context context){
        if (getUser == null) {
            getUser = Network.getRetrofitWithHeader(context).create(GetUser.class);
        }
        Observable<ResponseBody> observable = getUser.user(user);
        toSubscribe(observable, subscriber);
    }

    public void ModifyInfo(Subscriber<ResponseBody> subscriber, JsonObject jsonObject, Context context){
        if (modifyInfoApi == null) {
            modifyInfoApi = Network.getRetrofitWithHeader(context).create(ModifyInfoApi.class);
        }
        Observable<ResponseBody> observable = modifyInfoApi.modify(jsonObject);
        toSubscribe(observable, subscriber);
    }

    public void CheckPassword(Subscriber<ResponseBody> subscriber, JsonObject jsonObject, Context context){
        if (checkPassword == null) {
            checkPassword = Network.getRetrofitWithHeader(context).create(CheckPassword.class);
        }
        Observable<ResponseBody> observable = checkPassword.check(jsonObject);
        toSubscribe(observable, subscriber);
    }

    public void ModifyPassword(Subscriber<ResponseBody> subscriber, JsonObject jsonObject, Context context){
        if (modifyPassword == null) {
            modifyPassword = Network.getRetrofitWithHeader(context).create(ModifyPassword.class);
        }
        Observable<ResponseBody> observable = modifyPassword.start(jsonObject);
        toSubscribe(observable, subscriber);
    }

    public void bindEmail(Subscriber<ResponseBody> subscriber, JsonObject bindEmai, Context context) {
        if (bindEmailApi == null) {
            bindEmailApi = Network.getRetrofitWithHeader(context).create(BindEmailApi.class);
        }
        Observable<ResponseBody> observable = bindEmailApi.bindEmail(bindEmai);
        toSubscribe(observable, subscriber);
    }

    public void unBindEmail(Subscriber<ResponseBody> subscriber, JsonObject bindEmai, Context context) {
        if (unBindEmailApi == null) {
            unBindEmailApi = Network.getRetrofitWithHeader(context).create(UnBindEmailApi.class);
        }
        Observable<ResponseBody> observable = unBindEmailApi.unbindEmail(bindEmai);
        toSubscribe(observable, subscriber);
    }

    public void setUserImage(Subscriber<ResponseBody> subscriber, Integer id, MultipartBody.Part photo,Context context) {
        if (setUserImage == null) {
            setUserImage = Network.getRetrofitWithHeader(context).create(SetUserImage.class);
        }
        Observable<ResponseBody> observable = setUserImage.setUserImage(id,photo, NS.currentTime());
        toSubscribe(observable, subscriber);
    }

    public void realName(Subscriber<ResponseBody> subscriber, Integer userId, String name, String sex,
                         String studentNum, String schoolName, String college, String profession,
                         String admissionYear, String degree,
                         MultipartBody.Part left, MultipartBody.Part right, Context context) {
        if (realName == null) {
            realName = Network.getRetrofit().create(RealName.class);
        }
        Observable<ResponseBody> observable = realName.start(userId, name, sex, studentNum, schoolName, college, profession,
                admissionYear, degree, left, right);
        toSubscribe(observable, subscriber);
    }

    public void realNameTest(Subscriber<ResponseBody> subscriber, Integer userId, String name,
                             MultipartBody.Part left, MultipartBody.Part right) {
        RealNameTest realNameTest = Network.getRetrofit().create(RealNameTest.class);
        Observable<ResponseBody> observable = realNameTest.start(userId, name, left, right);
        toSubscribe(observable, subscriber);
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
}
