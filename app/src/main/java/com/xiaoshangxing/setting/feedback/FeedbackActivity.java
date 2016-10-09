package com.xiaoshangxing.setting.feedback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xiaoshangxing.Network.netUtil.AppNetUtil;
import com.xiaoshangxing.Network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IBaseView;

/**
 * Created by 15828 on 2016/7/12.
 */
public class FeedbackActivity extends BaseActivity implements IBaseView {
    private EditText editText;
    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_feedback);
        editText = (EditText) findViewById(R.id.feedback_editText);
        submit = (Button) findViewById(R.id.feedback_submit);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                   if(s.toString().length()>0){
                       submit.setAlpha(1);
                       submit.setClickable(true);
                   }else {
                       submit.setAlpha((float) 0.5);
                       submit.setClickable(false);
                   }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editText.getText().toString())) {
                    AppNetUtil.Suggest(editText.getText().toString(), FeedbackActivity.this, new SimpleCallBack() {
                        @Override
                        public void onSuccess() {
                            showToast("反馈成功");
                            finish();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onBackData(Object o) {

                        }
                    });
                }
            }
        });
    }

    public void back(View view) {
        finish();
    }

    public void Commit(View view) {
        if (!TextUtils.isEmpty(editText.getText().toString())) {
            AppNetUtil.Suggest(editText.getText().toString(), FeedbackActivity.this, new SimpleCallBack() {
                @Override
                public void onSuccess() {
                    showToast("反馈成功");
                    finish();
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onBackData(Object o) {

                }
            });
        }
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }
}
