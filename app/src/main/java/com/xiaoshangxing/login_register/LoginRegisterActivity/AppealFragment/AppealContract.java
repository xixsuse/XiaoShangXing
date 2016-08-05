package com.xiaoshangxing.login_register.LoginRegisterActivity.AppealFragment;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;

/**
 * Created by FengChaoQun
 * on 2016/6/25
 */
public interface AppealContract {

    interface View extends IBaseView<Presenter> {
        void clickOnBack();

        void setButtonState(boolean state);

        void gotoAppealComplete();

        String getName();

        String getPhoneNumber();

        String getID();

        String getSchool();

        String getStudentID();

        String getEC();

        String getGoSchoolTime();

        String getRegisterTime();
    }

    interface Presenter extends IBasePresenter {
        void isContentOK();

        void clickOnAppeal();
    }
}
