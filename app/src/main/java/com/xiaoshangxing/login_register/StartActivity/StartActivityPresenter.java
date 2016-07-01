package com.xiaoshangxing.login_register.StartActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

import com.xiaoshangxing.login_register.LoginRegisterActivity.LoginRegisterActivity;

/**
 * Created by FengChaoQun
 * on 2016/6/22
 */
public class StartActivityPresenter implements StartActivityContract.Presenter {
    private StartActivityContract.View mView;
    private myHandler handler;
    private Context context;


    public StartActivityPresenter(@NonNull StartActivityContract.View view, Context context) {
        this.mView = view;
        this.context = context;
        view.setmPresenter(this);
    }

    @Override
    public void startWait() {
        handler = new myHandler(Looper.myLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = handler.obtainMessage();
                message.arg1 = 1;
                handler.sendMessage(message);

            }
        }).start();
    }

    @Override
    public boolean isFirstCome() {
        return true;
    }

    @Override
    public void clickOnLogin() {
        Intent intent = new Intent(context, LoginRegisterActivity.class);
        intent.putExtra("type", LoginRegisterActivity.FIRST_COME);
        intent.putExtra("number", "88888888888");
        context.startActivity(intent);
    }

    @Override
    public void clickOnRegister() {
        Intent intent = new Intent(context, LoginRegisterActivity.class);
        intent.putExtra("type", LoginRegisterActivity.REGISTER);
        context.startActivity(intent);
    }

    private class myHandler extends Handler {
        public myHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (isFirstCome()) {
                mView.showButton();
            } else {
                mView.intentLoginRegisterActivity();
            }

        }
    }

}
