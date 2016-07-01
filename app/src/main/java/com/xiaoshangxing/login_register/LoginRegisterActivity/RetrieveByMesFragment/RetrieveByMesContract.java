package com.xiaoshangxing.login_register.LoginRegisterActivity.RetrieveByMesFragment;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;

/**
 * Created by FengChaoQun
 * on 2016/6/24
 */
public class RetrieveByMesContract {

    public interface View extends IBaseView<Presenter> {
        /*
        **describe:返回
        */
        void back();

        /*
        **describe:获取手机号
        */
        String getPhoneNumber();

        /*
        **describe:设置按钮状态
        */
        void setButtonState(boolean state);

        /*
        **describe:弹出确认对话框
        */
        void showSure();
    }

    public interface Presenter extends IBasePresenter {
        void isContentOK();

        void clickOnNext();

    }
}
