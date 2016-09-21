package com.xiaoshangxing.xiaoshang.ShoolfellowHelp.ShoolfellowHelpFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.LoadUtils;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.input_activity.InputActivity;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.layout.LayoutHelp;
import com.xiaoshangxing.utils.loadingview.DotsTextView;
import com.xiaoshangxing.utils.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.xiaoshang.ShoolReward.ShoolRewardActivity;
import com.xiaoshangxing.xiaoshang.ShoolfellowHelp.MyShoolfellowHelp.MyShoolHelpFragment;
import com.xiaoshangxing.xiaoshang.ShoolfellowHelp.ShoolfellowHelpActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by FengChaoQun
 * on 2016/7/20
 */
public class ShoolfellowHelpFragment extends BaseFragment implements ShoolHelpContract.View {

    public static final String TAG = BaseFragment.TAG + "-ShoolfellowHelpFragment";
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.myState)
    TextView myState;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.reflesh_layout)
    PtrFrameLayout ptrFrameLayout;
    @Bind(R.id.mengban)
    View mengban;
    @Bind(R.id.collasp)
    LinearLayout collasp;
    @Bind(R.id.rules)
    RelativeLayout rules;

    private View mview;
    private shoolfellow_adpter adpter;
    private View headview, footview;
    private DotsTextView dotsTextView;
    private TextView loadingText;

    private ShoolHelpContract.Presenter mPresenter;

    public static ShoolfellowHelpFragment newInstance() {
        return new ShoolfellowHelpFragment();
    }

    private boolean isRefreshing;
    private boolean isLoading;
    private Realm realm;
    RealmResults<Published> publisheds;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.frag_shoolfellowhelp, null);
        ButterKnife.bind(this, mview);
        realm = Realm.getDefaultInstance();
        setmPresenter(new ShoolHelpPresenter(this, getContext()));
        initView();
        initFresh();
        return mview;
    }

    private void initView() {
        headview = View.inflate(getContext(), R.layout.headview_help_list, null);
        footview = View.inflate(getContext(), R.layout.footer, null);
        dotsTextView = (DotsTextView) footview.findViewById(R.id.dot);
        loadingText = (TextView) footview.findViewById(R.id.text);
        dotsTextView.start();
        listview.addHeaderView(headview);
        listview.addFooterView(footview);

        headview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnRule(true);
            }
        });
        if (getActivity().getIntent().getIntExtra(IntentStatic.TYPE, 0) == ShoolRewardActivity.OTHERS) {
            this.myState.setText("他的互帮");
            this.more.setVisibility(View.GONE);
            headview.setVisibility(View.GONE);
        }
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (totalItemCount - (firstVisibleItem + visibleItemCount - 1) == 5) {
//                    if (!isLoading) {
//                        mPresenter.LoadMore();
//                    }
//                }
//                if (firstVisibleItem + visibleItemCount == totalItemCount) {
//                    showFooter();
//                }
            }
        });

        initListview();
    }

    private void initListview() {
        publisheds = realm.where(Published.class)
                .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_HELP))
                .findAll().sort(NS.CREATETIME, Sort.DESCENDING);
        adpter = new shoolfellow_adpter(getContext(), 1, publisheds, this, (ShoolfellowHelpActivity) getActivity());
        listview.setAdapter(adpter);
    }

    private void initFresh() {
        LayoutHelp.initPTR(ptrFrameLayout, LoadUtils.needRefresh(LoadUtils.TIME_LOAD_HELP),
                new PtrDefaultHandler() {
                    @Override
                    public void onRefreshBegin(PtrFrameLayout frame) {
//                        mPresenter.RefreshData(frame, realm);
                    }
                });
    }

    @Override
    public void showPublishMenu(View v) {

        View menu = View.inflate(getContext(), R.layout.popupmenu_pubulish, null);
        final PopupWindow popupWindow = new PopupWindow(menu, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(getContext().getResources().
                getDrawable(R.drawable.nothing));
        popupWindow.setAnimationStyle(R.style.popwindow_anim);

        menu.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int mShowMorePopupWindowWidth = menu.getMeasuredWidth();

        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);

        popupWindow.showAsDropDown(v,
                -mShowMorePopupWindowWidth + this.getResources().getDimensionPixelSize(R.dimen.x30) + v.getWidth(),
                3);

        View publish = menu.findViewById(R.id.publish);
        View published = menu.findViewById(R.id.published);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPublish();
                popupWindow.dismiss();
            }
        });
        published.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPublished();
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void gotoPublish() {
        Intent intent = new Intent(getContext(), InputActivity.class);
        intent.putExtra(InputActivity.EDIT_STATE, InputActivity.SHOOLFELLOW_HELP);
        startActivity(intent);
    }

    @Override
    public void gotoPublished() {
        ShoolfellowHelpActivity activity = (ShoolfellowHelpActivity) getActivity();
        MyShoolHelpFragment fragment = activity.getMyShoolHelpFragment();
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .hide(this)
//                .add(R.id.main_fragment, fragment, MyShoolHelpFragment.TAG)
                .show(fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void setRefreshState(boolean is) {
        isRefreshing = is;
    }

    @Override
    public void setLoadState(boolean is) {
        isLoading = is;
    }

    @Override
    public void refreshPager() {
        adpter.notifyDataSetChanged();
    }

    @Override
    public void autoRefresh() {
        if (ptrFrameLayout != null) {
            if (mPresenter.isNeedRefresh()) {
                ptrFrameLayout.autoRefresh();
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
    public void clickOnRule(boolean is) {
        if (is){
            rules.setVisibility(View.VISIBLE);
            rules.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.verify_success_top));
        }else {
            rules.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.verify_success_top1));
            rules.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rules.setVisibility(View.GONE);
                }
            }, 800);
        }
//        Intent intent = new Intent(getContext(), TextBoard.class);
//        intent.putExtra(IntentStatic.TYPE, TextBoard.HELP);
//        startActivity(intent);
    }

    @Override
    public void setmPresenter(@Nullable ShoolHelpContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    @Override
    public void onDestroyView() {
        realm.close();
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @OnClick({R.id.back, R.id.more,R.id.collasp,R.id.mengban})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.more:
                showPublishMenu(view);
                break;
            case R.id.collasp:
                clickOnRule(false);
                break;
            case R.id.mengban:
                clickOnRule(false);
                break;
        }
    }
}
