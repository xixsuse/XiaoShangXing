package com.xiaoshangxing.yujian.IM.viewHodler;


import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.yujian.IM.CustomMessage.CustomAttachmentType;
import com.xiaoshangxing.yujian.IM.cache.TeamNotificationHelper;
import com.xiaoshangxing.yujian.IM.viewHodler.MessageVH.CustomNotificationText;

import java.util.ArrayList;
import java.util.List;

public class CommonRecentViewHolder extends RecentViewHolder {

    @Override
    protected String getContent() {
        return descOfMsg();
    }

    protected String descOfMsg() {
        if (recent.getMsgType() == MsgTypeEnum.text) {
            return parseTextMessage();
        } else if (recent.getMsgType() == MsgTypeEnum.tip) {
            String digest = null;
            if (getCallback() != null) {
                digest = getCallback().getDigestOfTipMsg(recent);
            }

            if (digest == null) {
                digest = getDefaultDigest(null);
            }

            return digest;
        } else if (recent.getAttachment() != null) {
            String digest = null;
            if (getCallback() != null) {
                digest = getCallback().getDigestOfAttachment(recent.getAttachment());
            }

            if (digest == null) {
                digest = getDefaultDigest(recent.getAttachment());
            }

            return digest;
        }
        return "";
    }

    // SDK本身只记录原始数据，第三方APP可根据自己实际需求，在最近联系人列表上显示缩略消息
    // 以下为一些常见消息类型的示例。
    private String getDefaultDigest(MsgAttachment attachment) {
        switch (recent.getMsgType()) {
            case text:
                return parseTextMessage();
            case image:
                return "[图片]";
            case video:
                return "[视频]";
            case audio:
                return "[语音消息]";
            case location:
                return "[位置]";
            case file:
                return "[文件]";
            case tip:
                return "[通知提醒]";
            case notification:
                return TeamNotificationHelper.getTeamNotificationText(recent.getContactId(),
                        recent.getFromAccount(),
                        (NotificationAttachment) recent.getAttachment());
            default:
                return CustomNotificationText.getRencentContactShowText(recent);
        }
    }

    private String parseTextMessage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(recent.getRecentMessageId());
        List<IMMessage> imMessages = new ArrayList<>();
        IMMessage imMessage;
        imMessages = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(arrayList);
        if (imMessages.size() > 0) {
            imMessage = imMessages.get(0);
        } else {
            return recent.getContent();
        }
        if (imMessage.getRemoteExtension() == null) {
            return recent.getContent();
        } else {
            Integer s = (Integer) imMessage.getRemoteExtension().get(NS.TYPE);
            if (s == null) {
                return recent.getContent();
            } else {
                switch (s) {
                    case CustomAttachmentType.Reward:
                        return "[校内悬赏私聊]" + recent.getContent();
                    case CustomAttachmentType.Help:
                        return "[校友互帮私聊]" + recent.getContent();
                    case CustomAttachmentType.Plan:
                        return "[计划发起私聊]" + recent.getContent();
                    case CustomAttachmentType.Sale:
                        return "[闲置出售私聊]" + recent.getContent();
                    default:
                        return recent.getContent();
                }
            }
        }
    }
}
