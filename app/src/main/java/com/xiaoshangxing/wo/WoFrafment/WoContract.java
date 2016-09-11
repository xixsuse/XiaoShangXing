package com.xiaoshangxing.wo.WoFrafment;

import android.content.Context;

import com.xiaoshangxing.input_activity.InputBoxLayout;
import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/7/7
 */
public interface WoContract {

   interface View extends IBaseView<Presenter> {
        /*
        **describe:前往设置界面
        */
        void gotoSet();

        /*
        **describe:前往发表动态界面
        */
        void gotopublish();

        /*
        **describe:前往新消息界面
        */
        void gotoNews();

      /*
      **describe:显示输入框
      */
        void showEdittext(Context context);

        /*
        **describe:隐藏输入框
        */
        void hideEdittext();

        /*
        **describe:显示listview尾
        */
        void showFooter();

        /*
       **describe:获取输入框内容
       */
        String getEdittextText();

        /*
        **describe:收缩导航栏
        */
        void hideTitle();

        void showTitle();

        /*
        **describe:设置名字
        */
        void setName(String name);

        /*
        **describe:设置头像
        */
        void setHead(String path);

        /*
        **describe:设置news
        */
        void setNews();

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
        **describe:设置输入框的回调
        */
        void setEditCallback(InputBoxLayout.CallBack callback);

       /*
       **describe:获取realm
       */
       Realm getRealm();
    }

   interface Presenter extends IBasePresenter {
      /*
      **describe:判断是否需要自动刷新
      */
      boolean isNeedRefresh();

      /*
      **describe:刷新
      */
      void RefreshData(PtrFrameLayout frame);

      /*
      **describe:加载更多
      */
      void LoadMore();

    }
}
