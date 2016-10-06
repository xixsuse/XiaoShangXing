package com.xiaoshangxing.xiaoshang.Calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/10/6
 */

public class CalendarInput extends BaseActivity implements OnDateSelectedListener, OnMonthChangedListener {
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.calender_log)
    ImageView calenderLog;
    @Bind(R.id.tv_nowDate)
    TextView tvNowDate;
    @Bind(R.id.tv_date_week)
    TextView tvDateWeek;
    @Bind(R.id.data_layout)
    RelativeLayout dataLayout;
    @Bind(R.id.text)
    EditText text;
    @Bind(R.id.current_month)
    TextView currentMonth;
    @Bind(R.id.down_arrow)
    ImageView downArrow;
    @Bind(R.id.up_layout)
    RelativeLayout upLayout;
    @Bind(R.id.calendarView)
    MaterialCalendarView calendarView;
    @Bind(R.id.next_month)
    TextView nextMonth;
    @Bind(R.id.calendar_lay)
    LinearLayout calendarLay;
    @Bind(R.id.complete)
    TextView complete;
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_input);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.calendar_lay));
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(text.getText().toString())) {
                    complete.setEnabled(false);
                    complete.setAlpha(0.5f);
                } else {
                    complete.setEnabled(true);
                    complete.setAlpha(1f);
                }
            }
        });
        calendarView.setTopbarVisible(false);
        calendarView.setWeekDayFormatter(new ArrayWeekDayFormatter(getResources().getTextArray(R.array.calendar)));
        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);
        calendarView.setDateTextAppearance(R.style.black_17sp);
        calendarView.setWeekDayTextAppearance(R.style.b1_13sp);
        calendarView.setTileSize(-1);
        calendarView.setTileWidthDp(50);
        calendarView.setTileHeightDp(35);
        calendarView.setCurrentDate(CalendarDay.today());
        calendarView.setSelectionColor(Color.TRANSPARENT);
    }

    private void showOrHideCalendar() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @OnClick({R.id.back, R.id.data_layout, R.id.text, R.id.complete, R.id.up_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.data_layout:
                showOrHideCalendar();
                break;
            case R.id.text:
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
            case R.id.complete:
                break;
            case R.id.up_layout:
                showOrHideCalendar();
                break;
        }
    }

    private void initMonth(CalendarDay date) {
        currentMonth.setText(getCurrentMonth(date) + "月");
        nextMonth.setText(getNextMonth(date) + "月");
        tvNowDate.setText(FORMATTER.format(date.getDate()));
        tvDateWeek.setText(TimeUtil.getWeekOfDate_zhou(date.getDate()));
    }

    private String getCurrentMonth(CalendarDay date) {
        if (date == null) {
            return "No Selection";
        }
        return String.valueOf(date.getMonth() + 1);
    }

    private String getNextMonth(CalendarDay date) {
        if (date == null) {
            return "No Selection";
        }
        if (date.getMonth() == 11) {
            return "1";
        }
        return String.valueOf(date.getMonth() + 2);
    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        initMonth(date);
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        initMonth(date);
    }
}
