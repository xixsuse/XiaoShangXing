package com.xiaoshangxing.wo;

import android.content.Context;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;

/**
 * Created by FengChaoQun
 * on 2016/7/7
 */
public class WoContract {

    public interface View extends IBaseView<Presenter> {
        /*
        **describe:前往设置界面
        */
        void gotoSet();

        /*
        **describe:前往发表动态界面
        */
        void gotopublish();

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
        void setHead();

        /*
        **describe:设置news
        */
        void setNews();

        /*
       **describe:开始刷新
       */
        void startRefresh();

        /*
       **describe:刷新完成
       */
        void CompleteRefresh();

        /*
       **describe:刷新失败
       */
        void RefreshFail();

        /*
       **describe:开始加载更多
       */
        void startLoadMore();

        /*
       **describe:加载更多完成
       */
        void CompleteLoadMore();

        /*
       **describe:加载更多失败
       */
        void LoadMoreFail();


    }

    public interface Presenter extends IBasePresenter {

    }
}
