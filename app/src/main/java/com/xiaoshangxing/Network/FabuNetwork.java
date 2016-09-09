package com.xiaoshangxing.Network;

import android.content.Context;

import com.xiaoshangxing.Network.Bean.Publish;
import com.xiaoshangxing.Network.api.getAndPublish.FabuApi;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by FengChaoQun
 * on 2016/9/8
 */
public class FabuNetwork {
    private FabuApi fabuApi;

    private FabuNetwork() {

    }

    //在访问LoginNetwork时创建单例
    private static class SingletonHolder {
        private static final FabuNetwork INSTANCE = new FabuNetwork();
    }

    //获取单例
    public static FabuNetwork getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void Fabu(Context context, Publish publish, Subscriber<ResponseBody> subscriber, Map<String, RequestBody> photos) {
        if (fabuApi == null) {
            fabuApi = Network.getRetrofitFormat(context).create(FabuApi.class);
        }
        Observable<ResponseBody> observable = fabuApi.fabu(publish.getUserId(), publish.getText(), publish.getLocation()
                , publish.getPersonLimit(), publish.getClientTime(), publish.getCategory(), publish.getSight()
                , publish.getPrice(), publish.getDorm(), publish.getSightUserids(), publish.getClientTime(), photos);
        toSubscribe(observable, subscriber);
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
}
