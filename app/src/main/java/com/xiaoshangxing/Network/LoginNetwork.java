package com.xiaoshangxing.Network;

import android.content.Context;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.api.Login_Register_Api.CheckCodeApi;
import com.xiaoshangxing.Network.api.Login_Register_Api.CheckEmialCodeApi;
import com.xiaoshangxing.Network.api.Login_Register_Api.ChkExistApi;
import com.xiaoshangxing.Network.api.Login_Register_Api.FindPWByEmailApi;
import com.xiaoshangxing.Network.api.Login_Register_Api.ForgetApi;
import com.xiaoshangxing.Network.api.Login_Register_Api.LoginApi;
import com.xiaoshangxing.Network.api.Login_Register_Api.RegisterApi;
import com.xiaoshangxing.Network.api.Login_Register_Api.SendCodeApi;

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
    private SendCodeApi sendCodeApi;
    private ChkExistApi chkExistApi;
    private ForgetApi forgetApi;
    private FindPWByEmailApi findPWByEmailApi;
    private CheckEmialCodeApi checkEmialCodeApi;

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

    public void ForgetPassword(Subscriber<ResponseBody> subscriber, JsonObject jsonObject) {
        if (forgetApi == null) {
            forgetApi = Network.getRetrofit().create(ForgetApi.class);
        }
        Observable<ResponseBody> observable = forgetApi.start(jsonObject);
        toSubscribe(observable, subscriber);
    }

    public void FindPWByEmail(Subscriber<ResponseBody> subscriber, JsonObject jsonObject) {
        if (findPWByEmailApi == null) {
            findPWByEmailApi = Network.getRetrofit().create(FindPWByEmailApi.class);
        }
        Observable<ResponseBody> observable = findPWByEmailApi.start(jsonObject);
        toSubscribe(observable, subscriber);
    }

    public void CheckEmailCode(Subscriber<ResponseBody> subscriber, JsonObject jsonObject) {
        if (checkEmialCodeApi == null) {
            checkEmialCodeApi = Network.getRetrofit().create(CheckEmialCodeApi.class);
        }
        Observable<ResponseBody> observable = checkEmialCodeApi.start(jsonObject);
        toSubscribe(observable, subscriber);
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
}
