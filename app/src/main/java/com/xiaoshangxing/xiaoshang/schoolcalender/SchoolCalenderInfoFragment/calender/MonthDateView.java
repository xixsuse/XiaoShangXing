package com.xiaoshangxing.xiaoshang.schoolcalender.SchoolCalenderInfoFragment.calender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.normalUtils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by quchwe on 2016/7/22 0022.
 */
public class MonthDateView extends View {
    protected static final int NUM_COLUMNS = 7;
    protected  int NUM_ROWS ;
    protected Paint mPaint;
    protected int mHaveSelDay;
    protected int mHaveSelMonth;
    protected int mHaveSelYear;
    protected int mStrokeWidth = 1;
    protected int myYear,myMonth,myDay;
    protected int mTopLineColor = getResources().getColor(R.color.g1);
    protected int mWeekedDayClor = getResources().getColor(R.color.g0);
    protected int mDayColor = Color.parseColor("#000000");
    protected int mSelectDayColor = Color.parseColor("#ffffff");
    protected int mSelectBGColor = Color.parseColor("#1FC2F3");
    protected int mCurrentColor = Color.parseColor("#ff0000");
    protected int pastColor  = getResources().getColor(R.color.g2);
    protected int nowColor = getResources().getColor(R.color.green1);
    protected int futureColor = getResources().getColor(R.color.blue3);
    protected int textColor = getResources().getColor(R.color.b1);
    protected Map<Integer,String> weekDayMap = new HashMap<>();
    protected int mCurrYear,mCurrMonth,mCurrDay;
    protected int mSelYear,mSelMonth,mSelDay;
    protected int mColumnSize,mRowSize;
    protected DisplayMetrics mDisplayMetrics;
    protected int mDaySize = 18;
    protected TextView currentMonth, nextMonth,date,weekDay;
    protected int weekRow;
    protected int [][] daysString = new int[6][7];
    protected int mCircleRadius =42;
    protected DateClick dateClick;
    protected int mCircleColor = Color.parseColor("#00ff00");
    protected List<Integer> daysHasThingList = new ArrayList<>();
    protected Map<Integer,Integer> dayOfWeek;
    protected int column;
    protected int row;
    protected int startX;
    protected int startCirclePointX;
    protected int endCirclePointX;
    protected int dayInteger;
    protected int startY;
    protected int startRecX;
    protected int startRecY ;
    protected int endRecX;
    protected int endRecY ;
    protected int mMonthDays;
    protected int weekNumber ;
    protected int circlePointX ;
    protected int circlePointY2;
    protected int circlePointY;
    protected String dayString;
    public MonthDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();
        Calendar calendar = Calendar.getInstance();
        mPaint = new Paint();
        mCurrYear = calendar.get(Calendar.YEAR);
        mCurrMonth = calendar.get(Calendar.MONTH);
        mCurrDay = calendar.get(Calendar.DATE);


