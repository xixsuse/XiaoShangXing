package com.xiaoshangxing.setting.shiming.result;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.shiming.ShenheActivity;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IntentStatic;

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
        Intent intent = new Intent(this, ShenheActivity.class);
        intent.putExtra(IntentStatic.TYPE, IntentStatic.CLOSE);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(this, ShenheActivity.class);
            intent.putExtra(IntentStatic.TYPE, IntentStatic.CLOSE);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
