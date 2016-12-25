package com.xiaoshangxing.wo.setting.realName.result;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.wo.setting.realName.shenhe.AdviceNoteActivity;
import com.xiaoshangxing.wo.setting.realName.shenhe.StudentCardActivity;
import com.xiaoshangxing.wo.setting.realName.shenhe.StudentIdentityCardActivity;

/**
 * modified by FengChaoQun on 2016/12/24 16:13
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
        startActivity(new Intent(this, StudentIdentityCardActivity.class));
    }

    public void xueshengka(View view) {
        startActivity(new Intent(this, StudentCardActivity.class));
    }

    public void tongzhishu(View view) {
        startActivity(new Intent(this, AdviceNoteActivity.class));
    }


}
