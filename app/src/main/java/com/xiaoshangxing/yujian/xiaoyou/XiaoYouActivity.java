package com.xiaoshangxing.yujian.xiaoyou;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.yujian.Serch.SerchPerson.SerchPersonActivity;
import com.xiaoshangxing.yujian.personInfo.PersonInfoActivity;
import com.xiaoshangxing.yujian.xiaoyou.tree.Node;
import com.xiaoshangxing.yujian.xiaoyou.tree.TreeListViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/8/15.
 */
public class XiaoYouActivity extends BaseActivity {
    @Bind(R.id.xiaoyou_leftarrow)
    ImageView xiaoyouLeftarrow;
    @Bind(R.id.xiaoyou_back)
    TextView xiaoyouBack;
    @Bind(R.id.serch)
    RelativeLayout serch;
    @Bind(R.id.id_tree)
    ListView idTree;
    private List<Node> mDatas = new ArrayList<Node>();
    private List<Node> datas = new ArrayList<Node>();
    private ListView mTree;
    private SimpleTreeAdapter mAdapter;

    private List<XiaoyouBean> data0 = new ArrayList<>();
    private List<XiaoyouBean> data1 = new ArrayList<>();
    private List<XiaoyouBean> data2 = new ArrayList<>();
    private List<XiaoyouBean> data3 = new ArrayList<>();

    private int size = 0, count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_xiaoyou);
        ButterKnife.bind(this);
        initDatas();
        mTree = (ListView) findViewById(R.id.id_tree);
        try {
            mAdapter = new SimpleTreeAdapter<Node>(mTree, this, mDatas, 10);
            mTree.setAdapter(mAdapter);
            mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(Node node, int position) {
                    if (node.getLevel() == 3) {
                        Toast.makeText(getApplicationContext(), node.getName1()
                                + "    parent1  " + node.getParent().getName1() + "   parent2   " + node.getParent().getParent().getName1() + "   parent3   " + node.getParent().getParent().getParent().getName1(), Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(XiaoYouActivity.this, PersonInfoActivity.class);
                        startActivity(intent);
                    }
                    if (node.getLevel() == 2) {
                        Toast.makeText(getApplicationContext(), node.getName1()
                                + "    parent1  " + node.getParent().getName1() + "   parent2   " + node.getParent().getParent().getName1(), Toast.LENGTH_SHORT).show();

                        if (data3 != null) {
                            mAdapter.removeChildrens(position);
                            mAdapter.addExtraNodes(position, data3);
                        }
                        Log.d("qqq", "id   " + node.getId());
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initDatas() {
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.cirecleiamge_default);
//        mDatas2.add(new TreeBean(1, 0, "文件管理系统","2333",bitmap));
//        mDatas2.add(new TreeBean(2, 1, "游戏","2333",bitmap));
//        mDatas2.add(new TreeBean(3, 1, "文档","2333",bitmap));
//        mDatas2.add(new TreeBean(4, 1, "程序","2333",bitmap));
//        mDatas2.add(new TreeBean(5, 2, "war3","2333",bitmap));
//        mDatas2.add(new TreeBean(6, 2, "刀塔传奇","2333",bitmap));
//        mDatas2.add(new TreeBean(7, 4, "面向对象","2333",bitmap));
//        mDatas2.add(new TreeBean(8, 4, "非面向对象","2333",bitmap));
//        mDatas2.add(new TreeBean(9, 7, "C++","2333",bitmap));
//        mDatas2.add(new TreeBean(10, 7, "JAVA","2333",bitmap));
//        mDatas2.add(new TreeBean(11, 7, "Javascript","2333",bitmap));
//        mDatas2.add(new TreeBean(12, 8, "C语言","2333",bitmap));
//        for(int i=0;i<100;i++){
//            mDatas2.add(new TreeBean(i+13, 6, "C语言","2333",bitmap));
//        }
//        for(int i=0;i<100;i++){
//            mDatas2.add(new TreeBean(i+113, 5, "C语言","2333",bitmap));
//        }


        //模拟数据
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.cirecleimage_default);
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.img_sheji);
        data0.add(new XiaoyouBean("至善学院", "宁静致远，至于至善", bitmap1));
        data0.add(new XiaoyouBean("食品学院", "勤业，创新，争先，共赢", bitmap1));
        data0.add(new XiaoyouBean("物联网工程学院", "心心相连，爱我物联", bitmap1));
        data0.add(new XiaoyouBean("机械工程学院", "创新能力佳，工程实践技能强", bitmap1));
        data0.add(new XiaoyouBean("化学与材料工程学院", "爱设计，爱设计学院", bitmap1));
        data0.add(new XiaoyouBean("理学院", "爱设计，爱设计学院", bitmap1));
        data0.add(new XiaoyouBean("商学院", "爱设计，爱设计学院", bitmap1));
        data0.add(new XiaoyouBean("法学院", "爱设计，爱设计学院", bitmap1));

        data1.add(new XiaoyouBean("视觉传达"));
        data1.add(new XiaoyouBean("环境艺术"));
        data1.add(new XiaoyouBean("公共艺术"));
        data1.add(new XiaoyouBean("美术学"));
        data1.add(new XiaoyouBean("广告学"));

        data2.add(new XiaoyouBean("2015级"));
        data2.add(new XiaoyouBean("2014级"));
        data2.add(new XiaoyouBean("2013级"));
        data2.add(new XiaoyouBean("2012级"));


        for (int i = 0; i < 20; i++) {
            data3.add(new XiaoyouBean("校友姓名" + i, "个性签名个性签名个性签名个性签名个性签名个性签名", bitmap));
        }

        for (int i = 0; i < data0.size(); i++) {
            mDatas.add(new Node(i + 1, 0, data0.get(i).getName1(), data0.get(i).getName2(), data0.get(i).getBitmap()));
        }
        size += data0.size();

        for (int i = 0; i < data0.size(); i++) {
            for (int j = 0; j < data1.size(); j++) {
                mDatas.add(new Node(size + data1.size() * i + j + 1, count + i + 1, data1.get(j).getName1()));
            }
        }

        size += data0.size() * data1.size();
        count += data0.size();

        for (int i = 0; i < data0.size() * data1.size(); i++) {
            for (int j = 0; j < data2.size(); j++) {
                mDatas.add(new Node(size + data2.size() * i + j + 1, count + i + 1, data2.get(j).getName1()));
            }
        }
        size += data0.size() * data1.size() * data2.size();
        count += data0.size() * data1.size();


//        for (int i = 0; i < data0.size() * data1.size() * data2.size(); i++) {
//            for (int j = 0; j < data3.size(); j++) {
//                mDatas.add(new Node(1000, count + i + 1, data3.get(j).getName1(), data3.get(j).getName2(), data3.get(j).getBitmap()));
//            }
//        }
//        size += data0.size() * data1.size() * data2.size() * data3.size();


    }

    public void Back(View view) {
        finish();
    }

    @OnClick(R.id.serch)
    public void onClick() {
        Intent intent = new Intent(XiaoYouActivity.this, SerchPersonActivity.class);
        startActivity(intent);
    }
}
