package com.xiaoshangxing.wo.PersonalState.check_photo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.LoadingDialog;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.wo.StateDetailsActivity.DetailsActivity;
import com.xiaoshangxing.wo.WoFrafment.check_photo.HackyViewPager;
import com.xiaoshangxing.wo.WoFrafment.check_photo.ImageDetailFragment;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * 图片查看器
 */
public class myStateImagePagerActivity extends FragmentActivity implements View.OnClickListener, myStateImagePagerContract.View {
    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    @Bind(R.id.praise_people_count)
    TextView praisePeopleCount;
    @Bind(R.id.comment_count)
    TextView commentCount;
    @Bind(R.id.comment_image)
    ImageView commentImage;

    private HackyViewPager mPager;
    private int pagerPosition;
    private TextView indicator;

    private View back, more, detail, backgroud;
    private TextView time;
    private EmotinText text;

    private ArrayList<String> urls = new ArrayList<>();
    private myStateImagePagerContract.Presenter mPresenter;
    private int published_id;
    private Published published;
    private Realm realm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_mystate);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        setmPresenter(new myStateImagePresenter(this, this));
        initView();

        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    private void initView() {

        if (!getIntent().hasExtra(IntentStatic.DATA)) {
            showToast("动态id出错");
            finish();
        }
        published_id = getIntent().getIntExtra(IntentStatic.DATA, -1);

        published = realm.where(Published.class).equalTo(NS.ID, published_id).findFirst();
        if (published == null) {
            showToast("获取动态信息出错");
            finish();
        }

        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        more = findViewById(R.id.more);
        more.setOnClickListener(this);
        detail = findViewById(R.id.details_entry_layout);
        detail.setOnClickListener(this);
        time = (TextView) findViewById(R.id.time);
        text = (EmotinText) findViewById(R.id.text);

        backgroud = findViewById(R.id.background);

        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        String[] urls2 = published.getImage().split(NS.SPLIT);

        for (String i : urls2) {
            urls.add(i);
        }

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
            }

        });

        mPager.setCurrentItem(pagerPosition);

        setTime();
        setText();
        setPraiseandComment();
    }

    @Override
    public void setTime() {
        time.setText(TimeUtil.getTimeShowString(published.getCreateTime(), false));
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
                        mPresenter.sendToFriend();
                        break;
                    case 1:
                        mPresenter.saveImage(urls.get(pagerPosition));
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
        intent.putExtra(IntentStatic.DATA, published_id);
        startActivity(intent);
    }

    @Override
    public void setText() {
        text.setText(TextUtils.isEmpty(published.getText()) ? "" : published.getText());
    }

    @Override
    public void setPraiseandComment() {
        int praise = published.getPraiseCount();
        int comment = published.getComments().size();
        if (praise == 0) {
            praisePeopleCount.setVisibility(View.GONE);
        } else {
            praisePeopleCount.setText(String.valueOf(praise));
        }
        if (comment == 0) {
            commentCount.setVisibility(View.GONE);
            commentImage.setPadding(0, 0, ScreenUtils.getAdapterPx(R.dimen.x32, this), 0);
        } else {
            commentCount.setText(String.valueOf(comment));
            commentImage.setPadding(0, 0, ScreenUtils.getAdapterPx(R.dimen.x12, this), 0);
        }
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
                mPresenter.deleteImage();
                dialog_center.close();
            }

            @Override
            public void onButton2() {
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
        this.mPresenter = presenter;
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

    @Override
    public void showLoadingDialog(String text) {

    }

    @Override
    public void hideLoadingDialog() {

    }

    @Override
    public void setonDismiss(LoadingDialog.onDismiss on) {

    }

    @Override
    public void showToast(String toast) {

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
