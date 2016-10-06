package com.xiaoshangxing.xiaoshang.Plan.JoinedPlan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.LoadUtils;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.layout.LayoutHelp;
import com.xiaoshangxing.utils.loadingview.DotsTextView;
import com.xiaoshangxing.utils.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.xiaoshang.Plan.PlanFragment.Plan_Adpter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/9/29
 */
public class JoinedPlanFragment extends BaseFragment implements JoinedPlanContract.View {
    public static final String TAG = BaseFragment.TAG + "-JoinedPlanFragment";
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.line)
    View line;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.reflesh_layout)
    PtrFrameLayout ptrFrameLayout;


    public static JoinedPlanFragment newInstance() {
        return new JoinedPlanFragment();
    }

    private View mview;
    private Realm realm;
    private View footview;
    private DotsTextView dotsTextView;
    private TextView loadingText;
    private boolean isRefreshing;
    private boolean isLoading;
    private List<Published> publisheds = new ArrayList<>();
    private Plan_Adpter adpter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.frag_joinedplan, null);
        ButterKnife.bind(this, mview);
        realm = Realm.getDefaultInstance();
        initView();
        return mview;
    }

    private void initView() {
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
        LayoutHelp.initPTR(ptrFrameLayout, LoadUtils.needRefresh(LoadUtils.TIME_LOAD_SALE),
                new PtrDefaultHandler() {
                    @Override
                    public void onRefreshBegin(final PtrFrameLayout frame) {
                        LoadUtils.getPublished(realm, NS.CATEGORY_SALE, LoadUtils.TIME_LOAD_SALE, getContext(), false,
                                new LoadUtils.AroundLoading() {
                                    @Override
                                    public void before() {
                                        LoadUtils.clearDatabase(NS.CATEGORY_SALE, false, true);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
        ButterKnife.unbind(this);
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
        for (int i = 0; i <= 10; i++) {
            publisheds.add(new Published());
        }
        adpter = new Plan_Adpter(getContext(), 1, publisheds);
        listview.setAdapter(adpter);
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