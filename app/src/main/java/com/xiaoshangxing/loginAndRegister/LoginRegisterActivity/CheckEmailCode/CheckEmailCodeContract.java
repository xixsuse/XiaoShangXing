package com.xiaoshangxing.loginAndRegister.LoginRegisterActivity.CheckEmailCode;

import com.xiaoshangxing.utils.baseClass.IBasePresenter;
import com.xiaoshangxing.utils.baseClass.IBaseView;

/**
 * Created by FengChaoQun
 * on 2016/6/23
 */
public interface CheckEmailCodeContract {

    interface View extends IBaseView<Presenter> {

        /*
        **describe:设置手机号
        */
        void setPhoneNumber(String phoneNumber);

        /*
        **describe:获取验证码
        */
        String getVertifyCode();

        /*
        **describe:设置提交按钮点击状态
        */
        void setButtonEnable(boolean enable);

        /*
       **describe:弹出验证码错误对话框
       */
        void showFailDialog();

        /*
       **describe:弹出收不到验证码菜单
       */
        void showNoDialogMenu();

        /*
        **describe:跳转到重置密码界面
         */
        void gotoResetPassword();

        /*
        **describe:获取手机号码
        */
        String getPhone();

    }

    interface Presenter extends IBasePresenter {

        /*
        **describe:点击提交按钮
        */
        void clickOnSubmit();


        /*
        **describe:输入内容是否完整
        */
        void isContentOK();

        /*
        **describe:获取验证码
        */
        void getCode();
    }
}
