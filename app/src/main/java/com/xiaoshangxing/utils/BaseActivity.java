package com.xiaoshangxing.utils;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by FengChaoQun
 * on 2016/6/11
 */
public class BaseActivity extends AppCompatActivity {

    public static final String TAG = AppContracts.TAG + "-Activity";

    protected FragmentManager mFragmentManager;
    protected XSXApplication mApplication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentManager = getSupportFragmentManager();
        mApplication = (XSXApplication) getApplication();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /*
        **describe:默认隐藏标题栏
        */
        hideTitle(true);
    }

    protected void hideTitle(boolean hideTitle) {
        if (!hideTitle) {
            getSupportActionBar().show();
        } else {
            getSupportActionBar().hide();
        }

    }


}
