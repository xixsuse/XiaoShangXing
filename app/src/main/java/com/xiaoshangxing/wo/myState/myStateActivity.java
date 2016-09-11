package com.xiaoshangxing.wo.myState;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.Network.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.UserCache;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.utils.pull_refresh.PtrHandler;
import com.xiaoshangxing.utils.pull_refresh.StoreHouseHeader;
import com.xiaoshangxing.wo.NewsActivity.NewsActivity;
import com.xiaoshangxing.wo.WoFrafment.check_photo.ImagePagerActivity;
import com.xiaoshangxing.wo.myState.check_photo.myStateImagePagerActivity;

import java.util.ArrayList;
import java.util.List;

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
    private List<String> list = new ArrayList<String>();
    private StateContract.Presenter mPresenter;
    private String account;
    private TextView name1;
    private TextView name2;
    private CirecleImage head;
    private Realm realm;
    private boolean is_refresh = false;           //记录是否正在刷新
    private boolean is_loadMore = false;          //记录是否正在加载更多

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

        for (int i = 0; i < 12; i++) {
            list.add("hh");
        }

        String[] urls2 = {
                "http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg",
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

        Mystate_adpter mystate_adpter = new Mystate_adpter(this, 1, list, this);

        RealmResults<Published> publisheds = realm.where(Published.class).
                equalTo(NS.USER_ID, TempUser.id).findAll().sort("id", Sort.DESCENDING);
        MystateAdpter mystateAdpter = new MystateAdpter(this, publisheds, realm);
        listView.setAdapter(mystateAdpter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position > 0) {
//                    Intent intent = new Intent(myStateActivity.this, myStateImagePagerActivity.class);
//                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageUrls);
//                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
//                    startActivity(intent);
//                }
//            }
//        });
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
        current_type = getIntent().getIntExtra(TYPE, 0);
        if (!getIntent().hasExtra(IntentStatic.EXTRA_ACCOUNT)) {
            showToast("账号有误");
            finish();
        }
        account = getIntent().getStringExtra(IntentStatic.EXTRA_ACCOUNT);
        UserCache userCache = new UserCache(this, account, realm);
        userCache.getName(name1);
        userCache.getName(name2);
        userCache.getHead(head);
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
        StoreHouseHeader header = new StoreHouseHeader(this);
        header.setPadding(0, getResources().getDimensionPixelSize(R.dimen.y144), 0, 20);
        header.initWithString("SWALK");
        header.setTextColor(getResources().getColor(R.color.green1));
        header.setBackgroundColor(getResources().getColor(R.color.w0));
        header.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        ptrFrameLayout.setDurationToCloseHeader(2000);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (!is_refresh && !is_loadMore) {
                    mPresenter.LoadData(frame);
                }
            }
        });

        if (mPresenter.isNeedRefresh()) {
            ptrFrameLayout.autoRefresh();
        }
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
}
