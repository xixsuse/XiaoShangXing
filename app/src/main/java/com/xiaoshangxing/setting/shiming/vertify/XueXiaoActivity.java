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
import com.xiaoshangxing.input_activity.Location.NearCollege;
import com.xiaoshangxing.utils.BaseActivity;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by tianyang on 2016/10/5.
 */
public class XueXiaoActivity extends BaseActivity {
    private ListView mListView;
    private ArrayAdapter mAdapter;
    private TextView next, schoolText;
    public static String xuexiao; //学校
    private NearCollege nearCollege;
    private ArrayList<String> schools = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_xuexiao);

        mListView = (ListView) findViewById(R.id.list);
        next = (TextView) findViewById(R.id.next);
        schoolText = (TextView) findViewById(R.id.schoolTx);

        nearCollege = new NearCollege(this, new NearCollege.NearShoolCallback() {
            @Override
            public void Star() {
                schoolText.setText("定位中...");
            }

            @Override
            public void Erro() {
                schoolText.setText("定位失败");
            }

            @Override
            public void Success(HashSet<String> hashSet) {
                for (String i : hashSet) {
                    schools.add(i);
                }

                mAdapter = new ArrayAdapter<String>(XueXiaoActivity.this, R.layout.item_nodisturb, schools);
                mListView.setAdapter(mAdapter);
                if (schools.size() > 0) {
                    schoolText.setText(schools.get(0));
                }

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                schoolText.setText(schools.get(position));
            }
        });

        nearCollege.start();
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
