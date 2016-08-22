package com.xiaoshangxing.yujian.groupchatInfo.groupName;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/8/12.
 */
public class GroupNameActivity extends BaseActivity {
    @Bind(R.id.groupChatName_save)
    TextView save;
    @Bind(R.id.groupChatName_edittext)
    EditText editText;
    @Bind(R.id.groupChatName_clear)
    ImageView clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_groupname);
        ButterKnife.bind(this);

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
                    save.setAlpha(1);
                    save.setClickable(true);
                    clear.setVisibility(View.VISIBLE);
                } else {
                    save.setAlpha((float) 0.5);
                    save.setClickable(false);
                    clear.setVisibility(View.GONE);
                }
            }
        });

    }


    @OnClick({R.id.groupChatName_back, R.id.groupChatName_save, R.id.groupChatName_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.groupChatName_back:
                finish();
                break;
            case R.id.groupChatName_save:

                break;
            case R.id.groupChatName_clear:
                editText.setText("");
                break;
        }
    }
}
