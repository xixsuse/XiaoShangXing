package com.xiaoshangxing.login_register.LoginRegisterActivity.SetPasswordFragment;

import android.util.Log;

import com.xiaoshangxing.Network.Bean.Register;
import com.xiaoshangxing.Network.LoginNetwork;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;

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
                            Log.d("register", "success");
                            mView.showRegisterSuccess();
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
        Register register = new Register();
        register.setPhone(mView.getPhone());
        register.setPassword(mView.getPassword());
        register.setTimestamp("12");
        LoginNetwork.getInstance().Register(observer, register);

    }
}
