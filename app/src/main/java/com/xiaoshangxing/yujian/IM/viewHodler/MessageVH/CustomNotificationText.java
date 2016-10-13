package com.xiaoshangxing.yujian.IM.viewHodler.MessageVH;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.xiaoshangxing.yujian.IM.CustomMessage.ApplyPlanMessage;
import com.xiaoshangxing.yujian.IM.CustomMessage.CustomNotificationMessage;
import com.xiaoshangxing.yujian.IM.CustomMessage.TransmitMessage_NoImage;
import com.xiaoshangxing.yujian.IM.CustomMessage.TransmitMessage_WithImage;
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

    private static String getApllyPlanText(IMMessage message, boolean pass) {
        StringBuilder sb = new StringBuilder();
        if (message.getDirect() == MsgDirectionEnum.Out) {
            sb.append("你");
            if (pass) {
                sb.append("同意了");
            } else {
                sb.append("拒绝了");
            }
            sb.append(NimUserInfoCache.getInstance().getUserDisplayNameYou(message.getSessionId()));
            sb.append("的加入计划请求");
        } else {
            sb.append(NimUserInfoCache.getInstance().getUserDisplayNameYou(message.getFromAccount()));
            if (pass) {
                sb.append("同意了");
            } else {
                sb.append("拒绝了");
            }

            sb.append("你的加入计划请求");
        }

        return sb.toString();
    }

    public static String getRencentContactShowText(RecentContact recent) {

        MsgAttachment attachment = recent.getAttachment();
        if (attachment instanceof CustomNotificationMessage) {
            return getApllyPlanContactText(recent);
        } else if (attachment instanceof ApplyPlanMessage) {
            return "[申请加入计划]";
        } else if (attachment instanceof TransmitMessage_NoImage) {
            return "[分享]";
        } else if (attachment instanceof TransmitMessage_WithImage) {
            return "[分享]";
        }

        return "未知消息";
    }

    private static String getApllyPlanContactText(RecentContact recent) {
        CustomNotificationMessage customNotificationMessage = (CustomNotificationMessage) recent.getAttachment();
        switch (customNotificationMessage.getNotificationType()) {
            case ApllyPlan_PASS:
                return getApllyPlanContactText(recent.getFromAccount(), true);
            case ApllyPlan_REFUSE:
                return getApllyPlanContactText(recent.getFromAccount(), false);
            default:
                return "未知通知";
        }
    }

    private static String getApllyPlanContactText(String fromAccount, boolean pass) {
        StringBuilder sb = new StringBuilder();
        String selfName = NimUserInfoCache.getInstance().getUserDisplayNameYou(fromAccount);
        sb.append(selfName);
        if (pass) {
            sb.append("同意了");
        } else {
            sb.append("拒绝了");
        }
        sb.append("你的加入计划请求");
        return sb.toString();
    }


}
