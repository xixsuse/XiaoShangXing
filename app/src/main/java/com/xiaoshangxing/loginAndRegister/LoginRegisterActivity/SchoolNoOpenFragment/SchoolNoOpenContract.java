package com.xiaoshangxing.loginAndRegister.LoginRegisterActivity.SchoolNoOpenFragment;

import com.xiaoshangxing.utils.baseClass.IBasePresenter;
import com.xiaoshangxing.utils.baseClass.IBaseView;

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
