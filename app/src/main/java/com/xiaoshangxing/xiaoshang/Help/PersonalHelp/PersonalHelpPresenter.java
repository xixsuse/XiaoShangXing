package com.xiaoshangxing.xiaoshang.Help.PersonalHelp;

import android.content.Context;

import com.xiaoshangxing.utils.customView.pull_refresh.PtrFrameLayout;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/8/6
 */
public class PersonalHelpPresenter implements PersonalhelpContract.Presenter {
    private PersonalhelpContract.View mView;
    private Context context;
    private Realm realm;

    public PersonalHelpPresenter(PersonalhelpContract.View mView, Context context, Realm realm) {
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
