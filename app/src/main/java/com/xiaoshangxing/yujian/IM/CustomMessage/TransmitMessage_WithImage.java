package com.xiaoshangxing.yujian.IM.CustomMessage;

import com.xiaoshangxing.Network.netUtil.NS;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by FengChaoQun
 * on 2016/9/16
 * 用于转发有图片的动态消息
 */
public class TransmitMessage_WithImage extends CustomAttachment {
    private int state_id;

    TransmitMessage_WithImage() {
        super(CustomAttachmentType.Transmit_noimage);
    }

    @Override
    protected void parseData(JSONObject data) {
        try {
            state_id = data.getInt(NS.ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(NS.ID, state_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
