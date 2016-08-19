package com.xiaoshangxing.xiaoshang.idlesale.MyIdleSale;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;

/**
 * Created by quchwe on 2016/8/7 0007.
 */

public interface MyIdleContract  {
    interface Presenter extends IBasePresenter {

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
