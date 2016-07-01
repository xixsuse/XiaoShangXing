package com.xiaoshangxing.login_register.LoginRegisterActivity.LoginFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.login_register.LoginRegisterActivity.InputAccountFragment.InputAccountFragment;
import com.xiaoshangxing.login_register.LoginRegisterActivity.LoginRegisterActivity;
import com.xiaoshangxing.login_register.LoginRegisterActivity.RetrieveByMesFragment.RetrieveByMesFragment;
import com.xiaoshangxing.login_register.LoginRegisterActivity.RgInputPhoNumberFragment.RgInputPhoNumberFragment;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.CirecleImage;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LoadingDialog;
import com.xiaoshangxing.utils.LocationUtil;

/**
 * Created by FengChaoQun
 * on 2016/6/22
 */
public class LoginFragment extends BaseFragment implements LoginFragmentContract.View, View.OnClickListener {

    public static final String TAG = BaseFragment.TAG + "-LoginFragment";

    private LoginFragmentContract.Presenter mPresenter;
    private View mview;
    private CirecleImage headPortrait;
    private EditText et_phoneNumber;
    private EditText et_passWord;
    private Button btn_login;
    private TextView tv_retrieve_password;
    private TextView tv_register;
    private LoadingDialog mLoadingDialog;
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
        getNumber = getActivity().getIntent().getStringExtra("number");
        if (!TextUtils.isEmpty(getNumber)) {
            mPresenter.loginWithAccount(getNumber);
        }
        return view;
    }

    private void initView() {
        mLoadingDialog = new LoadingDialog(getActivity());

        headPortrait = (CirecleImage) mview.findViewById(R.id.ci_headPortrait);
        et_phoneNumber = (EditText) mview.findViewById(R.id.et_account);
        et_passWord = (EditText) mview.findViewById(R.id.et_password);

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
//                .setCustomAnimations(R.animator.slide_fragment_horizontal_right_in, R.animator.slide_fragment_horizontal_left_out, R.animator.slide_fragment_horizontal_left_in, R.animator.slide_fragment_horizontal_right_out)
                .replace(R.id.loginregisterContent, frag)
                .addToBackStack(RgInputPhoNumberFragment.TAG)
                .commit();
    }

    @Override
    public void showFailDialog() {
        mDialogUtils = new DialogUtils.Dialog_Center(mActivity);


        Dialog alertDialog = mDialogUtils.Message("账号或密码错误，请重新填写。").Button("确定")
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
        LocationUtil.setWidth(getActivity(), alertDialog,
                getActivity().getResources().getDimensionPixelSize(R.dimen.x780));
    }

    public void setmPresenter(@Nullable LoginFragmentContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void showHeadPotrait(int id) {
        headPortrait.setImageResource(id);
    }

    @Override
    public void setPhoneNumber(String number) {
        et_phoneNumber.setText(number);
        et_phoneNumber.setSelection(et_phoneNumber.getText().length());
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
    public String getPassword() {
        return et_passWord.getText().toString();
    }

    @Override
    public void showLoadingDialog() {
        mLoadingDialog.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mLoadingDialog.getWindow().getAttributes();
        lp.width = (int) (getActivity().getResources().getDimensionPixelSize(R.dimen.x360)); //设置宽度
        lp.height = (int) (getActivity().getResources().getDimensionPixelSize(R.dimen.y360)); //设置宽度
        mLoadingDialog.getWindow().setAttributes(lp);
    }

    @Override
    public void showRetrievePasswordMenu() {


        DialogUtils.DialogMenu mActionSheet = new DialogUtils.DialogMenu(getActivity());
        mActionSheet.addMenuItem("短信验证找回").addMenuItem("邮箱验证找回");
        mActionSheet.show();
        LocationUtil.bottom_FillWidth(getActivity(), mActionSheet);
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
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void hideLoadingDialog() {
        mLoadingDialog.dismiss();
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
    }

    @Override
    public String toString() {
        return TAG;
    }
}
