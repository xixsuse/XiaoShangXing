package com.xiaoshangxing.login_register.StartActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.xiaoshangxing.MainActivity;
import com.xiaoshangxing.R;
import com.xiaoshangxing.login_register.LoginRegisterActivity.LoginFragment.LoginFragment;
import com.xiaoshangxing.login_register.LoginRegisterActivity.LoginRegisterActivity;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

/**
 * Created by FengChaoQun
 * on 2016/8/10
 */
public class FlashActivity extends BaseActivity {
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
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
//                Intent intent = new Intent(FlashActivity.this, StartActivity.class);
//                startActivity(intent);
//                finish();

            }
        }, 2000);
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
