package com.xiaoshangxing.wo.Roll;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.bean.Published;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.wo.PersonalState.PersonalStateActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/7/12
 */
public class RollActivity extends BaseActivity {
    public static final String TYPE = "TYPE";
    public static final int FORBIDDEN = 1000;
    public static final int NOTICE = 2000;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.listview)
    ListView listview;
    private int current_type;
    private int publishedId;
    private Published published;

    private List<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        publishedId = getIntent().getIntExtra(IntentStatic.DATA, -1);
        if (publishedId == -1) {
            showToast("信息不明");
            finish();
            return;
        }

        published = realm.where(Published.class).equalTo(NS.ID, publishedId).findFirst();
        if (published == null) {
            showToast("没有找到该动态信息");
            finish();
            return;
        }

        String ids = published.getNotice();
        if (TextUtils.isEmpty(ids)) {
            return;
        } else {
            for (String id : ids.split(NS.SPLIT)) {
                list.add("" + id);
            }
        }

        roll_listview_adpter adpter = new roll_listview_adpter(this, 1, list);
        listview.setAdapter(adpter);

        initType();

    }

    private void initType() {
        current_type = getIntent().getIntExtra(TYPE, 0);
        switch (current_type) {
            case NOTICE:
                title.setText("提醒谁看");
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent state_intent = new Intent(RollActivity.this, PersonalStateActivity.class);
                        state_intent.putExtra(PersonalStateActivity.TYPE, PersonalStateActivity.OTHRE);
                        state_intent.putExtra(IntentStatic.EXTRA_ACCOUNT, list.get(position));
                        startActivity(state_intent);
                    }
                });
                break;
            case FORBIDDEN:
                title.setText("不给谁看");
                break;
            default:
                showToast("跳转错误");
                break;
        }
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
