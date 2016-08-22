package com.xiaoshangxing.setting;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.about.AboutActivity;
import com.xiaoshangxing.setting.currency.CurrencyActivity;
import com.xiaoshangxing.setting.mailboxbind.MailBoxBindActivity;
import com.xiaoshangxing.setting.modifypassword.ModifyPassWordActivity;
import com.xiaoshangxing.setting.newNotice.NewNoticeActivity;
import com.xiaoshangxing.setting.personalinfo.PersonalInfoActivity;
import com.xiaoshangxing.setting.privacy.PrivacyActivity;
import com.xiaoshangxing.setting.utils.ActionSheet;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.XSXApplication;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

import java.io.ByteArrayInputStream;

/**
 * Created by tianyang on 2016/7/9.
 */
public class SettingActivity extends BaseActivity {
    private ActionSheet mActionSheet;
    private CirecleImage imgCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_setmain);
        imgCover = (CirecleImage) findViewById(R.id.setting_main_imag);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setImgCover();
    }

    private void setImgCover() {
        SharedPreferences sharedPreferences = getSharedPreferences("headImg", Context.MODE_PRIVATE);
        String imageString = sharedPreferences.getString("smallImage", "");
        byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
        if (bitmap != null) imgCover.setImageBitmap(bitmap);
    }


    public void ExitSetting(View view) {
        if (mActionSheet == null) {
            mActionSheet = new ActionSheet(this);
            mActionSheet.addMenuTopItem("退出后不会删除任何历史数据，下次登录依然可以使用此账号")
                    .addMenuBottomItem("退出登录");
        }
        mActionSheet.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mActionSheet.getWindow().getAttributes();
        lp.width = (display.getWidth()); //设置宽度
        mActionSheet.getWindow().setAttributes(lp);
        mActionSheet.setMenuBottomListener(new ActionSheet.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                Toast.makeText(SettingActivity.this, item, Toast.LENGTH_SHORT).show();
                SPUtils.put(SettingActivity.this,SPUtils.IS_QUIT,true);
                XSXApplication xsxApplication=(XSXApplication)getApplication();
                xsxApplication.exit();
//                finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(SettingActivity.this, "onCancel", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void PersonInfo(View view) {
        Intent intent = new Intent(this, PersonalInfoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

//    public void Feedbak(View view) {
//        Intent intent = new Intent(this, FeedbackActivity.class);
//        startActivity(intent);
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
//    }

    public void NewNotice(View view) {
        Intent intent = new Intent(this, NewNoticeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public void BindMailBox(View view) {
        Intent intent = new Intent(this, MailBoxBindActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
//        Intent intent = new Intent(this, ModifyMailBoxActivity.class);
//        startActivity(intent);
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public void Privacy(View view) {
        Intent intent = new Intent(this, PrivacyActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public void Currency(View view) {
        Intent intent = new Intent(this, CurrencyActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }


    public void VertifyPassWord(View view) {
        final DialogUtils.Dialog_WithEditText dialogUtils = new DialogUtils.Dialog_WithEditText(this);
        final Dialog alertDialog = dialogUtils.Title("验证原密码")
                .Message("为保障您的数据安全，修改密码前请填写\n原密码。")
                .Button("取消", "确定").MbuttonOnClick(new DialogUtils.Dialog_WithEditText.buttonOnClick() {
                    @Override
                    public void onButton1() {
                        dialogUtils.close();
                    }

                    @Override
                    public void onButton2() {
                        Log.d("qqq","    "+dialogUtils.getText());
                        Intent intent = new Intent(SettingActivity.this, ModifyPassWordActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                        dialogUtils.close();
                    }
                }).create();
        alertDialog.show();
      //  LocationUtil.setWidth(this, alertDialog, getResources().getDimensionPixelSize(R.dimen.x780));
    }

    public void about(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public void SettingBack(View view) {
        finish();
    }
}
