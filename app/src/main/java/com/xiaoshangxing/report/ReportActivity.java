package com.xiaoshangxing.report;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.xiaoshangxing.R;
import com.xiaoshangxing.input_activity.InputActivity;
import com.xiaoshangxing.input_activity.album.AlbumActivity;
import com.xiaoshangxing.input_activity.album.Bimp;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.report.reportEvidenceFragment.ReportEvidenceFragment;
import com.xiaoshangxing.report.reportReasonFragment.ReportReasonFragment;

import java.util.ArrayList;
import java.util.List;

import io.realm.annotations.Ignore;

/**
 * Created by tianyang on 2016/7/1.
 */
public class ReportActivity extends BaseActivity {
    public static final String TAG = BaseActivity.TAG + "-LoginRegisterActivity";
    private String reportText;
//    private String folderName;
//    private boolean isCanceled;
    private ReportEvidenceFragment reportEvidenceFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ReportReasonFragment reportReasonFragment = new ReportReasonFragment();
        reportEvidenceFragment = new ReportEvidenceFragment();

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

//    public void setFolderName(String folderName) {
//        this.folderName = folderName;
//    }
//
//    public String getFolderName() {
//        return folderName;
//    }

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
