package com.xiaoshangxing.xiaoshang.ShoolfellowHelp.ShoolfellowHelpFragment;

import android.view.View;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;

/**
 * Created by FengChaoQun
 * on 2016/7/22
 */
public class ShoolHelpContract {
    public interface View extends IBaseView<Presenter> {
        /*
        **describe:点击右上角显示菜单
        */
        void showPublishMenu(android.view.View v);

        void gotoPublish();

        void gotoPublished();

        void toastErro();
    }

    public interface Presenter extends IBasePresenter {

        /*
        **describe:刷新数据
        */
        void refreshDate();
        /*
        **describe:加载更多数据
        */
        void loadMore();
        /*
        **describe:转发 评论 赞
        */
        void transmit();
        void comment();
        void praise();
    }
}
