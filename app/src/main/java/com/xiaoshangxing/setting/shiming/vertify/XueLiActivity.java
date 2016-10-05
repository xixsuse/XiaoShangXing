package com.xiaoshangxing.setting.shiming.vertify;

import android.content.Intent;
import android.os.Bundle;
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
public class XueLiActivity extends BaseActivity {
    private ListView mListView;
    private String[] strings;
    private ArrayAdapter mAdapter;
    private TextView finish;
    public static String yearStr, xueli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_xueli);

        mListView = (ListView) findViewById(R.id.list);
        finish = (TextView) findViewById(R.id.finish);

        strings = getResources().getStringArray(R.array.XueLi);
        mAdapter = new ArrayAdapter<String>(this, R.layout.item_nodisturb, strings);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                xueli = strings[position];
                finish.setEnabled(true);
                finish.setAlpha(1);
            }
        });

    }


    public void Back(View view) {
        finish();
    }

    public void Finish(View view) {
        VertifyActivity.ruxuenianfenStr = yearStr;
        startActivity(new Intent(this, VertifyActivity.class));
    }
}
