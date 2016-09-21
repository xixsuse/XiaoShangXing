package com.xiaoshangxing.wo.myState;

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
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.layout.LayoutHelp;
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
public class myStateActivity extends BaseActivity implements StateContract.View {
    public static final String TYPE = "TYPE";
    public static final int SELF = 1000;
    public static final int OTHRE = 2000;
    @Bind(R.id.reflesh_layout)
    PtrFrameLayout ptrFrameLayout;
    private int current_type;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.more)
    ImageView newsList;
    @Bind(R.id.title)
    TextView title;
    private ListView listView;
    private RelativeLayout headView;
    private StateContract.Presenter mPresenter;
    private String account;
    private TextView name1;
    private TextView name2;
    private CirecleImage head;
    private Realm realm;
    private boolean is_refresh = false;           //记录是否正在刷新
    private boolean is_loadMore = false;          //记录是否正在加载更多
    private RealmResults<Published> publisheds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystate);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        setmPresenter(new StatePresenter(this, this));
        initView();
        typeOfState();
        refreshData();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listview);
        headView = (RelativeLayout) getLayoutInflater().inflate(R.layout.util_mystate_header, null);
        name1 = (TextView) headView.findViewById(R.id.name1);
        name2 = (TextView) headView.findViewById(R.id.name2);
        head = (CirecleImage) headView.findViewById(R.id.head_image);
        listView.addHeaderView(headView);
        headView.setEnabled(false);
        initFresh();
    }

    @Override
    public void refreshData() {
        publisheds = realm.where(Published.class)
                .equalTo(NS.USER_ID, Integer.valueOf(account))
                .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_STATE))
                .findAll().sort(NS.ID, Sort.DESCENDING);

        MystateAdpter mystateAdpter = new MystateAdpter(this, publisheds, realm);
        listView.setAdapter(mystateAdpter);
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
        Intent news_intent = new Intent(myStateActivity.this, NewsActivity.class);
        startActivity(news_intent);
    }

    @Override
    public void typeOfState() {
        if (!getIntent().hasExtra(IntentStatic.EXTRA_ACCOUNT)) {
            showToast("账号有误");
            finish();
        }
        account = getIntent().getStringExtra(IntentStatic.EXTRA_ACCOUNT);
        if (account == null) {
            showToast("账号有误");
            finish();
        }
        UserInfoCache.getInstance().getName(name1, Integer.valueOf(account));
        UserInfoCache.getInstance().getName(name2, Integer.valueOf(account));
        UserInfoCache.getInstance().getHead(head, Integer.valueOf(account), this);

        if (TempUser.isMine(account)) {
            title.setText("我的动态");
            newsList.setVisibility(View.VISIBLE);
        } else {
            title.setText("动态");
            newsList.setVisibility(View.GONE);
        }
    }

    public int getCurrent_type() {
        return current_type;
    }

    private void initFresh() {
        LayoutHelp.initPTR(ptrFrameLayout, LoadUtils.needRefresh(LoadUtils.TIME_LOAD_SELFSTATE), new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                LoadUtils.getPublished(realm, NS.CATEGORY_STATE, LoadUtils.TIME_LOAD_SELFSTATE, myStateActivity.this,true,
                        new LoadUtils.AroundLoading() {
                            @Override
                            public void before() {

                            }

                            @Override
                            public void complete() {
                                frame.refreshComplete();
                            }

                            @Override
                            public void onSuccess() {
                                refreshData();
                            }

                            @Override
                            public void error() {
                                frame.refreshComplete();
                            }
                        });
            }
        });
    }

    @Override
    protected void onDestroy() {
        realm.close();
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
