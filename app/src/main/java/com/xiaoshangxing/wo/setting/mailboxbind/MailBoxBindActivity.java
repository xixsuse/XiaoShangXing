package com.xiaoshangxing.wo.setting.mailboxbind;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.xiaoshangxing.network.InfoNetwork;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.utils.baseClass.IBaseView;
import com.xiaoshangxing.utils.customView.dialog.DialogLocationAndSize;
import com.xiaoshangxing.utils.customView.dialog.DialogUtils;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;

/**
 * Created by 15828 on 2016/7/13.
 */
public class MailBoxBindActivity extends BaseActivity implements IBaseView {
    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_text)
    TextView rightText;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.bindmailbox_edittext)
    EditText bindmailboxEdittext;
    @Bind(R.id.mailbox_clear)
    ImageView mailboxClear;
    @Bind(R.id.bindmailbox_linear1)
    LinearLayout bindmailboxLinear1;
    @Bind(R.id.bindmailbox_text1)
    TextView bindmailboxText1;
    @Bind(R.id.bindmailbox_resendmail)
    Button bindmailboxResendmail;
    @Bind(R.id.bindmailbox_breakmaibox)
    Button bindmailboxBreakmaibox;
    private EditText editText;
    private ImageView clear;

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_bindmailbox);
        ButterKnife.bind(this);
        title.setText("绑定邮箱地址");
        rightText.setText("发送");
        rightText.setTextColor(getResources().getColor(R.color.green1));
        rightText.setAlpha(0.5f);

        editText = (EditText) findViewById(R.id.bindmailbox_edittext);
        clear = (ImageView) findViewById(R.id.mailbox_clear);
        if (clear != null) clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
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
                    rightText.setAlpha(1);
                    rightText.setClickable(true);
                    clear.setVisibility(View.VISIBLE);
                } else {
                    rightText.setAlpha((float) 0.5);
                    rightText.setClickable(false);
                    clear.setVisibility(View.GONE);
                }
            }
        });

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
        DialogLocationAndSize.setWidth(alertDialog, R.dimen.x780);
    }

    @OnClick({R.id.back, R.id.right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right_text:
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

                InfoNetwork.getInstance().bindEmail(progressSubsciber, jsonObject, this);
                break;
        }
    }
}
