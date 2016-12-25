package com.xiaoshangxing.wo.setting.realName;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xiaoshangxing.R;
import com.xiaoshangxing.wo.setting.realName.shenhe.AdviceNoteActivity;
import com.xiaoshangxing.wo.setting.realName.shenhe.StudentCardActivity;
import com.xiaoshangxing.wo.setting.realName.shenhe.StudentIdentityCardActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseActivity;

/**
 *modified by FengChaoQun on 2016/12/24 16:33
 * description:优化代码
 */
public class SelectVertifyModelActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_shiming);
    }


    public void Back(View view) {
        finish();
    }

    public void xueshengzheng(View view) {
        startActivity(new Intent(SelectVertifyModelActivity.this, StudentIdentityCardActivity.class));
    }

    public void xueshengka(View view) {
        startActivity(new Intent(SelectVertifyModelActivity.this, StudentCardActivity.class));
    }

    public void tongzhishu(View view) {
        startActivity(new Intent(SelectVertifyModelActivity.this, AdviceNoteActivity.class));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getIntExtra(IntentStatic.TYPE, -1) == IntentStatic.CLOSE) {
            finish();
        }
    }
}
