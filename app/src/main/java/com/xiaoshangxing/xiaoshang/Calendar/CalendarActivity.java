package com.xiaoshangxing.xiaoshang.Calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.BaseFragment;

/**
 * Created by FengChaoQun
 * on 2016/10/5
 */

public class CalendarActivity extends BaseActivity {
    public static final String TAG = BaseActivity.TAG + "-CalendarActivity";

    private CalendarFragment calendarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_fraglayout);
        BaseFragment frag = (BaseFragment) mFragmentManager.findFragmentById(R.id.main_fragment);
        if (frag != null) {
            return;
        }
//        parseIntent();
        setEnableRightSlide(false);
        initAllFrafments();
    }

    private void initAllFrafments() {
        Fragment frag;

        frag = mFragmentManager.findFragmentByTag(CalendarFragment.TAG);
        calendarFragment = (frag == null) ? CalendarFragment.newInstance() : (CalendarFragment) frag;

        mFragmentManager.beginTransaction().add(R.id.main_fragment,
                calendarFragment, CalendarFragment.TAG).commit();
    }


}
