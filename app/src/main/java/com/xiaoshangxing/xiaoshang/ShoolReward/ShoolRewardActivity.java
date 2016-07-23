package com.xiaoshangxing.xiaoshang.ShoolReward;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.xiaoshang.ShoolReward.MyShoolReward.MyShoolRewardFragment;
import com.xiaoshangxing.xiaoshang.ShoolReward.ShoolRewardFragment.ShoolRewardFragment;
import com.xiaoshangxing.xiaoshang.ShoolReward.collect.CollectFragment;
import com.xiaoshangxing.xiaoshang.ShoolfellowHelp.MyShoolfellowHelp.MyShoolHelpFragment;
import com.xiaoshangxing.xiaoshang.ShoolfellowHelp.ShoolfellowHelpFragment.ShoolfellowHelpFragment;

/**
 * Created by FengChaoQun
 * on 2016/7/20
 */
public class ShoolRewardActivity extends BaseActivity {
    public static final String TAG = BaseActivity.TAG + "-ShoolRewardActivity";

    private ShoolRewardFragment shoolRewardFragment;
    private MyShoolRewardFragment myShoolRewardFragment;
    private CollectFragment collectFragment;

    private boolean isHideMenu;//记录是否需要点击返回键隐藏菜单
    private boolean isCollect;//记录是否是collect界面在显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoolfellowhelp);
        BaseFragment frag = (BaseFragment) mFragmentManager.findFragmentById(R.id.main_fragment);
        if (frag != null) {
            return;
        }

        initAllFrafments();

        frag = getShoolRewardFragment();
        mFragmentManager.beginTransaction().add(R.id.main_fragment,
                frag, ShoolfellowHelpFragment.TAG).commit();
//        frag = getMyShoolHelpFragment();
//        mFragmentManager.beginTransaction().add(R.id.main_fragment,
//                frag, MyShoolHelpFragment.TAG).commit();
    }

    private void initAllFrafments() {
        Fragment frag;

        frag = mFragmentManager.findFragmentByTag(ShoolRewardFragment.TAG);
        shoolRewardFragment = (frag == null) ? ShoolRewardFragment.newInstance() : (ShoolRewardFragment) frag;

        frag = mFragmentManager.findFragmentByTag(MyShoolRewardFragment.TAG);
        myShoolRewardFragment = (frag == null) ? MyShoolRewardFragment.newInstance() : (MyShoolRewardFragment) frag;

        frag = mFragmentManager.findFragmentByTag(CollectFragment.TAG);
        collectFragment = (frag == null) ? CollectFragment.newInstance() : (CollectFragment) frag;
    }

    public ShoolRewardFragment getShoolRewardFragment() {
        return shoolRewardFragment;
    }

    public MyShoolRewardFragment getMyShoolRewardFragment() {
        return myShoolRewardFragment;
    }

    public CollectFragment getCollectFragment() {
        return collectFragment;
    }

    public boolean isHideMenu() {
        return isHideMenu;
    }

    public void setHideMenu(boolean hideMenu) {
        isHideMenu = hideMenu;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isHideMenu) {
                myShoolRewardFragment.showHideMenu(false);
                return true;
            } else if (isCollect){
                collectFragment.showHideMenu(false);
                return true;
            }
            else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }
}
