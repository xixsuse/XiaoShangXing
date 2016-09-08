package com.xiaoshangxing.setting.mailboxbind;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/7/15.
 */
public class ModifyMailBoxActivity extends BaseActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.modifymailbox_back)
    TextView modifymailboxBack;
    @Bind(R.id.bindmailbox_breakmaibox)
    Button bindmailboxBreakmaibox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_modifymailbox);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back, R.id.modifymailbox_back, R.id.bindmailbox_breakmaibox})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                break;
            case R.id.modifymailbox_back:
                break;
            case R.id.bindmailbox_breakmaibox:
                break;
        }
    }
}
