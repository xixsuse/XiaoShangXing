package com.xiaoshangxing.setting.modifypassword;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.InfoNetwork;
import com.xiaoshangxing.Network.NS;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by 15828 on 2016/7/15.
 */
public class ModifyPassWordActivity extends BaseActivity implements IBaseView {
    private EditText editText1, editText2, editText3;
    private Button confirm;
    private boolean flag1 = false, flag2 = false, flag3 = false;
    private String oldPassword;
    private IBaseView iBaseView=this;
    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_modifypasswd);
        editText1 = (EditText) findViewById(R.id.modify_editText1);
        editText2 = (EditText) findViewById(R.id.modify_editText2);
        editText3 = (EditText) findViewById(R.id.modify_editText3);
        confirm = (Button) findViewById(R.id.confirm_modify);
        addListener();
        oldPassword=getIntent().getStringExtra(IntentStatic.DATA);
    }

    private void addListener() {
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    flag1 = true;
                    if (flag2 && flag3) {
                        confirm.setAlpha(1);
                        confirm.setClickable(true);
                    }
                } else {
                    flag1 = false;
                    confirm.setAlpha((float) 0.5);
                    confirm.setClickable(false);
                }
            }
        });
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    flag2 = true;
                    if (flag1 && flag3) {
                        confirm.setAlpha(1);
                        confirm.setClickable(true);
                    }
                } else {
                    flag2 = false;
                    confirm.setAlpha((float) 0.5);
                    confirm.setClickable(false);
                }
            }
        });
        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    flag3 = true;
                    if (flag1 && flag2) {
                        confirm.setAlpha(1);
                        confirm.setClickable(true);
                    }
                } else {
                    flag3 = false;
                    confirm.setAlpha((float) 0.5);
                    confirm.setClickable(false);
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText1.getText().toString().equals((String)SPUtils.get(ModifyPassWordActivity.this,
                        SPUtils.CURRENT_COUNT,SPUtils.DEFAULT_STRING))){
                    showToast("手机号码错误");
                    return;
                }

                if (!editText2.getText().toString().equals(editText3.getText().toString())){
                    showToast("两次密码不一致");
                    return;
                }
                ProgressSubscriberOnNext<ResponseBody> next=new ProgressSubscriberOnNext<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody e) throws JSONException {
                        try {
                            JSONObject jsonObject=new JSONObject(e.string());
                            if (jsonObject.getString(NS.CODE).equals("9000")){
                                showToast("修改成功");
                                finish();
                            }else {
                                showToast("修改失败");
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                            showToast("修改失败");
                        }
                    }
                };

                ProgressSubsciber<ResponseBody> progressSubsciber=new ProgressSubsciber<ResponseBody>(next,iBaseView);

                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("userId", (Integer) SPUtils.get(ModifyPassWordActivity.this,SPUtils.ID,SPUtils.DEFAULT_int));
                jsonObject.addProperty("oldPassword",oldPassword);
                jsonObject.addProperty("newPassword",editText2.getText().toString());
                jsonObject.addProperty(NS.TIMESTAMP,System.currentTimeMillis());
                InfoNetwork.getInstance().ModifyPassword(progressSubsciber,jsonObject,ModifyPassWordActivity.this);
            }
        });
    }


    public void Cancel(View view) {
        finish();
    }
}
