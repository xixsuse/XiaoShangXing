package com.xiaoshangxing.setting.about;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.version_text)
    TextView versionText;
    @Bind(R.id.comment)
    RelativeLayout comment;
    @Bind(R.id.function)
    RelativeLayout function;
    @Bind(R.id.feeback)
    RelativeLayout feeback;
    @Bind(R.id.update)
    RelativeLayout update;

    private int versionCode;
    private String version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_about);
        ButterKnife.bind(this);
        title.setText("关于校上行");
        more.setVisibility(View.GONE);

        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
            version = info.versionName;
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionText.setText("校上行内测版:" + version);
    }

//    public void Back(View view) {
//        finish();
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


    @OnClick({R.id.comment, R.id.function, R.id.feeback, R.id.update, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.comment:
                break;
            case R.id.function:
                break;
            case R.id.feeback:
                FeedBack(view);
                break;
            case R.id.update:
                update();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void update() {
        UpdateManager updateManager = new UpdateManager(this, "" + version, true);
        updateManager.checkUpdateInfo();
    }

    @OnClick(R.id.back)
    public void onClick() {
    }
}
