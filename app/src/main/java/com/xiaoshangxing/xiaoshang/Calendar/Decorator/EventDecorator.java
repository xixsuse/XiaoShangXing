package com.xiaoshangxing.xiaoshang.Calendar.Decorator;

import android.content.Context;
import android.text.style.TextAppearanceSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;

import java.util.Collection;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {

    private int color;
    private HashSet<CalendarDay> dates;
    private Context context;

    public EventDecorator(int color, Collection<CalendarDay> dates, Context context) {
        this.color = color;
        this.dates = new HashSet<>(dates);
        this.context = context;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new MyDotSpan(ScreenUtils.getAdapterPx(R.dimen.x50, context), color, false));
        view.addSpan(new TextAppearanceSpan(context, R.style.white));
    }
}
