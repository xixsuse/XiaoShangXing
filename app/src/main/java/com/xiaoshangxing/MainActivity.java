package com.xiaoshangxing;

import android.os.Bundle;

import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.wo.WoFragment;

/**
 * Created by FengChaoQun
 * on 2016/6/21
 */
public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        BaseFragment frag = (BaseFragment) mFragmentManager.findFragmentById(R.id.loginregisterContent);
        if (frag != null) {
            return;
        }
        frag = WoFragment.newInstance();
        mFragmentManager.beginTransaction().add(R.id.main_fragment,
                frag, WoFragment.TAG).commit();
    }
}
