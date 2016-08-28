package com.xiaoshangxing.login_register.LoginRegisterActivity.AlterPasswordFragment;

import android.util.Log;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.LoginNetwork;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.utils.XSXApplication;

import org.json.JSONObject;

import okhttp3.ResponseBody;

/**
 * Created by FengChaoQun
 * on 2016/6/24
 */
public class AlterPasswordPresenter implements AlterPasswordContract.Presenter {
    private AlterPasswordContract.View mView;

    public AlterPasswordPresenter(AlterPasswordContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void isContentOK() {
        if (mView.getPassword1().length() >= 8 && mView.getPassword2().length() >= 8) {
            mView.setCompleteState(true);
        } else {
            mView.setCompleteState(false);
        }
    }

    @Override
    public void clickOnComplete() {
        if (mView.getPassword1().equals(mView.getPassword2())) {
            ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
                @Override
                public void onNext(ResponseBody responseBody) {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(responseBody.string());
                        switch (Integer.valueOf((String) jsonObject.get("code"))) {
                            case 9000:
                                Log.d("register", "success");
                                mView.showAlterSuccess();
                                break;
                            case 9003:
                                Log.d("register","error");
                                mView.showToast("该账号已存在，请前往注册。");
                            default:
                                Log.d("register", "erro");
                                String msg;
                                if (jsonObject.get("msg") instanceof JSONObject) {
                                    msg= (jsonObject.getJSONObject("msg")).getString("token");
                                } else {
                                    msg= jsonObject.getString("msg");
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
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("phone",mView.getPhoneNumber());
            jsonObject.addProperty("password",mView.getPassword1());
            jsonObject.addProperty("timestamp",System.currentTimeMillis());
            LoginNetwork.getInstance().Register(observer, jsonObject, XSXApplication.getInstance());
        } else {
            mView.showPasswordDiffer();
        }
    }
}
