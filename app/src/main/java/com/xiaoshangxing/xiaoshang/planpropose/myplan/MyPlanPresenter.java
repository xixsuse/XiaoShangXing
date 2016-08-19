package com.xiaoshangxing.xiaoshang.planpropose.myplan;

import android.support.annotation.NonNull;

import com.xiaoshangxing.data.Bean;
import com.xiaoshangxing.xiaoshang.planpropose.PlanList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quchwe on 2016/7/25 0025.
 */
public class MyPlanPresenter implements MyPlanContract.Presenter {

    private final MyPlanContract.View mView;
    private final Bean bean;

    public MyPlanPresenter(@NonNull MyPlanContract.View view,@NonNull Bean bean){
        this.mView = view;
        this.bean = bean;
        mView.setmPresenter(this);
    }
    @Override
    public List<PlanList> getMyPlanList() {

        List<PlanList> planLists = new ArrayList<>();
        PlanList p = new PlanList();
        p.setFull(true);
        p.setLimitPepoleNumber(1);
        p.setCompleted(true);
        p.setAcademy("设计学院");
        p.setName("孙路阳");
        p.setPlanName("春游小分队" );
        p.setText(
                "明日天气晴朗，适合出游哦");
//        p.setBitmap();
        p.setTime("20分钟前");
        p.setPaticipateNumber(1);
        planLists.add(p);
        return planLists;
    }
}
