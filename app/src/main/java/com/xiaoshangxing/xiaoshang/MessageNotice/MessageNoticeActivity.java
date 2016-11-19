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

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Notice;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.layout.LayoutHelp;
import com.xiaoshangxing.utils.layout.loadingview.DotsTextView;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.utils.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private Message_adpter message_adpter;
    private NoticeAdpter adpter;
    private ArrayList<String> list = new ArrayList<String>();
    private MessageNoticeContract.Presenter mPresenter;
    private List<Notice> notices = new ArrayList<>();

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
        refreshPager();
    }

    private void initFresh() {
        LayoutHelp.initPTR(ptrFrameLayout, false, new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                isRefreshing = true;
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.refreshData();
                        ptrFrameLayout.refreshComplete();
                        isRefreshing = false;
                    }
                }, 1500);
            }
        });

    }

    @Override
    public void refreshPager() {
        for (int i = 0; i <= 10; i++) {
            list.add("" + i);
        }
        message_adpter = new Message_adpter(this, 1, list);
//        listview.setAdapter(message_adpter);

        for (int i = 0; i <= 10; i++) {
            notices.add(new Notice());
        }
        adpter = new NoticeAdpter(this, 1, notices);
        listview.setAdapter(adpter);
    }

    @Override
    public void autoRefresh() {
        if (ptrFrameLayout != null) {
            if (mPresenter.isNeedRefresh()) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.autoRefresh();
                    }
                }, 100);
            }
        }
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
}
