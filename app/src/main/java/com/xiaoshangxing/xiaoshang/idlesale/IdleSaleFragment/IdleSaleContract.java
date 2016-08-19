package com.xiaoshangxing.xiaoshang.idlesale.IdleSaleFragment;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.xiaoshang.idlesale.IdleBean;

import java.util.List;

/**
 * Created by quchwe on 2016/7/30 0030.
 */
public interface IdleSaleContract {
    interface Presenter extends IBasePresenter {
        List<IdleBean> getList();
        void sendMessage(String message);


        void RefreshData();
    }
    interface View extends IBaseView<Presenter> {
        void showPop();
        void showReleseDialog();
        void showFavoriteDialog();
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
