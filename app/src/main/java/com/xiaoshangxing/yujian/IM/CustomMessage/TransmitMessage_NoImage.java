package com.xiaoshangxing.yujian.IM.CustomMessage;

import com.xiaoshangxing.Network.netUtil.NS;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by FengChaoQun
 * on 2016/9/16
 * 用于转发没有图片的动态消息
 */
public class TransmitMessage_NoImage extends CustomAttachment {
    private int state_id;

    public TransmitMessage_NoImage(int type) {
        super(type);
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

    public void setState_id(int state_id) {
        this.state_id = state_id;
    }

    public int getState_id() {
        return state_id;
    }
}
