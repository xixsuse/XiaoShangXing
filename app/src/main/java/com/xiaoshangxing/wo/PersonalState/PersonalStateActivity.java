package com.xiaoshangxing.wo.PersonalState;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.LoadUtils;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.input_activity.InputActivity;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.layout.LayoutHelp;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.utils.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.wo.NewsActivity.NewsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by FengChaoQun
 * on 2016/7/9
 */
public class PersonalStateActivity extends BaseActivity implements StateContract.View {
    public static final String TYPE = "TYPE";
    public static final int SELF = 1000;
    public static final int OTHRE = 2000;
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
    @Bind(R.id.listview)
    ListView listView;
    @Bind(R.id.reflesh_layout)
    PtrFrameLayout ptrFrameLayout;
    @Bind(R.id.no_content)
    TextView noContent;
    private int current_type;
    private RelativeLayout headView;
    private View headView2, footview;
    private StateContract.Presenter mPresenter;
    private String account;
    private TextView name;
    private CirecleImage head;
    private boolean is_refresh = false;           //记录是否正在刷新
    private boolean is_loadMore = false;          //记录是否正在加载更多
    private RealmResults<Published> publisheds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystate);
        ButterKnife.bind(this);
        setmPresenter(new StatePresenter(this, this));
        initView();
        typeOfState();
        refreshData();
        initFresh();
    }

    private void initView() {
        back.setPadding(ScreenUtils.getAdapterPx(R.dimen.x18, this), 0, 0, 0);
        more.setImageResource(R.mipmap.bell);
        more.setPadding(0, 0, ScreenUtils.getAdapterPx(R.dimen.x18, this), 0);
        titleBottomLine.setVisibility(View.GONE);

        listView.setDividerHeight(0);
        headView = (RelativeLayout) getLayoutInflater().inflate(R.layout.util_mystate_header, null);
        name = (TextView) headView.findViewById(R.id.name);
        head = (CirecleImage) headView.findViewById(R.id.head_image);
        listView.addHeaderView(headView);
        headView2 = View.inflate(this, R.layout.publish_lay, null);
        footview = View.inflate(this, R.layout.footer, null);
        listView.addFooterView(footview);
        headView.setEnabled(false);
        listView.setHeaderDividersEnabled(false);
        headView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent publish_intent = new Intent(PersonalStateActivity.this, InputActivity.class);
                publish_intent.putExtra(InputActivity.LIMIT, 9);
                publish_intent.putExtra(InputActivity.EDIT_STATE, InputActivity.PUBLISH_STATE);
                startActivity(publish_intent);
            }
        });
    }

    @Override
    public void refreshData() {
        publisheds = realm.where(Published.class)
                .equalTo(NS.USER_ID, Integer.valueOf(account))
                .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_STATE))
                .findAllSorted(NS.CREATETIME, Sort.DESCENDING);

        MystateAdpter mystateAdpter = new MystateAdpter(this, publisheds, realm);
        listView.setAdapter(mystateAdpter);
        if (publisheds.size() > 0) {
            noContent.setVisibility(View.GONE);
        } else {
            noContent.setVisibility(View.VISIBLE);
            noContent.setText("还没有发布动态");
        }
    }

    @Override
    public void setRefreshState(boolean is) {
        this.is_refresh = is;
    }

    @Override
    public void setLoadState(boolean is) {
        this.is_loadMore = is;
    }

    @Override
    public Realm getRealm() {
        return realm;
    }

    @Override
    public void setmPresenter(@Nullable StateContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void gotoNews() {
//        showToast(NS.ON_DEVELOPING);
        Intent news_intent = new Intent(PersonalStateActivity.this, NewsActivity.class);
        startActivity(news_intent);
    }

    @Override
    public void typeOfState() {
        if (!getIntent().hasExtra(IntentStatic.EXTRA_ACCOUNT)) {
            showToast("账号有误");
            finish();
            return;
        }
        account = getIntent().getStringExtra(IntentStatic.EXTRA_ACCOUNT);
        if (account == null) {
            showToast("账号有误");
            finish();
            return;
        }
        head.setIntent_type(CirecleImage.PERSON_INFO, account);

        UserInfoCache.getInstance().getHeadIntoImage(account, head);
        UserInfoCache.getInstance().getExIntoTextview(account, NS.USER_NAME, name);

        if (TempUser.isMine(account)) {
            title.setText("我的动态");
            more.setVisibility(View.VISIBLE);
        } else {
            title.setText("动态");
            more.setVisibility(View.GONE);
        }

        if (TempUser.isMine(account)) {
            listView.addHeaderView(headView2);
        }
    }

    public int getCurrent_type() {
        return current_type;
    }

    private void initFresh() {

        final LoadUtils.AroundLoading aroundLoading = new LoadUtils.AroundLoading() {
            @Override
            public void before() {
            }

            @Override
            public void complete() {
                ptrFrameLayout.refreshComplete();
            }

            @Override
            public void onSuccess() {
                refreshData();
            }

            @Override
            public void error() {
                ptrFrameLayout.refreshComplete();
            }
        };

        if (TempUser.isMine(account)) {
            LayoutHelp.initPTR(ptrFrameLayout, LoadUtils.needRefresh(LoadUtils.TIME_LOAD_SELFSTATE), new PtrDefaultHandler() {
                @Override
                public void onRefreshBegin(final PtrFrameLayout frame) {
                    LoadUtils.getPublished(realm, NS.CATEGORY_STATE, LoadUtils.TIME_LOAD_SELFSTATE, PersonalStateActivity.this, true,
                            aroundLoading);
                }
            });
        } else {
            LayoutHelp.initPTR(ptrFrameLayout, true, new PtrDefaultHandler() {
                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    LoadUtils.getPersonalPublished(realm, NS.CATEGORY_STATE, Integer.valueOf(account), PersonalStateActivity.this,
                            aroundLoading);
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.back, R.id.more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.more:
                gotoNews();
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
