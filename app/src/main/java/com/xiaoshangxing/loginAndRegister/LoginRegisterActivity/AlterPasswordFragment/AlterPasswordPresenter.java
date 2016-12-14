package com.xiaoshangxing.loginAndRegister.LoginRegisterActivity.AlterPasswordFragment;

import com.google.gson.JsonObject;
import com.xiaoshangxing.network.LoginNetwork;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.network.netUtil.NS;

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
                        switch (Integer.valueOf((String) jsonObject.get(NS.CODE))) {
                            case 200:
                                mView.showAlterSuccess();
                                break;
                            default:
                                String msg;
                                if (jsonObject.get(NS.MSG) instanceof JSONObject) {
                                    msg = (jsonObject.getJSONObject(NS.MSG)).getString(NS.TOKEN);
                                } else {
                                    msg = jsonObject.getString(NS.MSG);
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
            jsonObject.addProperty("phone", mView.getPhoneNumber());
            jsonObject.addProperty("newPassword", mView.getPassword1());
            LoginNetwork.getInstance().ForgetPassword(observer, jsonObject);
        } else {
            mView.showPasswordDiffer();
        }
    }
}