        setSelectYearMonth(mCurrYear,mCurrMonth,mCurrDay);
        setHaveSelDate(mSelYear,mSelMonth,mSelDay);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {



        mPaint.setTextSize(mDaySize*mDisplayMetrics.scaledDensity);

        mMonthDays = DateUtils.getMonthDays(mSelYear, mSelMonth);
        weekNumber = DateUtils.getFirstDayWeek(mSelYear, mSelMonth);

        if (weekNumber!=1) {
            if (mMonthDays  - 7 * 3 -(7-weekNumber+1)>7){
                NUM_ROWS = 6;
            }else NUM_ROWS=5;
        }else {
            if(mMonthDays==28){
                NUM_ROWS=4;
            }else NUM_ROWS=5;

        }
        Log.d("tag",NUM_ROWS+"");
        initSize();
        dayOfWeek = DateUtils.getDayWeek(mSelYear,mSelMonth);
        //Log.d("DateView", "DateView:" + mSelMonth+"月1号周" + weekNumber);
        if(mHaveSelDay<10) {
            date.setText(mHaveSelYear + "年" + (mHaveSelMonth + 1) + "月0" + mHaveSelDay + "日");
        }else {
            date.setText(mHaveSelYear + "年" + (mHaveSelMonth + 1) + "月" + mHaveSelDay + "日");
        }
        weekDay.setText("周"+DateUtils.getWeekCHN(DateUtils.getDayWeek(mHaveSelYear,mHaveSelMonth).get(mHaveSelDay)));
        for(int day = 0;day < mMonthDays;day++){


            setData(day);
//			if (daysHasThingList.contains(mSelDay)&&mSelDay ==dayInteger){
//				if (date!=null){
//					date.setText(mSelYear+"年"+(mSelMonth+1)+"月"+mSelDay+"日");
//				}
//				if (weekDay!=null){
//					//Log.d("jjb",mCurrDay+"");
//					weekDay.setText("周"+DateUtils.getWeekCHN(dayOfWeek.get(mCurrDay)-1));
//				}
//			}
            int mSelectDay = getTime(dayInteger);
            Log.d("tag", "onDraw: "+mSelectDay);
            int CurrentDay = Integer.valueOf(DateUtils.getCurrentTimeInteger());
            Log.d("tag", "onDraw: "+CurrentDay);
            if (daysHasThingList.contains(mSelectDay)){
                if (mSelectDay<CurrentDay){
                    mPaint.setColor(pastColor);

                    if(getTime(mHaveSelDay)==mSelectDay){
                        mPaint.setAlpha(38);
                        canvas.drawCircle(circlePointX,circlePointY,mCircleRadius,mPaint);
                    }else {
                        canvas.drawCircle(circlePointX,circlePointY,mCircleRadius,mPaint);
                        mSelectDayColor = pastColor;
                    }
                }else if (mSelectDay==CurrentDay){
                    mPaint.setColor(nowColor);

                    if(getTime(mHaveSelDay)==mSelectDay){
                        mPaint.setAlpha(38);
                        canvas.drawCircle(circlePointX,circlePointY,mCircleRadius,mPaint);
                    }else {
                        canvas.drawCircle(circlePointX,circlePointY,mCircleRadius,mPaint);
                        mSelectDayColor = nowColor;
                    }
                }else if (mSelectDay>CurrentDay){
                    mPaint.setColor(futureColor);
                    if(getTime(mHaveSelDay)==mSelectDay){
                        mPaint.setAlpha(38);
                        canvas.drawCircle(circlePointX,circlePointY,mCircleRadius,mPaint);
                    }else {
                        canvas.drawCircle(circlePointX,circlePointY,mCircleRadius,mPaint);
                        mSelectDayColor = futureColor;
                    }
                }


                weekRow = row+1;
//				if (dayString)
            }

            if (dayOfWeek.get(Integer.valueOf(dayString))==1||dayOfWeek.get(Integer.valueOf(dayString))==7){
                mPaint.setColor(mWeekedDayClor);
            }else{
                mPaint.setColor(textColor);
            }
            if (daysHasThingList.contains(mSelectDay)){
                mPaint.setColor(getResources().getColor(R.color.w0));
                if (mSelectDay<CurrentDay){
                    if (getTime(mHaveSelDay)==mSelectDay) {
                        mPaint.setColor(pastColor);
                    }

                }else if (mSelectDay==CurrentDay){
                    if (getTime(mSelDay)==mSelectDay) {
                        mPaint.setColor(nowColor);
                    }

                }else if (mSelectDay>CurrentDay){
                    if (getTime(mSelDay)==mSelectDay) {
                        mPaint.setColor(futureColor);
                    }
                }
                //mPaint.setColor(getResources().getColor(R.color.w0));
            }

            canvas.drawText(dayString, startX, startY, mPaint);
            if(currentMonth != null){
//			currentMonth.setText(mSelYear + "年" + (mSelMonth + 1) + "月");
                currentMonth.setText( mSelMonth + 1+"月");
            }

//			if (date!=null){
//				date.setText(mSelYear+"年"+(mSelMonth+1)+"月"+mSelDay+"日");
//			}
//			if (weekDay!=null){
//				//Log.d("jjb",mCurrDay+"");
//				weekDay.setText("周"+DateUtils.getWeekCHN(dayOfWeek.get(mCurrDay)-1));
//			}
            if(nextMonth != null){
                if (mSelMonth+1==12){
                    nextMonth.setText("1月");
                }
                else nextMonth.setText(mSelMonth+2+"月");
            }

        }
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(mTopLineColor);
        p.setStrokeWidth(mStrokeWidth);
        canvas.drawLine(((weekNumber-1)*mColumnSize), 0,getWidth() , 0, p);
        for (int i=1;i<NUM_ROWS+1;i++){
            canvas.drawLine(0, i*mRowSize-1, getWidth(), i*mRowSize-1, p);
            Log.d("线",i+"");
        }
    }

