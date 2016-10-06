package com.xiaoshangxing.setting.shiming.vertify;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    private TextView finish;
    private String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_xingbie);
        mListView = (ListView) findViewById(R.id.list);
        finish = (TextView) findViewById(R.id.finish);

        strings = getResources().getStringArray(R.array.Sex);
        mAdapter = new ArrayAdapter<String>(this, R.layout.item_nodisturb, strings);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sex = strings[position];
                finish.setEnabled(true);
                finish.setAlpha(1);
            }
        });

    }

    public void Back(View view) {
        finish();
    }

    public void Finish(View view) {
        VertifyActivity.sexStr = sex;
        finish();
    }
}
