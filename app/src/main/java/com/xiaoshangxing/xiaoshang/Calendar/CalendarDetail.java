package com.xiaoshangxing.xiaoshang.Calendar;

import android.os.Bundle;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.CalendarData;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmoticonsEditText;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IntentStatic;

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

    private int momentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_detail);
        ButterKnife.bind(this);
        momentId = getIntent().getIntExtra(IntentStatic.DATA, -1);
        if (momentId == -1) {
            showToast("校历信息异常");
            return;
        }
        CalendarData calendarData = realm.where(CalendarData.class).equalTo("momentId", momentId).findFirst();
        if (calendarData == null) {
            showToast("校历信息异常");
            return;
        }
        overridePendingTransition(0, 0);
        text.setText("" + calendarData.getText());
    }

    @OnClick(R.id.text)
    public void onClick() {
        finish();
    }
}
