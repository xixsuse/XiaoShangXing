package com.xiaoshangxing.loginAndRegister.LoginRegisterActivity.RgInputVertifyCodeFragment;

import android.os.CountDownTimer;
import android.util.Log;

import com.google.gson.JsonObject;
import com.xiaoshangxing.network.LoginNetwork;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.network.netUtil.HmacSHA256Utils;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.utils.XSXApplication;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by FengChaoQun
 * on 2016/6/23
 */
public class RgInputVertifyCodePresenter implements RgInputVertifyCodeContract.Presenter {
    private RgInputVertifyCodeContract.View mView;
    private CountDownTimer countDownTimer;

    public RgInputVertifyCodePresenter(final RgInputVertifyCodeContract.View mView) {
        this.mView = mView;
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mView.setRemainTime("接受短信大约需要" + millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                mView.setRemainTimeChange();
            }
        };
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
                        case 9001:
                            String token = jsonObject.getJSONObject(NS.MSG).getString(NS.TOKEN);
                            String digest = HmacSHA256Utils.digest(token, mView.getPhone());
                            SPUtils.put(XSXApplication.getInstance(), SPUtils.DIGEST, digest);
                            SPUtils.put(XSXApplication.getInstance(), SPUtils.PHONENUMNBER, mView.getPhone());
                            Log.d("digest", digest);
                            mView.gotoWhere();
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
        jsonObject.addProperty("timeStamp", System.currentTimeMillis());
        LoginNetwork.getInstance().CheckCode(observer, jsonObject);
    }

    @Override
    public void getCode() {
        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(e.string());
                    switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
                        case 200:
                            mView.showToast("发送成功");
                            startCountTime();
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
        jsonObject.addProperty("phone", mView.getPhone());
        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());

        LoginNetwork.getInstance().sendCode(progressSubsciber, jsonObject);
    }

    @Override
    public void startCountTime() {
        countDownTimer.start();
    }

    @Override
    public void stopCountTime() {
        countDownTimer.cancel();
        mView.setRemainTimeEnable(false);
    }

    @Override
    public void noReceiveCode() {
        mView.showNoDialogMenu();
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
