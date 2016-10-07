package com.xiaoshangxing.login_register.LoginRegisterActivity.SetPasswordFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.LoginNetwork;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.Network.netUtil.HmacSHA256Utils;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.login_register.LoginRegisterActivity.LoginRegisterActivity;
import com.xiaoshangxing.login_register.LoginRegisterActivity.SelectSchoolFreagment.SelectSchoolFragment;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

import org.json.JSONObject;

import io.realm.Realm;
import okhttp3.ResponseBody;

/**
 * Created by FengChaoQun
 * on 2016/6/24
 */
public class SetPasswordFragment extends BaseFragment implements SetPasswordContract.View, View.OnClickListener {
    public static final String TAG = BaseFragment.TAG + "-RgInputVertifyCodeFragment";

    private SetPasswordContract.Presenter mPresenter;

    private View mView;
    private TextView cancel;
    private EditText et_password;
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
        et_password = (EditText) mView.findViewById(R.id.et_password);
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
        LocationUtil.setWidth(getActivity(), alertDialog,
                getActivity().getResources().getDimensionPixelSize(R.dimen.x420));

        new Handler().postDelayed(new Runnable() {
            public void run() {
                alertDialog.dismiss();
//                SelectSchoolFragment frag = ((LoginRegisterActivity) mActivity).getSelectSchoolFragment();
//                getFragmentManager().beginTransaction()
//                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
//                                R.anim.slide_in_left, R.anim.slide_out_left)
//                        .replace(R.id.loginregisterContent, frag)
//                        .addToBackStack(SelectSchoolFragment.TAG)
//                        .commit();
                login();
            }
        }, 1000);
    }

    private void login() {
        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(responseBody.string());
                    switch (Integer.valueOf((String) jsonObject.get(NS.CODE))) {
                        case 200:
                            if (jsonObject.get(NS.MSG) instanceof JSONObject) {
                                String token = jsonObject.getJSONObject(NS.MSG).getString(NS.TOKEN);
                                String digest = HmacSHA256Utils.digest(token, getPhone());
                                //  存储摘要 账号 id  头像
                                SPUtils.put(getContext(), SPUtils.TOKEN, token);
                                SPUtils.put(getContext(), SPUtils.DIGEST, digest);
                                SPUtils.put(getContext(), SPUtils.PHONENUMNBER, getPhone());
                                String headPath = jsonObject.getJSONObject(NS.MSG).getJSONObject("userDto").getString("userImage");
                                if (!TextUtils.isEmpty(headPath) && !headPath.equals("null")) {
                                    SPUtils.put(getContext(), SPUtils.CURRENT_COUNT_HEAD, headPath);
                                }
                                int id = jsonObject.getJSONObject(NS.MSG).getJSONObject("userDto").getInt(NS.ID);
                                SPUtils.put(getContext(), SPUtils.ID, id);
                                Log.d("digest", digest);
                                //   存储账号信息
                                final JSONObject userDao = jsonObject.getJSONObject(NS.MSG).getJSONObject("userDto");
                                Realm realm = Realm.getDefaultInstance();
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        User user = realm.createOrUpdateObjectFromJson(User.class, userDao);
                                        Log.d("user", user.toString());
                                    }
                                });
                                realm.close();
                            }

                            SelectSchoolFragment frag = ((LoginRegisterActivity) mActivity).getSelectSchoolFragment();
                            getFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                                            R.anim.slide_in_left, R.anim.slide_out_left)
                                    .replace(R.id.loginregisterContent, frag)
                                    .addToBackStack(SelectSchoolFragment.TAG)
                                    .commit();

                            break;
//                        case 9001:
//                            mView.showFailDialog("用户名不存在");
//                            break;
//                        case 9002:
//                            mView.showFailDialog("账号或密码错误，请重新填写。");
//                            break;
//                        case 9003:
//                            mView.showFailDialog("失败次数过多，该账号暂时被锁定");
//                            break;
                        default:
                            if (jsonObject.get(NS.MSG) instanceof JSONObject) {
                                Log.d("login", (String) (jsonObject.getJSONObject(NS.MSG)).get(NS.TOKEN));
                            } else {
                                Log.d("login", jsonObject.getString(NS.MSG));
                                showToast(jsonObject.getString(NS.MSG));
                            }
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ProgressSubsciber<ResponseBody> observer = new ProgressSubsciber<ResponseBody>(onNext, SetPasswordFragment.this);

        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("phone", getPhone());
        jsonObject1.addProperty("password", getPassword());
        jsonObject1.addProperty("timeStamp", System.currentTimeMillis());
        LoginNetwork.getInstance().Login(observer, jsonObject1);
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
