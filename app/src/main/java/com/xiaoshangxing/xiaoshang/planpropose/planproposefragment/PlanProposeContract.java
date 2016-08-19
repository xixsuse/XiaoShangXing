package com.xiaoshangxing.xiaoshang.planpropose.planproposefragment;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.xiaoshang.planpropose.PlanList;

import java.util.List;

/**
 * Created by quchwe on 2016/7/22 0022.
 */
public interface PlanProposeContract {

    interface Presenter extends IBasePresenter {
        List<PlanList> getPlanList();

        void toPlanInfo();
        void popClick();
        /*
            **describe:判断是否需要自动刷新
            */
        boolean isNeedRefresh();

        /*
        **describe:刷新
        */
        void RefreshData();

        /*
        **describe:加载更多
        */
        void LoadMore();
    }

    interface  View extends IBaseView<Presenter> {
        void showDialog(PlanList planList);
        void showPopupClick();
        void setRvOnClick();
        void sharePlan();
        void showShareDialog(PlanList planList);
        /*
//      **describe:设置刷新状态
//      */
        void setRefreshState(boolean is);

        /*
        **describe:获取刷新状态
        */
        boolean isRefreshing();

        /*
        **describe:设置加载状态
        */
        void setLoadState(boolean is);

        /*
        **describe:获取加载状态
        */
        boolean isLoading();

        /*
        **describe:刷新页面
        */
        void refreshPager();

        /*
        **describe:自动下拉刷新
        */
        void autoRefresh();

        /*
        **describe:没有数据了
        */
        void showNoData();

        /*
       **describe:显示listview尾
       */
        void showFooter();

        /*
        **describe:点击公告规则
        */
        void clickOnRule();

    }
}
