package com.xiaoshangxing.yujian.IM.uinfo;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户资料变动观察者。
 */
public class UserInfoObservable {

    private List<UserInfoObserver> observers = new ArrayList<>();
    private Handler uiHandler;

    public UserInfoObservable(Context context) {
        uiHandler = new Handler(context.getMainLooper());
    }
//  添加
    synchronized public void registerObserver(UserInfoObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }
//  移除
    synchronized public void unregisterObserver(UserInfoObserver observer) {
        if (observer != null) {
            observers.remove(observer);
        }
    }
//  有变化时通知
    synchronized public void notifyObservers(final List<String> accounts) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                for (UserInfoObserver observer : observers) {
                    observer.onUserInfoChanged(accounts);
                }
            }
        });
    }

    public interface UserInfoObserver {
        void onUserInfoChanged(List<String> accounts);
    }
}
