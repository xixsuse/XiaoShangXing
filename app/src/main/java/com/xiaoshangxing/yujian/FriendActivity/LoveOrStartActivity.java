package com.xiaoshangxing.yujian.FriendActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IntentStatic;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/9/5
 */
public class LoveOrStartActivity extends BaseActivity {
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.myState)
    TextView myState;
    @Bind(R.id.title)
    RelativeLayout title;
    @Bind(R.id.serch_layout)
    RelativeLayout serchLayout;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.count)
    TextView count;

    private love_satr_adpter adpter;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_or_start);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        type=getIntent().getIntExtra(IntentStatic.TYPE,love_satr_adpter.LOVE);
        List<String> list= new ArrayList<>();
        for (int i=0;i<5;i++){
            list.add("1");
        }
        adpter=new love_satr_adpter(this,1,list,type);
        listview.setAdapter(adpter);
        if (type==love_satr_adpter.LOVE){
            count.setText(adpter.getCount()+"个我留心的人");
        }else {
            count.setText(adpter.getCount()+"个我的星星们");
        }
    }

    @OnClick({R.id.back, R.id.serch_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.serch_layout:
                break;
        }
    }
}
