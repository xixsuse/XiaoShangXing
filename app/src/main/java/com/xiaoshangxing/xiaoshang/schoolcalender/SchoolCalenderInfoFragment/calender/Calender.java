package com.xiaoshangxing.xiaoshang.schoolcalender.SchoolCalenderInfoFragment.calender;

/**
 * Created by quchwe on 2016/7/22 0022.
 */
public  class Calender {
    private int year;
    private int month;
    private int day;
    public static Calender create(int year, int month, int day){
        return null;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
