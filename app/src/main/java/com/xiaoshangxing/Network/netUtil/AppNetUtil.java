package com.xiaoshangxing.Network.netUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.xiaoshangxing.Network.AppNetwork;
import com.xiaoshangxing.Network.LoginNetwork;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.login_register.StartActivity.FlashActivity;
import com.xiaoshangxing.setting.DataSetting;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.XSXApplication;
import com.xiaoshangxing.utils.normalUtils.SPUtils;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.yujian.IM.NimUIKit;
import com.xiaoshangxing.yujian.IM.cache.DataCacheManager;
import com.xiaoshangxing.yujian.IM.kit.LoginSyncDataStatusObserver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.ResponseBody;

/**
 * Created by FengChaoQun
 * on 2016/10/8
 *APP在全局中可能需要用到的网络操作，包括：登录IM 退出 重新登录 用户被踢下线 反馈
 */

public class AppNetUtil {
    private static AppNetUtil appNetUtil;

    public static AppNetUtil getInstance() {
        if (appNetUtil == null) {
            appNetUtil = new AppNetUtil();
        }
        return new AppNetUtil();
    }

    private AppNetUtil() {

    }

    /**
     * description:意见反馈
     *
     * @param text           内容
     * @param simpleCallBack 回调
     */

    public static void Suggest(String text, final IBaseView iBaseView, final SimpleCallBack simpleCallBack) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.USER_ID, TempUser.id);
        jsonObject.addProperty("suggestion", text);

        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) {
                try {
                    JSONObject jsonObject1 = new JSONObject(e.string());
                    if (jsonObject1.getString(NS.CODE).equals(NS.REQUEST_SUCCESS)) {
                        simpleCallBack.onSuccess();
                    } else {
                        iBaseView.showToast(jsonObject1.getString(NS.MSG));
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };

        ProgressSubsciber<ResponseBody> progressSubsciber = new ProgressSubsciber<>(onNext, iBaseView);

        AppNetwork.getInstance().Suggest(progressSubsciber, jsonObject, XSXApplication.getInstance());

    }

    /**
     * description:登录IM
     */
    public static void LoginIm(final Context context) {
        AbortableFuture<LoginInfo> loginRequest;
        final String account = String.valueOf(SPUtils.get(context, SPUtils.ID, -1));
        String token = (String) SPUtils.get(context, SPUtils.TOKEN, SPUtils.DEFAULT_STRING);
        if (SPUtils.DEFAULT_STRING.equals(token) || account.equals("-1")) {
            Toast.makeText(context, "账号有误，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("login:", "user:" + account + "--" + "token:" + token);
        loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account, token));
//            打开本地数据库
        loginRequest.setCallback(new RequestCallback() {
            @Override
            public void onSuccess(Object o) {
                NimUIKit.setAccount(account);
                // 初始化消息提醒
                NIMClient.toggleNotification(DataSetting.IsAcceptedNews(context));
                // 初始化免打扰
                if (DataSetting.getStatusConfig() == null) {
                    DataSetting.setStatusConfig(XSXApplication.getInstance().notificationConfig);
                }
                NIMClient.updateStatusBarNotificationConfig(DataSetting.getStatusConfig());
                // 构建缓存
                DataCacheManager.buildDataCacheAsync();
            }

            @Override
            public void onFailed(int i) {
                Log.d("loginIM_failed", "fail");
            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    /**
     * description:登录后台服务器
     *
     * @param phone        手机号
     * @param password     密码
     * @param loginXSXback 回调
     * @return
     */

    public static void LoginXSX(final String phone, String password, final Context context, IBaseView iBaseView, final LoginXSXback loginXSXback) {

        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(responseBody.string());
                    switch (jsonObject.getInt(NS.CODE)) {
                        case 200:
                            String token = jsonObject.getJSONObject(NS.MSG).getString(NS.TOKEN);
                            String digest = HmacSHA256Utils.digest(token, phone);

                            //  存储 token 摘要 账号 id 是否实名认证 头像

                            SPUtils.put(context, SPUtils.TOKEN, token);
                            SPUtils.put(context, SPUtils.DIGEST, digest);
                            SPUtils.put(context, SPUtils.PHONENUMNBER, phone);

                            String headPath = jsonObject.getJSONObject(NS.MSG).getJSONObject("userDto").getString("userImage");
                            if (!TextUtils.isEmpty(headPath) && !headPath.equals("null")) {
                                SPUtils.put(context, SPUtils.CURRENT_COUNT_HEAD, headPath);
                            }
                            int id = jsonObject.getJSONObject(NS.MSG).getJSONObject("userDto").getInt(NS.ID);
                            SPUtils.put(context, SPUtils.ID, id);
                            Log.d("digest", digest);
                            //   存储账号信息
                            final JSONObject userDao = jsonObject.getJSONObject(NS.MSG).getJSONObject("userDto");
                            Realm realm = Realm.getDefaultInstance();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    User user = realm.createOrUpdateObjectFromJson(User.class, userDao);
                                    SPUtils.put(context, SPUtils.IS_REAL_NAME, user.isRealName());
                                    Log.d("user", user.toString());
                                }
                            });
                            realm.close();

                            //登录IM
                            AppNetUtil.LoginIm(context);
                            loginXSXback.onSuccess();
                            break;
                        default:
                            loginXSXback.onError(jsonObject.getString(NS.MSG));
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ProgressSubsciber<ResponseBody> observer = new ProgressSubsciber<ResponseBody>(onNext, iBaseView);

        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("phone", phone);
        jsonObject1.addProperty("password", password);
        jsonObject1.addProperty(NS.TIMESTAMP, System.currentTimeMillis());
        LoginNetwork.getInstance().Login(observer, jsonObject1);
    }

    public interface LoginXSXback {
        void onSuccess();

        void onError(String msg);

    }

    /**
     * description:用户被踢下线
     */

    public static void KitOut(final Context context) {
        final DialogUtils.Dialog_Center dialog_center = new DialogUtils.Dialog_Center(context);
        Dialog dialog = dialog_center.Message("你的账号在别处登录,\n是否重新登录?").
                Button("重新登录", "直接退出")
                .MbuttonOnClick(new DialogUtils.Dialog_Center.buttonOnClick() {
                    @Override
                    public void onButton1() {
                        dialog_center.close();
                        ReLogin(context);
                    }

                    @Override
                    public void onButton2() {
                        dialog_center.close();
                        LogOut(context);

                    }
                }).create();
        dialog.show();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (ScreenUtils.getAdapterPx(R.dimen.x780, context)); //设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(false);
    }

    /**
     * description:退出APP
     */

    public static void LogOut(Context context) {
        ClearData(context);
        XSXApplication.getInstance().exit();
    }

    /**
     * description:重新登录
     */

    public static void ReLogin(Context context) {
        ClearData(context);
        Intent intent = new Intent(context, FlashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    //清除有关缓存信息
    public static void ClearData(Context context) {
        // 清理缓存&注销监听&清除状态
        NimUIKit.clearCache();
        LoginSyncDataStatusObserver.getInstance().reset();
        SPUtils.put(context, SPUtils.IS_QUIT, true);
        SPUtils.remove(context, SPUtils.ID);
        NIMClient.getService(AuthService.class).logout();
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
    }
}
