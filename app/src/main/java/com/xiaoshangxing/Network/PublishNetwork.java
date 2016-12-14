package com.xiaoshangxing.network;

import android.content.Context;

import com.google.gson.JsonObject;
import com.xiaoshangxing.network.api.getAndPublish.CancelOperate;
import com.xiaoshangxing.network.api.getAndPublish.ChangePublishStatu;
import com.xiaoshangxing.network.api.getAndPublish.Comment;
import com.xiaoshangxing.network.api.getAndPublish.DeletePublished;
import com.xiaoshangxing.network.api.getAndPublish.GetAllPublished;
import com.xiaoshangxing.network.api.getAndPublish.GetCalendar;
import com.xiaoshangxing.network.api.getAndPublish.GetCalendarInputer;
import com.xiaoshangxing.network.api.getAndPublish.GetCollect;
import com.xiaoshangxing.network.api.getAndPublish.GetJoinedPlan;
import com.xiaoshangxing.network.api.getAndPublish.GetPublishedApi;
import com.xiaoshangxing.network.api.getAndPublish.GetTransmitInfo;
import com.xiaoshangxing.network.api.getAndPublish.Operate;
import com.xiaoshangxing.network.api.getAndPublish.RefreshPublished;
import com.xiaoshangxing.network.api.getAndPublish.TransmitApi;

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
    private GetAllPublished getAllPublished;
    private Comment comment;
    private GetTransmitInfo getTransmitInfo;
    private RefreshPublished refreshPublished;
    private TransmitApi transmitApi;
    private DeletePublished deletePublished;
    private Operate operate;
    private CancelOperate cancelOperate;
    private ChangePublishStatu changePublishStatu;
    private GetCollect getCollect;
    private GetCalendar getCalendar;
    private GetCalendarInputer getCalendarInputer;
    private GetJoinedPlan getJoinedPlan;

    private PublishNetwork() {

    }

    //获取单例
    public static PublishNetwork getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void getPersonalPublished(Subscriber<ResponseBody> subscriber, JsonObject jsonObject, Context context) {
        if (getPublishedApi == null) {
            getPublishedApi = Network.getRetrofitWithHeader(context).create(GetPublishedApi.class);
        }
        Observable<ResponseBody> observable = getPublishedApi.getPublished(jsonObject);
        toSubscribe(observable, subscriber);
    }

    public void getAllPublished(Subscriber<ResponseBody> subscriber, JsonObject jsonObject, Context context) {
        if (getAllPublished == null) {
            getAllPublished = Network.getRetrofitWithHeader(context).create(GetAllPublished.class);
        }
        Observable<ResponseBody> observable = getAllPublished.start(jsonObject);
        toSubscribe(observable, subscriber);
    }

    public void comment(Subscriber<ResponseBody> subscriber, JsonObject jsonObject, Context context) {
        if (comment == null) {
            comment = Network.getRetrofitWithHeader(context).create(Comment.class);
        }
        Observable<ResponseBody> observable = comment.start(jsonObject);
        toSubscribe(observable, subscriber);
    }

    public void getTransmitInfo(Subscriber<ResponseBody> subscriber, JsonObject jsonObject, Context context) {
        if (getTransmitInfo == null) {
            getTransmitInfo = Network.getRetrofitWithHeader(context).create(GetTransmitInfo.class);
        }
        Observable<ResponseBody> observable = getTransmitInfo.start(jsonObject);
        toSubscribe(observable, subscriber);
    }

    public void refreshPublished(Subscriber<ResponseBody> subscriber, JsonObject jsonObject, Context context) {
        if (refreshPublished == null) {
            refreshPublished = Network.getRetrofitWithHeader(context).create(RefreshPublished.class);
        }
        Observable<ResponseBody> observable = refreshPublished.start(jsonObject);
        toSubscribe(observable, subscriber);
    }

    public void transmit(Subscriber<ResponseBody> subscriber, JsonObject jsonObject, Context context) {
        if (transmitApi == null) {
            transmitApi = Network.getRetrofitWithHeader(context).create(TransmitApi.class);
        }
        Observable<ResponseBody> observable = transmitApi.start(jsonObject);
        toSubscribe(observable, subscriber);
    }

    public void deletePublished(Subscriber<ResponseBody> subscriber, JsonObject jsonObject, Context context) {
        if (deletePublished == null) {
            deletePublished = Network.getRetrofitWithHeader(context).create(DeletePublished.class);
        }
        Observable<ResponseBody> observable = deletePublished.start(jsonObject);
        toSubscribe(observable, subscriber);
    }

    public void operate(Subscriber<ResponseBody> subscriber, JsonObject jsonObject, Context context) {
        if (operate == null) {
            operate = Network.getRetrofitWithHeader(context).create(Operate.class);
        }
        Observable<ResponseBody> observable = operate.start(jsonObject);
        toSubscribe(observable, subscriber);
    }

    public void cancleOperate(Subscriber<ResponseBody> subscriber, JsonObject jsonObject, Context context) {
        if (cancelOperate == null) {
            cancelOperate = Network.getRetrofitWithHeader(context).create(CancelOperate.class);
        }
        Observable<ResponseBody> observable = cancelOperate.start(jsonObject);
        toSubscribe(observable, subscriber);
    }

    public void changePublishStatu(Subscriber<ResponseBody> subscriber, JsonObject jsonObject, Context context) {
        if (changePublishStatu == null) {
            changePublishStatu = Network.getRetrofitWithHeader(context).create(ChangePublishStatu.class);
        }
        Observable<ResponseBody> observable = changePublishStatu.start(jsonObject);
        toSubscribe(observable, subscriber);
    }

    public void getCollect(Subscriber<ResponseBody> subscriber, JsonObject jsonObject, Context context) {
        if (getCollect == null) {
            getCollect = Network.getRetrofitWithHeader(context).create(GetCollect.class);
        }
        Observable<ResponseBody> observable = getCollect.start(jsonObject);
        toSubscribe(observable, subscriber);
    }

    public void getCalendar(Subscriber<ResponseBody> subscriber, JsonObject jsonObject, Context context) {
        if (getCalendar == null) {
            getCalendar = Network.getRetrofitWithHeader(context).create(GetCalendar.class);
        }
        Observable<ResponseBody> observable = getCalendar.start(jsonObject);
        toSubscribe(observable, subscriber);
    }

    public void getCalendarInputer(Subscriber<ResponseBody> subscriber, JsonObject jsonObject, Context context) {
        if (getCalendarInputer == null) {
            getCalendarInputer = Network.getRetrofitWithHeader(context).create(GetCalendarInputer.class);
        }
        Observable<ResponseBody> observable = getCalendarInputer.start(jsonObject);
        toSubscribe(observable, subscriber);
    }

    public void getJoinedPlan(Subscriber<ResponseBody> subscriber, JsonObject jsonObject, Context context) {
        if (getJoinedPlan == null) {
            getJoinedPlan = Network.getRetrofitWithHeader(context).create(GetJoinedPlan.class);
        }
        Observable<ResponseBody> observable = getJoinedPlan.start(jsonObject);
        toSubscribe(observable, subscriber);
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    //在访问PublishNetwork时创建单例
    private static class SingletonHolder {
        private static final PublishNetwork INSTANCE = new PublishNetwork();
    }

}
