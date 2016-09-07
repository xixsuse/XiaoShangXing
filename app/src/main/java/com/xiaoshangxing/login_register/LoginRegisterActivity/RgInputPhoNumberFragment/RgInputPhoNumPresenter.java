package com.xiaoshangxing.login_register.LoginRegisterActivity.RgInputPhoNumberFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.LoginNetwork;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by FengChaoQun
 * on 2016/6/23
 */
public class RgInputPhoNumPresenter implements RgInputPhoNumContract.Presenter {
    private RgInputPhoNumContract.View mView;
    private Context context;
    private Activity activity;
    private FragmentManager mFragmentManager;

    public RgInputPhoNumPresenter(RgInputPhoNumContract.View mView, Context context,
                                  Activity activity, FragmentManager mFragmentManager) {
        this.mView = mView;
        this.context = context;
        this.activity = activity;
        this.mFragmentManager = mFragmentManager;
    }

    @Override
    public void isContentOK() {
        if (mView.getPhoneNum().length() == 11) {
            mView.setButtonEnable(true);
        } else {
            mView.setButtonEnable(false);
        }
    }

    @Override
    public void clickOnRegister() {
        ProgressSubscriberOnNext<ResponseBody> onNext=new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                try {
                    JSONObject jsonObject=new JSONObject(e.string());
                    switch (Integer.valueOf(jsonObject.getString("code"))){
                        case 200:
                            mView.showSureDialog();
                            break;
                        case 9101:
                            mView.showRegisteredDialog();
                        default:
                            mView.showToast(jsonObject.get("msg").toString());
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };

        ProgressSubsciber<ResponseBody> progressSubsciber=new ProgressSubsciber<>(onNext,mView);
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("phone",mView.getPhoneNum());
        LoginNetwork.getInstance().checkExist(progressSubsciber,jsonObject);
    }

    @Override
    public void sureSendVertifyCode() {

        ProgressSubscriberOnNext<ResponseBody> onNext=new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                try {
                    JSONObject jsonObject=new JSONObject(e.string());
                    switch (Integer.valueOf(jsonObject.getString("code"))){
                        case 200:
                            mView.gotoInputVertifyCode();
                            break;
                        case 9003:
                            mView.showRegisteredDialog();
                        default:
                            mView.showToast(jsonObject.get("msg").toString());
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };

        ProgressSubsciber<ResponseBody> progressSubsciber=new ProgressSubsciber<>(onNext,mView);
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("phone",mView.getPhoneNum());
        jsonObject.addProperty("timeStamp",System.currentTimeMillis());

        LoginNetwork.getInstance().sendCode(progressSubsciber,jsonObject);

    }

    @Override
    public void clickOnCancer() {
        activity.finish();
    }
}
