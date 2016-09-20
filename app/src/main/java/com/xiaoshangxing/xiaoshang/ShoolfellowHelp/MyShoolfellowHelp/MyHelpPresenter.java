package com.xiaoshangxing.xiaoshang.ShoolfellowHelp.MyShoolfellowHelp;

import android.content.Context;

import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/8/6
 */
public class MyHelpPresenter implements MyhelpContract.Presenter {
    private MyhelpContract.View mView;
    private Context context;
    private Realm realm;

    public MyHelpPresenter(MyhelpContract.View mView, Context context, Realm realm) {
        this.mView = mView;
        this.context = context;
        this.realm = realm;
    }

    @Override
    public void transmit() {

    }

    @Override
    public void delete() {

    }

    @Override
    public void completeOrCancle() {

    }

    @Override
    public void refreshData(final PtrFrameLayout frame) {
    }

    @Override
    public void LoadMore() {

    }
}
