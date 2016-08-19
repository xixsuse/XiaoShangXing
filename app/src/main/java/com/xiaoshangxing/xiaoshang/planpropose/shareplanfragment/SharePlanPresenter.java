package com.xiaoshangxing.xiaoshang.planpropose.shareplanfragment;

import android.support.annotation.NonNull;

import com.xiaoshangxing.data.Bean;

/**
 * Created by quchwe on 2016/7/25 0025.
 */
public class SharePlanPresenter implements SharePlanContract.Presenter {
    private final SharePlanContract.View mView;
    private final Bean bean;

    public SharePlanPresenter(@NonNull SharePlanContract.View view,@NonNull Bean bean){
        this.mView = view;
        this.bean = bean;
        mView.setmPresenter(this);
    }

    @Override
    public void saveShareInfo(String info) {

    }
}
