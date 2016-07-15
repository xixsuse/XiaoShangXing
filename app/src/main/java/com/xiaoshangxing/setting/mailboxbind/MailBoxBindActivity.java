package com.xiaoshangxing.setting.mailboxbind;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;

/**
 * Created by 15828 on 2016/7/13.
 */
public class MailBoxBindActivity extends BaseActivity implements View.OnClickListener {
    private EditText editText;
    private TextView back, send;
    private ImageView clear;

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
            default:
                break;
        }
    }
}
