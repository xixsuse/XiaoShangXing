package com.xiaoshangxing.xiaoshang.Reward.PersonalReward;

import android.content.Context;

import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/8/6
 */
public class PersonalRewardPresenter implements PersonalRewardContract.Presenter {
    private PersonalRewardContract.View mView;
    private Context context;
    private Realm realm;

    public PersonalRewardPresenter(PersonalRewardContract.View mView, Context context, Realm realm) {
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
    public void refreshData(PtrFrameLayout frame) {
//        LoadUtils.getPersonalPublished(frame, mView, realm, NS.CATEGORY_REWARD, new LoadUtils.AroundLoading() {
//            @Override
//            public void before() {
//
//            }
//
//            @Override
//            public void complete() {
//                mView.refreshData();
//            }
//
//            @Override
//            public void error() {
//
//            }
//        });
    }

    @Override
    public void LoadMore() {

    }
}
