package com.xiaoshangxing.yujian.IM.kit;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.xiaoshangxing.yujian.IM.CustomMessage.ApplyPlanMessage;
import com.xiaoshangxing.yujian.IM.CustomMessage.CustomAttachment;
import com.xiaoshangxing.yujian.IM.CustomMessage.CustomAttachmentType;
import com.xiaoshangxing.yujian.IM.CustomMessage.CustomNotificationMessage;
import com.xiaoshangxing.yujian.IM.CustomMessage.TransmitMessage_NoImage;
import com.xiaoshangxing.yujian.IM.CustomMessage.TransmitMessage_WithImage;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;

import static com.xiaoshangxing.yujian.IM.viewHodler.MessageVH.CustomNotificationText.ApllyPlan_PASS;
import static com.xiaoshangxing.yujian.IM.viewHodler.MessageVH.CustomNotificationText.ApllyPlan_REFUSE;

/**
 * Created by FengChaoQun
 * on 2016/11/7
 */

public class NotifyContent {

    public static String makeNotifyContent(String nick, IMMessage message) {
        MsgAttachment msgAttachment = message.getAttachment();
        if (msgAttachment instanceof CustomNotificationMessage) {
            return getApllyPlanNotifyText(message);
        } else if (msgAttachment instanceof ApplyPlanMessage) {
            return nick + "申请加入计划";
        } else if (msgAttachment instanceof TransmitMessage_NoImage) {
            return getShareContactText(message);
        } else if (msgAttachment instanceof TransmitMessage_WithImage) {
            return getShareContactText(message);
        } else {
            return null;
        }
    }

    private static String getApllyPlanNotifyText(IMMessage imMessage) {
        CustomNotificationMessage customNotificationMessage = (CustomNotificationMessage) imMessage.getAttachment();
        switch (customNotificationMessage.getNotificationType()) {
            case ApllyPlan_PASS:
                return getApllyPlanNotifyText(imMessage.getFromAccount(), true);
            case ApllyPlan_REFUSE:
                return getApllyPlanNotifyText(imMessage.getFromAccount(), false);
            default:
                return "未知通知";
        }
    }

    private static String getApllyPlanNotifyText(String fromAccount, boolean pass) {
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

    private static String getShareContactText(IMMessage imMessage) {
        CustomAttachment customAttachment = (CustomAttachment) imMessage.getAttachment();
        switch (customAttachment.getType()) {
            case CustomAttachmentType.Help:
                return "[校友互帮]";
            case CustomAttachmentType.Reward:
                return "[校内悬赏]";
            case CustomAttachmentType.Plan:
                return "[计划发起]";
            case CustomAttachmentType.Sale:
                return "[闲置出售]";
            default:
                return "[未知]";
        }
    }
}
