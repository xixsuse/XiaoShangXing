package com.xiaoshangxing.loginAndRegister.LoginRegisterActivity.CheckEmailCode;

import com.google.gson.JsonObject;
import com.xiaoshangxing.network.LoginNetwork;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.network.netUtil.NS;

import org.json.JSONObject;

import okhttp3.ResponseBody;

/**
 * Created by FengChaoQun
 * on 2016/6/23
 */
public class CheckEmailCodePresenter implements CheckEmailCodeContract.Presenter {
    private CheckEmailCodeContract.View mView;

    public CheckEmailCodePresenter(final CheckEmailCodeContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void clickOnSubmit() {
        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(responseBody.string());
                    switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
                        case 200:
                            mView.gotoResetPassword();
                            break;
                        default:
                            mView.showFailDialog();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ProgressSubsciber<ResponseBody> observer = new ProgressSubsciber<ResponseBody>(onNext, mView);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("phone", mView.getPhone());
        jsonObject.addProperty("code", mView.getVertifyCode());
//        jsonObject.addProperty("timeStamp", System.currentTimeMillis());
        LoginNetwork.getInstance().CheckEmailCode(observer, jsonObject);
    }

    @Override
    public void getCode() {
//        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
//            @Override
//            public void onNext(ResponseBody e) throws JSONException {
//                try {
//                    JSONObject jsonObject = new JSONObject(e.string());
//                    switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
//                        case 200:
//                            mView.showToast("发送成功");
//                            startCountTime();
//                            break;
//                        default:
//                            mView.showToast(jsonObject.get(NS.MSG).toString());
//                    }
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        };
//
//        ProgressSubsciber<ResponseBody> progressSubsciber = new ProgressSubsciber<>(onNext, mView);
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("phone", mView.getPhone());
//        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());
//
//        LoginNetwork.getInstance().sendCode(progressSubsciber, jsonObject);
    }


    @Override
    public void isContentOK() {
        if (mView.getVertifyCode().length() >= 4) {
            mView.setButtonEnable(true);
        } else {
            mView.setButtonEnable(false);
        }
    }
}
