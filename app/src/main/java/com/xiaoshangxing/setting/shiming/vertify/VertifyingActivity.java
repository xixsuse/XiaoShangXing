package com.xiaoshangxing.setting.shiming.vertify;

import android.os.Bundle;
import android.view.View;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;

/**
 * Created by tianyang on 2016/9/20.
 */
public class VertifyingActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertifying);

    }

    public void Back(View view) {
        finish();
    }
}
