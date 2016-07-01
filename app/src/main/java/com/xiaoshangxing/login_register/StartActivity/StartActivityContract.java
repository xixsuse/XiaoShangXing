package com.xiaoshangxing.login_register.StartActivity;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;

/**
 * Created by FengChaoQun
 * on 2016/6/22
 */
public interface StartActivityContract {

    public interface View extends IBaseView<Presenter> {
        /*
        **describe:显示按钮
        */
        void showButton();

        /*
        **describe:跳转到LoginRegisterActivity
        */
        void intentLoginRegisterActivity();
    }

    public interface Presenter extends IBasePresenter {
        /*
        **describe:开始等待计时
        */
        void startWait();

        /*
        **describe:是否第一次进入APP
        */
        boolean isFirstCome();

        void clickOnLogin();

        void clickOnRegister();
    }
}
