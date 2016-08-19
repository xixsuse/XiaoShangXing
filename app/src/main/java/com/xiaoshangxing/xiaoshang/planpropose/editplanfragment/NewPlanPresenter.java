package com.xiaoshangxing.xiaoshang.planpropose.editplanfragment;

import android.support.annotation.NonNull;

import com.xiaoshangxing.data.Bean;
import com.xiaoshangxing.xiaoshang.planpropose.PlanList;

/**
 * Created by quchwe on 2016/7/23 0023.
 */
public class NewPlanPresenter implements NewPlanContract.Presenter {
    private final Bean bean;
    private final NewPlanContract.View mView;
    public NewPlanPresenter(@NonNull Bean bean,@NonNull NewPlanContract.View view){
        this.bean = bean;
        this.mView = view;
        mView.setmPresenter(this);
    }
    @Override
    public void saveNewPlan(PlanList planList) {

    }
}
