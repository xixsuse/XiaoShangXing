package com.xiaoshangxing.xiaoshang.Sale.SaleFrafment;

import android.widget.EditText;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/7/22
 */
public class SaleContract {

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
        void showCollectDialog(int id);

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
        **describe:弹出收起键盘
        */
        void showEdittext(boolean is, EditText editText);
        /*
        **describe:弹出发布菜单
        */
        void showPublishType();
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

        /*
        **describe:收藏
        */
        void collect();
    }
}