    protected void drawBigCircle(int row,int column,int day,Canvas canvas) {
        if (daysHasThingList != null && daysHasThingList.size() > 0) {
            if (!daysHasThingList.contains(day)) return;
            mPaint.setColor(mCircleColor);
            float circleX = (float) (mColumnSize * column + mColumnSize);
            float circley = (float) (mRowSize * row + mRowSize*0.2);
            canvas.drawCircle(circleX, circley, mCircleRadius, mPaint);
        }
    }
    @Override
    public boolean performClick() {
        return super.performClick();
    }

    protected int downX = 0,downY = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventCode=  event.getAction();
        switch(eventCode){
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if(Math.abs(upX-downX) < 10 && Math.abs(upY - downY) < 10){//点击事件
                    performClick();
                    doClickAction((upX + downX)/2,(upY + downY)/2);
                }else if(upY - downY > 50){
                    onLeftClick();

                }else if(downY-upY>50){
                    onRightClick();
                }
                break;
        }
        return true;
    }

    /**
     * 初始化列宽行高
     */
    protected void initSize(){
        mColumnSize = getWidth() / NUM_COLUMNS;
        mRowSize = getHeight() / NUM_ROWS;
        Log.d("高度",mRowSize+"");
    }

    /**
     * 设置年月
     * @param year
     * @param month
     */
    protected void setSelectYearMonth(int year,int month,int day){
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
    }
    /**
     * 执行点击事件
     * @param x
     * @param y
     */
    protected void doClickAction(int x,int y){


        int row = y / mRowSize;
        int column = x / mColumnSize;
        setSelectYearMonth(mSelYear,mSelMonth,daysString[row][column]);
        setHaveSelDate(mSelYear,mSelMonth,mSelDay);
        invalidate();

        //执行activity发送过来的点击处理事件
        if(dateClick != null){
            dateClick.onClickOnDate();
        }
    }

    /**
     * 左点击，日历向后翻页
     */
    public void onLeftClick(){
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if(month == 0){//若果是1月份，则变成12月份
            year = mSelYear-1;
            month = 11;
        }else if(DateUtils.getMonthDays(year, month) == day){
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month-1;
            day = DateUtils.getMonthDays(year, month);
        }else{
            month = month-1;
        }
        setSelectYearMonth(year,month,day);
        invalidate();
    }

    /**
     * 右点击，日历向前翻页
     */
    public void onRightClick(){
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if(month == 11){//若果是12月份，则变成1月份
            year = mSelYear+1;
            month = 0;
        }else if(DateUtils.getMonthDays(year, month) == day){
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month + 1;
            day = DateUtils.getMonthDays(year, month);
        }else{
            month = month + 1;
        }
        setSelectYearMonth(year,month,day);
        invalidate();
    }

    /**
     * 获取选择的年份
     * @return
     */
    public int getmSelYear() {
        return mSelYear;
    }
    /**
     * 获取选择的月份
     * @return
     */
    public int getmSelMonth() {
        return mSelMonth;
    }
    /**
     * 获取选择的日期
     * @param
     */
    public int getmSelDay() {
        return this.mSelDay;
    }
    /**
     * 普通日期的字体颜色，默认黑色
     * @param mDayColor
     */
    public void setmDayColor(int mDayColor) {
        this.mDayColor = mDayColor;
    }

    /**
     * 选择日期的颜色，默认为白色
     * @param mSelectDayColor
     */
    public void setmSelectDayColor(int mSelectDayColor) {
        this.mSelectDayColor = mSelectDayColor;
    }
    public int getMselectDayColor(){
        return this.mSelectDayColor;
    }

    /**
     * 选中日期的背景颜色，默认蓝色
     * @param mSelectBGColor
     */
    public void setmSelectBGColor(int mSelectBGColor) {
        this.mSelectBGColor = mSelectBGColor;
    }
    /**
     * 当前日期不是选中的颜色，默认红色
     * @param mCurrentColor
     */
    public void setmCurrentColor(int mCurrentColor) {
        this.mCurrentColor = mCurrentColor;
    }

    /**
     * 日期的大小，默认18sp
     * @param mDaySize
     */
    public void setmDaySize(int mDaySize) {
        this.mDaySize = mDaySize;
    }
    /**
     * 设置显示当前日期的控件
     * @param tv_date
     * 		显示日期
     * @param tv_week
     * 		显示周
     */
    public void setTextView(TextView tv_date,TextView tv_week,TextView date,TextView weekDay){
        this.currentMonth = tv_date;
        this.nextMonth = tv_week;
        this.date = date;
        this.weekDay = weekDay;
        invalidate();
    }

    /**
     * 设置事务天数
     * @param daysHasThingList
     */
    public void setDaysHasThingList(List<Integer> daysHasThingList) {
        this.daysHasThingList = daysHasThingList;
    }

    /***
     * 设置圆圈的半径，默认为6
     * @param mCircleRadius
     */
    public void setmCircleRadius(int mCircleRadius) {
        this.mCircleRadius = mCircleRadius;
    }

    /**
     * 设置圆圈的半径
     * @param mCircleColor
     */
    public void setmCircleColor(int mCircleColor) {
        this.mCircleColor = mCircleColor;
    }

    /**
     * 设置日期的点击回调事件
     * @author shiwei.deng
     *
     */
    public interface DateClick{
        public void onClickOnDate();
    }

    /**
     * 设置日期点击事件
     * @param dateClick
     */
    public void setDateClick(DateClick dateClick) {
        this.dateClick = dateClick;
    }

    /**
     * 跳转至今天
     */
    public void setTodayToView(){
        setSelectYearMonth(mCurrYear,mCurrMonth,mCurrDay);
        invalidate();
    }
    protected void setData(int day){
        dayString = (day + 1) + "";
        //Log.d("tag",dayString);
        column = (day+weekNumber - 1) % 7;
        row = (day+weekNumber - 1) / 7;
        daysString[row][column]=day + 1;

        dayInteger = Integer.valueOf(dayString);
        if (dayOfWeek.get(dayInteger)==1) {
            startX = (int)(mColumnSize - mPaint.measureText(dayString))/2-20;
            startCirclePointX = mColumnSize * column-20;
        }else  {
            startX = (int) ((mColumnSize * column ) + 5 * column+(mColumnSize - mPaint.measureText(dayString))/2);
            startCirclePointX = mColumnSize * column + 5*column;
        }

        startY = (int) (mRowSize * row + mRowSize/2 - (mPaint.ascent() + mPaint.descent())/2);
        startRecX = mColumnSize * column-20;
        startRecY = mRowSize * row;
        endRecX = startRecX + mColumnSize;
        endRecY = startRecY + mRowSize;
        endCirclePointX = startCirclePointX+mColumnSize;
        circlePointX = (startCirclePointX+endCirclePointX)/2;
        circlePointY = (startRecY+endRecY)/2;
    }
    //获得选中日期 格式为 20160312
    public  int getTime(int day){
        int date;
        String dateString = mSelYear+"";
        if (mSelMonth<9){
            dateString += "0"+(mSelMonth+1);

        }else dateString+=""+(mSelMonth+1);

        if (day<10){
            dateString+="0"+day;
        }else dateString+=""+day;

        return Integer.valueOf(dateString);
    }
    protected void setHaveSelDate(int year,int month,int day){
        mHaveSelDay = day;
        mHaveSelMonth = month;
        mHaveSelYear = year;
    }
}
