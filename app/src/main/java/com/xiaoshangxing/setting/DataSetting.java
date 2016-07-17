package com.xiaoshangxing.setting;

import android.content.Context;

import com.xiaoshangxing.utils.normalUtils.SPUtils;

/**
 * Created by 15828 on 2016/7/16.
 */
public class DataSetting {
    /**
     * 是否开启接受新消息通知
     *
     * @return
     */
    public static boolean IsAccepted(Context context) {
        return (boolean) SPUtils.get(context, "newnotice_accept", false);
    }

    /**
     * 是否开启通知新消息详情
     *
     * @return
     */
    public static boolean IsInformed(Context context) {
        return (boolean) SPUtils.get(context, "newnotice_inform", false);
    }

    /**
     * 是否开启声音
     *
     * @return
     */
    public static boolean IsSounded(Context context) {
        return (boolean) SPUtils.get(context, "newnotice_sound", false);
    }

    /**
     * 是否开启振动
     *
     * @return
     */
    public static boolean IsVibrationed(Context context) {
        return (boolean) SPUtils.get(context, "newnotice_vibration", false);
    }

    /**
     * 是否开启校友圈更新
     *
     * @return
     */
    public static boolean IsRenewed(Context context) {
        return (boolean) SPUtils.get(context, "newnotice_renew", false);
    }

    /**
     * 是否开启允许查看十张图片
     *
     * @return
     */
    public static boolean IsAllowedTenPicture(Context context) {
        return (boolean) SPUtils.get(context, "privacy_allow", false);
    }

    /**
     * 是否开启听筒模式
     *
     * @return
     */
    public static boolean IsReceiveMode(Context context) {
        return (boolean) SPUtils.get(context, "currency_receivemode", false);
    }


    /**
     * 消息免打扰列表
     *
     * @return
     */
    public static Integer NoDisturbList(Context context) {
        return (Integer) SPUtils.get(context, "nodisturbList", 0);
    }


}
