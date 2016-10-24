package com.xiaoshangxing.Network;

import android.content.Context;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.api.AppApi.SuggestionApi;
import com.xiaoshangxing.Network.api.AppApi.UpdateApi;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by FengChaoQun
 * on 2016/10/6
 */

public class AppNetwork {
    private UpdateApi updateApi;
    private SuggestionApi suggestionApi;

    private AppNetwork() {

    }

    //在访问AppNetwork时创建单例
    private static class SingletonHolder {
        private static final AppNetwork INSTANCE = new AppNetwork();
    }

    //获取单例
    public static AppNetwork getInstance() {
        return AppNetwork.SingletonHolder.INSTANCE;
    }

    public void Update(Subscriber<ResponseBody> subscriber, JsonObject jsonObject, Context context){
        if (updateApi == null) {
            updateApi = Network.getRetrofitWithHeader(context).create(UpdateApi.class);
        }
        Observable<ResponseBody> observable = updateApi.start(jsonObject);
        toSubscribe(observable, subscriber);
    }

    public void Suggest(Subscriber<ResponseBody> subscriber, JsonObject jsonObject, Context context) {
        if (suggestionApi == null) {
            suggestionApi = Network.getRetrofitWithHeader(context).create(SuggestionApi.class);
        }
        Observable<ResponseBody> observable = suggestionApi.start(jsonObject);
        toSubscribe(observable, subscriber);
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
}
