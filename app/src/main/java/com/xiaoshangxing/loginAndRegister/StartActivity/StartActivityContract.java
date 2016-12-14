package com.xiaoshangxing.loginAndRegister.StartActivity;

import com.xiaoshangxing.utils.baseClass.IBasePresenter;
import com.xiaoshangxing.utils.baseClass.IBaseView;

/**
 * Created by FengChaoQun
 * on 2016/6/22
 */
public interface StartActivityContract {

    interface View extends IBaseView<Presenter> {
        /*
        **describe:显示按钮
        */
        void showButton();

        /*
        **describe:跳转到LoginRegisterActivity
        */
        void intentLoginRegisterActivity();

        /*
        **describe:跳转到主界面
        */
        void intentMainActivity();

        /*
        **describe:跳转到登录页面
        */
        void gotoLogin();

        /*
        **describe:跳转到注册页面
        */
        void gotoRegister();
    }

    interface Presenter extends IBasePresenter {
        /*
        **describe:开始等待计时
        */
        void startWait();

        /*
        **describe:是否第一次进入APP
        */
        boolean isFirstCome();

        /*
        **describe:最近是否有退出操作
        */
        boolean isQuit();

        void clickOnLogin();

        void clickOnRegister();
    }
}
