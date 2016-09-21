package com.xiaoshangxing.setting.shiming.vertify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.Picker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tianyang on 2016/9/19.
 */
public class VertifyShiMingActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.sex)
    TextView sex;
    @Bind(R.id.sexRightArrow)
    ImageView sexRightArrow;
    @Bind(R.id.xuehao)
    EditText xuehao;
    @Bind(R.id.school)
    TextView school;
    @Bind(R.id.xueyuan)
    TextView xueyuan;
    @Bind(R.id.zhuanye)
    TextView zhuanye;
    @Bind(R.id.ruxuenianfen)
    TextView ruxuenianfen;
    @Bind(R.id.VertifyButton)
    Button VertifyButton;

    private ViewPager viewPager;
    private List<View> viewList = new ArrayList<View>();
    private ViewAdapter viewAdapter;
    private Picker picker1, picker2, picker3;
    private LinearLayout ButtomView;
    private boolean isClosed = true;
    private String[] strings1, strings2, strings3;
    private ImageView imgLeft, imgRight;
    private boolean isSelected1 = false, isSelected2 = false, isSelected3 = false;
    public static String nameStr, sexStr, xuehaoStr, schoolStr,
            xueyuanStr, zhuanyeStr, ruxuenianfenStr;
    public static boolean nameFlag = false, sexFlag = false, xuehaoFlag = false,
            xuexiaoFlag = false, xueyuanFlag = false, zhuanyeFlag = false, nianfenFlag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shiming_vertify);
        ButterKnife.bind(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        imgLeft = (ImageView) findViewById(R.id.left);
        imgRight = (ImageView) findViewById(R.id.right);
        ButtomView = (LinearLayout) findViewById(R.id.view);


        View view1 = getLayoutInflater().inflate(R.layout.layout_xueyuan, null);
        View view2 = getLayoutInflater().inflate(R.layout.layout_zhuanye, null);
        View view3 = getLayoutInflater().inflate(R.layout.layout_ruxuenianfen, null);
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewAdapter = new ViewAdapter(viewList);
        viewPager.setAdapter(viewAdapter);
        viewPager.setOnPageChangeListener(this);

        picker1 = (Picker) view1.findViewById(R.id.picker1);
        picker2 = (Picker) view2.findViewById(R.id.picker2);
        picker3 = (Picker) view3.findViewById(R.id.picker3);

        strings1 = new String[8];
        strings2 = new String[8];
        strings3 = new String[8];
        for (int i = 0; i < 8; i++) {
            strings1[i] = "设计学院" + i;
            strings2[i] = "工业设计" + i;
            strings3[i] = 2006 + i + "";
        }

        picker1.setOffset(1);
        picker1.setItems(Arrays.asList(strings1));
        picker1.setOnWheelViewListener(new Picker.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                isSelected1 = true;
                xueyuanStr = item;
            }
        });

        picker2.setOffset(1);
        picker2.setItems(Arrays.asList(strings2));
        picker2.setOnWheelViewListener(new Picker.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                isSelected2 = true;
                zhuanyeStr = item;
            }
        });

        picker3.setOffset(1);
        picker3.setItems(Arrays.asList(strings3));
        picker3.setOnWheelViewListener(new Picker.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                isSelected3 = true;
                ruxuenianfenStr = item;
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    nameFlag = true;
                } else {
                    nameFlag = false;
                }
                if (isfilled()) setButtonStyleGreen();
                else resetButtonStyle();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        xuehao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    xuehaoFlag = true;
                } else {
                    xuehaoFlag = false;
                }
                if (isfilled()) setButtonStyleGreen();
                else resetButtonStyle();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    public void Back(View view) {
        finish();
    }

    public void XingBie(View view) {
        startActivity(new Intent(this, XingBieActivity.class));
    }

    public void XueXiao(View view) {
        startActivity(new Intent(this, DiQUActivity.class));
    }

    public void XueYuan(View view) {
        if (isClosed) {
            ButtomView.setVisibility(View.VISIBLE);
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_in);
            ButtomView.startAnimation(anim);
            anim.setFillAfter(true);
            isClosed = false;
            viewPager.setCurrentItem(0);
            imgLeft.setVisibility(View.INVISIBLE);
        } else {
            viewPager.setCurrentItem(0);
        }

    }

    public void ZhuanYe(View view) {
        if (isClosed) {
            ButtomView.setVisibility(View.VISIBLE);
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_in);
            ButtomView.startAnimation(anim);
            anim.setFillAfter(true);
            isClosed = false;
            viewPager.setCurrentItem(1);
        } else {
            viewPager.setCurrentItem(1);
        }

    }

    public void RuXueNianFen(View view) {
        if (isClosed) {
            ButtomView.setVisibility(View.VISIBLE);
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_in);
            ButtomView.startAnimation(anim);
            anim.setFillAfter(true);
            isClosed = false;
            viewPager.setCurrentItem(2);
        } else {
            viewPager.setCurrentItem(2);
        }
    }

    public void VertifyNow(View view) {
        startActivity(new Intent(this, VertifyingActivity.class));

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                imgLeft.setVisibility(View.INVISIBLE);
                imgRight.setVisibility(View.VISIBLE);
                break;
            case 1:
                imgLeft.setVisibility(View.VISIBLE);
                imgRight.setVisibility(View.VISIBLE);
                break;
            case 2:
                imgLeft.setVisibility(View.VISIBLE);
                imgRight.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public void Close(View view) {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_out);
        ButtomView.startAnimation(anim);
        anim.setFillAfter(true);
        isClosed = true;
        Log.d("qqq", "isSelected1  " + isSelected1);


        int position = viewPager.getCurrentItem();
        if (position == 0 && isSelected1) {
            xueyuan.setText(xueyuanStr);
            xueyuanFlag = true;
            if (isfilled()) setButtonStyleGreen();
            else resetButtonStyle();
        } else if (position == 1 && isSelected2) {
            zhuanye.setText(zhuanyeStr);
            zhuanyeFlag = true;
            if (isfilled()) setButtonStyleGreen();
            else resetButtonStyle();
        } else if (position == 2 && isSelected3) {
            ruxuenianfen.setText(ruxuenianfenStr);
            nianfenFlag = true;
            if (isfilled()) setButtonStyleGreen();
            else resetButtonStyle();
        } else if (position == 0 && !isSelected1) {
            xueyuan.setText(strings1[0]);
            xueyuanFlag = true;
            if (isfilled()) setButtonStyleGreen();
            else resetButtonStyle();
        } else if (position == 1 && !isSelected2) {
            zhuanye.setText(strings2[0]);
            zhuanyeFlag = true;
            if (isfilled()) setButtonStyleGreen();
            else resetButtonStyle();
        } else if (position == 2 && !isSelected3) {
            ruxuenianfen.setText(strings3[0]);
            nianfenFlag = true;
            if (isfilled()) setButtonStyleGreen();
            else resetButtonStyle();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        nameStr = name.getText().toString();
        xuehaoStr = xuehao.getText().toString();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (nameStr != null) name.setText(nameStr);
        if (xuehaoStr != null) xuehao.setText(xuehaoStr);
        if (sexStr != null) {
            sexRightArrow.setVisibility(View.INVISIBLE);
            sex.setVisibility(View.VISIBLE);
            sex.setText(sexStr);
            sexFlag = true;
            if (isfilled()) setButtonStyleGreen();
            else resetButtonStyle();
        }
        if (xueyuanStr != null) xueyuan.setText(xueyuanStr);
        if (zhuanyeStr != null) zhuanye.setText(zhuanyeStr);
        if (ruxuenianfenStr != null) ruxuenianfen.setText(ruxuenianfenStr);
        if (schoolStr != null) {
            school.setText(schoolStr);
            xuexiaoFlag = true;
            if (isfilled()) setButtonStyleGreen();
            else resetButtonStyle();
        }
    }


    //设置button为Green样式
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
        return nameFlag && sexFlag && xuehaoFlag && xuexiaoFlag && xueyuanFlag && zhuanyeFlag && nianfenFlag;
    }
}
