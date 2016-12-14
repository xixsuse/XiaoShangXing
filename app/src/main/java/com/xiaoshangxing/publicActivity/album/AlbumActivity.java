package com.xiaoshangxing.publicActivity.album;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;

import com.xiaoshangxing.R;
import com.xiaoshangxing.publicActivity.inputActivity.InputActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.utils.baseClass.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/8/8
 */
public class AlbumActivity extends BaseActivity {
    public static final String LIMIT = "LIMIT";
    public static final String SELECTED = "SELECTED";
    public static final int SELECT_PHOTO_FROM_ALBUM = 20000;
    @Bind(R.id.main_fragment)
    FrameLayout mainFragment;
    private AlbumListFragment albumListFragment;
    private ImageBucket current_imagebucket;
    private int limit;
    private List<String> select_image_urls = new ArrayList<String>();
    private int resultCode = 0;
    private boolean isSelectPicturesOK;
    private boolean isOrig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_fraglayout);
        ButterKnife.bind(this);
        BaseFragment frag = (BaseFragment) mFragmentManager.findFragmentById(R.id.main_fragment);
        if (frag != null) {
            return;
        }
        initAllFragments();
        initData();
    }

    private void initData() {
        setLimit(getIntent().getIntExtra(LIMIT, 9));
        setSelect_image_urls(getIntent().getStringArrayListExtra(SELECTED));
    }


    private void initAllFragments() {
        Fragment frag;

        frag = mFragmentManager.findFragmentByTag(AlbumListFragment.TAG);
        albumListFragment = (frag == null) ? AlbumListFragment.newInstance() : (AlbumListFragment) frag;

        frag = getAlbumListFragment();
        mFragmentManager.beginTransaction().add(R.id.main_fragment,
                frag, AlbumListFragment.TAG).commit();

    }

    public AlbumListFragment getAlbumListFragment() {
        return albumListFragment;
    }

    public ImageBucket getCurrent_imagebucket() {
        return current_imagebucket;
    }

    public void setCurrent_imagebucket(ImageBucket current_imagebucket) {
        this.current_imagebucket = current_imagebucket;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<String> getSelect_image_urls() {
        return select_image_urls;
    }

    public void setSelect_image_urls(List<String> select_image_urls) {
        this.select_image_urls = select_image_urls;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == InputActivity.SELECT_PHOTO_ONE_BY_ONE) {
            setSelect_image_urls(data.getStringArrayListExtra(InputActivity.SELECT_IMAGE_URLS));
            isOrig = data.getBooleanExtra(IntentStatic.IS_ORIG, false);
        }
    }

    public boolean isSelectPicturesOK() {
        return isSelectPicturesOK;
    }

    public void setSelectPicturesOK(boolean selectPicturesOK) {
        isSelectPicturesOK = selectPicturesOK;
    }

    public boolean isOrig() {
        return isOrig;
    }

    public void setOrig(boolean orig) {
        isOrig = orig;
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(InputActivity.SELECT_IMAGE_URLS, (ArrayList<String>) select_image_urls);
        intent.putExtra(IntentStatic.IS_ORIG, isOrig);
        setResult(isSelectPicturesOK ? RESULT_OK : RESULT_CANCELED, intent);
        super.finish();
    }

}
