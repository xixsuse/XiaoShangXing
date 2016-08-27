package com.xiaoshangxing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.input_activity.InputBoxLayout;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.FileUtils;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.normalUtils.SPUtils;
import com.xiaoshangxing.wo.WoFrafment.WoFragment;
import com.xiaoshangxing.xiaoshang.XiaoShangFragment;
import com.xiaoshangxing.yujian.YujianFragment.YuJianFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/6/21
 */
public class MainActivity extends BaseActivity {
    public static final String TAG = BaseActivity.TAG + "-MainActivity";

    @Bind(R.id.image_xiaoshang)
    ImageView imageXiaoshang;
    @Bind(R.id.xiaoshang)
    TextView xiaoshang;
    @Bind(R.id.xiaoshang_lay)
    RelativeLayout xiaoshangLay;
    @Bind(R.id.image_yujian)
    ImageView imageYujian;
    @Bind(R.id.yujian)
    TextView yujian;
    @Bind(R.id.yujian_lay)
    RelativeLayout yujianLay;
    @Bind(R.id.image_wo)
    ImageView imageWo;
    @Bind(R.id.wo)
    TextView wo;
    @Bind(R.id.wolay)
    RelativeLayout wolay;
    @Bind(R.id.radio_layout)
    LinearLayout radioLayout;
    @Bind(R.id.xiaoshang_dot)
    CirecleImage xiaoshangDot;
    @Bind(R.id.yujian_dot)
    CirecleImage yujianDot;
    @Bind(R.id.wo_dot)
    CirecleImage woDot;

    private int current;

    private WoFragment woFragment;
    private XiaoShangFragment xiaoShangFragment;
    private YuJianFragment yuJianFragment;
    private InputBoxLayout inputBoxLayout;


    private static final int BAIDU_READ_PHONE_STATE =1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);
        initInputBox();
        initAllFragments();
    }

    private void initInputBox() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.edit_and_emot);
        inputBoxLayout = new InputBoxLayout(this, relativeLayout, this);
    }

    private void initAllFragments() {
        SPUtils.put(this, SPUtils.IS_FIRS_COME, false);//当这个页面打开时，表明不是第一次进入APP了
        SPUtils.put(this, SPUtils.IS_QUIT, false);//当这个页面打开时，清除退出记录
        Fragment frag;
        frag = mFragmentManager.findFragmentByTag(WoFragment.TAG);
        woFragment = (frag == null) ? WoFragment.newInstance() : (WoFragment) frag;

        frag = mFragmentManager.findFragmentByTag(XiaoShangFragment.TAG);
        xiaoShangFragment = (frag == null) ? XiaoShangFragment.newInstance() : (XiaoShangFragment) frag;

        frag = mFragmentManager.findFragmentByTag(yuJianFragment.TAG);
        yuJianFragment = (frag == null) ? yuJianFragment.newInstance() : (YuJianFragment) frag;

        if (!xiaoShangFragment.isAdded()) {
            mFragmentManager.beginTransaction().add(R.id.main_fragment, xiaoShangFragment, XiaoShangFragment.TAG)
                    .commit();
        }
        if (!woFragment.isAdded()) {
            mFragmentManager.beginTransaction().add(R.id.main_fragment, woFragment, WoFragment.TAG)
                    .commit();
        }

        if (!yuJianFragment.isAdded()) {
            mFragmentManager.beginTransaction().add(R.id.main_fragment, yuJianFragment, YuJianFragment.TAG)
                    .commit();
        }

        setXiaoshang(true);
//        setYUjian(true);
    }

    @OnClick({R.id.xiaoshang_lay, R.id.yujian_lay, R.id.wolay, R.id.emotion, R.id.normal_emot, R.id.favorite
            , R.id.send, R.id.delete_emot})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.xiaoshang_lay:
                if (current == 1) {
                    return;
                } else {
                    setXiaoshang(true);
                    setYUjian(false);
                    setWo(false);
                }

                break;
            case R.id.yujian_lay:
                if (current == 2) {
                    return;
                } else {
                    setXiaoshang(false);
                    setYUjian(true);
                    setWo(false);
                }
                break;
            case R.id.wolay:
                if (current == 3) {
                    return;
                } else {
                    setXiaoshang(false);
                    setYUjian(false);
                    setWo(true);
                }
                break;
        }
    }

    private void setWo(boolean is) {
        if (is) {
            imageWo.setImageResource(R.mipmap.wo_on);
            wo.setTextColor(getResources().getColor(R.color.green1));

//            mFragmentManager.beginTransaction().replace(R.id.main_fragment,
//                    woFragment, XiaoShangFragment.TAG).commit();
            mFragmentManager.beginTransaction().hide(xiaoShangFragment).hide(yuJianFragment).show(woFragment)
                    .commit();
            woFragment.autoRefresh();
            current = 3;
        } else {
            imageWo.setImageResource(R.mipmap.wo_off);
            wo.setTextColor(getResources().getColor(R.color.g0));
        }

    }

    private void setYUjian(boolean is) {
        if (is) {
            imageYujian.setImageResource(R.mipmap.yujian_on);
            yujian.setTextColor(getResources().getColor(R.color.green1));
            mFragmentManager.beginTransaction().hide(woFragment).hide(xiaoShangFragment).show(yuJianFragment)
                    .commit();
            current = 2;
        } else {
            imageYujian.setImageResource(R.mipmap.yujian_off);
            yujian.setTextColor(getResources().getColor(R.color.g0));
        }

    }

    private void setXiaoshang(boolean is) {
        if (is) {
            imageXiaoshang.setImageResource(R.mipmap.xiaoshang_on);
            xiaoshang.setTextColor(getResources().getColor(R.color.green1));

//            mFragmentManager.beginTransaction().replace(R.id.main_fragment,
//                    xiaoShangFragment, XiaoShangFragment.TAG).commit();
            mFragmentManager.beginTransaction().hide(woFragment).hide(yuJianFragment).show(xiaoShangFragment)
                    .commit();
            current = 1;
        } else {
            imageXiaoshang.setImageResource(R.mipmap.xiaoshang_off);
            xiaoshang.setTextColor(getResources().getColor(R.color.g0));
        }
    }

    public InputBoxLayout getInputBoxLayout() {
        return inputBoxLayout;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public String toString() {
        return TAG;
    }
}
