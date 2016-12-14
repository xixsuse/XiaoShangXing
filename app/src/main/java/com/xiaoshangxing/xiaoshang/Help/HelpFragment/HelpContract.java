package com.xiaoshangxing.xiaoshang.Help.HelpFragment;

import com.xiaoshangxing.utils.baseClass.IBasePresenter;
import com.xiaoshangxing.utils.baseClass.IBaseView;
import com.xiaoshangxing.utils.customView.pull_refresh.PtrFrameLayout;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/7/22
 */
public class HelpContract {
    public interface View extends IBaseView<Presenter> {
        /*
        **describe:设置刷新状态
        */
        void setRefreshState(boolean is);

        /*
        **describe:设置加载状态
        */
        void setLoadState(boolean is);

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

        /*
        **describe:弹出发布菜单
        */
        void showPublishMenu(android.view.View view);

        /*
        **describe:前往发布界面
        */
        void gotoPublish();

        /*
        **describe:前往我的发布界面
        */
        void gotoPublished();
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
    }
}
