package com.xiaoshangxing.network.netUtil;

/**
 * Created by FengChaoQun
 * on 2016/9/19
 */
public interface SimpleCallBack {
    void onSuccess();

    void onError(Throwable e);

    void onBackData(Object o);
}
