package com.xiaoshangxing.loginAndRegister.LoginRegisterActivity.SetPasswordFragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.xiaoshangxing.network.netUtil.AppNetUtil;
import com.xiaoshangxing.R;
import com.xiaoshangxing.loginAndRegister.LoginRegisterActivity.LoginRegisterActivity;
import com.xiaoshangxing.utils.customView.ClearableEditTextWithIcon;
import com.xiaoshangxing.wo.setting.realName.vertify.SchoolActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseFragment;
import com.xiaoshangxing.utils.customView.dialog.DialogLocationAndSize;
import com.xiaoshangxing.utils.customView.dialog.DialogUtils;

/**
 * Created by FengChaoQun
 * on 2016/6/24
 */
public class SetPasswordFragment extends BaseFragment implements SetPasswordContract.View, View.OnClickListener {
    public static final String TAG = BaseFragment.TAG + "-RgInputVertifyCodeFragment";

    private SetPasswordContract.Presenter mPresenter;

    private View mView;
    private TextView cancel;
    private ClearableEditTextWithIcon et_password;
    private CheckBox cb_show_password;
    private Button comlplete;

    public static SetPasswordFragment newInstance() {
        return new SetPasswordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_set_password, container, false);
        mView = view;
        setmPresenter(new SetPasswordFragmentPresenter(this));
        initView();
        return view;
    }

    private void initView() {
        cancel = (TextView) mView.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        et_password = (ClearableEditTextWithIcon) mView.findViewById(R.id.et_password);
        cb_show_password = (CheckBox) mView.findViewById(R.id.cb_show_password);
        comlplete = (Button) mView.findViewById(R.id.btn_complete_register);
        comlplete.setOnClickListener(this);

        et_password.addTextChangedListener(new TextWatcher() {
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

        cb_show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showOrHidePassword(true);
                } else {
                    showOrHidePassword(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                clickOnCancle();
                break;
            case R.id.btn_complete_register:
                mPresenter.clickOnCompleteButton();
                break;
        }
    }

    @Override
    public void clickOnCancle() {
        getActivity().finish();
    }

    @Override
    public void showOrHidePassword(boolean showOrHide) {
        if (showOrHide) {
            //设置密码可见
            et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            //设置光标位置在最后
            et_password.setSelection(et_password.getText().length());
        } else {
            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            et_password.setSelection(et_password.getText().length());
        }
    }

    @Override
    public String getPhone() {
        return ((LoginRegisterActivity) getActivity()).getPhoneNumer();
    }

    @Override
    public String getPassword() {
        return et_password.getText().toString();
    }

    @Override
    public void setButtonState(boolean state) {
        if (state) {
            comlplete.setEnabled(true);
            comlplete.setAlpha(1);
        } else {
            comlplete.setEnabled(false);
            comlplete.setAlpha(0.5f);
        }
    }


    @Override
    public void showRegisterSuccess() {
        DialogUtils.Dialog_No_Button dialog_no_button = new DialogUtils.Dialog_No_Button(getActivity(), "已完成注册");
        final Dialog alertDialog = dialog_no_button.create();
        alertDialog.show();
        DialogLocationAndSize.setWidth(alertDialog, R.dimen.x420);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                alertDialog.dismiss();
                login();
            }
        }, 1000);
    }

    private void login() {
        AppNetUtil.LoginXSX(getPhone(), getPassword(), mContext, this, new AppNetUtil.LoginXSXback() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(getContext(), SchoolActivity.class);
                intent.putExtra(IntentStatic.TYPE, IntentStatic.REGISTER);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onError(String msg) {
                showToast(msg);
            }
        });
    }

    @Override
    public void setmPresenter(@Nullable SetPasswordContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((LoginRegisterActivity) getActivity()).setIs_finish(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((LoginRegisterActivity) getActivity()).setIs_finish(false);
    }
}
