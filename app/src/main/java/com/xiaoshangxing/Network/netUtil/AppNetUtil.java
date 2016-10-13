package com.xiaoshangxing.Network.netUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.xiaoshangxing.Network.AppNetwork;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
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

import okhttp3.ResponseBody;

/**
 * Created by FengChaoQun
 * on 2016/10/8
 */

public class AppNetUtil {

    public static AppNetUtil getInstance() {
        return new AppNetUtil();
    }

    /**
     * description:意见反馈
     *
     * @param text           内容
     * @param simpleCallBack 回调
     * @return
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
                    if (jsonObject1.getString(NS.CODE).equals("200")) {
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
     *
     * @param
     * @return
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
                Log.d("loginok", "fail");
            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(new Observer<StatusCode>() {
            @Override
            public void onEvent(StatusCode statusCode) {

                if (statusCode == StatusCode.LOGINED) {
                    Log.d("login_state", "ok");
                } else {
                    Log.d("login_state", "" + statusCode.name());
                }
                if (statusCode == StatusCode.KICKOUT) {
                    Log.d("kit", "" + NIMClient.getService(AuthService.class).getKickedClientType());
                }
            }
        }, true);
    }

    /**
     * description:用户被踢下线
     *
     * @param
     * @return
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
     *
     * @param
     * @return
     */

    public static void LogOut(Context context) {
        ClearData(context);
        XSXApplication.getInstance().exit();
    }

    /**
     * description:重新登录
     *
     * @param
     * @return
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
    }
}
