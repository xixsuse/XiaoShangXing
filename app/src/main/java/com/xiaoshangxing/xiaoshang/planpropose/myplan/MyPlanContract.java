package com.xiaoshangxing.xiaoshang.planpropose.myplan;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.xiaoshang.planpropose.PlanList;

import java.util.List;

/**
 * Created by quchwe on 2016/7/25 0025.
 */
public interface MyPlanContract {
    interface Presenter extends IBasePresenter {
        List<PlanList> getMyPlanList();
    }
    interface View extends IBaseView<Presenter> {
        void showMyPlanList();
        void showPlanLongClick();

        void setRvClick();
        void showDialog();
        void longClickPop();
        void showSelect();
        void showDeleteDialog(boolean b);
        void showNoPlan();

        void showCancelPlan();
    }
}
