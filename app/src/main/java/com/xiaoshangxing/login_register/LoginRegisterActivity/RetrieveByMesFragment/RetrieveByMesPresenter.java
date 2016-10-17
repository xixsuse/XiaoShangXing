package com.xiaoshangxing.login_register.LoginRegisterActivity.RetrieveByMesFragment;

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
public class RetrieveByMesPresenter implements RetrieveByMesContract.Presenter {
    private RetrieveByMesContract.View mView;

    public RetrieveByMesPresenter(RetrieveByMesContract.View mView) {
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
    public void clickOnNext() {
        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(e.string());
                    switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
                        case 200:
                            mView.showUnRegiter();
                            break;
                        case 9101:
                            mView.showSure();
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
        LoginNetwork.getInstance().checkExist(progressSubsciber, jsonObject);
    }

    @Override
    public void clickOnSure() {
        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(e.string());
                    switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
                        case 200:
                            mView.gotoInputCode();
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
        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());

        LoginNetwork.getInstance().sendCode(progressSubsciber, jsonObject);

//        if (true){
//            mView.gotoInputCode();
//        }else {
//            mView.showUnRegiter();
//        }
    }
}
