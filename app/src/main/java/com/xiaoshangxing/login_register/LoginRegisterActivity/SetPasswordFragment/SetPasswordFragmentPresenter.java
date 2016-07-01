package com.xiaoshangxing.login_register.LoginRegisterActivity.SetPasswordFragment;

/**
 * Created by FengChaoQun
 * on 2016/6/24
 */
public class SetPasswordFragmentPresenter implements SetPasswordContract.Presenter {
    private SetPasswordContract.View mView;

    public SetPasswordFragmentPresenter(SetPasswordContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void isContentOK() {
        if (mView.getPassword().length() >= 8) {
            mView.setButtonState(true);
        } else {
            mView.setButtonState(false);
        }
    }

    @Override
    public void clickOnCompleteButton() {

//        String str=mView.getPassword();
//        boolean isok=false;
//
//        for(int i=str.length();--i>=0;){
//            int chr=str.charAt(i);
//            if(chr<48 || chr>57){
//                isok=true;
//                break;
//            }
//
//        }


        mView.showRegisterSuccess();

    }
}
