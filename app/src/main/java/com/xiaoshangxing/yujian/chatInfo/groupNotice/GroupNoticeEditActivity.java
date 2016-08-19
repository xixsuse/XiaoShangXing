package com.xiaoshangxing.yujian.chatInfo.groupNotice;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/8/12.
 */
public class GroupNoticeEditActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.groupNoticeEdit_back)
    TextView back;
    @Bind(R.id.groupNoticeEdit_finish)
    TextView finish;
    @Bind(R.id.groupNotice_editText)
    EditText editText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_groupnotice_edit);
        ButterKnife.bind(this);
        finish.setEnabled(false);
        back.setOnClickListener(this);
        finish.setOnClickListener(this);

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
                    finish.setAlpha(1);
                    finish.setEnabled(true);
                } else {
                    finish.setAlpha((float) 0.5);
                    finish.setEnabled(false);
                }
            }
        });




    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.groupNoticeEdit_back:
                finish();
                break;
            case R.id.groupNoticeEdit_finish:
                finish();
                break;
        }

    }
}
