package com.xiaoshangxing.setting.currency;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.currency.chatBackground.ChatBackgroundActivity;
import com.xiaoshangxing.setting.currency.currenctFragment.CurrencyFragment;
import com.xiaoshangxing.utils.BaseActivity;

/**
 * Created by 15828 on 2016/7/14.
 */
public class CurrencyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_fraglayout);
        mFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, new CurrencyFragment())
                .commit();
    }

    public void ChatBackground(View view) {
        Intent intent = new Intent(this, ChatBackgroundActivity.class);
        startActivity(intent);
    }

}
