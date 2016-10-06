package com.xiaoshangxing.setting.shiming.result;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.shiming.shenhe.TongZhiShuActivity;
import com.xiaoshangxing.setting.shiming.shenhe.XueShengKaActivity;
import com.xiaoshangxing.setting.shiming.shenhe.XueShengZhenActivity;
import com.xiaoshangxing.utils.BaseActivity;

/**
 * Created by tianyang on 2016/10/5.
 */
public class VertifyFailedActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_failed);

    }

    public void Back(View view) {
        finish();
    }

    //显示错误
    public void ShowErrors(View view) {
    }

    public void xueshengzheng(View view) {
        startActivity(new Intent(this, XueShengZhenActivity.class));
    }

    public void xueshengka(View view) {
        startActivity(new Intent(this, XueShengKaActivity.class));
    }

    public void tongzhishu(View view) {
        startActivity(new Intent(this, TongZhiShuActivity.class));
    }


}
