package com.xiaoshangxing.xiaoshang.Calendar.CalendarInputer;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.LoadUtils;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;

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
    @Bind(R.id.anounce)
    ImageView anounce;
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
        headview = new View(this);
        listview.addHeaderView(headview);
        listview.setDividerHeight(1);
        anounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnRule(true);
            }
        });
        content.setText(getString(R.string.calender));

        LoadUtils.getCalendarInputer(this, realm, new LoadUtils.AroundLoading() {
            @Override
            public void before() {

            }

            @Override
            public void complete() {

            }

            @Override
            public void onSuccess() {
                List<User> users = realm.where(User.class).equalTo("isVip", "1").findAll();
                if (users.size() > 1) {
                    adpter = new CalendarInputer_Adpter(CalendarInputer.this, 1, users);
                    listview.setAdapter(adpter);
                }
            }

            @Override
            public void error() {

            }
        });


        title.setText("添加入口");
        more.setVisibility(View.GONE);
        backText.setText("返回");


    }

    public void clickOnRule(boolean is) {
        if (is) {
            rules.setVisibility(View.VISIBLE);
            rules.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_y_show));
        } else {
            rules.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_y_hide));
            rules.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rules.setVisibility(View.GONE);
                }
            }, 300);
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
