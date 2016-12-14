package com.xiaoshangxing.yujian.groupchatInfo.groupCode;

import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoshangxing.R;
import com.xiaoshangxing.wo.setting.utils.ActionSheet;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.utils.customView.CirecleImage;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/8/12.
 */
public class GroupCodeActivity extends BaseActivity {
    @Bind(R.id.groupCode_back)
    TextView groupCodeBack;
    @Bind(R.id.groupImg)
    CirecleImage groupImg;
    @Bind(R.id.groupName)
    TextView groupName;
    @Bind(R.id.groupCode_CodeImg)
    ImageView groupCodeCodeImg;
    @Bind(R.id.groupCode_text)
    TextView groupCodeText;

    private ActionSheet mActionSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_groupcode);
        ButterKnife.bind(this);

        setGroupImg();    //设置群头像
        setGroupName();   //设置群名称
        setGroupCode();   //设置群二维码
        setNoEffectTime(); //设置二维码失效日期
    }

    private void setNoEffectTime() {
        String time = "2016年10月12日";
        String format = getResources().getString(R.string.groupCode_text);
        groupCodeText.setText(String.format(format, time));

    }

    private void setGroupCode() {
        //groupCodeCodeImg
    }

    private void setGroupName() {
        //groupName

    }

    private void setGroupImg() {
        //groupImg

    }


    @OnClick({R.id.groupCode_back, R.id.groupCode_threepoint})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.groupCode_back:
                finish();
                break;
            case R.id.groupCode_threepoint:
                if (mActionSheet == null) {
                    mActionSheet = new ActionSheet(this);
                    mActionSheet.addMenuItem(getResources().getString(R.string.savePicture));
                }
                mActionSheet.show();
                Display display = getWindowManager().getDefaultDisplay();
                WindowManager.LayoutParams lp = mActionSheet.getWindow().getAttributes();
                lp.width = (int) (display.getWidth()); //设置宽度
                mActionSheet.getWindow().setAttributes(lp);
                mActionSheet.setMenuListener(new ActionSheet.MenuListener() {
                    @Override
                    public void onItemSelected(int position, String item) {
                        Toast.makeText(GroupCodeActivity.this, item, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                break;
        }
    }
}
