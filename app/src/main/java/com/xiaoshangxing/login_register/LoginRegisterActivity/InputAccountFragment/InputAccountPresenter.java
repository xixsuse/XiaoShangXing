package com.xiaoshangxing.login_register.LoginRegisterActivity.InputAccountFragment;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.LoginNetwork;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.Network.netUtil.NS;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by FengChaoQun
 * on 2016/6/24
 */
public class InputAccountPresenter implements InputAccountContract.Presenter {
    private InputAccountContract.View mView;

    public InputAccountPresenter(InputAccountContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void isContentOK() {
        if (mView.getPhoneNumber().length() == 11) {
            mView.setButtonState(true);
        } else {
            mView.setButtonState(false);
        }
    }

    @Override
    public void clickOnButton() {
        mView.savePhoneNumber();

        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(e.string());
                    switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
                        case 200:
                            mView.gotoVerticyEmailCode();
                            break;
                        case 401:
                            mView.gotoNoEmail();
                            break;
                        default:
                            mView.showToast(jsonObject.get(NS.MSG).toString());
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };

        ProgressSubsciber<ResponseBody> progressSubsciber = new ProgressSubsciber<>(onNext, mView);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("phone", mView.getPhoneNumber());
        LoginNetwork.getInstance().FindPWByEmail(progressSubsciber, jsonObject);
    }
}
