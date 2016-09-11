package com.xiaoshangxing.wo.myState;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/8/5
 */
public interface StateContract {

    interface View extends IBaseView<Presenter> {
        /*
        **describe:判断是自己的动态还是别人的
        */
        void typeOfState();
        /*
        **describe:跳转到消息界面
        */
        void gotoNews();
        /*
        **describe:刷新页面数据
        */
        void refreshData();

        /*
       **describe:设置刷新状态
       */
        void setRefreshState(boolean is);

        /*
        **describe:设置加载状态
        */
        void setLoadState(boolean is);

        /*
       **describe:获取realm
       */
        Realm getRealm();
    }

    interface Presenter extends IBasePresenter {

        void LoadData(PtrFrameLayout frame);

        boolean isNeedRefresh();
    }
}
