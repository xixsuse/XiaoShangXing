package com.xiaoshangxing.report;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.xiaoshangxing.R;
import com.xiaoshangxing.input_activity.InputActivity;
import com.xiaoshangxing.input_activity.album.Bimp;
import com.xiaoshangxing.report.reportEvidenceFragment.ReportEvidenceFragment;
import com.xiaoshangxing.report.reportReasonFragment.ReportReasonFragment;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IntentStatic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianyang on 2016/7/1.
 */
public class ReportActivity extends BaseActivity {
    public static final String TAG = BaseActivity.TAG + "-LoginRegisterActivity";
    private String reportText;
    private String reportType;
    private ReportEvidenceFragment reportEvidenceFragment;
    private String account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_fraglayout);

        account = getIntent().getStringExtra(IntentStatic.DATA);
        if (TextUtils.isEmpty(account)) {
            showToast("举报对象id不明");
            finish();
            return;
        }

        ReportReasonFragment reportReasonFragment = new ReportReasonFragment();
        reportEvidenceFragment = new ReportEvidenceFragment();

        mFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, reportReasonFragment, ReportReasonFragment.TAG)
                .commit();

    }

    public void setReportText(String text){
        this.reportText = text;
    }

    public String getReportText() {
        return reportText;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bimp.tempSelectBitmap.clear();
    }

    public ReportEvidenceFragment getReportEvidenceFragment() {
        return reportEvidenceFragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode==20000){
                List<String> selected=new ArrayList<>();
                selected=data.getStringArrayListExtra(InputActivity.SELECT_IMAGE_URLS);
                reportEvidenceFragment.setSelect_image_urls(selected);
            }
    }
}
