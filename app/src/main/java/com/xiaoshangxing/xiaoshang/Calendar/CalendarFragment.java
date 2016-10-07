package com.xiaoshangxing.xiaoshang.Calendar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.xiaoshangxing.Network.netUtil.LoadUtils;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.xiaoshang.Calendar.CalendarInputer.CalendarInputer;
import com.xiaoshangxing.xiaoshang.Calendar.Decorator.CurrentDecorator;
import com.xiaoshangxing.xiaoshang.Calendar.Decorator.EventDecorator;
import com.xiaoshangxing.xiaoshang.Calendar.Decorator.OneDayDecorator;
import com.xiaoshangxing.xiaoshang.Calendar.Decorator.WeekDecorator;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/10/5
 */

public class CalendarFragment extends BaseFragment implements OnDateSelectedListener, OnMonthChangedListener {
    public static final String TAG = BaseFragment.TAG + "-CalendarFragment";
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.reflesh_layout)
    PtrFrameLayout refleshLayout;
    @Bind(R.id.current_month)
    TextView currentMonth;
    @Bind(R.id.down_arrow)
    ImageView downArrow;
    @Bind(R.id.calendarView)
    MaterialCalendarView calendarView;
    @Bind(R.id.next_month)
    TextView nextMonth;
    @Bind(R.id.calendar_lay)
    LinearLayout calendarLay;
    @Bind(R.id.up_layout)
    RelativeLayout upLayout;
    @Bind(R.id.calender_log)
    ImageView calenderLog;
    @Bind(R.id.tv_nowDate)
    TextView tvNowDate;
    @Bind(R.id.tv_date_week)
    TextView tvDateWeek;
    @Bind(R.id.ib_arrow_up)
    ImageButton ibArrowUp;
    @Bind(R.id.bottom_layout)
    RelativeLayout bottomLayout;
    @Bind(R.id.notice)
    TextView notice;
    private View mview;
    private Realm realm;
    private BottomSheetBehavior mBottomSheetBehavior;

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private List<Published> publisheds = new ArrayList<>();
    private Calendar_adpter adpter;
    private List<CalendarDay> allDatas, previous, future;
    private CalendarDay today, currentSelected;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.frag_calendar, null);
        ButterKnife.bind(this, mview);
        realm = Realm.getDefaultInstance();
        initView();
        return mview;
    }

    private void initView() {
        today = CalendarDay.today();
        mBottomSheetBehavior = BottomSheetBehavior.from(mview.findViewById(R.id.calendar_lay));
        initCalendar();
        initMonth(calendarView.getCurrentDate());
        initListview(today);
        View view = new View(getContext());
        listview.addHeaderView(view);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), CalendarDetail.class);
                startActivity(intent);
            }
        });
    }

    private void initCalendar() {
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

        calendarView.state().edit()
                .setMinimumDate(new CalendarDay(2016, 8, 1))
                .setMaximumDate(new CalendarDay(2017, 6, 30));

        currentSelected = CalendarDay.today();

        refresh();

    }

    private void initDecorator() {

        calendarView.removeDecorators();

        calendarView.addDecorators(
                oneDayDecorator
        );

        if (allDatas.contains(today)) {
            if (currentSelected.equals(today)) {
                calendarView.addDecorator(new CurrentDecorator(Color.GREEN, currentSelected));
            } else {
                ArrayList<CalendarDay> todays = new ArrayList<>();
                todays.add(today);
                calendarView.addDecorator(new EventDecorator(Color.GREEN, todays));
            }
        }

        if (previous.contains(currentSelected)) {
            calendarView.addDecorator(new CurrentDecorator(Color.GRAY, currentSelected));
            if (previous.size() > 1) {
                List<CalendarDay> current_previous = previous.subList(0, previous.size());
                current_previous.remove(currentSelected);
                calendarView.addDecorator(new EventDecorator(Color.GRAY, current_previous));
            }
        } else {
            calendarView.addDecorator(new EventDecorator(Color.GRAY, previous));
        }

        if (future.contains(currentSelected)) {
            calendarView.addDecorator(new CurrentDecorator(Color.BLUE, currentSelected));
            if (future.size() > 1) {
                List<CalendarDay> current_future = future.subList(0, future.size());
                current_future.remove(currentSelected);
                calendarView.addDecorator(new EventDecorator(Color.GRAY, current_future));
            }
        } else {
            calendarView.addDecorator(new EventDecorator(Color.BLUE, future));
        }

    }

    private void initListview(CalendarDay day) {
        for (int i = 0; i < 20; i++) {
            publisheds.add(new Published());
        }
        if (publisheds.size() < 1) {
            refleshLayout.setVisibility(View.GONE);
            notice.setText("当天没有活动");
            notice.setVisibility(View.VISIBLE);
        } else {
            refleshLayout.setVisibility(View.VISIBLE);
            notice.setVisibility(View.GONE);
        }
        adpter = new Calendar_adpter(getContext(), 1, publisheds);
        listview.setAdapter(adpter);
    }

    private void initMonth(CalendarDay date) {
        currentMonth.setText(getCurrentMonth(date) + "月");
        nextMonth.setText(getNextMonth(date) + "月");
        tvNowDate.setText(FORMATTER.format(date.getDate()));
        tvDateWeek.setText(TimeUtil.getWeekOfDate_zhou(date.getDate()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        realm.close();
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

    @OnClick({R.id.back, R.id.more, R.id.up_layout, R.id.bottom_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.more:
                Intent intent = new Intent(getContext(), CalendarInputer.class);
                startActivity(intent);
                break;
            case R.id.up_layout:
                showOrHideCalendar();
                break;
            case R.id.bottom_layout:
                showOrHideCalendar();
                break;
        }
    }

    private void showOrHideCalendar() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    private void refresh() {
        allDatas = new ArrayList<>();
        previous = new ArrayList<>();
        future = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        for (int i = 0; i < 30; i++) {
            CalendarDay day = CalendarDay.from(calendar);
            allDatas.add(day);
            calendar.add(Calendar.DATE, 2);
            if (day.isAfter(today)) {
                future.add(day);
            } else if (day.isBefore(today)) {
                previous.add(day);
            }
        }
        calendarView.addDecorator(new EventDecorator(Color.parseColor("#b2b2b2"), previous));
        calendarView.addDecorator(new EventDecorator(Color.parseColor("#00aeef"), future));
        calendarView.addDecorators(
                oneDayDecorator,
                new WeekDecorator(getContext())
        );
        if (allDatas.contains(today)) {
            ArrayList<CalendarDay> todays = new ArrayList<>();
            todays.add(today);
            calendarView.addDecorator(new EventDecorator(Color.parseColor("#45C01A"), todays));
        }
    }

    private void getData(CalendarDay calendarDay) {
        String year = String.valueOf(calendarDay.getYear());
        String month = String.valueOf(calendarDay.getMonth());
        LoadUtils.getCalendar(year, month, getContext(), realm, new LoadUtils.AroundLoading() {
            @Override
            public void before() {

            }

            @Override
            public void complete() {

            }

            @Override
            public void onSuccess() {
                refresh();
            }

            @Override
            public void error() {

            }
        });
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        initMonth(date);
        oneDayDecorator.setDate(date.getDate());
//        initDecorator();
        initListview(date);
        widget.invalidateDecorators();
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        initMonth(date);
        getData(date);
    }
}
