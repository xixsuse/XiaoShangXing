package com.xiaoshangxing.wo.setting.privacy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xiaoshangxing.R;
import com.xiaoshangxing.wo.setting.privacy.blackListFragment.BlackListFragment;
import com.xiaoshangxing.wo.setting.privacy.forbiddenListFragment.ForbiddenListFragment;
import com.xiaoshangxing.wo.setting.privacy.privacyFragment.PrivacyFragment;
import com.xiaoshangxing.wo.setting.privacy.shieldListFragment.ShieldListFragment;
import com.xiaoshangxing.utils.baseClass.BaseActivity;

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
                .replace(R.id.main_fragment, new ForbiddenListFragment())
                .commit();
    }


    public void PrivacySecond(View view) {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .addToBackStack(null)
                .replace(R.id.main_fragment, new ShieldListFragment())
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
