package com.xiaoshangxing.xiaoshang.MessageNotice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.PushMsg;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.NotifycationUtil;
import com.xiaoshangxing.utils.layout.loadingview.DotsTextView;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.xiaoshangxing.utils.NotifycationUtil.NOTIFY_XIAOSHANG;
import static com.xiaoshangxing.utils.NotifycationUtil.NT_SCHOOL_HELP;
import static com.xiaoshangxing.utils.NotifycationUtil.NT_SCHOOL_NOTICE_YOU;
import static com.xiaoshangxing.utils.NotifycationUtil.NT_SCHOOL_PLAN;
import static com.xiaoshangxing.utils.NotifycationUtil.NT_SCHOOL_REWARD;
import static com.xiaoshangxing.utils.NotifycationUtil.NT_SCHOOL_SALE;

/**
 * Created by FengChaoQun
 * on 2016/8/2
 */
public class MessageNoticeActivity extends BaseActivity implements MessageNoticeContract.View {

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
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.reflesh_layout)
    PtrFrameLayout ptrFrameLayout;
    private View footview;
    private DotsTextView dotsTextView;
    private TextView loadingText;

    private boolean isRefreshing;
    private boolean isLoading;
    private NoticeAdpter_realm adpter_realm;
    private MessageNoticeContract.Presenter mPresenter;
    private RealmResults<PushMsg> pushMsgs;
    private NotifycationUtil.OnNotifyChange onNotifyChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_refresh_listview);
        ButterKnife.bind(this);
        setmPresenter(new MessageNoticePresenter(this, this));
        initView();
        initFresh();
        autoRefresh();
    }

    private void initView() {
        title.setText("消息通知");
        leftText.setText("返回");
        more.setVisibility(View.GONE);
        View view = new View(this);
        listview.addHeaderView(view);
        footview = View.inflate(this, R.layout.footer, null);
        dotsTextView = (DotsTextView) footview.findViewById(R.id.dot);
        dotsTextView.start();
        loadingText = (TextView) footview.findViewById(R.id.text);
        listview.addFooterView(footview);
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount - (firstVisibleItem + visibleItemCount - 1) == 5) {
                    if (!isLoading) {
                        mPresenter.loadMore();
                    }
                }
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    showFooter();
                }
            }
        });
        listview.setPadding(ScreenUtils.getAdapterPx(R.dimen.x24, this), 0, ScreenUtils.getAdapterPx(R.dimen.x24, this), 0);
        listview.setDividerHeight(ScreenUtils.getAdapterPx(R.dimen.y56, this));
        doWhenEnter();
        refreshPager();

        onNotifyChange = new NotifycationUtil.OnNotifyChange() {
            @Override
            public void onChange(PushMsg pushMsg) {
                if (pushMsg.getPushType().equals(NotifycationUtil.NT_SCHOOL_REWARD)
                        || pushMsg.getPushType().equals(NotifycationUtil.NT_SCHOOL_HELP)
                        || pushMsg.getPushType().equals(NotifycationUtil.NT_SCHOOL_PLAN)
                        || pushMsg.getPushType().equals(NotifycationUtil.NT_SCHOOL_SALE)
                        || pushMsg.getPushType().equals(NotifycationUtil.NT_SCHOOL_NOTICE_YOU)) {
                    refreshPager();
                    doWhenEnter();
                }
            }
        };
        NotifycationUtil.registerObserver(onNotifyChange);
    }

    private void initFresh() {
    }

    /**
     * 用户进入这个页面 则将本地有关推送记录置为已读 并清除通知栏的有关通知
     */
    private void doWhenEnter() {
        final List<PushMsg> pushMsgs = realm.where(PushMsg.class).not().equalTo("isRead", "1")
                .beginGroup()
                .equalTo(NS.PUSH_TYPE, NT_SCHOOL_HELP)
                .or().equalTo(NS.PUSH_TYPE, NT_SCHOOL_REWARD)
                .or().equalTo(NS.PUSH_TYPE, NT_SCHOOL_PLAN)
                .or().equalTo(NS.PUSH_TYPE, NT_SCHOOL_SALE)
                .or().equalTo(NS.PUSH_TYPE, NT_SCHOOL_NOTICE_YOU)
                .endGroup()
                .findAllSorted(NS.PUSH_TIME, Sort.DESCENDING);
        if (!pushMsgs.isEmpty()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (PushMsg i : pushMsgs) {
                        i.setIsRead("1");
                    }
                }
            });
        }
        NotifycationUtil.clearNotify(NOTIFY_XIAOSHANG);
    }

    @Override
    public void refreshPager() {
        pushMsgs = realm.where(PushMsg.class)
                .equalTo(NS.PUSH_TYPE, NT_SCHOOL_HELP)
                .or().equalTo(NS.PUSH_TYPE, NT_SCHOOL_REWARD)
                .or().equalTo(NS.PUSH_TYPE, NT_SCHOOL_PLAN)
                .or().equalTo(NS.PUSH_TYPE, NT_SCHOOL_SALE)
                .or().equalTo(NS.PUSH_TYPE, NT_SCHOOL_NOTICE_YOU)
                .findAllSorted(NS.PUSH_TIME, Sort.DESCENDING);
        if (pushMsgs.isEmpty()) {
            return;
        }
        adpter_realm = new NoticeAdpter_realm(this, pushMsgs);
        listview.setAdapter(adpter_realm);
    }

    @Override
    public void autoRefresh() {
    }

    @Override
    public void showNoData() {
        dotsTextView.stop();
        loadingText.setText("没有动态啦");
    }

    @Override
    public void showFooter() {
        dotsTextView.start();
        loadingText.setText("加载中");
    }

    @Override
    public void setRefreshState(boolean is) {
        isRefreshing = is;
    }

    @Override
    public boolean isRefreshing() {
        return isRefreshing;
    }

    @Override
    public void setLoadState(boolean is) {
        isLoading = is;
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void setmPresenter(@Nullable MessageNoticeContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotifycationUtil.unRegisterObserver(onNotifyChange);
    }
}
