package com.xiaoshangxing.yujian.personInfo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.DataSetting;
import com.xiaoshangxing.setting.personalinfo.TagView.TagViewActivity;
import com.xiaoshangxing.setting.utils.photo_choosing.RoundedImageView;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;

/**
 * Created by 15828 on 2016/7/25.
 */
public class PersonInfoActivity extends BaseActivity {
    private TextView name, xueyuan, hometown, tag;
    private RoundedImageView head, img1, img2, img3, img4;
    private String tagContent;

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

        tagContent = "标签1  标签2  标签3  标签4  标签5";
        tag.setText(tagContent);

    }

    public void Back(View view) {
        finish();
    }

    public void Next(View view) {
        Intent intent = new Intent(this, SetInfoActivity.class);
        startActivity(intent);
    }

    public void More(View view) {

        if( DataSetting.IsFocused(this)){
            Intent intent = new Intent(this, MoreInfoActivity.class);
            startActivity(intent);
        }else {
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

    }

    //标签
    public void Tag(View view) {
        Intent intent = new Intent(this, TagViewActivity.class);
        startActivity(intent);
    }
}
