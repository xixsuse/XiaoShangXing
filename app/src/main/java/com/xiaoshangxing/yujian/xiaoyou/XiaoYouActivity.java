package com.xiaoshangxing.yujian.xiaoyou;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.xiaoshangxing.Network.InfoNetwork;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Schoolmate;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.yujian.Schoolfellow.ItemBean.BaseItemBean;
import com.xiaoshangxing.yujian.Schoolfellow.ItemBean.CollegeItem;
import com.xiaoshangxing.yujian.Schoolfellow.ItemBean.GradeItem;
import com.xiaoshangxing.yujian.Schoolfellow.ItemBean.MateItem;
import com.xiaoshangxing.yujian.Schoolfellow.ItemBean.ProfessionItem;
import com.xiaoshangxing.yujian.Schoolfellow.List.ShoolfellowAdapter;
import com.xiaoshangxing.yujian.Serch.SerchPerson.SerchPersonActivity;
import com.xiaoshangxing.yujian.personInfo.PersonInfoActivity;
import com.xiaoshangxing.yujian.xiaoyou.tree.Node;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;

/**
 * Created by 15828 on 2016/8/15.
 */
public class XiaoYouActivity extends BaseActivity implements IBaseView {

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
    @Bind(R.id.id_tree)
    ListView idTree;
    private List<Node> mDatas = new ArrayList<Node>();
    private List<Node> datas = new ArrayList<Node>();
    private ListView mTree;
    private SimpleTreeAdapter mAdapter;
    private ShoolfellowAdapter adapter;
    private View serch;

    private List<XiaoyouBean> data0 = new ArrayList<>();
    private List<XiaoyouBean> data1 = new ArrayList<>();
    private List<XiaoyouBean> data2 = new ArrayList<>();
    private List<XiaoyouBean> data3 = new ArrayList<>();

    private int size = 0, count = 0;

    private List<BaseItemBean> itemBeanList = new ArrayList<>();
    private View headview;

    private HashSet<CollegeItem> collegeItems = new HashSet<>();
    private HashSet<ProfessionItem> professionItems = new HashSet<>();
    private List<Schoolmate> schoolmates = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_xiaoyou);
        ButterKnife.bind(this);
        title.setText("校友");
        more.setVisibility(View.GONE);
        titleBottomLine.setVisibility(View.GONE);

        headview = View.inflate(this, R.layout.schoolfellow_list_head, null);
        serch = headview.findViewById(R.id.serch_layout);
        serch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(XiaoYouActivity.this, SerchPersonActivity.class);
                startActivity(intent);
            }
        });
        idTree.setAdapter(null);
        idTree.addHeaderView(headview);
