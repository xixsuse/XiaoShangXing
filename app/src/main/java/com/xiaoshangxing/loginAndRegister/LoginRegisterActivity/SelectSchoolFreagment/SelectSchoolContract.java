package com.xiaoshangxing.loginAndRegister.LoginRegisterActivity.SelectSchoolFreagment;

import com.xiaoshangxing.utils.baseClass.IBasePresenter;
import com.xiaoshangxing.utils.baseClass.IBaseView;

/**
 * Created by FengChaoQun
 * on 2016/6/25
 */
public interface SelectSchoolContract {

    interface View extends IBaseView<Presenter> {
        void clickOnSerch();

        void clickOnCurrent();

        void gotoAppeal();

        void gotoMain();

        /*
        **describe:设置listview的Adapter
        */
        void setAdapter(String[] arrayList);

        void setCurrentSchool(String school);
    }

    interface Presenter extends IBasePresenter {
        void clickOnReflesh();

        void isSchoolAccess(String name);

        /*
        **describe:加载数据
        */
        void refreshList();
    }

}
