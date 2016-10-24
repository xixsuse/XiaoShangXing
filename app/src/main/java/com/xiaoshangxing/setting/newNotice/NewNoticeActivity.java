package com.xiaoshangxing.setting.newNotice;

import android.os.Bundle;
import android.view.View;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.newNotice.newNoticeFragment.NewNoticeFrament;
import com.xiaoshangxing.setting.newNotice.noDisturbFragment.NoDisturbFragment;
import com.xiaoshangxing.utils.BaseActivity;

/**
 * Created by 15828 on 2016/7/13.
 */
public class NewNoticeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_fraglayout);
        mFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, new NewNoticeFrament())
                .commit();
    }

    public void nodisturb(View view) {
        mFragmentManager.beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .replace(R.id.main_fragment, new NoDisturbFragment())
                .commit();
    }
}
