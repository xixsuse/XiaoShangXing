package com.xiaoshangxing.wo.setting;

import android.content.Context;

import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

/**
 *modified by FengChaoQun on 2016/12/24 18:44
 * description:删除部分无用方法
 */
public class DataSetting {
    /**
     * 新消息通知——是否开启接受新消息通知
     *
     * @return
     */
    public static boolean IsAcceptedNews(Context context) {
        return (boolean) SPUtils.get(context, SPUtils.NewNotice, true);
    }

    /**
     * 新消息通知——是否关闭通知新消息详情
     *
     * @return
     */
    public static boolean IsHideInformed(Context context) {
        return (boolean) SPUtils.get(context, SPUtils.HideNewsDetail, false);
    }

    /**
     * 新消息通知——是否开启声音
     *
     * @return
     */
    public static boolean IsSounded(Context context) {
        return (boolean) SPUtils.get(context, SPUtils.NewsSound, true);
    }

    /**
     * 新消息通知——是否开启振动
     *
     * @return
     */
    public static boolean IsVibrationed(Context context) {
        return (boolean) SPUtils.get(context, SPUtils.NewsVibrate, true);
    }

    /**
     * 新消息通知——是否开启校友圈更新
     *
     * @return
     */
    public static boolean IsRenewed(Context context) {
        return (boolean) SPUtils.get(context, SPUtils.NewsForXiaoyou, true);
    }

    /**
     * 隐私——是否开启允许查看十张图片
     *
     * @return
     */
    public static boolean IsAllowedTenPicture(Context context) {
        return (boolean) SPUtils.get(context, "privacy_allow", false);
    }

    /**
     * 通用——是否开启听筒模式
     *
     * @return
     */
    public static boolean isEarPhone(Context context) {
        return (boolean) SPUtils.get(context, SPUtils.EarPhone, true);
    }

    /**
     * description:获取个人设置
     */
    public static StatusBarNotificationConfig getStatusConfig() {
        return SPUtils.getConfig();
    }

    /**
     * description:存储个人设置
     */

    public static void setStatusConfig(StatusBarNotificationConfig config) {
        SPUtils.savePersonalSetting(config);
    }

    /**
     * 新消息通知——消息免打扰列表
     *
     * @return
     */
    public static Integer getNoDisturbList(Context context) {
        return (Integer) SPUtils.get(context, SPUtils.NoDisturb, 2);
    }

}
