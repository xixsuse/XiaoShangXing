package com.xiaoshangxing.Network;

import android.content.Context;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.Bean.Publish;
import com.xiaoshangxing.Network.api.Login_Register_Api.CheckCodeApi;
import com.xiaoshangxing.Network.api.Login_Register_Api.ChkExistApi;
import com.xiaoshangxing.Network.api.Login_Register_Api.LoginApi;
import com.xiaoshangxing.Network.api.PublishApi;
import com.xiaoshangxing.Network.api.Login_Register_Api.RegisterApi;
import com.xiaoshangxing.Network.api.Login_Register_Api.SendCodeApi;
import com.xiaoshangxing.Network.api.InfoApi.SetUserImage;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by FengChaoQun
 * on 2016/8/4
 */
public class LoginNetwork {
    private LoginApi mloginApi;
    private RegisterApi registerApi;
    private CheckCodeApi checkCodeApi;
    private PublishApi publishApi;
    private SendCodeApi sendCodeApi;
    private SetUserImage setUserImage;
    private ChkExistApi chkExistApi;


    private LoginNetwork() {

    }

    //在访问LoginNetwork时创建单例
    private static class SingletonHolder {
        private static final LoginNetwork INSTANCE = new LoginNetwork();
    }

    //获取单例
    public static LoginNetwork getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void Login(Subscriber<ResponseBody> subscriber, JsonObject login) {
        if (mloginApi == null) {
            mloginApi = Network.getRetrofit().create(LoginApi.class);
        }
        Observable<ResponseBody> observable = mloginApi.login(login);
        toSubscribe(observable, subscriber);
    }

    public void sendCode(Subscriber<ResponseBody> subscriber, JsonObject string) {
        if (sendCodeApi == null) {
            sendCodeApi = Network.getRetrofit().create(SendCodeApi.class);
        }
        Observable<ResponseBody> observable = sendCodeApi.sendCode(string);
        toSubscribe(observable, subscriber);
    }

    public void checkExist(Subscriber<ResponseBody> subscriber, JsonObject string) {
        if (chkExistApi == null) {
            chkExistApi = Network.getRetrofit().create(ChkExistApi.class);
        }
        Observable<ResponseBody> observable = chkExistApi.check(string);
        toSubscribe(observable, subscriber);
    }

    public void Register(Subscriber<ResponseBody> subscriber, JsonObject register,Context context){
        if (registerApi == null) {
            registerApi = Network.getRetrofitWithHeader(context).create(RegisterApi.class);
        }
        Observable<ResponseBody> observable = registerApi.login(register);
        toSubscribe(observable, subscriber);
    }

    public void CheckCode(Subscriber<ResponseBody> subscriber, JsonObject checkCode){
        if (checkCodeApi == null) {
            checkCodeApi = Network.getRetrofit().create(CheckCodeApi.class);
        }
        Observable<ResponseBody> observable = checkCodeApi.login(checkCode);
        toSubscribe(observable, subscriber);
    }




    public void Publish(Subscriber<ResponseBody> subscriber, Publish publish, Context context) {
        if (publishApi == null) {
            publishApi = Network.getRetrofitWithHeader(context).create(PublishApi.class);
        }
        Observable<ResponseBody> observable = publishApi.publish(publish);
        toSubscribe(observable, subscriber);
    }

//    public void setUserImage(Subscriber<ResponseBody> subscriber, Integer id, MultipartBody.Part path/*String path*/, long time, Context context) {
//        if (setUserImage == null) {
//            setUserImage = Network.getRetrofitWithHeader(context).create(SetUserImage.class);
//        }
//        Observable<ResponseBody> observable = setUserImage.setUserImage(id, path, time);
//        toSubscribe(observable, subscriber);
//    }


    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
}
