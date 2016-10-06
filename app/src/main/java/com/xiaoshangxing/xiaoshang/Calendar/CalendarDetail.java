package com.xiaoshangxing.xiaoshang.Calendar;

import android.os.Bundle;

import com.xiaoshangxing.R;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmoticonsEditText;
import com.xiaoshangxing.utils.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/10/6
 */

public class CalendarDetail extends BaseActivity {
    @Bind(R.id.text)
    EmoticonsEditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_detail);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.text)
    public void onClick() {
        finish();
    }
}
