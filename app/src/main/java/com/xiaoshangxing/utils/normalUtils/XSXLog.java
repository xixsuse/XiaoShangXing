package com.xiaoshangxing.utils.normalUtils;

import android.util.Log;

/**
 * Created by FengChaoQun
 * on 2016/6/11
 */
public class XSXLog {

    public static void d(Object o, String describe) {
        Log.d(o.toString(), describe);
    }

    public static void d(int number, String s) {
        Log.d("" + number, s);
    }


}
