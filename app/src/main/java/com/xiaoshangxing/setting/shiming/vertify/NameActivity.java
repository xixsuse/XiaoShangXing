package com.xiaoshangxing.setting.shiming.vertify;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;

/**
 * Created by tianyang on 2016/10/5.
 */
public class NameActivity extends BaseActivity {
    private EditText editText;
    private TextView finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_name);
        editText = (EditText) findViewById(R.id.edittext);
        finish = (TextView) findViewById(R.id.finish);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    finish.setAlpha(1);
                    finish.setEnabled(true);
                } else {
                    finish.setAlpha((float) 0.5);
                    finish.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void Back(View view) {
        finish();
    }

    public void Finish(View view) {
        VertifyActivity.nameStr = editText.getText().toString();
        finish();
    }
}
