package com.xiaoshangxing.xiaoshang.Plan.JoinedPlan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.LoadUtils;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.JoinedPlan;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.layout.LayoutHelp;
import com.xiaoshangxing.utils.layout.loadingview.DotsTextView;
import com.xiaoshangxing.utils.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Sort;

/**
 * Created by FengChaoQun
 * on 2016/9/29
 */
public class JoinedPlanFragment extends BaseFragment implements JoinedPlanContract.View {
    public static final String TAG = BaseFragment.TAG + "-JoinedPlanFragment";
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
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.no_content)
    TextView noContent;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.title_refresh_listview, null);
        ButterKnife.bind(this, mview);
        initView();
        initFresh();
        return mview;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static JoinedPlanFragment newInstance() {
        return new JoinedPlanFragment();
    }

    private View mview;
    private View footview;
    private DotsTextView dotsTextView;
    private TextView loadingText;
    private boolean isRefreshing;
    private boolean isLoading;
    private List<JoinedPlan> joinedPlen = new ArrayList<>();
    private JoinedPlan_Adpter adpter;

    private void initView() {
        title.setText("我加入的计划");
        more.setVisibility(View.GONE);

        footview = View.inflate(getContext(), R.layout.footer, null);
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

//                imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(),
//                            0);
//                }
            }
        });
        refreshPager();
    }


    private void initFresh() {
        LayoutHelp.initPTR(ptrFrameLayout, LoadUtils.needRefresh(LoadUtils.TIME_JOINED_PLAN),
                new PtrDefaultHandler() {
                    @Override
                    public void onRefreshBegin(final PtrFrameLayout frame) {
                        LoadUtils.getJoinedPlan(String.valueOf(TempUser.id), realm, getContext(), new LoadUtils.AroundLoading() {
                            @Override
                            public void before() {
//                                LoadUtils.clearJoinPlan();
                            }

                            @Override
                            public void complete() {
                                frame.refreshComplete();
                            }

                            @Override
                            public void onSuccess() {
                                refreshPager();
                            }

                            @Override
                            public void error() {
                                frame.refreshComplete();
                            }
                        });
                    }
                });
    }

    @OnClick(R.id.back)
    public void onClick() {
        getFragmentManager().popBackStack();
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
    public void refreshPager() {
        joinedPlen = realm.where(JoinedPlan.class)
                .equalTo(NS.USER_ID, TempUser.id)
                .findAllSorted(NS.CREATETIME, Sort.DESCENDING);
        adpter = new JoinedPlan_Adpter(getContext(), 1, getActivity(), joinedPlen);
        listview.setAdapter(adpter);
        if (joinedPlen.size() > 0) {
            noContent.setVisibility(View.GONE);
        } else {
            noContent.setVisibility(View.VISIBLE);
            noContent.setText("你还没有加入任何计划");
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
    public void setmPresenter(@Nullable JoinedPlanContract.Presenter presenter) {

    }
}
