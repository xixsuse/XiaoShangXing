package com.xiaoshangxing.xiaoshang.planpropose.editplanfragment;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.xiaoshang.planpropose.PlanList;

/**
 * Created by quchwe on 2016/7/23 0023.
 */
public interface NewPlanContract {
    interface Presenter extends IBasePresenter {
        void saveNewPlan(PlanList planList);

    }
    interface View extends IBaseView<Presenter> {
        void showEditComplete();
        void getNewPlan();
        void showEditError();

    }

}
