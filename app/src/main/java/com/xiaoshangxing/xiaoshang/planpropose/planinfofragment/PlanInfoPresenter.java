package com.xiaoshangxing.xiaoshang.planpropose.planinfofragment;

import android.support.annotation.NonNull;

import com.xiaoshangxing.data.Bean;
import com.xiaoshangxing.xiaoshang.planpropose.PlanList;

/**
 * Created by quchwe on 2016/7/23 0023.
 */
public class PlanInfoPresenter implements PlanInfoContract.Presenter {

    private final PlanInfoContract.View mView;
    private final Bean bean;


    public PlanInfoPresenter(@NonNull Bean bean,@NonNull PlanInfoContract.View view){
        this.bean = bean;
        this.mView = view;
        mView.setmPresenter(this);
    }
    @Override
    public void joinPlan() {

    }

    @Override
    public void inviteFriends() {

    }

    @Override
    public PlanList getPlanList() {
        return null;
    }
}
