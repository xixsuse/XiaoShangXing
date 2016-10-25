package com.xiaoshangxing.setting.shiming.vertify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
public class ZhuanYeActivity extends BaseActivity {
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
    @Bind(R.id.edittext)
    EditText editText;
    @Bind(R.id.list)
    ListView mListView;
    private String[] strings;
    private ArrayAdapter mAdapter;
    private static String zhuanye; //学院

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_zhuanye);
        ButterKnife.bind(this);
        title.setText("专业");
        finish.setText("完成");
        finish.setTextColor(getResources().getColor(R.color.green1));
        finish.setAlpha(0.5f);
        finish.setEnabled(false);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

        strings = getResources().getStringArray(R.array.Year);
        mAdapter = new ArrayAdapter<String>(this, R.layout.item_nodisturb, strings);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                finish.setEnabled(true);
                finish.setAlpha(1);
                editText.setText(strings[position]);
            }
        });

    }

    public void Finish() {
        zhuanye = editText.getText().toString();
//        VertifyActivity.schoolStr = XueXiaoActivity.xuexiao;

        Intent intent = new Intent(this, VertifyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
//        startActivity(new Intent(this, VertifyActivity.class));
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
