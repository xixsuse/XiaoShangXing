package com.xiaoshangxing.xiaoshang.schoolcalender.SchoolCalenderInfoFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;

/**
 * Created by quchwe on 2016/7/22 0022.
 */
public class CalenderInfoFullScreen extends BaseActivity {

    private TextView tv_text;
    private RelativeLayout calender_full;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();

// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_calender_info_full_screen);
        // status bar is hidden, so hide that too if necessary.
//      android.app.ActionBar actionbar = getActionBar();
//        actionbar.hide();
        s = getIntent().getStringExtra("data");
        initView();
        if (s!=null){
            tv_text.setText(s);
        }

        calender_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        tv_text = (TextView) findViewById(R.id.tv_text);
        calender_full = (RelativeLayout) findViewById(R.id.calender_full);
    }
}