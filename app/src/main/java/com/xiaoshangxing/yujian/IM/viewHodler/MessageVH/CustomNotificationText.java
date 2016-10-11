package com.xiaoshangxing.yujian.IM.viewHodler.MessageVH;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.xiaoshangxing.yujian.IM.CustomMessage.CustomNotificationMessage;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;

/**
 * Created by FengChaoQun
 * on 2016/10/11
 */

public class CustomNotificationText {
    public static final int ApllyPlan_PASS = 1;     //同意加入计划
    public static final int ApllyPlan_REFUSE = 2;     //拒绝加入计划

    public static String getNotificationText(IMMessage message) {
        CustomNotificationMessage customNotificationMessage = (CustomNotificationMessage) message.getAttachment();
        switch (customNotificationMessage.getNotificationType()) {
            case ApllyPlan_PASS:
                return getApllyPlanText(message, true);
            case ApllyPlan_REFUSE:
                return getApllyPlanText(message, false);
            default:
                return "未知通知";
        }
    }

    public static String getApllyPlanText(IMMessage message, boolean pass) {
        StringBuilder sb = new StringBuilder();
        String selfName = NimUserInfoCache.getInstance().getUserDisplayNameYou(message.getFromAccount());
        sb.append(selfName);
        if (pass) {
            sb.append("同意了");
        } else {
            sb.append("拒绝了");
        }
        sb.append(NimUserInfoCache.getInstance().getUserDisplayNameYou(message.getSessionId()));
        sb.append("的加入计划请求");
        return sb.toString();
    }


}
