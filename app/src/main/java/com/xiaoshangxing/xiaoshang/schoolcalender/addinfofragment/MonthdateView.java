package com.xiaoshangxing.xiaoshang.schoolcalender.addinfofragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.normalUtils.DateUtils;
import com.xiaoshangxing.xiaoshang.schoolcalender.SchoolCalenderInfoFragment.calender.MonthDateView;

/**
 * Created by quchwe on 2016/7/22 0022.
 */
public class MonthdateView extends MonthDateView {
    public MonthdateView(Context context, AttributeSet attrs) {
        super(context, attrs);

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

        setDate();
        for(int day = 0;day < mMonthDays;day++){
            setData(day);
            if (getTime(day+1)<Integer.valueOf(DateUtils.getCurrentTimeInteger())){
                continue;
            }

            int mSelectDay = getTime(dayInteger);
           // Log.d("tag", "onDraw: "+mSelectDay);
            int CurrentDay = Integer.valueOf(DateUtils.getCurrentTimeInteger());
            Log.d("tag", "onDraw: "+CurrentDay);
            /**
             * 将当前日期
             * 对点击日期的绘制，如果用户点击了某个日期，且这个日期不是当前日期
             * 则将这个日期背景设为蓝色圆，字体颜色白色、
             *
             */



            if (mHaveSelMonth!=mSelMonth){
//                if (mSelDay != dayInteger)else if (mSelDay == dayInteger){
//                    mPaint.setColor(textColor);
//                    canvas.drawText(dayString, startX, startY, mPaint);
//                }
                if (dayOfWeek.get(Integer.valueOf(dayString)) == 1 || dayOfWeek.get(Integer.valueOf(dayString)) == 7) {
                    mPaint.setColor(mWeekedDayClor);
                    canvas.drawText(dayString, startX, startY, mPaint);
                }else  {
                    mPaint.setColor(textColor);
                    canvas.drawText(dayString, startX, startY, mPaint);
                }
            }
            if (mHaveSelMonth ==mSelMonth){
                if (mSelDay != dayInteger) {
                    mPaint.setColor(textColor);
                    canvas.drawText(dayString, startX, startY, mPaint);
                }
                if (dayOfWeek.get(Integer.valueOf(dayString)) == 1 || dayOfWeek.get(Integer.valueOf(dayString)) == 7) {
                    mPaint.setColor(mWeekedDayClor);
                    canvas.drawText(dayString, startX, startY, mPaint);
                }
                    if (getTime(dayInteger) == Integer.valueOf(DateUtils.getCurrentTimeInteger())) {
                    if (mHaveSelDay == dayInteger) {
                        mPaint.setColor(nowColor);
                        mPaint.setAlpha(38);
                        canvas.drawCircle(circlePointX, circlePointY, mCircleRadius, mPaint);
                        mPaint.setAlpha(255);
                        canvas.drawText(dayString, startX, startY, mPaint);
                    } else {
                        mPaint.setColor(nowColor);
                        canvas.drawCircle(circlePointX, circlePointY, mCircleRadius, mPaint);
                        mPaint.setColor(getResources().getColor(R.color.w0));
                        canvas.drawText(dayString, startX, startY, mPaint);
                    }
                } else if (mSelDay == dayInteger && getTime(dayInteger) != Integer.valueOf(DateUtils.getCurrentTimeInteger())) {
                    mPaint.setColor(futureColor);
                    canvas.drawCircle(circlePointX, circlePointY, mCircleRadius, mPaint);
                    mPaint.setColor(getResources().getColor(R.color.w0));
                    canvas.drawText(dayString, startX, startY, mPaint);
                }
            }

            if(currentMonth != null){
//			currentMonth.setText(mSelYear + "年" + (mSelMonth + 1) + "月");
                currentMonth.setText( mSelMonth + 1+"月");
            }

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
//        for(int day = 0;day <mMonthDays;day++){
//
//            if (getTime(day)<Integer.valueOf(DateUtils.getCurrentTimeInteger())){
//                return;
//            }
//            setData(day);
//            if (mSelYear>mCurrYear){
//                setDate();
//            }else if (mSelYear==mCurrYear){
//                if (mSelMonth>mCurrMonth){
//                    setDate();
//                }else if (mSelMonth==mCurrMonth){
//                    if (mSelDay>=mCurrDay){
//                        setDate();
//                    }
//                }
//            }
//
//        }
    }


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
                    if (mSelYear>mCurrYear) {
                        onLeftClick();
                    }else if (mSelYear==mCurrYear){
                        if (mSelMonth>mCurrMonth){
                            onLeftClick();
                        }
                    }

                }else if(downY-upY>50){
                    onRightClick();
                }
                break;
        }
        return true;
    }

    @Override
    protected void doClickAction(int x, int y) {
        super.doClickAction(x, y);
    }
    private void setDate(){
        if (date!=null) {
            if(mHaveSelDay<10) {
                date.setText(mHaveSelYear + "年" + (mHaveSelMonth + 1) + "月0" + mHaveSelDay + "日");
            }else {
                date.setText(mHaveSelYear + "年" + (mHaveSelMonth + 1) + "月" + mHaveSelDay + "日");
            }

        }
        if (weekDay!=null){
            //Log.d("jjb",mCurrDay+"");
            weekDay.setText("周"+DateUtils.getWeekCHN(DateUtils.getDayWeek(mHaveSelYear,mHaveSelMonth).get(mHaveSelDay)));
        }
    }

}