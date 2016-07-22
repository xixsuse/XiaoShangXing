package com.xiaoshangxing.xiaoshang.ShoolfellowHelp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.xiaoshang.ShoolfellowHelp.MyShoolfellowHelp.MyShoolHelpFragment;
import com.xiaoshangxing.xiaoshang.ShoolfellowHelp.ShoolfellowHelpFragment.ShoolfellowHelpFragment;

/**
 * Created by FengChaoQun
 * on 2016/7/20
 */
public class ShoolfellowHelpActivity extends BaseActivity {
    public static final String TAG = BaseActivity.TAG + "-ShoolfellowHelpActivity";

    private ShoolfellowHelpFragment shoolfellowHelpFragment;
    private MyShoolHelpFragment myShoolHelpFragment;

    private boolean isHideMenu;//记录是否需要点击返回键隐藏菜单

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoolfellowhelp);
        BaseFragment frag = (BaseFragment) mFragmentManager.findFragmentById(R.id.main_fragment);
        if (frag != null) {
            return;
        }

        initAllFrafments();

        frag = getShoolfellowHelpFragment();
        mFragmentManager.beginTransaction().add(R.id.main_fragment,
                frag, ShoolfellowHelpFragment.TAG).commit();
//        frag = getMyShoolHelpFragment();
//        mFragmentManager.beginTransaction().add(R.id.main_fragment,
//                frag, MyShoolHelpFragment.TAG).commit();
    }

    private void initAllFrafments() {
        Fragment frag;

        frag = mFragmentManager.findFragmentByTag(ShoolfellowHelpFragment.TAG);
        shoolfellowHelpFragment = (frag == null) ? ShoolfellowHelpFragment.newInstance() : (ShoolfellowHelpFragment) frag;

        frag = mFragmentManager.findFragmentByTag(MyShoolHelpFragment.TAG);
        myShoolHelpFragment = (frag == null) ? MyShoolHelpFragment.newInstance() : (MyShoolHelpFragment) frag;
    }

    public ShoolfellowHelpFragment getShoolfellowHelpFragment() {
        return shoolfellowHelpFragment;
    }

    public MyShoolHelpFragment getMyShoolHelpFragment() {
        return myShoolHelpFragment;
    }

    public boolean isHideMenu() {
        return isHideMenu;
    }

    public void setHideMenu(boolean hideMenu) {
        isHideMenu = hideMenu;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isHideMenu) {
                myShoolHelpFragment.showHideMenu(false);
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }
}
