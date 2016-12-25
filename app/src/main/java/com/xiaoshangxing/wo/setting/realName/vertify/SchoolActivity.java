package com.xiaoshangxing.wo.setting.realName.vertify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.MainActivity;
import com.xiaoshangxing.R;
import com.xiaoshangxing.publicActivity.Location.NearCollege;
import com.xiaoshangxing.wo.setting.realName.chooseschool.ApplyForSchool;
import com.xiaoshangxing.wo.setting.realName.chooseschool.SerchSchoolActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseActivity;

import java.util.ArrayList;
import java.util.HashSet;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *modified by FengChaoQun on 2016/12/24 16:30
 * description:优化代码
 */
public class SchoolActivity extends BaseActivity {
    public static String xuexiao; //学校
    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_text)
    TextView next;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.view1)
    CardView view1;
    @Bind(R.id.imgLc)
    ImageView imgLc;
    @Bind(R.id.schoolTx)
    TextView schoolTx;
    @Bind(R.id.refresh)
    ImageView refresh;
    @Bind(R.id.list)
    ListView list;
    @Bind(R.id.aplly_shcool)
    TextView apllyShcool;
    private ListView mListView;
    private ArrayAdapter mAdapter;
    private TextView schoolText, applySchool;
    private NearCollege nearCollege;
    private ArrayList<String> schools = new ArrayList<>();
    private boolean isRegister;//标记是否是注册时选择学校

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_xuexiao);
        ButterKnife.bind(this);
        title.setText("学校");
        next.setText("下一步");
        next.setTextColor(getResources().getColor(R.color.green1));
        next.setAlpha(0.5f);
        next.setEnabled(false);

        mListView = (ListView) findViewById(R.id.list);
        schoolText = (TextView) findViewById(R.id.schoolTx);
        applySchool = (TextView) findViewById(R.id.aplly_shcool);

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
                Intent intent = new Intent(SchoolActivity.this, ApplyForSchool.class);
                startActivity(intent);
            }
        });

        nearCollege = new NearCollege(this, new NearCollege.NearShoolCallback() {
            @Override
            public void Star() {
                showLoadingDialog("定位中...");
                schoolText.setText("定位中...");
            }

            @Override
            public void Erro() {
                schoolText.setText("定位失败");
                hideLoadingDialog();
            }

            @Override
            public void Success(HashSet<String> hashSet) {
                hideLoadingDialog();
                next.setClickable(true);
                for (String i : hashSet) {
                    schools.add(i);
                }

                mAdapter = new ArrayAdapter<String>(SchoolActivity.this, R.layout.item_nodisturb, schools);
                mListView.setAdapter(mAdapter);
                if (schools.size() > 0) {
                    schoolText.setText(schools.get(0));
                    if (VertifyActivity.schoolStr != null) {
                        schoolText.setText(VertifyActivity.schoolStr);
                    }
                }
                next.setAlpha(1f);
                next.setEnabled(true);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                schoolText.setText(schools.get(position));
            }
        });

        nearCollege.start();

        if (VertifyActivity.schoolStr != null) {
            schoolText.setText(VertifyActivity.schoolStr);
        }
    }


    public void Next() {
        if (isRegister) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, CollegeActivity.class));
            VertifyActivity.schoolStr = schoolText.getText().toString();
        }
    }

    public void SearchView(View view) {
        Intent intent = new Intent(this, SerchSchoolActivity.class);
        intent.putExtra(IntentStatic.TYPE, isRegister);
        startActivity(intent);
    }

    @OnClick({R.id.back, R.id.right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right_text:
                Next();
                break;
        }
    }
}
