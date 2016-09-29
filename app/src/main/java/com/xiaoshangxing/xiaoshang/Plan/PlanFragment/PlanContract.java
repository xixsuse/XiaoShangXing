package com.xiaoshangxing.xiaoshang.Plan.PlanFragment;

import android.widget.EditText;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/7/22
 */
public class PlanContract {

    public interface View extends IBaseView<Presenter> {
        /*
        **describe:点击右上角显示菜单
        */
        void showPublishMenu(android.view.View v);

        void gotoPublish();

        void gotoPublished();

        void gotoJoined();


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
        **describe:刷新
        */
        void RefreshData(PtrFrameLayout frame, Realm realm);

        /*
        **describe:加载更多
        */
        void LoadMore();

    }
}
