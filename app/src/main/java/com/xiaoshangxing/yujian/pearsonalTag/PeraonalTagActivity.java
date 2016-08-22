package com.xiaoshangxing.yujian.pearsonalTag;

import android.os.Bundle;
import android.view.View;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.utils.TagView.Tag;
import com.xiaoshangxing.setting.utils.TagView.TagListView;
import com.xiaoshangxing.utils.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianyang on 2016/8/22.
 */
public class PeraonalTagActivity extends BaseActivity {
    private TagListView mTagListView;
    private final List<Tag> mTags = new ArrayList<Tag>();
    private String[] tags = {"摩羯座", "强迫症", "啊宅", "少女"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiity_yujian_personaltag);
        mTagListView = (TagListView) findViewById(R.id.tagview);

        for (String tag1 : tags) {
            Tag tag = new Tag();
            tag.setId(0);
            tag.setChecked(true);
            tag.setTitle(tag1);
            mTags.add(tag);
        }
        assert mTagListView != null;
        mTagListView.setTags(mTags);

    }

    public void Back(View view) {
        finish();
    }
}
