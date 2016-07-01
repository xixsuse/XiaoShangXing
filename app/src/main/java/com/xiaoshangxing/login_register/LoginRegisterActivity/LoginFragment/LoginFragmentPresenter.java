package com.xiaoshangxing.login_register.LoginRegisterActivity.LoginFragment;

import android.content.Context;

import com.xiaoshangxing.R;

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
        if (mView.getPhoneNumber().length() == 11 && mView.getPassword().length() >= 8) {
            mView.setButtonEnable(true);
        } else {
            mView.setButtonEnable(false);
        }
    }

    @Override
    public void isHasHeadPotrait() {
        if (mView.getPhoneNumber().equals("88888888888")) {
            mView.showHeadPotrait(R.mipmap.ic_launcher);
        } else {
            mView.showHeadPotrait(R.mipmap.cirecleiamge_default);
        }
    }

    @Override
    public void loginWithAccount(String number) {
        mView.setPhoneNumber(number);
        isHasHeadPotrait();
    }

    @Override
    public void clickOnLogin() {
        if (mView.getPhoneNumber().equals("88888888888")) {
            mView.showLoadingDialog();
        } else {
            mView.showFailDialog();
        }

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
