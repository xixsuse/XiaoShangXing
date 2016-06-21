package com.xiaoshangxing.utils;

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
        mApplication=(XSXApplication) getApplication();

    }


}
