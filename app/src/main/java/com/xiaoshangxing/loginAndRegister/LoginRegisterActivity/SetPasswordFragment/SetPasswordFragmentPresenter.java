package com.xiaoshangxing.loginAndRegister.LoginRegisterActivity.SetPasswordFragment;

import android.util.Log;

import com.google.gson.JsonObject;
import com.xiaoshangxing.network.LoginNetwork;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.utils.XSXApplication;

import org.json.JSONObject;

import okhttp3.ResponseBody;

/**
 * Created by FengChaoQun
 * on 2016/6/24
 */
public class SetPasswordFragmentPresenter implements SetPasswordContract.Presenter {
    private SetPasswordContract.View mView;

    public SetPasswordFragmentPresenter(SetPasswordContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void isContentOK() {
        if (mView.getPassword().length() >= 8) {
            mView.setButtonState(true);
        } else {
            mView.setButtonState(false);
        }
    }

    @Override
    public void clickOnCompleteButton() {
        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(responseBody.string());
                    switch (Integer.valueOf((String) jsonObject.get("code"))) {
                        case 9000:
                            mView.showRegisterSuccess();
                            break;
                        case 9003:
                            mView.showToast("该账号已存在，请前往注册。");
                        default:
                            Log.d("register", "erro");
                            String msg;
                            if (jsonObject.get("msg") instanceof JSONObject) {
                                msg = (jsonObject.getJSONObject("msg")).getString("token");
                            } else {
                                msg = jsonObject.getString("msg");
                            }
                            mView.showToast(msg);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mView.showToast("数据解析异常");
                }
            }
        };
        ProgressSubsciber<ResponseBody> observer = new ProgressSubsciber<ResponseBody>(onNext, mView);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("phone", mView.getPhone());
        jsonObject.addProperty("password", mView.getPassword());
        jsonObject.addProperty("timestamp", System.currentTimeMillis());
        LoginNetwork.getInstance().Register(observer, jsonObject, XSXApplication.getInstance());

    }
}
