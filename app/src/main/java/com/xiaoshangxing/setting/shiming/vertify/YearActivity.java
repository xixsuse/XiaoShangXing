package com.xiaoshangxing.setting.shiming.vertify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tianyang on 2016/10/5.
 */
public class YearActivity extends BaseActivity {
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
    @Bind(R.id.list)
    ListView mListView;
    private String[] strings;
    private ArrayAdapter mAdapter;
    private String year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_year);
        ButterKnife.bind(this);
        title.setText("入学年份");
        next.setText("下一步");
        next.setEnabled(false);
        next.setTextColor(getResources().getColor(R.color.green1));
        next.setAlpha(0.5f);


        strings = getResources().getStringArray(R.array.Year);
        mAdapter = new ArrayAdapter<String>(this, R.layout.item_nodisturb, strings);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                year = strings[position];
                next.setEnabled(true);
                next.setAlpha(1);
            }
        });
    }

    @OnClick({R.id.back, R.id.right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right_text:
                XueLiActivity.yearStr = year;
                startActivity(new Intent(this, XueLiActivity.class));
                break;
        }
    }
}
