package com.xiaoshangxing.setting.shiming.vertify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.shiming.result.VertifyingActivity;
import com.xiaoshangxing.utils.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tianyang on 2016/10/5.
 */
public class VertifyActivity extends BaseActivity {
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.sex)
    TextView sex;
    @Bind(R.id.xuehao)
    TextView xuehao;
    @Bind(R.id.school)
    TextView school;
    @Bind(R.id.ruxuenianfen)
    TextView ruxuenianfen;


    @Bind(R.id.nameWrongImg)
    ImageView nameWrongImg;
    @Bind(R.id.sexWrongImg)
    ImageView sexWrongImg;
    @Bind(R.id.xuehaoWrongImg)
    ImageView xuehaoWrongImg;
    @Bind(R.id.schholWrongImg)
    ImageView schholWrongImg;
    @Bind(R.id.schoolRightArrow)
    ImageView schoolRightArrow;

    @Bind(R.id.VertifyButton)
    Button VertifyButton;

    public static String nameStr, sexStr, xuehaoStr, schoolStr, ruxuenianfenStr;

    public static boolean nameFlag = false, sexFlag = false, xuehaoFlag = false,
            xuexiaoFlag = false, nianfenFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify);
        ButterKnife.bind(this);

    }

    public void Back(View view) {
        finish();
    }

    public void Name(View view) {
        startActivity(new Intent(this, NameActivity.class));
    }

    public void XingBie(View view) {
        startActivity(new Intent(this, XingBieActivity.class));
    }

    public void XueHao(View view) {
        startActivity(new Intent(this, XueHaoActivity.class));
    }

    public void XueXiao(View view) {
        startActivity(new Intent(this, XueXiaoActivity.class));
    }

    public void RuXueNianFen(View view) {
        startActivity(new Intent(this, YearActivity.class));
    }

    public void VertifyNow(View view) {
        startActivity(new Intent(this, VertifyingActivity.class));
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (nameStr != null) {
            name.setText(nameStr);
            nameFlag = true;
            if (isfilled()) setButtonStyleGreen();
            else resetButtonStyle();
        }
        if (xuehaoStr != null) {
            xuehao.setText(xuehaoStr);
            xuehaoFlag = true;
            if (isfilled()) setButtonStyleGreen();
            else resetButtonStyle();
        }
        if (sexStr != null) {
            sex.setText(sexStr);
            sexFlag = true;
            if (isfilled()) setButtonStyleGreen();
            else resetButtonStyle();
        }
        if (ruxuenianfenStr != null) {
            ruxuenianfen.setText(ruxuenianfenStr);
            nianfenFlag = true;
            if (isfilled()) setButtonStyleGreen();
            else resetButtonStyle();
        }
        if (schoolStr != null) {
            school.setText(schoolStr);
            xuexiaoFlag = true;
            if (isfilled()) setButtonStyleGreen();
            else resetButtonStyle();
        }
    }


    public void setButtonStyleGreen() {
        VertifyButton.setAlpha(1);
        VertifyButton.setBackground(getResources().getDrawable(R.drawable.buttonstyle_green1));
        VertifyButton.setTextColor(getResources().getColor(R.color.w0));
        VertifyButton.setEnabled(true);
    }

    //还原button为浅色
    public void resetButtonStyle() {
        VertifyButton.setAlpha((float) 0.3);
        VertifyButton.setBackground(getResources().getDrawable(R.drawable.buttonstyle_w0));
        VertifyButton.setTextColor(getResources().getColor(R.color.b0));
        VertifyButton.setEnabled(false);
    }

    public boolean isfilled() {
        return nameFlag && sexFlag && xuehaoFlag && xuexiaoFlag && nianfenFlag;
    }


}
