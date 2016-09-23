package com.xiaoshangxing.login_register.StartActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.xiaoshangxing.MainActivity;
import com.xiaoshangxing.R;
import com.xiaoshangxing.login_register.LoginRegisterActivity.LoginFragment.LoginFragment;
import com.xiaoshangxing.login_register.LoginRegisterActivity.LoginRegisterActivity;
import com.xiaoshangxing.setting.DataSetting;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.XSXApplication;
import com.xiaoshangxing.utils.normalUtils.SPUtils;
import com.xiaoshangxing.yujian.IM.NimUIKit;
import com.xiaoshangxing.yujian.IM.cache.DataCacheManager;

import java.util.ArrayList;

/**
 * Created by FengChaoQun
 * on 2016/8/10
 */
public class FlashActivity extends BaseActivity {
    private Handler handler;
    private AbortableFuture<LoginInfo> loginRequest;
    public static final String MESSAGE="MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if (getIntent().hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)){
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    GotoSeeMessage();
                }
            }, 1000);
        }else {
            normalStart();
        }

    }

    private void normalStart(){
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirstCome()) {
                    Intent intent = new Intent(FlashActivity.this, StartActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (isQuit()) {
                        intentLoginRegisterActivity();
                    } else {
                        intentMainActivity();
                    }
                }
            }
        }, 2000);

        Login();
    }

    private void GotoSeeMessage(){
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
                ArrayList<IMMessage> messages = (ArrayList<IMMessage>) intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
                if (messages == null || messages.size() > 1) {
                    showMainActivity(new Intent().putExtra(MESSAGE, 1));
                } else {
                    showMainActivity(new Intent().putExtra(NimIntent.EXTRA_NOTIFY_CONTENT, messages.get(0)).putExtra(MESSAGE, 1));
                }
            }
        }else {
            normalStart();
        }
    }

    private void showMainActivity() {
        showMainActivity(null);
    }

    private void showMainActivity(Intent intent) {
        MainActivity.start(FlashActivity.this, intent);
        finish();
    }

    //  登录IM  并存储和初始化相关信息
    private void Login() {
        final String account = "123456";
        loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account, "123456"));
//            打开本地数据库
            loginRequest.setCallback(new RequestCallback() {
                @Override
                public void onSuccess(Object o) {
                    NimUIKit.setAccount(account);
                    // 初始化消息提醒
                    NIMClient.toggleNotification(DataSetting.IsAcceptedNews(FlashActivity.this));
                    // 初始化免打扰
                    if (DataSetting.getStatusConfig() == null) {
                        DataSetting.setStatusConfig(XSXApplication.getInstance().notificationConfig);
                    }
                    NIMClient.updateStatusBarNotificationConfig(DataSetting.getStatusConfig());
                    // 构建缓存
                    DataCacheManager.buildDataCacheAsync();

                    Toast.makeText(FlashActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
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
                        Log.d("loginok", "ok");
                    } else {
                        Log.d("loginok2", "" + statusCode.name());
                    }
                    if (statusCode == StatusCode.KICKOUT) {
                        Log.d("kit", "" + NIMClient.getService(AuthService.class).getKickedClientType());
                    }
                }
            }, true);


    }

    public boolean isFirstCome() {
        return (boolean) SPUtils.get(this, SPUtils.IS_FIRS_COME, true);
    }

    public boolean isQuit() {
        return (boolean) SPUtils.get(this, SPUtils.IS_QUIT, true);
    }

    /*
   **describe:跳转到登录注册页面 携带账号并关闭该页面
   */
    public void intentLoginRegisterActivity() {
        Intent intent = new Intent(this, LoginRegisterActivity.class);
        intent.putExtra(LoginRegisterActivity.INTENT_TYPE, LoginRegisterActivity.LOGIN);
        intent.putExtra(LoginFragment.LOGIN_WITH_NUMBER,
                (String) SPUtils.get(this, SPUtils.CURRENT_COUNT, SPUtils.DEFAULT_STRING));
        startActivity(intent);
        finish();
    }

    /*
   **describe:跳转到主页面 并关闭该页面
   */
    public void intentMainActivity() {
        Intent main_intent = new Intent(this, MainActivity.class);
        startActivity(main_intent);
        finish();
    }
}
