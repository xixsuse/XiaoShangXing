package com.xiaoshangxing.setting;

import android.content.Context;

import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

/**
 * Created by 15828 on 2016/7/16.
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
     * description:存储个人设置
     */

    public static void setStatusConfig(StatusBarNotificationConfig config) {
        SPUtils.savePersonalSetting(config);
    }

    /**
     * description:获取个人设置
     */
    public static StatusBarNotificationConfig getStatusConfig() {
        return SPUtils.getConfig();
    }


    /**
     * 新消息通知——消息免打扰列表
     *
     * @return
     */
    public static Integer getNoDisturbList(Context context) {
        return (Integer) SPUtils.get(context, SPUtils.NoDisturb, 2);
    }


    /**
     * 资料设置——是否设为星标好友
     *
     * @return
     */
    public static boolean IsStarMarkfriends(Context context) {
        return (boolean) SPUtils.get(context, "starMarkfriends", false);
    }

    /**
     * 资料设置——是否暗恋他
     *
     * @return
     */
    public static boolean IsCrush(Context context) {
        return (boolean) SPUtils.get(context, "crush", false);
    }

    /**
     * 资料设置——是否不让他看我的校友圈
     *
     * @return
     */
    public static boolean IsBuKanWo(Context context) {
        return (boolean) SPUtils.get(context, "bukanwo", false);
    }

    /**
     * 资料设置——是否不看他的校友圈
     *
     * @return
     */
    public static boolean IsBuKanTa(Context context) {
        return (boolean) SPUtils.get(context, "bukanta", false);
    }

    /**
     * 资料设置——是否加入黑名单
     *
     * @return
     */
    public static boolean IsAddToBlackList(Context context) {
        return (boolean) SPUtils.get(context, "addToBlackList", false);
    }


    /**
     * 个人资料——是否已留心
     *
     * @return
     */
    public static boolean IsFocused(Context context) {
        return (boolean) SPUtils.get(context, "focus", false);
    }




}
