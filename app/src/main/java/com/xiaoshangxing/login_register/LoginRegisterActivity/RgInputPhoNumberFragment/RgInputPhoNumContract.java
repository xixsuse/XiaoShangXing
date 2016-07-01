package com.xiaoshangxing.login_register.LoginRegisterActivity.RgInputPhoNumberFragment;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;

/**
 * Created by FengChaoQun
 * on 2016/6/22
 */
public class RgInputPhoNumContract {

    public interface View extends IBaseView<Presenter> {
        /*
        **describe:设置按钮可否点击
        */
        void setButtonEnable(boolean enable);

        /*
        **describe:获取手机号
        */
        String getPhoneNum();

        /*
        **describe:弹出该账号已注册对话框
        */
        void showRegisteredDialog();

        /*
        **describe:弹出确认对话框
        */
        void showSureDialog();

        /*
         **describe:显示、关闭LoadingDialog
         */
        void showLoadingDialog();

        void hideLoadingDialog();
    }

    public interface Presenter extends IBasePresenter {
        /*
        **describe:内容是否合法
        */
        void isContentOK();

        /*
        **describe:点击注册按钮
        */
        void clickOnRegister();

        /*
        **describe:点击取消按钮
        */
        void clickOnCancer();

    }
}