//        initDatas();
//        mTree = (ListView) findViewById(R.id.id_tree);
//        try {
//            mAdapter = new SimpleTreeAdapter<Node>(mTree, this, mDatas, 10);
//            mTree.setAdapter(mAdapter);
//            mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
//                @Override
//                public void onClick(Node node, int position) {
//                    if (node.getLevel() == 3) {
//                        Toast.makeText(getApplicationContext(), node.getName1()
//                                + "    parent1  " + node.getParent().getName1() + "   parent2   " + node.getParent().getParent().getName1() + "   parent3   " + node.getParent().getParent().getParent().getName1(), Toast.LENGTH_SHORT).show();
//
//
//                        Intent intent = new Intent(XiaoYouActivity.this, PersonInfoActivity.class);
//                        startActivity(intent);
//                    }
//                    if (node.getLevel() == 2) {
//                        Toast.makeText(getApplicationContext(), node.getName1()
//                                + "    parent1  " + node.getParent().getName1() + "   parent2   " + node.getParent().getParent().getName1(), Toast.LENGTH_SHORT).show();
//
//                        if (data3 != null) {
//                            mAdapter.removeChildrens(position);
//                            mAdapter.addExtraNodes(position, data3);
//                        }
//                        Log.d("qqq", "id   " + node.getId());
//                    }
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        initListview();
        getCollege();
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

    private void initListview() {

        itemBeanList.addAll(collegeItems);
        adapter = new ShoolfellowAdapter(this, itemBeanList, idTree);
        idTree.setAdapter(adapter);
        adapter.setOnItemClick(new ShoolfellowAdapter.onItemClick() {
            @Override
            public void click(int position, BaseItemBean itemBean) {
                if (itemBean.isExpand()) {
                    adapter.collasp(position);
                    return;
                }
                adapter.removeChildrens(position);
                switch (itemBean.getType()) {
                    case BaseItemBean.COLLEGE:
                        getProfession(position, itemBean);
                        break;
                    case BaseItemBean.PROFESSION:
                        itemBean.getChildren().clear();
                        itemBean.getChildren().add(new GradeItem(itemBean.getId(), itemBean, GradeItem.GRADE_2013));
                        itemBean.getChildren().add(new GradeItem(itemBean.getId(), itemBean, GradeItem.GRADE_2014));
                        itemBean.getChildren().add(new GradeItem(itemBean.getId(), itemBean, GradeItem.GRADE_2015));
                        itemBean.getChildren().add(new GradeItem(itemBean.getId(), itemBean, GradeItem.GRADE_2016));
                        adapter.addData(position, itemBean);
                        break;
                    case BaseItemBean.GRADE:
                        getStudent(position, itemBean);
                        break;
                    case BaseItemBean.PERSON:
                        Intent intent = new Intent(XiaoYouActivity.this, PersonInfoActivity.class);
                        intent.putExtra(IntentStatic.EXTRA_ACCOUNT, itemBean.getId());
                        startActivity(intent);
                        break;
                }

            }
        });

    }

    private void getCollege() {
        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(e.string());
                    switch (jsonObject.getInt(NS.CODE)) {
                        case NS.CODE_200:
                            String[] temp = jsonObject.getString(NS.MSG).split(NS.SPLIT);
                            for (String i : temp) {
                                String[] temp2 = i.split(NS.SPLIT2);
                                collegeItems.add(new CollegeItem(temp2[0], temp2[1], null));
                            }
                            initListview();
                            break;
                        case 403:
                            showToast("无数据");
                            break;
                        default:
                            showToast(jsonObject.getString(NS.MSG));
                            break;
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };

        ProgressSubsciber<ResponseBody> subsciber = new ProgressSubsciber<>(onNext, this);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("schoolName", "江南大学");
        InfoNetwork.getInstance().getCollge(subsciber, jsonObject, this);
    }

    private void getProfession(final int position, final BaseItemBean collegeItem) {
        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(e.string());
                    switch (jsonObject.getInt(NS.CODE)) {
                        case NS.CODE_200:
                            collegeItem.getChildren().clear();
                            String[] temp = jsonObject.getString(NS.MSG).split(NS.SPLIT);
                            for (String i : temp) {
                                String[] temp2 = i.split(NS.SPLIT2);
                                collegeItem.getChildren().add(new ProfessionItem(temp2[0], temp2[1], collegeItem));
                            }
                            adapter.addData(position, collegeItem);
                            break;
                        case 403:
                            showToast("无数据");
                            break;
                        default:
                            showToast(jsonObject.getString(NS.MSG));
                            break;
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };

        ProgressSubsciber<ResponseBody> subsciber = new ProgressSubsciber<>(onNext, this);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("collegeId", collegeItem.getId());
        InfoNetwork.getInstance().getProfession(subsciber, jsonObject, this);
    }

    private void getStudent(final int position, final BaseItemBean grade) {
        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(e.string());
                    switch (jsonObject.getInt(NS.CODE)) {
                        case NS.CODE_200:
                            grade.getChildren().clear();
                            Gson gson = new Gson();
                            schoolmates = gson.fromJson(jsonObject.getJSONArray(NS.MSG).toString(), new TypeToken<List<Schoolmate>>() {
                            }.getType());
                            if (!schoolmates.isEmpty()) {
                                for (Schoolmate i : schoolmates) {
                                    grade.getChildren().add(new MateItem(i.getId(), grade, i.getUsername(), i.getUserImage(), i.getSignature()));
                                }
                            }
                            adapter.addData(position, grade);
                            break;
                        case 403:
                            showToast("无数据");
                            break;
                        default:
                            showToast(jsonObject.getString(NS.MSG));
                            break;
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };

        ProgressSubsciber<ResponseBody> subsciber = new ProgressSubsciber<>(onNext, this);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("professionName", grade.getParent().getShowName());
        jsonObject.addProperty("collegeName", grade.getParent().getParent().getShowName());
        jsonObject.addProperty("schoolName", "江南大学");
        jsonObject.addProperty("admissionYear", ((GradeItem) grade).getGrade());
        InfoNetwork.getInstance().GetSchoolmate(subsciber, jsonObject, this);
    }

    public void Back(View view) {
        finish();
    }

    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }
}
