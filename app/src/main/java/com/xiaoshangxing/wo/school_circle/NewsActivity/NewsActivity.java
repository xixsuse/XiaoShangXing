package com.xiaoshangxing.wo.school_circle.NewsActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/7/11
 */
public class NewsActivity extends BaseActivity {

    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.myState)
    TextView myState;
    @Bind(R.id.empty)
    TextView empty;
    @Bind(R.id.title)
    RelativeLayout title;
    @Bind(R.id.listview)
    ListView listview;

    private newsAdapter adapter;
    private List<String> list=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        for (int i=0;i<=15;i++){
            list.add(""+i);
        }

        adapter=new newsAdapter(this,11,list);

        listview.setAdapter(adapter);
    }

    @OnClick({R.id.back, R.id.empty, R.id.title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.empty:
                DialogUtils.DialogMenu2 dialogMenu2=new DialogUtils.DialogMenu2(this);
                dialogMenu2.addMenuItem("清空所有消息");
                dialogMenu2.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
                    @Override
                    public void onItemSelected(int position, String item) {
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                dialogMenu2.initView();
                dialogMenu2.show();
                LocationUtil.bottom_FillWidth(this, dialogMenu2);
                break;
            case R.id.title:
                break;
        }
    }
}
