package com.xiaoshangxing.yujian.personInfo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.DataSetting;
import com.xiaoshangxing.setting.personalinfo.TagView.TagViewActivity;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.ImageButtonText;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.layout.RoundedImageView;
import com.xiaoshangxing.utils.normalUtils.SPUtils;
import com.xiaoshangxing.yujian.ChatActivity.ChatActivity;
import com.xiaoshangxing.yujian.pearsonalTag.PeraonalTagActivity;

/**
 * Created by 15828 on 2016/7/25.
 */
public class PersonInfoActivity extends BaseActivity implements ImageButtonText.OnImageButtonTextClickListener {
    private TextView name, xueyuan, hometown, tag;
    private RoundedImageView head, img1, img2, img3, img4;
    private String tagContent;
    private ImageButtonText mImagButtonText;
    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_personinfo);
        name = (TextView) findViewById(R.id.personinfo_name);
        xueyuan = (TextView) findViewById(R.id.personinfo_xueyaun);
        hometown = (TextView) findViewById(R.id.personifo_hometown_content);
        tag = (TextView) findViewById(R.id.personinfo_tag_content);
        head = (RoundedImageView) findViewById(R.id.personinfo_headimg);
        img1 = (RoundedImageView) findViewById(R.id.dynamic_image1);
        img2 = (RoundedImageView) findViewById(R.id.dynamic_image2);
        img3 = (RoundedImageView) findViewById(R.id.dynamic_image3);
        img4 = (RoundedImageView) findViewById(R.id.dynamic_image4);
        mImagButtonText = (ImageButtonText) findViewById(R.id.bt1);
        mImagButtonText.setmOnImageButtonTextClickListener(this);

        tagContent = "标签1  标签2  标签3  标签4  标签5";
        tag.setText(tagContent);

        account=getIntent().getStringExtra(IntentStatic.EXTRA_ACCOUNT);

    }

    public void Back(View view) {
        finish();
    }

    public void Next(View view) {
        Intent intent = new Intent(this, SetInfoActivity.class);
        startActivity(intent);
    }

    public void More(View view) {

        if (DataSetting.IsFocused(this)) {
            Intent intent = new Intent(this, MoreInfoActivity.class);
            startActivity(intent);
        } else {
            final DialogUtils.Dialog_Center2 dialogUtils = new DialogUtils.Dialog_Center2(this);
            final Dialog alertDialog = dialogUtils.Message("你未留心对方，不能查看\n对方更多资料")
                    .Button("确定").MbuttonOnClick(new DialogUtils.Dialog_Center2.buttonOnClick() {
                        @Override
                        public void onButton1() {
                            dialogUtils.close();
                        }

                        @Override
                        public void onButton2() {

                        }

                    }).create();
            alertDialog.show();
            LocationUtil.setWidth(this, alertDialog,
                    getResources().getDimensionPixelSize(R.dimen.x780));
        }

    }

    //打个招呼
    public void SayHello(View view) {
        //test
        if (account==null){
            account="17768345313";
        }
        ChatActivity.start(this,account,null, SessionTypeEnum.P2P);
    }

    //标签
    public void Tag(View view) {
        //别人的
        Intent intent = new Intent(this, PeraonalTagActivity.class);
        startActivity(intent);
        //自己的
//        Intent intent = new Intent(this, TagViewActivity.class);
//        startActivity(intent);
    }

    @Override
    public void OnImageButtonTextClick() {
        if (!mImagButtonText.isChecked()) {
            mImagButtonText.setChecked(true);
            mImagButtonText.getImgView().setImageResource(R.mipmap.icon_liuxin_select);
            SPUtils.put(this, "focus", true);

            DialogUtils.Dialog_Linxin dialog_no_button =
                    new DialogUtils.Dialog_Linxin(this, "已添加到我关注的人");
            final Dialog notice_dialog = dialog_no_button.create();
            notice_dialog.show();
            LocationUtil.setWidth(this, notice_dialog,
                    getResources().getDimensionPixelSize(R.dimen.x420));

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    notice_dialog.dismiss();
                }
            }, 500);
        } else {
            final DialogUtils.Dialog_Center2 dialogUtils = new DialogUtils.Dialog_Center2(this);
            Dialog alertDialog = dialogUtils.Message("确定不再留心？")
                    .Button("取消", "确定").MbuttonOnClick(new DialogUtils.Dialog_Center2.buttonOnClick() {
                        @Override
                        public void onButton1() {
                            dialogUtils.close();
                        }

                        @Override
                        public void onButton2() {
                            mImagButtonText.setChecked(false);
                            mImagButtonText.getImgView().setImageResource(R.mipmap.icon_liuxin);
                            SPUtils.put(PersonInfoActivity.this, "focus", false);
                            dialogUtils.close();
                        }
                    }).create();
            alertDialog.show();
            LocationUtil.setWidth(this, alertDialog,
                    getResources().getDimensionPixelSize(R.dimen.x780));
        }
    }


}
