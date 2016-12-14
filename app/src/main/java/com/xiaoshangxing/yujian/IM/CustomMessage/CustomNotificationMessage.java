package com.xiaoshangxing.yujian.IM.CustomMessage;

import android.util.Log;

import com.xiaoshangxing.network.netUtil.NS;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by FengChaoQun
 * on 2016/10/12
 * 自定义通知
 */
public class CustomNotificationMessage extends CustomAttachment {
    private int notificationType;

    public CustomNotificationMessage() {
        super(CustomAttachmentType.CustomNotification);
    }

    @Override
    protected void parseData(JSONObject data) {
        try {
            Log.d("parseData", data.toString());
            notificationType = data.getInt(NS.CODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(NS.CODE, notificationType);
            Log.d("packData", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }
}
