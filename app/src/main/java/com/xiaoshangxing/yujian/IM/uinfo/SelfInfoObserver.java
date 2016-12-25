package com.xiaoshangxing.yujian.IM.uinfo;

import android.content.Context;
import android.os.Handler;

import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.yujian.IM.NimUIKit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FengChaoQun
 * on 2016/12/24
 */

public class SelfInfoObserver {
    private List<SelfInfoCallback> observers = new ArrayList<>();
    private Handler uiHandler;
    private static SelfInfoObserver instance;

    private SelfInfoObserver(Context context) {
        uiHandler = new Handler(context.getMainLooper());
    }

    public static SelfInfoObserver getInstance() {
        if (instance == null) {
            instance = new SelfInfoObserver(NimUIKit.getContext());
        }
        return instance;
    }

    //  添加
    synchronized public void registerObserver(SelfInfoCallback observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    //  移除
    synchronized public void unregisterObserver(SelfInfoCallback observer) {
        if (observer != null) {
            observers.remove(observer);
        }
    }

    synchronized public void registerObserver(SelfInfoCallback observer, boolean is) {
        if (is) {
            registerObserver(observer);
        } else {
            unregisterObserver(observer);
        }
    }

    //  有变化时通知
    synchronized public void notifyObservers(final List<NimUserInfo> nimUserInfos) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                for (NimUserInfo userInfo : nimUserInfos) {
                    if (userInfo.getAccount().equals(TempUser.getId())) {
                        for (SelfInfoCallback observer : observers) {
                            observer.onCallback(userInfo);
                        }
                    }
                }
            }
        });
    }

    public interface SelfInfoCallback {
        void onCallback(NimUserInfo userInfo);
    }
}
