package com.xiaoshangxing.login_register.LoginRegisterActivity.SchoolNoOpenFragment;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;

/**
 * Created by FengChaoQun
 * on 2016/6/25
 */
public interface SchoolNoOpenContract {

    interface View extends IBaseView<Presenter> {
        void clickOnCancle();

        void setSchool(String school);

        void showSuccess();
    }

    interface Presenter extends IBasePresenter {
        void clickOnButton();
    }
}
