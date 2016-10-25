package com.xiaoshangxing.setting.shiming.vertify;

import android.os.Bundle;
import android.text.TextUtils;
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
 * Created by tianyang on 2016/9/19.
 */
public class XingBieActivity extends BaseActivity {
    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_text)
    TextView finish;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.list)
    ListView mListView;

    private String[] strings;
    private ArrayAdapter mAdapter;
    private String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_xingbie);
        ButterKnife.bind(this);
        title.setText("性别");
        finish.setText("完成");
        finish.setTextColor(getResources().getColor(R.color.green1));
        finish.setAlpha(0.5f);
        finish.setEnabled(false);

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

    @OnClick({R.id.back, R.id.right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right_text:
                if (TextUtils.isEmpty(sex)) {
                    showToast("请选择性别");
                    return;
                }
                VertifyActivity.sexStr = sex;
                finish();
                break;
        }
    }
}
