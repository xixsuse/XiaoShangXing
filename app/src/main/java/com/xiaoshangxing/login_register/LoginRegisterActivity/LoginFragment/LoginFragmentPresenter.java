package com.xiaoshangxing.login_register.LoginRegisterActivity.LoginFragment;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.HmacSHA256Utils;
import com.xiaoshangxing.Network.LoginNetwork;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

import org.json.JSONObject;

import io.realm.Realm;
import okhttp3.ResponseBody;

/**
 * Created by FengChaoQun
 * on 2016/6/22
 */
public class LoginFragmentPresenter implements LoginFragmentContract.Presenter {
    private LoginFragmentContract.View mView;
    private Context context;

    public LoginFragmentPresenter(LoginFragmentContract.View view, Context context) {
        this.mView = view;
        this.context = context;
    }

    @Override
    public void isContentOK() {
        if (mView.getPhoneNumber().length() == 11 && mView.getPassword().length() >= 6) {
            mView.setButtonEnable(true);
        } else {
            mView.setButtonEnable(false);
        }
    }

    @Override
    public void isHasHeadPotrait() {
        String count = (String) SPUtils.get(context, SPUtils.CURRENT_COUNT, SPUtils.DEFAULT_STRING);
        if (count.equals(SPUtils.DEFAULT_STRING)) {
            return;
        }
        if (mView.getPhoneNumber().equals(SPUtils.get(context, SPUtils.CURRENT_COUNT, SPUtils.DEFAULT_STRING))) {
            mView.showHeadPotrait(true);
        } else {
            mView.showHeadPotrait(false);
        }
    }

    @Override
    public void loginWithAccount(String number) {
        mView.setPhoneNumber(number);
        isHasHeadPotrait();
    }

    @Override
    public void clickOnLogin() {

        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(responseBody.string());
                    switch (Integer.valueOf((String) jsonObject.get("code"))) {
                        case 200:
                            Log.d("login", "success");
                            if (jsonObject.get("msg") instanceof JSONObject) {
                                String token=jsonObject.getJSONObject("msg").getString("token");
                                String digest= HmacSHA256Utils.digest(token,mView.getPhoneNumber());
//                                存储摘要 账号  头像
                                SPUtils.put(context,SPUtils.DIGEST,digest);
                                SPUtils.put(context,SPUtils.CURRENT_COUNT,mView.getPhoneNumber());
                                String headPath = jsonObject.getJSONObject("msg").getJSONObject("userDto").getString("photoCover");
                                if (!TextUtils.isEmpty(headPath) && !headPath.equals("null")) {
                                    SPUtils.put(context, SPUtils.CURRENT_COUNT_HEAD, headPath);
                                }

                                Log.d("digest",digest);
//                                存储账号信息
                                final JSONObject userDao = jsonObject.getJSONObject("msg").getJSONObject("userDto");
                                Realm realm = Realm.getDefaultInstance();
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        User user = realm.createOrUpdateObjectFromJson(User.class, userDao);
                                        Log.d("user", user.toString());
                                    }
                                });
                                realm.close();
                            }
                            mView.gotoMainActivity();
                            break;
                        case 9001:
                            Log.d("login", "用户名不存在");
                            mView.showFailDialog("用户名不存在");
                            break;
                        case 9002:
                            Log.d("login", "密码错误");
                            mView.showFailDialog("账号或密码错误，请重新填写。");
                            break;
                        case 9003:
                            mView.showFailDialog("失败次数过多，该账号暂时被锁定");
                            Log.d("login", "锁定");
                            break;
                        default:
                            if (jsonObject.get("msg") instanceof JSONObject) {
                                Log.d("login", (String) (jsonObject.getJSONObject("msg")).get("token"));
                            } else {
                                Log.d("login", jsonObject.getString("msg"));
                                mView.showFailDialog(jsonObject.getString("msg"));
                            }
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ProgressSubsciber<ResponseBody> observer = new ProgressSubsciber<ResponseBody>(onNext, mView);

        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("phone", mView.getPhoneNumber());
        jsonObject1.addProperty("password", mView.getPassword());
        jsonObject1.addProperty("timeStamp", System.currentTimeMillis());
        LoginNetwork.getInstance().Login(observer, jsonObject1);
    }

    @Override
    public void clickOnRetrievePassword() {
        mView.showRetrievePasswordMenu();
    }

    @Override
    public void clickOnRegister() {
        mView.gotoRegister();
    }
}
