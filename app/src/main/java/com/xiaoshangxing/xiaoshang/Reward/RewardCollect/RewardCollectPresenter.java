package com.xiaoshangxing.xiaoshang.Reward.RewardCollect;

import android.content.Context;

/**
 * Created by FengChaoQun
 * on 2016/8/6
 */
public class RewardCollectPresenter implements RewardCollectContract.Presenter {
    private RewardCollectContract.View mView;
    private Context context;

    public RewardCollectPresenter(RewardCollectContract.View mView, Context context) {
        this.mView = mView;
        this.context = context;
    }

    @Override
    public void delete() {

    }

    @Override
    public void transmit() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void unCollect() {
        mView.noticeDialog("已取消收藏");
    }
}
