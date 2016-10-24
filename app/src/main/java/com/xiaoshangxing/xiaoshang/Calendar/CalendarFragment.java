package com.xiaoshangxing.xiaoshang.Calendar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.CalendarData;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.layout.LayoutHelp;
import com.xiaoshangxing.utils.pull_refresh.PtrDefaultHandler;
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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by FengChaoQun
 * on 2016/10/5
 */

public class CalendarFragment extends BaseFragment implements OnDateSelectedListener, OnMonthChangedListener {
    public static final String TAG = BaseFragment.TAG + "-CalendarFragment";
    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
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

    private View mview;
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

    private Handler handler = new Handler();
    private Runnable runnable;
    private View headView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.frag_calendar, null);
        ButterKnife.bind(this, mview);
        initView();
        initFresh();
        return mview;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        handler.removeCallbacks(runnable);
    }

    private void initView() {

        title.setText("校历资讯");
        leftText.setText(R.string.xiaoshang);
        more.setImageResource(R.mipmap.add);

        allDatas = new ArrayList<>();
        previous = new ArrayList<>();
        future = new ArrayList<>();
        today = CalendarDay.today();

        headView = View.inflate(getContext(), R.layout.util_textview, null);
        listview.addHeaderView(headView);

        mBottomSheetBehavior = BottomSheetBehavior.from(mview.findViewById(R.id.calendar_lay));
        initCalendar();
        initMonth(calendarView.getCurrentDate());
        initListview(today);

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

        calendarView.addDecorators(
                oneDayDecorator,
                new WeekDecorator(getContext())
        );

        refresh(today);

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
                calendarView.addDecorator(new EventDecorator(Color.GREEN, todays, getContext()));
            }
        }

        if (previous.contains(currentSelected)) {
            calendarView.addDecorator(new CurrentDecorator(Color.GRAY, currentSelected));
            if (previous.size() > 1) {
                List<CalendarDay> current_previous = previous.subList(0, previous.size());
                current_previous.remove(currentSelected);
                calendarView.addDecorator(new EventDecorator(Color.GRAY, current_previous, getContext()));
            }
        } else {
            calendarView.addDecorator(new EventDecorator(Color.GRAY, previous, getContext()));
        }

        if (future.contains(currentSelected)) {
            calendarView.addDecorator(new CurrentDecorator(Color.BLUE, currentSelected));
            if (future.size() > 1) {
                List<CalendarDay> current_future = future.subList(0, future.size());
                current_future.remove(currentSelected);
                calendarView.addDecorator(new EventDecorator(Color.GRAY, current_future, getContext()));
            }
        } else {
            calendarView.addDecorator(new EventDecorator(Color.BLUE, future, getContext()));
        }

    }

    private void initListview(CalendarDay calendarDay) {

        List<CalendarData> calendarDatas = realm.where(CalendarData.class)
                .equalTo(NS.YEAR, String.valueOf(calendarDay.getYear()))
                .equalTo(NS.MONTH, String.valueOf(calendarDay.getMonth()))
                .equalTo(NS.DAY, String.valueOf(calendarDay.getDay()))
                .findAllSorted(NS.CREATETIME, Sort.DESCENDING);

        if (calendarDatas.size() < 1) {
            headView.setVisibility(View.VISIBLE);
            headView.setPadding(0, 48, 0, 0);
            showToast("当天没有活动");
        } else {
            headView.setVisibility(View.GONE);
            headView.setPadding(0, 0, 0, 0);
        }

        adpter = new Calendar_adpter(getContext(), 1, calendarDatas);
        listview.setAdapter(adpter);
    }

    private void initFresh() {
        LayoutHelp.initPTR(refleshLayout, false,
                new PtrDefaultHandler() {
                    @Override
                    public void onRefreshBegin(final PtrFrameLayout frame) {
                        LoadUtils.getCalendar(String.valueOf(currentSelected.getYear()),
                                String.valueOf(currentSelected.getMonth()), getContext(),
                                realm, new LoadUtils.AroundLoading() {
                                    @Override
                                    public void before() {

                                    }

                                    @Override
                                    public void complete() {
                                        refleshLayout.refreshComplete();
                                    }

                                    @Override
                                    public void onSuccess() {
                                        refresh(currentSelected);
                                    }

                                    @Override
                                    public void error() {
                                        refleshLayout.refreshComplete();
                                    }
                                });
                    }
                });
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

    private void refresh(CalendarDay calendarDay) {
        List<CalendarData> calendarDatas = realm.where(CalendarData.class)
                .equalTo(NS.YEAR, String.valueOf(calendarDay.getYear()))
                .equalTo(NS.MONTH, String.valueOf(calendarDay.getMonth()))
                .findAllSorted(NS.CREATETIME, Sort.DESCENDING);

        ArrayList<CalendarDay> calendarDays = new ArrayList<>();
        for (CalendarData i : calendarDatas) {
            CalendarDay j = new CalendarDay(Integer.valueOf(i.getYear()),
                    Integer.valueOf(i.getMonth()), Integer.valueOf(i.getDay()));
            calendarDays.add(j);
        }

        switch (compareDayByYearMonth(calendarDay, today)) {
            case -1:
                calendarView.addDecorator(new EventDecorator(Color.parseColor("#b2b2b2"), calendarDays, getContext()));
                break;
            case 0:
                parseThisMonthData(calendarDays);
                break;
            case 1:
                calendarView.addDecorator(new EventDecorator(Color.parseColor("#00aeef"), calendarDays, getContext()));
                break;
        }

    }

    private void parseThisMonthData(ArrayList<CalendarDay> calendarDays) {
        for (CalendarDay day : calendarDays) {
            if (day.isAfter(today)) {
                future.add(day);
            } else if (day.isBefore(today)) {
                previous.add(day);
            }
        }

        calendarView.addDecorator(new EventDecorator(Color.parseColor("#b2b2b2"), previous, getContext()));
        calendarView.addDecorator(new EventDecorator(Color.parseColor("#00aeef"), future, getContext()));

        if (calendarDays.contains(today)) {
            ArrayList<CalendarDay> todays = new ArrayList<>();
            todays.add(today);
            calendarView.addDecorator(new EventDecorator(Color.parseColor("#45C01A"), todays, getContext()));
        }
    }

    public static int compareDayByYearMonth(CalendarDay origin, CalendarDay object) {
        if (origin.getYear() == object.getYear()) {
            if (origin.getMonth() == object.getMonth()) {
                return 0;
            } else {
                return origin.getMonth() > object.getMonth() ? 1 : -1;
            }
        } else {
            return origin.getYear() > object.getYear() ? 1 : -1;
        }
    }

    private void getData(final CalendarDay calendarDay) {

        String year = String.valueOf(calendarDay.getYear());
        String month = String.valueOf(calendarDay.getMonth());

        if (realm.where(CalendarData.class).equalTo(NS.YEAR, year).equalTo(NS.MONTH, month).findAll().size() > 0)
            return;

        LoadUtils.getCalendar(year, month, getContext(), realm, new LoadUtils.AroundLoading() {
            @Override
            public void before() {

            }

            @Override
            public void complete() {

            }

            @Override
            public void onSuccess() {
                refresh(calendarDay);
            }

            @Override
            public void error() {

            }
        });
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        currentSelected = date;
        initMonth(date);
        oneDayDecorator.setDate(date.getDate());
//        initDecorator();
        initListview(date);
        widget.invalidateDecorators();
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, final CalendarDay date) {
        initMonth(date);
        runnable = new Runnable() {
            @Override
            public void run() {
                getData(date);
            }
        };
        handler.postDelayed(runnable, 500);
        currentSelected = date;
    }
}
