package com.xiaoshangxing.login_register.StartActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xiaoshangxing.MainActivity;
import com.xiaoshangxing.R;
import com.xiaoshangxing.login_register.LoginRegisterActivity.LoginFragment.LoginFragment;
import com.xiaoshangxing.login_register.LoginRegisterActivity.LoginRegisterActivity;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/10/14
 */

public class IndicatorActivity extends BaseActivity {
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.bottom_image)
    ImageView bottomImage;
    @Bind(R.id.root_view)
    RelativeLayout rootView;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private boolean isEnd;
    private boolean misScrolled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator);
        ButterKnife.bind(this);
        fragments.add(IndicatorFragment.newInstance(R.mipmap.guide_page01, R.mipmap.guide_page_letter01,false));
        fragments.add(IndicatorFragment.newInstance(R.mipmap.guide_page02, R.mipmap.guide_page_letter02,false));
        fragments.add(IndicatorFragment.newInstance(R.mipmap.guide_page03, R.mipmap.guide_page_letter03,true));
        FragAdapter adapter = new FragAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(adapter);
        setBottomImage(0);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                if (position == 2 && positionOffset == 0) {
//                    intent();
//                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    isEnd = true;
                } else {
                    isEnd = false;
                }
                setBottomImage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                switch (state) {
//                    case ViewPager.SCROLL_STATE_DRAGGING:
//                        misScrolled = false;
//                        break;
//                    case ViewPager.SCROLL_STATE_SETTLING:
//                        misScrolled = true;
//                        break;
//                    case ViewPager.SCROLL_STATE_IDLE:
//                        if (viewpager.getCurrentItem() == viewpager.getAdapter().getCount() - 1 && !misScrolled) {
//                            intent();
//                        }
//                        misScrolled = true;
//                        break;
//                }
            }
        });

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEnd) {
                    intent();
                }
            }
        });
    }

    public void intent() {
        if (isFirstCome()) {
            Intent intent = new Intent(IndicatorActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        } else {
            if (isQuit()) {
                intentLoginRegisterActivity();
            } else {
                intentMainActivity();
            }
        }
    }

    /*
  **describe:跳转到主页面 并关闭该页面
  */
    public void intentMainActivity() {
        Intent main_intent = new Intent(this, MainActivity.class);
        main_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(main_intent);
        finish();
    }

    /*
 **describe:跳转到登录注册页面 携带账号并关闭该页面
 */
    public void intentLoginRegisterActivity() {
        Intent intent = new Intent(this, LoginRegisterActivity.class);
        intent.putExtra(LoginRegisterActivity.INTENT_TYPE, LoginRegisterActivity.LOGIN);
        intent.putExtra(LoginFragment.LOGIN_WITH_NUMBER,
                (String) SPUtils.get(this, SPUtils.PHONENUMNBER, SPUtils.DEFAULT_STRING));
        startActivity(intent);
        finish();
    }

    public boolean isQuit() {
        return (boolean) SPUtils.get(this, SPUtils.IS_QUIT, true);
    }

    private void setBottomImage(int position) {
        switch (position) {
            case 0:
                bottomImage.setImageResource(R.mipmap.guidepage_point01);
                break;
            case 1:
                bottomImage.setImageResource(R.mipmap.guidepage_point02);
                break;
            case 2:
                bottomImage.setImageResource(R.mipmap.guidepage_point03);
                break;
        }
    }

    public boolean isFirstCome() {
        return (boolean) SPUtils.get(this, SPUtils.IS_FIRS_COME, true);
    }

    public class FragAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;

        public FragAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            // TODO Auto-generated constructor stub
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            return mFragments.get(arg0);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mFragments.size();
        }

    }
}
