package com.xiaoshangxing.wo.setting.realName.vertify;

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
import com.xiaoshangxing.utils.baseClass.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *modified by FengChaoQun on 2016/12/24 16:29
 * description:优化代码
 */
public class EducationBackgroundActivity extends BaseActivity {
    public static String yearStr, xueli;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_xueli);
        ButterKnife.bind(this);
        title.setText("学历");
        finish.setText("完成");
        finish.setTextColor(getResources().getColor(R.color.green1));
        finish.setAlpha(0.5f);
        finish.setEnabled(false);

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

    public void Finish() {
        VertifyActivity.ruxuenianfenStr = yearStr;
        VertifyActivity.degree = xueli;

        Intent intent = new Intent(this, VertifyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @OnClick({R.id.back, R.id.right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right_text:
                Finish();
                break;
        }
    }
}
