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
        setContentView(R.layout.activity_setting_newnotice);
        mFragmentManager.beginTransaction()
                .replace(R.id.setting_newnotice_Content, new NewNoticeFrament())
                .commit();
    }

    public void NewNotice_back(View view) {
        finish();
    }


    public void nodisturb(View view) {
        mFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.setting_newnotice_Content, new NoDisturbFragment())
                .commit();
    }
}
