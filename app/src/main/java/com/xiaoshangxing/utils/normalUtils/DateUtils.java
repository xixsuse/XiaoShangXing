package com.xiaoshangxing.utils.normalUtils;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateUtils {
//	private
	private  static final String[] weekString = new String[]{"日","一","二","三","四","五","六"};

	/**
     * 通过年份和月份 得到当月的日子
     * 
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDays(int year, int month) {
		month++;
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12: 
		    return 31;
		case 4:
		case 6:
		case 9:
		case 11: 
		    return 30;
		case 2:
			if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)){
				return 29;
			}else{
				return 28;
			}
		default:
			return  -1;
		}
    }


	public static String getWeekCHN(int k){
		Map<Integer,String> weekDayMap = new HashMap<>();

			for (int i = 0; i < 7; i++) {
				weekDayMap.put(i+1, weekString[i]);
			}
		

			return weekDayMap.get(k);
	}
    /**
     * 返回当前月份1号位于周几
     * @param year
     * 		年份
     * @param month
     * 		月份，传入系统获取的，不需要正常的
     * @return
     * 	日：1		一：2		二：3		三：4		四：5		五：6		六：7
     */
    public static int getFirstDayWeek(int year, int month){
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(year, month, 1);
    	Log.d("DateView", "DateView:First:" + calendar.getFirstDayOfWeek());
    	return calendar.get(Calendar.DAY_OF_WEEK);
    }
	public static Map<Integer,Integer> getDayWeek(int year, int month){
		List<Integer> dayOfWeek = new ArrayList<>();
		Map <Integer,Integer> dayOfWeekMap = new HashMap<>();
		Calendar calendar = Calendar.getInstance();
		for (int i = 1;i<getMonthDays(year,month)+1;i++){
			calendar.set(year,month,i);
			dayOfWeekMap.put(i,calendar.get(Calendar.DAY_OF_WEEK));
			//Log.d("今天日子",dayOfWeekMap.get(i)+"");
		}
		//calendar.set(year, month, 1);
//		Log.d("DateView", "DateView:First:" + calendar.getFirstDayOfWeek());
//		for (Integer i :dayOfWeek){
//			Log.d("今天日子",i+"");
//		}
		return dayOfWeekMap;
	}
	public static String getCurrentTimeCHN(){
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy年MM月dd日");

		String time=format.format(date);
		if (time.contains("年0")){
			format = new SimpleDateFormat("yyyy年M月dd日");
			time = format.format(date);
		}

		return time;
	}
	public static String getCurrentTimeInteger(){
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyyMMdd");

		String time=format.format(date);
//		if (time.contains("年0")){
//			format = new SimpleDateFormat("yyyy年M月dd日");
//			time = format.format(date);
//		}

		return time;
	}
	public static String  getDay() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return getWeekCHN(cal.get(Calendar.DAY_OF_WEEK));
	}


}
