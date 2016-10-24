package com.xiaoshangxing.utils;

import android.support.annotation.Nullable;

import rx.Subscription;

/**
 * Created by FengChaoQun
 * on 2016/6/22
 */
public interface IBaseView<T> {
    void setmPresenter(@Nullable T presenter);

    /*
     **describe:显示、关闭LoadingDialog
     */
    void showLoadingDialog(String text);

    void hideLoadingDialog();

    void  setonDismiss(LoadingDialog.onDismiss on);

    /*
    **describe: Toast
    */
    void showToast(String toast);
}
