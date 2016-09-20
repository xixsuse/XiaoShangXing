package com.xiaoshangxing.setting.shiming.vertify;

import android.os.Bundle;
import android.view.View;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;

/**
 * Created by tianyang on 2016/9/19.
 */
public class DiQUActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_diqu);

    }

    public void Back(View view) {
        finish();
    }
}
