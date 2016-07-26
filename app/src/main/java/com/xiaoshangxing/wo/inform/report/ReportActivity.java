package com.xiaoshangxing.wo.inform.report;

import android.os.Bundle;
import android.util.Log;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.utils.photo_choosing.Bimp;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.wo.inform.report.reportEvidenceFragment.ReportEvidenceFragment;
import com.xiaoshangxing.wo.inform.report.reportReasonFragment.ReportReasonFragment;

/**
 * Created by tianyang on 2016/7/1.
 */
public class ReportActivity extends BaseActivity {
    public static final String TAG = BaseActivity.TAG + "-LoginRegisterActivity";
    private String reportText,folderName;
//    private boolean isCanceled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ReportReasonFragment reportReasonFragment = new ReportReasonFragment();
        ReportEvidenceFragment reportEvidenceFragment = new ReportEvidenceFragment();

        mFragmentManager.beginTransaction()
                .replace(R.id.reportContent, reportReasonFragment, ReportReasonFragment.TAG)
                .commit();

    }

    public void setReportText(String text){
        this.reportText = text;
    }

    public String getReportText() {
        return reportText;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }

//    public void setCanceled(boolean canceled) {
//        isCanceled = canceled;
//        Log.d("qqq","setting..."+isCanceled);
//    }
//
//    public boolean isCanceled() {
//        return isCanceled;
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("qqq", "destory..");
        Bimp.tempSelectBitmap.clear();
    }
}
