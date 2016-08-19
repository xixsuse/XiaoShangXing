package com.xiaoshangxing.xiaoshang.schoolcalender;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.normalUtils.ActivityUtils;
import com.xiaoshangxing.xiaoshang.schoolcalender.SchoolCalenderInfoFragment.CalenderInfoFragment;
import com.xiaoshangxing.xiaoshang.schoolcalender.addEntranceFragment.AddEntranceFragment;
import com.xiaoshangxing.xiaoshang.schoolcalender.addinfofragment.AddInfoFragment;

/**
 * Created by quchwe on 2016/7/22 0022.
 */
public class SchoolCalenderActivity extends BaseActivity {
    public static final String TAG = BaseActivity.TAG + "-SchoolCalenderActivity";
    private AddEntranceFragment addEntranceFragment;
    private CalenderInfoFragment calenderInfoFragment;
    private AddInfoFragment addInfoFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_calender);
        initAllfrag();
        CalenderInfoFragment tasksFragment =
                (CalenderInfoFragment) getSupportFragmentManager().findFragmentById(R.id.fl_school_calender);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = CalenderInfoFragment.getInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), tasksFragment, R.id.fl_school_calender);
        }
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
    private void initAllfrag(){
        Fragment frag;
        frag = mFragmentManager.findFragmentByTag(CalenderInfoFragment.TAG);
        calenderInfoFragment = (frag == null) ? CalenderInfoFragment.getInstance() : (CalenderInfoFragment) frag;
        frag = mFragmentManager.findFragmentByTag(AddEntranceFragment.TAG);
        addEntranceFragment = (frag==null)? AddEntranceFragment.newInstance():(AddEntranceFragment)frag;
        frag = mFragmentManager.findFragmentByTag(AddInfoFragment.TAG);
        addInfoFragment = (frag==null)? AddInfoFragment.newInstance():(AddInfoFragment)frag;


    }

    public AddEntranceFragment getAddEntranceFragment() {
        return addEntranceFragment;
    }

    public CalenderInfoFragment getCalenderInfoFragment() {
        return calenderInfoFragment;
    }


    public AddInfoFragment getAddInfoFragment() {
        return addInfoFragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        AddInfoFragment frg = (AddInfoFragment)mFragmentManager.findFragmentByTag(AddInfoFragment.TAG);
        if (frg!=null) {
            Log.d("wq","qer");
            if (AddInfoFragment.IS_BACKED) {
                Log.d("wq","qer34");
                frg.onKeyDown(keyCode, event);
                AddInfoFragment.IS_BACKED = false;
                return true;
            }
        }
        return super.onKeyDown(keyCode,event);
    }

}

