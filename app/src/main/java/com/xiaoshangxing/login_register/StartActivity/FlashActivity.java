package com.xiaoshangxing.login_register.StartActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.xiaoshangxing.MainActivity;
import com.xiaoshangxing.R;
import com.xiaoshangxing.login_register.LoginRegisterActivity.LoginFragment.LoginFragment;
import com.xiaoshangxing.login_register.LoginRegisterActivity.LoginRegisterActivity;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

import java.util.ArrayList;

/**
 * Created by FengChaoQun
 * on 2016/8/10
 */
public class FlashActivity extends BaseActivity {
    private Handler handler;
    public static final String MESSAGE="MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
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
                (String) SPUtils.get(this, SPUtils.PHONENUMNBER, SPUtils.DEFAULT_STRING));
        startActivity(intent);
        finish();
    }

    /*
   **describe:跳转到主页面 并关闭该页面
   */
    public void intentMainActivity() {
        Intent main_intent = new Intent(this, MainActivity.class);
        main_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(main_intent);
        finish();
    }
}
