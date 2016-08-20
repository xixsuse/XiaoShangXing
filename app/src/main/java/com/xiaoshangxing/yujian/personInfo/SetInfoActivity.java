package com.xiaoshangxing.yujian.personInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.DataSetting;
import com.xiaoshangxing.setting.utils.ActionSheet;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.SwitchView;
import com.xiaoshangxing.utils.normalUtils.SPUtils;
import com.xiaoshangxing.report.ReportActivity;

/**
 * Created by 15828 on 2016/7/25.
 */
public class SetInfoActivity extends BaseActivity {
    private SwitchView starMarkfriends, crush, bukanwo, bukanta, addToBlackList;
    private ActionSheet mActionSheet1;
    private ActionSheet mActionSheet2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_setinfo);

        starMarkfriends = (SwitchView) findViewById(R.id.StarMarkfriends);
        crush = (SwitchView) findViewById(R.id.crush);
        bukanwo = (SwitchView) findViewById(R.id.bukanwo);
        bukanta = (SwitchView) findViewById(R.id.bukanta);
        addToBlackList = (SwitchView) findViewById(R.id.addtoblacklist);

        setUpData();

        starMarkfriends.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                SPUtils.put(SetInfoActivity.this, "starMarkfriends", true);
                starMarkfriends.setState(true);
            }

            @Override
            public void toggleToOff() {
                SPUtils.put(SetInfoActivity.this, "starMarkfriends", false);
                starMarkfriends.setState(false);
            }
        });

        crush.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                SPUtils.put(SetInfoActivity.this, "crush", true);
                crush.setState(true);
            }

            @Override
            public void toggleToOff() {
                SPUtils.put(SetInfoActivity.this, "crush", false);
                crush.setState(false);
            }
        });
        bukanwo.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                SPUtils.put(SetInfoActivity.this, "bukanwo", true);
                bukanwo.setState(true);
            }

            @Override
            public void toggleToOff() {
                SPUtils.put(SetInfoActivity.this, "bukanwo", false);
                bukanwo.setState(false);
            }
        });
        bukanta.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                SPUtils.put(SetInfoActivity.this, "bukanta", true);
                bukanta.setState(true);
            }

            @Override
            public void toggleToOff() {
                SPUtils.put(SetInfoActivity.this, "bukanta", false);
                bukanta.setState(false);
            }
        });
        addToBlackList.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                if (mActionSheet1 == null) {
                    mActionSheet1 = new ActionSheet(SetInfoActivity.this);
                    mActionSheet1.addMenuTopItem("加入黑名单，你将不再收到对方的消息，并且你们互相\n看不到对方校友圈的更新")
                            .addMenuBottomItem("确定");
                }
                mActionSheet1.show();
                WindowManager windowManager = getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = mActionSheet1.getWindow().getAttributes();
                lp.width = (display.getWidth()); //设置宽度
                mActionSheet1.getWindow().setAttributes(lp);
                mActionSheet1.setMenuBottomListener(new ActionSheet.MenuListener() {
                    @Override
                    public void onItemSelected(int position, String item) {
                        SPUtils.put(SetInfoActivity.this, "addToBlackList", true);
                        addToBlackList.setState(true);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }

            @Override
            public void toggleToOff() {
                SPUtils.put(SetInfoActivity.this, "addToBlackList", false);
                addToBlackList.setState(false);
            }
        });

    }

    private void setUpData() {
        starMarkfriends.setState(DataSetting.IsStarMarkfriends(this));
        crush.setState(DataSetting.IsCrush(this));
        bukanwo.setState(DataSetting.IsBuKanWo(this));
        bukanta.setState(DataSetting.IsBuKanTa(this));
        addToBlackList.setState(DataSetting.IsAddToBlackList(this));

    }

    public void Back(View view) {
        finish();
    }

    public void Report(View view) {
        Intent intent = new Intent(this, ReportActivity.class);
        startActivity(intent);
    }

    public void Delete(View view) {
        if (mActionSheet2 == null) {
            mActionSheet2 = new ActionSheet(SetInfoActivity.this);
            mActionSheet2.addMenuTopItem("将联系人“王振华”删除，同时删除与该联系人的聊天记录")
                    .addMenuBottomItem("确定");
        }
        mActionSheet2.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mActionSheet2.getWindow().getAttributes();
        lp.width = (display.getWidth()); //设置宽度
        mActionSheet2.getWindow().setAttributes(lp);
        mActionSheet2.setMenuBottomListener(new ActionSheet.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {

            }

            @Override
            public void onCancel() {

            }
        });
    }
}
