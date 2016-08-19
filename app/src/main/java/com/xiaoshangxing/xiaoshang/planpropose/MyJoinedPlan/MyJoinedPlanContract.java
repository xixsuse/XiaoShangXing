package com.xiaoshangxing.xiaoshang.planpropose.MyJoinedPlan;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;

/**
 * Created by quchwe on 2016/7/23 0023.
 */
public interface MyJoinedPlanContract {
    interface Presenter extends IBasePresenter {
            void getJoinedPlanList();

    }
    interface View extends IBaseView<Presenter> {
            void showPlanList();
            void showDialog();
    }
}
