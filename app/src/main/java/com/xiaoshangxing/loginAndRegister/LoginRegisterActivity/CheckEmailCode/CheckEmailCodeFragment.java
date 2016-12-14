package com.xiaoshangxing.loginAndRegister.LoginRegisterActivity.CheckEmailCode;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.loginAndRegister.LoginRegisterActivity.AlterPasswordFragment.AlterPasswordFragment;
import com.xiaoshangxing.loginAndRegister.LoginRegisterActivity.LoginRegisterActivity;
import com.xiaoshangxing.utils.baseClass.BaseFragment;
import com.xiaoshangxing.utils.customView.dialog.DialogLocationAndSize;
import com.xiaoshangxing.utils.customView.dialog.DialogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/10/17
 */

public class CheckEmailCodeFragment extends BaseFragment implements CheckEmailCodeContract.View {
    public static final String TAG = BaseFragment.TAG + "-CheckEmailCodeFragment";
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.phone_number)
    TextView PhoneNumber;
    @Bind(R.id.et_vertify_code)
    EditText etVertifyCode;
    @Bind(R.id.btn_submit)
    Button btnSubmit;
    @Bind(R.id.remain_time)
    TextView remainTime;
    private View mView;
    private CheckEmailCodeContract.Presenter mPresenter;

    public static CheckEmailCodeFragment newInstance() {
        return new CheckEmailCodeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_check_email_code, container, false);
        mView = view;
        ButterKnife.bind(this, view);
        initView();
        setmPresenter(new CheckEmailCodePresenter(this));
        return view;
    }

    private void initView() {
        etVertifyCode.addTextChangedListener(new TextWatcher() {
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
        setPhoneNumber(((LoginRegisterActivity) getActivity()).getPhoneNumer());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.back, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_submit:
                mPresenter.clickOnSubmit();
                break;
        }
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber.setText(phoneNumber);
    }

    @Override
    public String getVertifyCode() {
        return etVertifyCode.getText().toString();
    }

    @Override
    public void setButtonEnable(boolean enable) {
        if (enable) {
            btnSubmit.setEnabled(true);
            btnSubmit.setAlpha(1);
        } else {
            btnSubmit.setEnabled(false);
            btnSubmit.setAlpha(0.5f);
        }
    }

    @Override
    public void showFailDialog() {
        final DialogUtils.Dialog_Center dialogUtils = new DialogUtils.Dialog_Center(getActivity());
        Dialog alertDialog = dialogUtils.Message("验证码不正确，请重新输入。")
                .Button("确定")
                .MbuttonOnClick(new DialogUtils.Dialog_Center.buttonOnClick() {
                    @Override
                    public void onButton1() {
                        dialogUtils.close();
                    }

                    @Override
                    public void onButton2() {

                    }
                }).create();
        alertDialog.show();
        DialogLocationAndSize.setWidth(alertDialog, R.dimen.x780);
    }

    @Override
    public void showNoDialogMenu() {

    }

    @Override
    public void gotoResetPassword() {
        AlterPasswordFragment frag = ((LoginRegisterActivity) getActivity()).getAlterPasswordFragment();
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .replace(R.id.loginregisterContent, frag)
                .addToBackStack(AlterPasswordFragment.TAG)
                .commit();
    }

    @Override
    public String getPhone() {
        return ((LoginRegisterActivity) getActivity()).getPhoneNumer();
    }

    @Override
    public void setmPresenter(@Nullable CheckEmailCodeContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
