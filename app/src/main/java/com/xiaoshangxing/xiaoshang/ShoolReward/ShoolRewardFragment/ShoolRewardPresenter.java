package com.xiaoshangxing.xiaoshang.ShoolReward.ShoolRewardFragment;

import android.content.Context;

import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/8/6
 */
public class ShoolRewardPresenter implements ShoolRewardContract.Presenter {
    private ShoolRewardContract.View mView;
    private Context context;

    public ShoolRewardPresenter(ShoolRewardContract.View mView, Context context) {
        this.mView = mView;
        this.context = context;
    }

    @Override
    public boolean isNeedRefresh() {
        return true;
    }

    @Override
    public void RefreshData(PtrFrameLayout frame, Realm realm) {

    }


    @Override
    public void LoadMore() {

    }

    @Override
    public void collect() {
        mView.noticeDialog("已收藏");
    }
}
