package com.xiaoshangxing.loginAndRegister.StartActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.xiaoshangxing.MainActivity;
import com.xiaoshangxing.R;
import com.xiaoshangxing.loginAndRegister.LoginRegisterActivity.LoginFragment.LoginFragment;
import com.xiaoshangxing.loginAndRegister.LoginRegisterActivity.LoginRegisterActivity;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.utils.customView.ClearableEditTextWithIcon;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/8/10
 */
public class FlashActivity extends BaseActivity {
    public static final String MESSAGE = "MESSAGE";
    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.edittext)
    ClearableEditTextWithIcon edittext;
    @Bind(R.id.enter_button)
    TextView enterButton;
    @Bind(R.id.invite_code_lay)
    RelativeLayout inviteCodeLay;

    private Handler handler;
    private boolean isNeedInviteCode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        ButterKnife.bind(this);
        setEnableRightSlide(false);
        if (getIntent().hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    GotoSeeMessage();
                }
            }, 1000);
        } else {
            normalStart();
        }
    }

    private void initInviteCodeLay() {
        if (isNeedInviteCode) {
            inviteCodeLay.setVisibility(View.VISIBLE);
            edittext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!TextUtils.isEmpty(edittext.getText().toString())) {
                        enterButton.setAlpha(1);
                        enterButton.setEnabled(true);
                    } else {
                        enterButton.setAlpha(0.5f);
                        enterButton.setEnabled(false);
                    }
                }
            });
            enterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FlashActivity.this, IndicatorActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                        enterButton.performClick();
                        return false;
                    }
                    return false;
                }
            });
        } else {
            inviteCodeLay.setVisibility(View.GONE);
        }
    }

    private void normalStart() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirstCome()) {
                    initInviteCodeLay();
                } else if (isNeedGuide()) {
                    Intent intent = new Intent(FlashActivity.this, IndicatorActivity.class);
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

    private void GotoSeeMessage() {
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
        } else {
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

    public boolean isNeedGuide() {
        return (boolean) SPUtils.get(this, SPUtils.IS_NEED_GUIDE, true);
//        return true;
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
