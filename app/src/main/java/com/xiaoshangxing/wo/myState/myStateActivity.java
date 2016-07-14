package com.xiaoshangxing.wo.myState;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.wo.myState.check_photo.myStateImagePagerActivity;
import com.xiaoshangxing.wo.school_circle.NewsActivity.NewsActivity;
import com.xiaoshangxing.wo.school_circle.check_photo.ImagePagerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/7/9
 */
public class myStateActivity extends BaseActivity {
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.news_list)
    ImageView newsList;
    private ListView listView;
    private RelativeLayout headView;
    private List<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystate);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listview);
        headView = (RelativeLayout) getLayoutInflater().inflate(R.layout.util_mystate_header, null);
        listView.addHeaderView(headView);
        headView.setEnabled(false);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"});

        for (int i = 0; i < 12; i++) {
            list.add("hh");
        }

        String[] urls2 = {"http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg",
                "http://img.my.csdn.net/uploads/201410/19/1413698867_8323.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383290_1042.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383275_3977.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383264_3954.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383264_4787.jpg"
        };

        final ArrayList<String> imageUrls = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            imageUrls.add(urls2[i]);
        }

        Mystate_adpter mystate_adpter = new Mystate_adpter(this, 1, list);
        listView.setAdapter(mystate_adpter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    Intent intent = new Intent(myStateActivity.this, myStateImagePagerActivity.class);
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageUrls);
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                    startActivity(intent);
                }
            }
        });
    }

    @OnClick({R.id.back, R.id.news_list})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.news_list:
                Intent news_intent=new Intent(myStateActivity.this, NewsActivity.class);
                startActivity(news_intent);
                break;
        }
    }
}
