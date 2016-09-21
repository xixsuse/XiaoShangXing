package com.xiaoshangxing.setting.shiming.vertify;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.yujian.IM.kit.ListViewUtil;

/**
 * Created by tianyang on 2016/9/19.
 */
public class XingBieActivity extends BaseActivity {
    private ListView mListView;
    private String[] strings;
    private ArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_xingbie);
        mListView = (ListView) findViewById(R.id.list);
        strings = getResources().getStringArray(R.array.Sex);
        mAdapter = new ArrayAdapter<String>(this, R.layout.item_nodisturb, strings);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VertifyShiMingActivity.sexStr = strings[position];
            }
        });

    }

    public void Back(View view) {
        finish();
    }
}
