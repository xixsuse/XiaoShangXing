package com.xiaoshangxing.setting.shiming.result;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.BroadCast.FinishActivityRecever;

/**
 * Created by tianyang on 2016/9/20.
 */
public class VertifyingActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertifying);

    }

    public void Back(View view) {
        back();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void back() {
        FinishActivityRecever.sendFinishBroadcast(this);
        finish();
    }
}
