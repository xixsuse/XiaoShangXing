package com.xiaoshangxing.setting.shiming.vertify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;

/**
 * Created by tianyang on 2016/10/5.
 */
public class XueXiaoActivity extends BaseActivity {
    private ListView mListView;
    private String[] strings;
    private ArrayAdapter mAdapter;
    private TextView next, schoolText;
    public static String xuexiao; //学校

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_xuexiao);

        mListView = (ListView) findViewById(R.id.list);
        next = (TextView) findViewById(R.id.next);
        schoolText = (TextView) findViewById(R.id.schoolTx);

        strings = getResources().getStringArray(R.array.Year);
        mAdapter = new ArrayAdapter<String>(this, R.layout.item_nodisturb, strings);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                schoolText.setText(strings[position]);
            }
        });

    }

    public void Back(View view) {
        finish();
    }

    public void Next(View view) {
        xuexiao = schoolText.getText().toString();
        Log.d("qqq", "xuexiao   " + xuexiao);

        startActivity(new Intent(this, XueYuanActivity.class));

    }

    public void SearchView(View view) {
    }
}
