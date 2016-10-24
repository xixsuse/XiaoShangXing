package com.xiaoshangxing.setting.feedback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.AppNetUtil;
import com.xiaoshangxing.Network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IBaseView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/7/12.
 */
public class FeedbackActivity extends BaseActivity implements IBaseView {
    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.feedback_editText)
    EditText feedbackEditText;
    @Bind(R.id.feedback_submit)
    Button feedbackSubmit;
    private EditText editText;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_feedback);
        ButterKnife.bind(this);
        title.setText("意见反馈");
        more.setVisibility(View.GONE);

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
                if (s.toString().length() > 0) {
                    submit.setAlpha(1);
                    submit.setClickable(true);
                } else {
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

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
