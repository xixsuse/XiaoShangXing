package com.xiaoshangxing.login_register.LoginRegisterActivity.InputAccountFragment;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;

/**
 * Created by FengChaoQun
 * on 2016/6/24
 */
public class InputAccountContract {

    public interface View extends IBaseView<Presenter> {
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
    }

    public interface Presenter extends IBasePresenter {
        void isContentOK();

        void clickOnButton();
    }
}
