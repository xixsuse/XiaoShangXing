package com.xiaoshangxing.login_register.LoginRegisterActivity.LoginFragment;

import android.content.Context;

import com.xiaoshangxing.Network.netUtil.AppNetUtil;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

/**
 * Created by FengChaoQun
 * on 2016/6/22
 */
public class LoginFragmentPresenter implements LoginFragmentContract.Presenter {
    private LoginFragmentContract.View mView;
    private Context context;

    public LoginFragmentPresenter(LoginFragmentContract.View view, Context context) {
        this.mView = view;
        this.context = context;
    }

    @Override
    public void isContentOK() {
        if (mView.getPhoneNumber().length() == 11 && mView.getPassword().length() >= 6) {
            mView.setButtonEnable(true);
        } else {
            mView.setButtonEnable(false);
        }
    }

    @Override
    public void isHasHeadPotrait() {
        String count = (String) SPUtils.get(context, SPUtils.PHONENUMNBER, SPUtils.DEFAULT_STRING);
        if (count.equals(SPUtils.DEFAULT_STRING)) {
            return;
        }
        if (mView.getPhoneNumber().equals(SPUtils.get(context, SPUtils.PHONENUMNBER, SPUtils.DEFAULT_STRING))) {
            mView.showHeadPotrait(true);
        } else {
            mView.showHeadPotrait(false);
        }
    }

    @Override
    public void loginWithAccount(String number) {
        mView.setPhoneNumber(number);
        isHasHeadPotrait();
    }

    @Override
    public void clickOnLogin() {

        AppNetUtil.LoginXSX(mView.getPhoneNumber(), mView.getPassword(), context, mView, new AppNetUtil.LoginXSXback() {
            @Override
            public void onSuccess() {
                mView.gotoMainActivity();
            }

            @Override
            public void onError(String msg) {
                mView.showFailDialog(msg);
            }
        });
    }

    @Override
    public void clickOnRetrievePassword() {
        mView.showRetrievePasswordMenu();
    }

    @Override
    public void clickOnRegister() {
        mView.gotoRegister();
    }
}
