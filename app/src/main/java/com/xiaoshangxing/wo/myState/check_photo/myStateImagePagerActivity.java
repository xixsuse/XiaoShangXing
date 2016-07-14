package com.xiaoshangxing.wo.myState.check_photo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.image.SaveImageTask;
import com.xiaoshangxing.wo.myState.DetailsActivity;
import com.xiaoshangxing.wo.school_circle.check_photo.HackyViewPager;
import com.xiaoshangxing.wo.school_circle.check_photo.ImageDetailFragment;

import java.util.ArrayList;

/**
 * 图片查看器
 */
public class myStateImagePagerActivity extends FragmentActivity implements View.OnClickListener, myStateImagePagerContract.View {
    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";

    private HackyViewPager mPager;
    private int pagerPosition;
    private TextView indicator;

    private View back, more, detail, backgroud;
    private TextView time, text;

    private ArrayList<String> urls;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_mystate);

        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        more = findViewById(R.id.more);
        more.setOnClickListener(this);
        detail = findViewById(R.id.details_entry_layout);
        detail.setOnClickListener(this);
        time = (TextView) findViewById(R.id.time);
        text = (TextView) findViewById(R.id.text);


        backgroud = findViewById(R.id.background);

        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        urls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);

        mPager = (HackyViewPager) findViewById(R.id.pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
        mPager.setAdapter(mAdapter);
        indicator = (TextView) findViewById(R.id.indicator);

        CharSequence text1 = getString(R.string.viewpager_indicator, 1, mPager.getAdapter().getCount());
        indicator.setText(text1);

        backgroud.setMinimumHeight(text.getHeight());

        // 更新下标
        mPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = getString(R.string.viewpager_indicator, arg0 + 1, mPager.getAdapter().getCount());
                indicator.setText(text);
                pagerPosition = arg0;
                Log.d("select", "" + arg0);
                Log.d("url", urls.get(pagerPosition));
            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        mPager.setCurrentItem(pagerPosition);
    }

    @Override
    public void setTime() {

    }

    @Override
    public void showMoreDialog() {
        DialogUtils.DialogMenu2 menu = new DialogUtils.DialogMenu2(myStateImagePagerActivity.this);
        menu.addMenuItem("发送给好友");
        menu.addMenuItem("保存到本地");
        menu.addMenuItem("删除");
        menu.initView();
        menu.show();
        LocationUtil.bottom_FillWidth(this, menu);

        menu.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                switch (position) {
                    case 0:
                        Toast.makeText(myStateImagePagerActivity.this, "发送给好友", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(myStateImagePagerActivity.this, "保存到手机", Toast.LENGTH_SHORT).show();
                        SaveImageTask s = new SaveImageTask(myStateImagePagerActivity.this);
                        s.execute(urls.get(pagerPosition));
                        break;
                    case 2:
                        showMakesureDialog();
                        break;
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void gotoDetails() {
        Intent intent = new Intent(myStateImagePagerActivity.this, DetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void setText() {

    }

    @Override
    public void setPraiseandComment() {

    }

    @Override
    public void sendToFriend() {

    }

    @Override
    public void showMakesureDialog() {
        final DialogUtils.Dialog_Center dialog_center = new DialogUtils.Dialog_Center(this);
        if (true) {
            dialog_center.Message("与这张照片同时发布的一组照片都会被删除。");
            dialog_center.Button("全部删除", "取消");
        } else {
            dialog_center.Message("确认删除吗?");
            dialog_center.Button("删除", "取消");
        }
        dialog_center.MbuttonOnClick(new DialogUtils.Dialog_Center.buttonOnClick() {
            @Override
            public void onButton1() {
                Toast.makeText(myStateImagePagerActivity.this, "删除", Toast.LENGTH_SHORT).show();
                dialog_center.close();
            }

            @Override
            public void onButton2() {
                Toast.makeText(myStateImagePagerActivity.this, "取消", Toast.LENGTH_SHORT).show();
                dialog_center.close();
            }
        });
        Dialog dialog = dialog_center.create();
        dialog.show();
        LocationUtil.setWidth(this, dialog,
                getResources().getDimensionPixelSize(R.dimen.x780));
    }

    @Override
    public void setmPresenter(@Nullable myStateImagePagerContract.Presenter presenter) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more:
                showMoreDialog();
                break;
            case R.id.details_entry_layout:
                gotoDetails();
                break;
            case R.id.back:
                finish();
                break;
        }

    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ArrayList<String> fileList;

        public ImagePagerAdapter(FragmentManager fm, ArrayList<String> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList.get(position);
            return ImageDetailFragment.newInstance(url);
        }

    }
}
