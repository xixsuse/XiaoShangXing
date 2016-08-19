package com.xiaoshangxing.xiaoshang.planpropose.planproposefragment;

import com.xiaoshangxing.data.Bean;
import com.xiaoshangxing.xiaoshang.planpropose.PlanList;
import com.xiaoshangxing.xiaoshang.planpropose.ReleasePopUp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quchwe on 2016/7/23 0023.
 */
public class PlanProposePresenter implements PlanProposeContract.Presenter {

    private final Bean bean;
    private final  PlanProposeContract.View mView;
    private ReleasePopUp mReleasePopup;
    public PlanProposePresenter(Bean bean,PlanProposeContract.View view){
        this.bean = bean;
        this.mView = view;
        mView.setmPresenter(this);
    }
    @Override
    public List<PlanList> getPlanList() {
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

        PlanList p1 = new PlanList();
        p1.setFull(false);
        p1.setLimitPepoleNumber(2);
        p1.setCompleted(true);
        p1.setAcademy("设计学院");
        p1.setName("孙路阳");
        p1.setPlanName("春游小分队" );
        p1.setText(
                "明日天气晴朗，适合出游哦");
//        p.setBitmap();
        p1.setTime("20分钟前");
        p1.setPaticipateNumber(1);
        p1.setCompleted(false);
        planLists.add(p1);
        return planLists;
    }

    @Override
    public void toPlanInfo() {
        mView.setRvOnClick();
    }

    @Override
    public void popClick() {
        mView.showPopupClick();
    }

    @Override
    public boolean isNeedRefresh() {
        return true;
    }

    @Override
    public void RefreshData() {

    }

    @Override
    public void LoadMore() {

    }


}
