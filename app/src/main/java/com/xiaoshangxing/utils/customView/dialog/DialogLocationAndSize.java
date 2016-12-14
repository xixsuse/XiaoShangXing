package com.xiaoshangxing.utils.customView.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Display;
import android.view.WindowManager;

import com.xiaoshangxing.utils.normalUtils.ScreenUtils;

/**
 * Created by FengChaoQun
 * on 2016/6/27
 * 设置弹位置及大小
 */
public class DialogLocationAndSize {

    /**
     * 设置弹窗为底部弹出
     */
    public static void bottom_FillWidth(Activity activity, Dialog dialog) {
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }

    /**
     * description:设置弹窗的宽度 (自适应)
     *
     * @param width 弹窗宽度 对应xml文件里的数值 R.dimen.x900 而非准确数值
     */

    public static void setWidth(Dialog dialog, int width) {
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = ScreenUtils.getAdapterPx(width, dialog.getContext()); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }
}
