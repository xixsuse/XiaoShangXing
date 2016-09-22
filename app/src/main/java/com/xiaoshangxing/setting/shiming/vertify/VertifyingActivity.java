package com.xiaoshangxing.setting.shiming.vertify;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.personalinfo.PersonalInfoActivity;
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
        startActivity(new Intent(this, PersonalInfoActivity.class));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            startActivity(new Intent(this, PersonalInfoActivity.class));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
