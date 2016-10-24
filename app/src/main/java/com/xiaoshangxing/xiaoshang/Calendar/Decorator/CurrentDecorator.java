package com.xiaoshangxing.xiaoshang.Calendar.Decorator;

import android.text.style.TextAppearanceSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.xiaoshangxing.R;

/**
 * Decorate several days with a dot
 */
public class CurrentDecorator implements DayViewDecorator {

    private int color;
    private CalendarDay day;

    public CurrentDecorator(int color, CalendarDay day) {
        this.color = color;
        this.day = day;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day != null && day.equals(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new MyDotSpan(50, color, true));
    }
}
