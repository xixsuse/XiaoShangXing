package com.xiaoshangxing.Network;

import android.content.Context;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.Bean.BindEmai;
import com.xiaoshangxing.Network.api.BindEmailApi;
import com.xiaoshangxing.Network.api.GetPublishedApi;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by FengChaoQun
 * on 2016/8/24
 */
public class PublishNetwork {
    private GetPublishedApi getPublishedApi;

    private PublishNetwork() {

    }

    //在访问LoginNetwork时创建单例
    private static class SingletonHolder {
        private static final PublishNetwork INSTANCE = new PublishNetwork();
    }

    //获取单例
    public static PublishNetwork getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void getPublished(Subscriber<ResponseBody> subscriber, JsonObject jsonObject, Context context) {
        if (getPublishedApi == null) {
            getPublishedApi = Network.getRetrofitWithHeader(context).create(GetPublishedApi.class);
        }
        Observable<ResponseBody> observable = getPublishedApi.getPublished(jsonObject);
        toSubscribe(observable, subscriber);
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

}
