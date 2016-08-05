package com.xiaoshangxing.Network;

import com.xiaoshangxing.Network.Bean.Login;
import com.xiaoshangxing.Network.api.LoginApi;

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

    public void Login(Subscriber<ResponseBody> subscriber, Login login) {
        if (mloginApi == null) {
            mloginApi = Network.getRetrofit().create(LoginApi.class);
        }
        Observable<ResponseBody> observable = mloginApi.login(login);
        toSubscribe(observable, subscriber);
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
}
