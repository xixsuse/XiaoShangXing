package com.xiaoshangxing.loginAndRegister.LoginRegisterActivity.InputAccountFragment;

import com.xiaoshangxing.utils.baseClass.IBasePresenter;
import com.xiaoshangxing.utils.baseClass.IBaseView;

/**
 * Created by FengChaoQun
 * on 2016/6/24
 */
public interface InputAccountContract {

    interface View extends IBaseView<Presenter> {
        void clickOnBack();

        String getPhoneNumber();

        void setButtonState(boolean state);

        /*
        **describe:跳转到发送邮箱界面
        */
        void gotoSendedEmail();

        /*
       **describe:跳转到未绑定邮箱界面
       */
        void gotoNoEmail();

        /*
       **describe:储存号码
       */
        void savePhoneNumber();

        /*
        **describe:弹出未注册对话框
        */
        void showNoRegister();

        /*
        **describe:跳转到注册界面
        */
        void gotoRegister();

        void gotoVerticyEmailCode();


    }

    interface Presenter extends IBasePresenter {
        void isContentOK();

        void clickOnButton();
    }
}
