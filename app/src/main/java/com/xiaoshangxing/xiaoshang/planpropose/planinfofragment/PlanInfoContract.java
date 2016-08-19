package com.xiaoshangxing.xiaoshang.planpropose.planinfofragment;


import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.xiaoshang.planpropose.PlanList;


/**
 * Created by quchwe on 2016/7/23 0023.
 */
public interface PlanInfoContract {
    interface Presenter extends IBasePresenter {
        void joinPlan();
        void inviteFriends();
        PlanList getPlanList();
    }
    interface View extends IBaseView<Presenter> {
            void onJoinPlanClick();
            void onInviteFriendsClick();
            void showFullPeople();
            void setViewInfo();

    }
}
