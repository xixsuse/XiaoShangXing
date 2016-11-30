package com.xiaoshangxing.Network;

import android.content.Context;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.api.IMApi.CancleFavorApi;
import com.xiaoshangxing.Network.api.IMApi.Crush;
import com.xiaoshangxing.Network.api.IMApi.FavorApi;
import com.xiaoshangxing.Network.api.IMApi.GetCrush;
import com.xiaoshangxing.Network.api.IMApi.GetImages;
import com.xiaoshangxing.Network.api.IMApi.MyFavorApi;
import com.xiaoshangxing.Network.api.IMApi.MyStarApi;
import com.xiaoshangxing.Network.api.IMApi.SerchPersonApi;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by FengChaoQun
 * on 2016/10/6
 */

public class IMNetwork {

    private SerchPersonApi serchPersonApi;
    private MyFavorApi myFavorApi;
    private MyStarApi myStarApi;
    private FavorApi favorApi;
    private CancleFavorApi cancleFavorApi;
    private GetImages getImages;
    private Crush crush;
    private GetCrush getCrush;

    private IMNetwork() {

    }

    //在访问IMNetwork时创建单例
    private static class SingletonHolder {
        private static final IMNetwork INSTANCE = new IMNetwork();
    }

    //获取单例
    public static IMNetwork getInstance() {
        return IMNetwork.SingletonHolder.INSTANCE;
    }


    public void SerchPerson(Subscriber<ResponseBody> subscriber, JsonObject param, Context context) {
        if (serchPersonApi == null) {
            serchPersonApi = Network.getRetrofitWithHeader(context).create(SerchPersonApi.class);
        }
        Observable<ResponseBody> observable = serchPersonApi.start(param);
        toSubscribe(observable, subscriber);
    }

    public void MyFavor(Subscriber<ResponseBody> subscriber, String param, Context context) {
        if (myFavorApi == null) {
            myFavorApi = Network.getRetrofitWithHeader(context).create(MyFavorApi.class);
        }
        Observable<ResponseBody> observable = myFavorApi.start(param);
        toSubscribe(observable, subscriber);
    }

    public void MyStar(Subscriber<ResponseBody> subscriber, String param, Context context) {
        if (myStarApi == null) {
            myStarApi = Network.getRetrofitWithHeader(context).create(MyStarApi.class);
        }
        Observable<ResponseBody> observable = myStarApi.start(param);
        toSubscribe(observable, subscriber);
    }

    public void Favor(Subscriber<ResponseBody> subscriber, JsonObject param, Context context) {
        if (favorApi == null) {
            favorApi = Network.getRetrofitWithHeader(context).create(FavorApi.class);
        }
        Observable<ResponseBody> observable = favorApi.start(param);
        toSubscribe(observable, subscriber);
    }

    public void CancelFavor(Subscriber<ResponseBody> subscriber, JsonObject param, Context context) {
        if (cancleFavorApi == null) {
            cancleFavorApi = Network.getRetrofitWithHeader(context).create(CancleFavorApi.class);
        }
        Observable<ResponseBody> observable = cancleFavorApi.start(param);
        toSubscribe(observable, subscriber);
    }

    public void GetImages(Subscriber<ResponseBody> subscriber, JsonObject param, Context context) {
        if (getImages == null) {
            getImages = Network.getRetrofitWithHeader(context).create(GetImages.class);
        }
        Observable<ResponseBody> observable = getImages.start(param);
        toSubscribe(observable, subscriber);
    }

    public void Crush(Subscriber<ResponseBody> subscriber, JsonObject param, Context context) {
        if (crush == null) {
            crush = Network.getRetrofitWithHeader(context).create(Crush.class);
        }
        Observable<ResponseBody> observable = crush.start(param);
        toSubscribe(observable, subscriber);
    }

    public void GetCrush(Subscriber<ResponseBody> subscriber, JsonObject param, Context context) {
        if (getCrush == null) {
            getCrush = Network.getRetrofitWithHeader(context).create(GetCrush.class);
        }
        Observable<ResponseBody> observable = getCrush.start(param);
        toSubscribe(observable, subscriber);
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
}
