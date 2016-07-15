package com.xiaoshangxing.setting.currency;

import android.os.Bundle;
import android.view.View;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.currency.chatBackground.ChatBackgroundFragment;
import com.xiaoshangxing.setting.currency.chooseBackgroundFragment.ChooseBackgroundFragment;
import com.xiaoshangxing.setting.currency.currenctFragment.CurrencyFragment;
import com.xiaoshangxing.utils.BaseActivity;

/**
 * Created by 15828 on 2016/7/14.
 */
public class CurrencyActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_currency);
        mFragmentManager.beginTransaction()
                .replace(R.id.setting_currency_Content, new CurrencyFragment())
                .commit();
    }

    public void currency_back(View view) {
        finish();
    }

    public void ChatBackground(View view) {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .addToBackStack(null)
                .replace(R.id.setting_currency_Content, new ChatBackgroundFragment())
                .commit();
    }

    public void ChooseBackground(View view) {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .addToBackStack(null)
                .replace(R.id.setting_currency_Content, new ChooseBackgroundFragment())
                .commit();
    }
}
