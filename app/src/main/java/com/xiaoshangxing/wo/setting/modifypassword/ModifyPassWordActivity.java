package com.xiaoshangxing.wo.setting.modifypassword;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.google.gson.JsonObject;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.network.InfoNetwork;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.utils.baseClass.IBaseView;
import com.xiaoshangxing.utils.customView.ClearableEditTextWithIcon;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by 15828 on 2016/7/15.
 */
public class ModifyPassWordActivity extends BaseActivity implements IBaseView {
    private ClearableEditTextWithIcon editText2, editText3;
    private Button confirm;
    private String oldPassword;
    private IBaseView iBaseView = this;

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_modifypasswd);
        editText2 = (ClearableEditTextWithIcon) findViewById(R.id.modify_editText2);
        editText3 = (ClearableEditTextWithIcon) findViewById(R.id.modify_editText3);
        confirm = (Button) findViewById(R.id.confirm_modify);
        addListener();
        oldPassword = getIntent().getStringExtra(IntentStatic.DATA);
    }

    private void addListener() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText2.getText().length() >= 6 && editText3.getText().length() >= 6) {
                    confirm.setAlpha(1);
                    confirm.setClickable(true);
                } else {
                    confirm.setAlpha((float) 0.5);
                    confirm.setClickable(false);
                }
            }
        };

        editText2.addTextChangedListener(textWatcher);
        editText3.addTextChangedListener(textWatcher);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText2.getText().toString().equals(editText3.getText().toString())) {
                    showToast("两次密码不一致");
                    return;
                }
                ProgressSubscriberOnNext<ResponseBody> next = new ProgressSubscriberOnNext<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody e) throws JSONException {
                        try {
                            JSONObject jsonObject = new JSONObject(e.string());
                            if (jsonObject.getString(NS.CODE).equals("9000")) {
                                showToast("修改成功");
                                finish();
                            } else {
                                showToast("修改失败");
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                            showToast("修改失败");
                        }
                    }
                };

                ProgressSubsciber<ResponseBody> progressSubsciber = new ProgressSubsciber<ResponseBody>(next, iBaseView);

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("userId", TempUser.getId());
                jsonObject.addProperty("oldPassword", oldPassword);
                jsonObject.addProperty("newPassword", editText2.getText().toString());
                jsonObject.addProperty(NS.TIMESTAMP, System.currentTimeMillis());
                InfoNetwork.getInstance().ModifyPassword(progressSubsciber, jsonObject, ModifyPassWordActivity.this);
            }
        });
    }


    public void Cancel(View view) {
        finish();
    }
}
