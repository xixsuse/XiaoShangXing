package com.xiaoshangxing.setting.about;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.Network.AutoUpdate.UpdateManager;
import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.feedback.FeedbackActivity;
import com.xiaoshangxing.utils.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/7/15.
 */
public class AboutActivity extends BaseActivity {
    @Bind(R.id.about_leftarrow)
    ImageView aboutLeftarrow;
    @Bind(R.id.about_back)
    TextView aboutBack;
    @Bind(R.id.setting_about_toolbar)
    RelativeLayout settingAboutToolbar;
    @Bind(R.id.comment)
    RelativeLayout comment;
    @Bind(R.id.function)
    RelativeLayout function;
    @Bind(R.id.feeback)
    RelativeLayout feeback;
    @Bind(R.id.update)
    RelativeLayout update;
    @Bind(R.id.version_text)
    TextView versionText;

    private int versionCode;
    private String version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_about);
        ButterKnife.bind(this);
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
            version = info.versionName;
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionText.setText("" + version);
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


    @OnClick({R.id.comment, R.id.function, R.id.feeback, R.id.update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.comment:
                break;
            case R.id.function:
                break;
            case R.id.feeback:
                break;
            case R.id.update:
                update();
                break;
        }
    }

    private void update() {
        UpdateManager updateManager = new UpdateManager(this, "" + version, true);
        updateManager.checkUpdateInfo();
    }
}
