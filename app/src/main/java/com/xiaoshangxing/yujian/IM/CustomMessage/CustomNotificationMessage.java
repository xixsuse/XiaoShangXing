package com.xiaoshangxing.yujian.IM.CustomMessage;

import com.xiaoshangxing.Network.netUtil.NS;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by FengChaoQun
 * on 2016/9/16
 * 用于转发没有图片的动态消息
 */
public class CustomNotificationMessage extends CustomAttachment {
    private String text;
    private int notificationType;

    public CustomNotificationMessage() {
        super(CustomAttachmentType.CustomNotification);
    }

    @Override
    protected void parseData(JSONObject data) {
        try {
            notificationType = data.getInt(NS.CODE);
            text = data.getString(NS.TEXT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(NS.CODE, notificationType);
            jsonObject.put(NS.TEXT, text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }
}
