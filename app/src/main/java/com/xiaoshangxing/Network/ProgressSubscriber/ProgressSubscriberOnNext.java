package com.xiaoshangxing.Network.ProgressSubscriber;

import org.json.JSONException;

/**
 * Created by FengChaoQun
 * on 2016/8/4
 */
public interface ProgressSubscriberOnNext<T> {
    void onNext(T e) throws JSONException;
}
