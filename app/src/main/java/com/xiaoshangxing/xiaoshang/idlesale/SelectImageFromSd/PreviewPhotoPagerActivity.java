package com.xiaoshangxing.xiaoshang.idlesale.SelectImageFromSd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoshangxing.R;
import com.xiaoshangxing.input_activity.InputActivity;
import com.xiaoshangxing.wo.WoFrafment.check_photo.HackyViewPager;
import com.xiaoshangxing.wo.WoFrafment.check_photo.ImageDetailFragment;
import com.xiaoshangxing.xiaoshang.idlesale.SelectImageFromSd.AllPhotoActivity.AllIPhotosActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 图片查看器
 */
public class PreviewPhotoPagerActivity extends FragmentActivity {
    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String LIMIT = "LIMIT";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String ALL_IMAGE_PATHS = "all_image_urls";
    public static final String SELECTED_PICTURE_PATHES = "selected_pictures";

    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.checkbox)
    CheckBox checkbox;
    @Bind(R.id.title)
    RelativeLayout title;
//    @Bind(R.id.original)
//    CheckBox original;
    @Bind(R.id.count)
    TextView count;
    @Bind(R.id.complete)
    RelativeLayout complete;
    @Bind(R.id.details_entry_layout)
    RelativeLayout detailsEntryLayout;

    private HackyViewPager mPager;
    private int pagerPosition;
    private TextView indicator;


    private ArrayList<String> urls;
    //    private List<Integer> selectPosition=new ArrayList<Integer>();
    private ArrayList<String> select_image_urls = new ArrayList<String>();
    private int currentItem;
    private int limit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.idle_image_preview_pager);
        ButterKnife.bind(this);

        limit=getIntent().getIntExtra(LIMIT,3);

        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        urls = getIntent().getStringArrayListExtra(ALL_IMAGE_PATHS);
        Log.d("urls",urls.size()+"");
        select_image_urls = getIntent().getStringArrayListExtra(SELECTED_PICTURE_PATHES);

        mPager = (HackyViewPager) findViewById(R.id.pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
        mPager.setAdapter(mAdapter);
        setCheckBox(pagerPosition);
        indicator = (TextView) findViewById(R.id.indicator);

        CharSequence text1 = getString(R.string.viewpager_indicator, 1, mPager.getAdapter().getCount());
        indicator.setText(text1);

        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox.isChecked()){
                    if (select_image_urls.size() >= limit) {
                        Toast.makeText(PreviewPhotoPagerActivity.this, "最多只能选择"+limit+"张图片", Toast.LENGTH_SHORT).show();
                        checkbox.setChecked(false);
                    }else {
                        select_image_urls.add(urls.get(currentItem));
                    }
                }else {
                    if (select_image_urls.contains(urls.get(currentItem))) {
                        select_image_urls.remove(urls.get(currentItem));
                    }
                }

                setCount(select_image_urls.size());
            }
        });

        setCount(select_image_urls.size());

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
                setCheckBox(arg0);
                currentItem=arg0;
            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        mPager.setCurrentItem(pagerPosition);
    }

    private void setCheckBox(int position){
        if (select_image_urls.contains(urls.get(position))) {
            checkbox.setChecked(true);
        }else {
            checkbox.setChecked(false);
        }
    }

    private void setCount(int x){
        count.setText("("+x+")");
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    @OnClick({R.id.back, R.id.checkbox, R.id.complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
//                getBack();
                finish();
                break;
            case R.id.checkbox:
                break;
            case R.id.complete:
//                finish();
                Intent k = new Intent(this, InputActivity.class);
                k.putExtra(InputActivity.EDIT_STATE, InputActivity.XIANZHI);
                k.putStringArrayListExtra("path",select_image_urls);
                startActivity(k);
                finish();
                break;
        }
    }


    @Override
    public void finish() {
        Intent intent=new Intent();
//        ArrayList<String> select_picture_urls2 = new ArrayList<String>();
//        for (int i = 0; i < select_image_urls.size(); i++) {
//            select_picture_urls2.add(select_image_urls.get(i));
//        }
        intent.putExtra(AllIPhotosActivity.SELECTED_IMAGE, select_image_urls);
        setResult(AllIPhotosActivity.SELECT_PHOTO_RESULT_OK, intent);
        super.finish();
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
