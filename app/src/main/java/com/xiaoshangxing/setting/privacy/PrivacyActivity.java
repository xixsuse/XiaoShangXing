package com.xiaoshangxing.setting.privacy;

import android.os.Bundle;
import android.view.View;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.privacy.blackListFragment.BlackListFragment;
import com.xiaoshangxing.setting.privacy.privacyFistFragment.PrivacyFistFragment;
import com.xiaoshangxing.setting.privacy.privacyFragment.PrivacyFragment;
import com.xiaoshangxing.setting.privacy.privacySecondFragment.PrivacySecondFragment;
import com.xiaoshangxing.utils.BaseActivity;

/**
 * Created by 15828 on 2016/7/14.
 */
public class PrivacyActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_fraglayout);
        mFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, new PrivacyFragment())
                .commit();
    }



    public void privacy_back(View view) {
        finish();
    }

    public void blacklist(View view) {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .addToBackStack(null)
                .replace(R.id.main_fragment, new BlackListFragment())
                .commit();
    }

    public void PrivacyFirst(View view) {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .addToBackStack(null)
                .replace(R.id.main_fragment, new PrivacyFistFragment())
                .commit();
    }


    public void PrivacySecond(View view) {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .addToBackStack(null)
                .replace(R.id.main_fragment, new PrivacySecondFragment())
                .commit();
    }
}
