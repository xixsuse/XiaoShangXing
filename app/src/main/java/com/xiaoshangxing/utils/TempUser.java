package com.xiaoshangxing.utils;

import android.content.Context;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.InfoNetwork;
import com.xiaoshangxing.Network.NS;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by FengChaoQun
 * on 2016/9/8
 * 获取当前账号信息便捷入口
 */

public class TempUser {

    //    获取当前账号
    public static String getAccount(Context context) {
        String account = (String) SPUtils.get(context, SPUtils.CURRENT_COUNT, SPUtils.DEFAULT_STRING);
        return account.equals(SPUtils.DEFAULT_STRING) ? null : account;
    }

    //     获取当前账号id
    public static Integer getID(Context context) {
        Integer ID = (Integer) SPUtils.get(context, SPUtils.ID, SPUtils.DEFAULT_int);
        return ID == SPUtils.DEFAULT_int ? null : ID;
    }

    public static void reloadUserMessage(Context context) {
        final Realm realm = Realm.getDefaultInstance();
        Subscriber<ResponseBody> subscriber = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
                        case 9001:
                            final JSONObject user = jsonObject.getJSONObject(NS.MSG);
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.createOrUpdateObjectFromJson(User.class, user);
                                }
                            });
                            break;
                        default:
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.ID, getID(context));
        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());
        InfoNetwork.getInstance().GetUser(subscriber, jsonObject, context);
    }
}
