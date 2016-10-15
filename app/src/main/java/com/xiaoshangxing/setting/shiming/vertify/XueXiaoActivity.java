package com.xiaoshangxing.setting.shiming.vertify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaoshangxing.MainActivity;
import com.xiaoshangxing.R;
import com.xiaoshangxing.input_activity.Location.NearCollege;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IntentStatic;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by tianyang on 2016/10/5.
 */
public class XueXiaoActivity extends BaseActivity {
    private ListView mListView;
    private ArrayAdapter mAdapter;
    private TextView next, schoolText, applySchool;
    public static String xuexiao; //学校
    private NearCollege nearCollege;
    private ArrayList<String> schools = new ArrayList<>();
    private boolean isRegister;//标记是否是注册时选择学校
    private ImageView refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_xuexiao);

        mListView = (ListView) findViewById(R.id.list);
        next = (TextView) findViewById(R.id.next);
        schoolText = (TextView) findViewById(R.id.schoolTx);
        applySchool = (TextView) findViewById(R.id.aplly_shcool);
        refresh = (ImageView) findViewById(R.id.refresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nearCollege.start();
            }
        });

        if (getIntent().getIntExtra(IntentStatic.TYPE, -1) == IntentStatic.REGISTER) {
            isRegister = true;
        }

        applySchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(XueXiaoActivity.this, ApplyForSchool.class);
                startActivity(intent);
            }
        });

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
                next.setClickable(true);
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
        if (isRegister) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            startActivity(new Intent(this, XueYuanActivity.class));
            VertifyActivity.schoolStr = schoolText.getText().toString();
        }

    }

    public void SearchView(View view) {
        showToast("暂未开通");
    }
}
