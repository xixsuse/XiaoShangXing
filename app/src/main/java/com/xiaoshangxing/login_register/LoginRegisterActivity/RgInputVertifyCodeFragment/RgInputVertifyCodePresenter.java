package com.xiaoshangxing.login_register.LoginRegisterActivity.RgInputVertifyCodeFragment;

import android.os.CountDownTimer;

/**
 * Created by FengChaoQun
 * on 2016/6/23
 */
public class RgInputVertifyCodePresenter implements RgInputVertifyCodeContract.Presenter {
    private RgInputVertifyCodeContract.View mView;
    private CountDownTimer countDownTimer;

    public RgInputVertifyCodePresenter(final RgInputVertifyCodeContract.View mView) {
        this.mView = mView;

        countDownTimer = new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mView.setRemainTime("接受短信大约需要" + millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                mView.setRemainTimeChange();
            }
        };
    }

    @Override
    public void clickOnSubmit() {
        if (!mView.getVertifyCode().equals("888888")) {
            mView.showFailDialog();
        } else {
            mView.gotoWhere();
        }
    }

    @Override
    public void startCountTime() {
        countDownTimer.start();
    }

    @Override
    public void stopCountTime() {
        countDownTimer.cancel();
        mView.setRemainTimeEnable(false);
    }

    @Override
    public void noReceiveCode() {
        mView.showNoDialogMenu();
    }

    @Override
    public void isContentOK() {
        if (mView.getVertifyCode().length() == 6) {
            mView.setButtonEnable(true);
        } else {
            mView.setButtonEnable(false);
        }
    }
}
