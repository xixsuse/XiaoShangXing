package com.xiaoshangxing.loginAndRegister.LoginRegisterActivity.AlterPasswordFragment;

import com.xiaoshangxing.utils.baseClass.IBasePresenter;
import com.xiaoshangxing.utils.baseClass.IBaseView;

/**
 * Created by FengChaoQun
 * on 2016/6/24
 */
public interface AlterPasswordContract {

    interface View extends IBaseView<Presenter> {
        void clickOnCancle();

        /*
        **describe:设置完成按钮状态
        */
        void setCompleteState(boolean state);

        /*
        **describe:获取手机号码
        */
        String getPhoneNumber();

        /*
        **describe:设置手机号码
        */
        void setPhoneNumber(String phoneNumber);

        /*
        **describe:获取密码
        */
        String getPassword1();

        String getPassword2();

        /*
        **describe:跳转到登录界面
        */
        void gotoLogin();

        /*
       **describe:弹出密码不一致对话框
       */
        void showPasswordDiffer();

        /*
      **describe:弹出修改成功对话框
      */
        void showAlterSuccess();
    }

    interface Presenter extends IBasePresenter {
        void isContentOK();

        void clickOnComplete();
    }
}
