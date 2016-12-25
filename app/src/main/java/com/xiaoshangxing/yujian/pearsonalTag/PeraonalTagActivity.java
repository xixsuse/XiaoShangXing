package com.xiaoshangxing.yujian.pearsonalTag;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.customView.TagView.Tag;
import com.xiaoshangxing.utils.customView.TagView.TagListView;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 *modified by FengChaoQun on 2016/12/24 19:21
 * description:优化代码
 */
public class PeraonalTagActivity extends BaseActivity {
    private final List<Tag> mTags = new ArrayList<Tag>();
    private TagListView mTagListView;
    private String[] tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiity_yujian_personaltag);
        mTagListView = (TagListView) findViewById(R.id.tagview);

        String tagString = getIntent().getStringExtra(IntentStatic.DATA);
        if (TextUtils.isEmpty(tagString)) {
            return;
        } else {
            tags = tagString.split(NS.SPLIT);
        }

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
