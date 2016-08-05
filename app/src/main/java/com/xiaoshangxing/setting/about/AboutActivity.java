package com.xiaoshangxing.setting.about;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.feedback.FeedbackActivity;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.report.ReportActivity;

/**
 * Created by 15828 on 2016/7/15.
 */
public class AboutActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_about);

    }

    public void Back(View view) {
        finish();
    }

//    public void ReportAndComplaint(View view) {
//        Intent intent = new Intent(this, ReportActivity.class);
//        startActivity(intent);
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
//    }

    public void FunctionIntroduction(View view) {

    }

    public void evaluate(View view) {

    }

    public void FeedBack(View view) {
        Intent intent = new Intent(this, FeedbackActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }


}
