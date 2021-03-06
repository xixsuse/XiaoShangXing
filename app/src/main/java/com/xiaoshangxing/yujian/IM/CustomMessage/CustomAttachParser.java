package com.xiaoshangxing.yujian.IM.CustomMessage;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhoujianghua on 2015/4/9.
 */
public class CustomAttachParser implements MsgAttachmentParser {

    private static final String KEY_TYPE = "type";
    private static final String KEY_DATA = "data";

    public static String packData(int type, JSONObject data) {
        JSONObject object = new JSONObject();
        try {
            object.put(KEY_TYPE, type);
            if (data != null) {
                object.put(KEY_DATA, data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    @Override
    public MsgAttachment parse(String json) {
        CustomAttachment attachment = null;
        try {
            JSONObject object = new JSONObject(json);
            int type = object.getInt(KEY_TYPE);
            JSONObject data = object.getJSONObject(KEY_DATA);
            switch (type) {
                case CustomAttachmentType.Reward:
                    attachment = new TransmitMessage_NoImage(CustomAttachmentType.Reward);
                    break;
                case CustomAttachmentType.Help:
                    attachment = new TransmitMessage_NoImage(CustomAttachmentType.Help);
                    break;
                case CustomAttachmentType.Plan:
                    attachment = new TransmitMessage_NoImage(CustomAttachmentType.Plan);
                    break;
                case CustomAttachmentType.Sale:
                    attachment = new TransmitMessage_WithImage(CustomAttachmentType.Sale);
                    break;
                case CustomAttachmentType.ApplyPlan:
                    attachment = new ApplyPlanMessage();
                    break;
                case CustomAttachmentType.CustomNotification:
                    attachment = new CustomNotificationMessage();
                    break;
                default:
                    attachment = new DefaultCustomAttachment();
                    break;
            }

            if (attachment != null) {
                attachment.fromJson(data);
            }
        } catch (Exception e) {

        }

        return attachment;
    }
}
