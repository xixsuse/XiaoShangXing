package com.xiaoshangxing.xiaoshang.Calendar.CalendarInputer;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.bean.User;
import com.xiaoshangxing.network.netUtil.LoadUtils;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.utils.customView.RuleUtil;
import com.xiaoshangxing.utils.customView.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/10/6
 */

public class CalendarInputer extends BaseActivity {
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.reflesh_layout)
    PtrFrameLayout refleshLayout;
    @Bind(R.id.no_content)
    TextView noContent;
    @Bind(R.id.mengban)
    View mengban;
    @Bind(R.id.rule_image)
    ImageView ruleImage;
    @Bind(R.id.rule_button)
    ImageView ruleButton;
    @Bind(R.id.wrap_view)
    RelativeLayout wrapView;
    @Bind(R.id.rules)
    RelativeLayout rules;
    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    private View headview;
    private View rootView;
    private RuleUtil ruleUtil;

    private CalendarInputer_Adpter adpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = View.inflate(this, R.layout.title_anounce_refresh_listview, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        headview = new View(this);
        listview.addHeaderView(headview);
        listview.setDividerHeight(1);
        ruleImage.setImageResource(R.mipmap.gonggao_xl);
        title.setText("添加入口");
        more.setVisibility(View.GONE);
        ruleUtil = new RuleUtil(rootView, this);
        listview.setPadding(0, ScreenUtils.getAdapterPx(R.dimen.y96, this), 0, 0);
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
                if (!users.isEmpty()) {
                    adpter = new CalendarInputer_Adpter(CalendarInputer.this, 1, users);
                    listview.setAdapter(adpter);
                }
            }

            @Override
            public void error() {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (ruleUtil.needhideRules()) {
                return true;
            }
            return super.onKeyDown(keyCode, event);
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}
