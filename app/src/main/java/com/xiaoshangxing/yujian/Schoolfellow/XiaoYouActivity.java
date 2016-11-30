package com.xiaoshangxing.yujian.Schoolfellow;

import android.content.Intent;
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
    private ShoolfellowAdapter adapter;
    private View serch;

    private List<BaseItemBean> itemBeanList = new ArrayList<>();
    private View headview;

    private HashSet<CollegeItem> collegeItems = new HashSet<>();
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
        getCollege();
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
