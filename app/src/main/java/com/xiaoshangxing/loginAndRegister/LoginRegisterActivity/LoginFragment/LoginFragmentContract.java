package com.xiaoshangxing.loginAndRegister.LoginRegisterActivity.LoginFragment;

import com.xiaoshangxing.utils.baseClass.IBasePresenter;
import com.xiaoshangxing.utils.baseClass.IBaseView;

/**
 * Created by FengChaoQun
 * on 2016/6/22
 */
public interface LoginFragmentContract {

    interface View extends IBaseView<Presenter> {
        /*
        **describe:显示用户头像
        */
        void showHeadPotrait(boolean is);

        /*
        **describe:获取用户输入的号码和密码
        */
        String getPhoneNumber();

        /*
        **describe:设置账号
        */
        void setPhoneNumber(String number);

        String getPassword();

        /*
        **describe:设置按钮可点击
        */
        void setButtonEnable(boolean is_enable);

        /*
        **describe:弹出验证失败对话框
        */
        void showFailDialog(String error);

        /*
       **describe:跳转到注册界面
       */
        void gotoRegister();

        /*
       **describe:显示找回密码菜单
       */
        void showRetrievePasswordMenu();

        /*
        **describe:跳转到主页面
        */
        void gotoMainActivity();

    }

    interface Presenter extends IBasePresenter {
        /*
        **describe:内容是否齐全
        */
        void isContentOK();

        /*
        **describe:该账号在本地是否有头像
        */
        void isHasHeadPotrait();

        /*
        **describe:有账号登录
        */
        void loginWithAccount(String number);

        /*
        **describe:各个点击事件
        */
        void clickOnLogin();

        void clickOnRetrievePassword();

        void clickOnRegister();
    }
}
