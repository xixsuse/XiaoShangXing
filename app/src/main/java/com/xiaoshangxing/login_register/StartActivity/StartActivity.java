package com.xiaoshangxing.login_register.StartActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xiaoshangxing.MainActivity;
import com.xiaoshangxing.R;
import com.xiaoshangxing.login_register.LoginRegisterActivity.LoginFragment.LoginFragment;
import com.xiaoshangxing.login_register.LoginRegisterActivity.LoginRegisterActivity;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

public class StartActivity extends BaseActivity implements View.OnClickListener, StartActivityContract.View {

    public static final String TAG = BaseActivity.TAG + "-StartActivity";
    private Button btn_login;
    private Button btn_register;
    private ImageView login_imag;
    private RelativeLayout flash;
    private StartActivityContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setmPresenter(new StartActivityPresenter(this, this));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mPresenter.startWait();
    }

    /*
    **describe:初始化按钮
    */
    private void initButton() {
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        flash=(RelativeLayout)findViewById(R.id.flash_lay);
        login_imag=(ImageView)findViewById(R.id.login_img);
    }

    /*
    **describe:跳转到登录注册页面 并关闭该页面
    */
    @Override
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
    @Override
    public void intentMainActivity() {
        Intent main_intent = new Intent(this, MainActivity.class);
        startActivity(main_intent);
        finish();
    }

    /*
    **describe:跳转到登录页面
    */
    @Override
    public void gotoLogin() {
        Intent intent = new Intent(this, LoginRegisterActivity.class);
        intent.putExtra(LoginRegisterActivity.INTENT_TYPE, LoginRegisterActivity.LOGIN);
        startActivity(intent);
    }

    /*
    **describe:跳转到注册页面
    */
    @Override
    public void gotoRegister() {
        Intent intent = new Intent(this, LoginRegisterActivity.class);
        intent.putExtra(LoginRegisterActivity.INTENT_TYPE, LoginRegisterActivity.REGISTER);
        startActivity(intent);
    }

    /*
     **describe:显示按钮
     */
    @Override
    public void showButton() {
        initButton();

        flash.setBackgroundColor(getResources().getColor(R.color.flash_gray));
        login_imag.setVisibility(View.VISIBLE);

        btn_login.setVisibility(View.VISIBLE);
        btn_register.setVisibility(View.VISIBLE);
        //淡入
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(btn_login, "hs", 0, 1).setDuration(1000);
        animator2.setInterpolator(new AccelerateInterpolator());
        animator2.start();
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                btn_login.setAlpha((Float) animation.getAnimatedValue());
                btn_login.setScaleX((Float) animation.getAnimatedValue());
                btn_login.setScaleY((Float) animation.getAnimatedValue());
                btn_register.setAlpha((Float) animation.getAnimatedValue());
                btn_register.setScaleX((Float) animation.getAnimatedValue());
                btn_register.setScaleY((Float) animation.getAnimatedValue());
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                mPresenter.clickOnLogin();
                break;
            case R.id.btn_register:
                mPresenter.clickOnRegister();
                break;
        }
    }

    public void setmPresenter(@Nullable StartActivityContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public String toString() {
        return TAG;
    }

}
