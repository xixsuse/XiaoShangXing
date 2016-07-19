package com.xiaoshangxing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.wo.WoFragment;
import com.xiaoshangxing.xiaoshang.XiaoShangFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/6/21
 */
public class MainActivity extends BaseActivity {


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

    private int current;

    private WoFragment woFragment;
    private XiaoShangFragment xiaoShangFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);

        initAllFragments();
        BaseFragment frag = (BaseFragment) mFragmentManager.findFragmentById(R.id.loginregisterContent);
        if (frag != null) {
            return;
        }
//        frag = WoFragment.newInstance();
//        mFragmentManager.beginTransaction().add(R.id.main_fragment,
//                frag, WoFragment.TAG).commit();
        frag = new XiaoShangFragment();
        mFragmentManager.beginTransaction().add(R.id.main_fragment,
                frag, WoFragment.TAG).commit();

        setWo(true);
    }

    private void initAllFragments() {
        Fragment frag;
        frag = mFragmentManager.findFragmentByTag(WoFragment.TAG);
        woFragment = (frag == null) ? WoFragment.newInstance() : (WoFragment) frag;

        frag = mFragmentManager.findFragmentByTag(XiaoShangFragment.TAG);
        xiaoShangFragment = (frag == null) ? XiaoShangFragment.newInstance() : (XiaoShangFragment) frag;

    }

    @OnClick({R.id.xiaoshang_lay, R.id.yujian_lay, R.id.wolay})
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

            mFragmentManager.beginTransaction().replace(R.id.main_fragment,
                    woFragment, XiaoShangFragment.TAG).commit();
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

            mFragmentManager.beginTransaction().replace(R.id.main_fragment,
                    xiaoShangFragment, XiaoShangFragment.TAG).commit();
            current = 1;
        } else {
            imageXiaoshang.setImageResource(R.mipmap.xiaoshang_off);
            xiaoshang.setTextColor(getResources().getColor(R.color.g0));
        }
    }

}
