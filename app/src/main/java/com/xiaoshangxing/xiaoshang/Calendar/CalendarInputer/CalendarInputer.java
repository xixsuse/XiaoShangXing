package com.xiaoshangxing.xiaoshang.Calendar.CalendarInputer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.xiaoshang.Calendar.CalendarInput;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/10/6
 */

public class CalendarInputer extends BaseActivity {
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.line)
    View line;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.reflesh_layout)
    PtrFrameLayout refleshLayout;
    @Bind(R.id.mengban)
    View mengban;
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.collasp)
    LinearLayout collasp;
    @Bind(R.id.rules)
    RelativeLayout rules;
    @Bind(R.id.back_text)
    TextView backText;
    private View headview;

    private CalendarInputer_Adpter adpter;
    private List<Published> publisheds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_sale);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        headview = View.inflate(this, R.layout.headview_help_list, null);
        listview.addHeaderView(headview);
        listview.setDividerHeight(1);
        headview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnRule(true);
            }
        });
        content.setText(getString(R.string.calender));
        for (int i = 0; i < 10; i++) {
            publisheds.add(new Published());
        }
        adpter = new CalendarInputer_Adpter(this, 1, publisheds);
        listview.setAdapter(adpter);

        title.setText("添加入口");
        more.setVisibility(View.GONE);
        backText.setText("返回");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CalendarInputer.this, CalendarInput.class);
                startActivity(intent);
            }
        });

    }

    public void clickOnRule(boolean is) {
        if (is) {
            rules.setVisibility(View.VISIBLE);
            rules.startAnimation(AnimationUtils.loadAnimation(this, R.anim.verify_success_top));
        } else {
            rules.startAnimation(AnimationUtils.loadAnimation(this, R.anim.verify_success_top1));
            rules.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rules.setVisibility(View.GONE);
                }
            }, 800);
        }

    }

    @OnClick({R.id.back, R.id.mengban, R.id.collasp})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.mengban:
                clickOnRule(false);
                break;
            case R.id.collasp:
                clickOnRule(false);
                break;
        }
    }
}
