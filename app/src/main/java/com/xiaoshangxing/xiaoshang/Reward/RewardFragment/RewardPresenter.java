package com.xiaoshangxing.xiaoshang.Reward.RewardFragment;

import android.content.Context;

import com.xiaoshangxing.utils.customView.pull_refresh.PtrFrameLayout;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/8/6
 */
public class RewardPresenter implements RewardContract.Presenter {
    private RewardContract.View mView;
    private Context context;

    public RewardPresenter(RewardContract.View mView, Context context) {
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
