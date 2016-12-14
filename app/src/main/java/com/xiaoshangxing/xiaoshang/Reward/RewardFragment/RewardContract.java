package com.xiaoshangxing.xiaoshang.Reward.RewardFragment;

import com.xiaoshangxing.utils.baseClass.IBasePresenter;
import com.xiaoshangxing.utils.baseClass.IBaseView;
import com.xiaoshangxing.utils.customView.pull_refresh.PtrFrameLayout;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/7/22
 */
public class RewardContract {
    public interface View extends IBaseView<Presenter> {
        /*
        **describe:点击右上角显示菜单
        */
        void showPublishMenu(android.view.View v);

        void gotoPublish();

        void gotoPublished();

        void gotoCollect();


        /*
        **describe:显示收藏与否弹窗
        */
        void showCollectDialog(int id, boolean isCancle);

        /*
        **describe:收藏与取消时提示弹窗
        */
        void noticeDialog(String message);

        /*
       **describe:设置刷新状态
       */
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
        void clickOnRule(boolean is);

    }

    public interface Presenter extends IBasePresenter {

        /*
        **describe:判断是否需要自动刷新
        */
        boolean isNeedRefresh();

        /*
        **describe:刷新
        */
        void RefreshData(PtrFrameLayout frame, Realm realm);

        /*
        **describe:加载更多
        */
        void LoadMore();

        /*
        **describe:收藏
        */
        void collect();
    }
}
