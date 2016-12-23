package com.xiaoshangxing.loginAndRegister.LoginRegisterActivity.LoginFragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaoshangxing.MainActivity;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.bean.User;
import com.xiaoshangxing.loginAndRegister.LoginRegisterActivity.InputAccountFragment.InputAccountFragment;
import com.xiaoshangxing.loginAndRegister.LoginRegisterActivity.LoginRegisterActivity;
import com.xiaoshangxing.loginAndRegister.LoginRegisterActivity.RetrieveByMesFragment.RetrieveByMesFragment;
import com.xiaoshangxing.loginAndRegister.LoginRegisterActivity.RgInputPhoNumberFragment.RgInputPhoNumberFragment;
import com.xiaoshangxing.loginAndRegister.StartActivity.StartActivity;
import com.xiaoshangxing.utils.XSXApplication;
import com.xiaoshangxing.utils.baseClass.BaseFragment;
import com.xiaoshangxing.utils.customView.CirecleImage;
import com.xiaoshangxing.utils.customView.ClearableEditTextWithIcon;
import com.xiaoshangxing.utils.customView.dialog.DialogLocationAndSize;
import com.xiaoshangxing.utils.customView.dialog.DialogUtils;
import com.xiaoshangxing.utils.customView.dialog.LoadingDialog;
import com.xiaoshangxing.utils.imageUtils.MyGlide;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

/**
 * Created by FengChaoQun
 * on 2016/6/22
 */
public class LoginFragment extends BaseFragment implements LoginFragmentContract.View, View.OnClickListener {

    public static final String TAG = BaseFragment.TAG + "-LoginFragment";

    public static final String LOGIN_WITH_NUMBER = "LOGIN_WITH_NUMBER";//携带账号登录

    private LoginFragmentContract.Presenter mPresenter;
    private View mview;
    private CirecleImage headPortrait;
    private ClearableEditTextWithIcon et_phoneNumber;
    private ClearableEditTextWithIcon et_passWord;
    private Button btn_login;
    private TextView tv_retrieve_password;
    private TextView tv_register;
    private String getNumber;
    private DialogUtils.Dialog_Center mDialogUtils;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mview = view;
        setmPresenter(new LoginFragmentPresenter(this, getActivity()));
        initView();
        getNumber = getActivity().getIntent().getStringExtra(LOGIN_WITH_NUMBER);
        if (!TextUtils.isEmpty(getNumber) && !getNumber.equals(SPUtils.DEFAULT_STRING)) {
            mPresenter.loginWithAccount(getNumber);
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView() {

        headPortrait = (CirecleImage) mview.findViewById(R.id.ci_headPortrait);
        et_phoneNumber = (ClearableEditTextWithIcon) mview.findViewById(R.id.et_account);
        et_passWord = (ClearableEditTextWithIcon) mview.findViewById(R.id.et_password);

        btn_login = (Button) mview.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        tv_retrieve_password = (TextView) mview.findViewById(R.id.tv_retrievePassword);
        tv_retrieve_password.setOnClickListener(this);

        tv_register = (TextView) mview.findViewById(R.id.tv_newRegister);
        tv_register.setOnClickListener(this);

        et_phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPresenter.isHasHeadPotrait();
                mPresenter.isContentOK();
            }
        });

        et_passWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPresenter.isContentOK();
            }
        });
    }

    @Override
    public void gotoRegister() {
        RgInputPhoNumberFragment frag = ((LoginRegisterActivity) mActivity).getRgInputPhoNumberFragment();
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .replace(R.id.loginregisterContent, frag)
                .addToBackStack(RgInputPhoNumberFragment.TAG)
                .commit();
    }

    @Override
    public void gotoMainActivity() {
        //记录当前账号
        SPUtils.put(getContext(), SPUtils.PHONENUMNBER, getPhoneNumber());

        Intent main_intent = new Intent(getContext(), MainActivity.class);
        main_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(main_intent);
        getActivity().finish();
        XSXApplication xsxApplication = (XSXApplication) getActivity().getApplication();
        xsxApplication.finish_activity(StartActivity.TAG);
    }

    @Override
    public void showFailDialog(String error) {
        mDialogUtils = new DialogUtils.Dialog_Center(mActivity);
        Dialog alertDialog = mDialogUtils.Message(/*"账号或密码错误，请重新填写。"*/error).Button("确定")
                .MbuttonOnClick(new DialogUtils.Dialog_Center.buttonOnClick() {
                    @Override
                    public void onButton1() {
                        mDialogUtils.close();
                    }

                    @Override
                    public void onButton2() {

                    }
                }).create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        DialogLocationAndSize.setWidth(alertDialog, R.dimen.x780);
    }

    public void setmPresenter(@Nullable LoginFragmentContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void showHeadPotrait(boolean is) {
        if (is) {
            User user = realm.where(User.class).equalTo("phone", getPhoneNumber()).findFirst();
            if (user != null) {
                if (!TextUtils.isEmpty(user.getUserImage())) {
                    MyGlide.with(this, user.getUserImage(), headPortrait);
                }
            }
        } else {
            headPortrait.setImageResource(R.mipmap.cirecleimage_default);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                mPresenter.clickOnLogin();
                break;
            case R.id.tv_retrievePassword:
                mPresenter.clickOnRetrievePassword();
                break;
            case R.id.tv_newRegister:
                mPresenter.clickOnRegister();
                break;
        }
    }

    @Override
    public String getPhoneNumber() {
        return et_phoneNumber.getText().toString();
    }

    @Override
    public void setPhoneNumber(String number) {
        et_phoneNumber.setText(number);
        et_phoneNumber.setSelection(et_phoneNumber.getText().length());
    }

    @Override
    public String getPassword() {
        return et_passWord.getText().toString();
    }

    @Override
    public void showRetrievePasswordMenu() {
        DialogUtils.DialogMenu mActionSheet = new DialogUtils.DialogMenu(getActivity());
        mActionSheet.addMenuItem("短信验证找回").addMenuItem("邮箱验证找回");
        mActionSheet.show();
        DialogLocationAndSize.bottom_FillWidth(getActivity(), mActionSheet);
        mActionSheet.setMenuListener(new DialogUtils.DialogMenu.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                switch (position) {
                    case 0:
                        RetrieveByMesFragment frag = ((LoginRegisterActivity) mActivity).getRetrieveByMesFragment();
                        getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                                        R.anim.slide_in_left, R.anim.slide_out_left)
                                .replace(R.id.loginregisterContent, frag)
                                .addToBackStack(RgInputPhoNumberFragment.TAG)
                                .commit();
                        break;
                    case 1:
                        InputAccountFragment frag2 = ((LoginRegisterActivity) mActivity).getInputEmailFragment();
                        getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                                        R.anim.slide_in_left, R.anim.slide_out_left)
                                .replace(R.id.loginregisterContent, frag2)
                                .addToBackStack(InputAccountFragment.TAG)
                                .commit();
                        break;
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void setButtonEnable(boolean is_enable) {
        if (is_enable) {
            btn_login.setEnabled(true);
            btn_login.setAlpha(1);
        } else {
            btn_login.setEnabled(false);
            btn_login.setAlpha(0.5f);
        }
        Log.d("enable", "-" + is_enable);
    }

    @Override
    public String toString() {
        return TAG;
    }
}
