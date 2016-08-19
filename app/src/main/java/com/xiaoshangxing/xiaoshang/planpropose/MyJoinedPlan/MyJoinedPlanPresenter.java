package com.xiaoshangxing.xiaoshang.planpropose.MyJoinedPlan;

import android.support.annotation.NonNull;

import com.xiaoshangxing.data.Bean;

/**
 * Created by quchwe on 2016/7/25 0025.
 */
public class MyJoinedPlanPresenter implements MyJoinedPlanContract.Presenter {

    private final Bean bean;
    private final MyJoinedPlanContract.View view;

    public MyJoinedPlanPresenter(@NonNull MyJoinedPlanContract.View view, @NonNull Bean bean){
        this.bean = bean;
        this.view = view;
        this.view.setmPresenter(this);
    }


    @Override
    public void getJoinedPlanList() {

    }
}
