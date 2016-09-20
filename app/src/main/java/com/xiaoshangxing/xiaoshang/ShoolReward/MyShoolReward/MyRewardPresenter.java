package com.xiaoshangxing.xiaoshang.ShoolReward.MyShoolReward;

import android.content.Context;

import com.xiaoshangxing.Network.netUtil.LoadUtils;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/8/6
 */
public class MyRewardPresenter implements MyRewardContract.Presenter {
    private MyRewardContract.View mView;
    private Context context;
    private Realm realm;

    public MyRewardPresenter(MyRewardContract.View mView, Context context, Realm realm) {
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
//        LoadUtils.getSelfState(frame, mView, realm, NS.CATEGORY_REWARD, new LoadUtils.AroundLoading() {
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
