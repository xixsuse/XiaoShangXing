package com.xiaoshangxing.wo.roll;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/7/12
 */
public class rollActivity extends BaseActivity {
    public static final String TYPE="TYPE";
    public static final int FORBIDDEN=1000;
    public static final int NOTICE=2000;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.listview)
    ListView listview;

    private List<String> list=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        for (int i=0;i<6;i++){
            list.add(""+i);
        }
        roll_listview_adpter adpter=new roll_listview_adpter(this,1,list);
        listview.setAdapter(adpter);

        int type=getIntent().getIntExtra("type",1000);
        if (type==NOTICE){
            title.setText("提醒谁看");
        }else {
            title.setText("不给谁看");
        }

    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
