package com.xiaoshangxing.setting.mailboxbind;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.InfoNetwork;
import com.xiaoshangxing.Network.NS;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by 15828 on 2016/7/13.
 */
public class MailBoxBindActivity extends BaseActivity implements View.OnClickListener, IBaseView {
    private EditText editText;
    private TextView back, send;
    private ImageView clear;

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_bindmailbox);
        editText = (EditText) findViewById(R.id.bindmailbox_edittext);
        back = (TextView) findViewById(R.id.mailboxbind_back);
        send = (TextView) findViewById(R.id.mailboxbind_send);
        clear = (ImageView) findViewById(R.id.mailbox_clear);
        if (clear != null) clear.setOnClickListener(this);
        back.setOnClickListener(this);
        send.setOnClickListener(this);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    send.setAlpha(1);
                    send.setClickable(true);
                    clear.setVisibility(View.VISIBLE);
                } else {
                    send.setAlpha((float) 0.5);
                    send.setClickable(false);
                    clear.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mailboxbind_back:
                finish();
                break;
            case R.id.mailbox_clear:
                editText.setText("");
                break;
            case R.id.mailboxbind_send:

                ProgressSubscriberOnNext<ResponseBody> next = new ProgressSubscriberOnNext<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody e) throws JSONException {
                        try {
                            JSONObject jsonObject = new JSONObject(e.string());
                            switch (Integer.valueOf((String) jsonObject.get(NS.CODE))) {
                                case 201:
                                    SendEmail_OK();
                                    break;
                                default:
                                    SendEmail_Fail(jsonObject.getString(NS.MSG));
                                    break;
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                    }
                };

                ProgressSubsciber<ResponseBody> progressSubsciber = new ProgressSubsciber<>(next, this);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("userId", (Integer) SPUtils.get(this, SPUtils.ID, SPUtils.DEFAULT_int));
                jsonObject.addProperty("email", editText.getText().toString());
                jsonObject.addProperty(NS.TIMESTAMP, System.currentTimeMillis());

                Log.d("json:", jsonObject.toString());

                InfoNetwork.getInstance().bindEmail(progressSubsciber, jsonObject, this);

                break;
            default:
                break;
        }
    }

    private void SendEmail_OK() {
        String text = editText.getText().toString();
        String text2 = "一封验证邮件已发送至：\n" + text + "，请登录\n你的邮箱查收邮件并验证。";
        final DialogUtils.Dialog_Center2 dialogUtils = new DialogUtils.Dialog_Center2(this);
        final Dialog alertDialog = dialogUtils.Message(text2)
                .Button("确定").MbuttonOnClick(new DialogUtils.Dialog_Center2.buttonOnClick() {
                    @Override
                    public void onButton1() {
                        dialogUtils.close();
                        finish();
                    }

                    @Override
                    public void onButton2() {

                    }
                }).create();
        alertDialog.show();
    }

    private void SendEmail_Fail(String error) {
//        String text = editText.getText().toString();
//        String text2 = "一封验证邮件已发送至：\n" + text + "，请登录\n你的邮箱查收邮件并验证。";
        final DialogUtils.Dialog_Center2 dialogUtils = new DialogUtils.Dialog_Center2(this);
        final Dialog alertDialog = dialogUtils.Message(error)
                .Button("确定").MbuttonOnClick(new DialogUtils.Dialog_Center2.buttonOnClick() {
                    @Override
                    public void onButton1() {
                        finish();
                    }

                    @Override
                    public void onButton2() {

                    }
                }).create();
        alertDialog.show();
    }
}
