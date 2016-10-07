package com.xiaoshangxing.setting.shiming;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.shiming.shenhe.TongZhiShuActivity;
import com.xiaoshangxing.setting.shiming.shenhe.XueShengKaActivity;
import com.xiaoshangxing.setting.shiming.shenhe.XueShengZhenActivity;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IntentStatic;

/**
 * Created by tianyang on 2016/9/19.
 */
public class ShenheActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_shiming);
    }


    public void Back(View view) {
        finish();
    }

    public void xueshengzheng(View view) {
        startActivity(new Intent(ShenheActivity.this, XueShengZhenActivity.class));
    }

    public void xueshengka(View view) {
        startActivity(new Intent(ShenheActivity.this, XueShengKaActivity.class));
    }

    public void tongzhishu(View view) {
        startActivity(new Intent(ShenheActivity.this, TongZhiShuActivity.class));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getIntExtra(IntentStatic.TYPE, -1) == IntentStatic.CLOSE) {
            finish();
        }
    }
}
