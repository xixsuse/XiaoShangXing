package com.xiaoshangxing.xiaoshang.Help.HelpFragment;

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

import com.xiaoshangxing.network.netUtil.LoadUtils;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.bean.Published;
import com.xiaoshangxing.publicActivity.inputActivity.InputActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseFragment;
import com.xiaoshangxing.utils.customView.LayoutHelp;
import com.xiaoshangxing.utils.customView.loadingview.DotsTextView;
import com.xiaoshangxing.utils.customView.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.customView.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.xiaoshang.Help.HelpActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by FengChaoQun
 * on 2016/7/20
 */
public class HelpFragment extends BaseFragment implements HelpContract.View {

    public static final String TAG = BaseFragment.TAG + "-ShoolfellowHelpFragment";
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
    @Bind(R.id.anounce)
    ImageView anounce;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.reflesh_layout)
    PtrFrameLayout ptrFrameLayout;
    @Bind(R.id.mengban)
    View mengban;
    @Bind(R.id.rule_image)
    ImageView ruleImage;
    @Bind(R.id.collasp)
    LinearLayout collasp;
    @Bind(R.id.rules)
    RelativeLayout rules;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.no_content)
    TextView noContent;
    RealmResults<Published> publisheds;
    private View mview;
    private Help_Adpter_realm adpter_realm;
    private View headview, footview;
    private DotsTextView dotsTextView;
    private TextView loadingText;
    private boolean isRefreshing;
    private boolean isLoading;
    private String account;
    private boolean isOthers;

    private HelpContract.Presenter mPresenter;

    public static HelpFragment newInstance() {
        return new HelpFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.title_anounce_refresh_listview, null);
        ButterKnife.bind(this, mview);
        setmPresenter(new HelpPresenter(this, getContext()));
        initView();
        initFresh();
        return mview;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        setCloseActivity();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setCloseActivity();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void initView() {
        title.setText(R.string.shoolfellowhelp);
        leftText.setText(R.string.xiaoshang);
        more.setImageResource(R.mipmap.add);
//        anounceContent.setText(R.string.help_rules);
        ruleImage.setImageResource(R.mipmap.gonggao_hb);
        headview = new View(getContext());
        footview = View.inflate(getContext(), R.layout.footer, null);
        dotsTextView = (DotsTextView) footview.findViewById(R.id.dot);
        loadingText = (TextView) footview.findViewById(R.id.text);
        dotsTextView.start();
        listview.addHeaderView(headview);
        listview.addFooterView(footview);

        anounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnRule(true);
            }
        });
        if (getActivity().getIntent().getIntExtra(IntentStatic.TYPE, 0) == IntentStatic.OTHERS) {
            title.setText("他的互帮");
            this.more.setVisibility(View.GONE);
            headview.setVisibility(View.GONE);
            account = getActivity().getIntent().getStringExtra(IntentStatic.ACCOUNT);
            isOthers = true;
            anounce.setVisibility(View.GONE);
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
        if (isOthers) {
            publisheds = realm.where(Published.class)
                    .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_HELP))
                    .equalTo(NS.USER_ID, Integer.valueOf(account))
                    .findAllSorted(NS.CREATETIME, Sort.DESCENDING);
        } else {
            publisheds = realm.where(Published.class)
                    .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_HELP))
                    .findAllSorted(NS.CREATETIME, Sort.DESCENDING);
        }
        adpter_realm = new Help_Adpter_realm(getContext(), publisheds, this, (HelpActivity) getActivity());
        listview.setAdapter(adpter_realm);
        if (publisheds.size() > 0) {
            noContent.setVisibility(View.GONE);
        } else {
            noContent.setVisibility(View.VISIBLE);
            noContent.setText(isOthers ? "他还没有发布互帮" : "还没有人发布互帮");
        }
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
                initListview();
            }

            @Override
            public void error() {
                ptrFrameLayout.refreshComplete();
            }
        };

        if (isOthers) {
            LayoutHelp.initPTR(ptrFrameLayout, true, new PtrDefaultHandler() {
                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    LoadUtils.getPersonalPublished(realm, NS.CATEGORY_HELP, Integer.parseInt(account), getContext(),
                            aroundLoading);
                }
            });
        } else {
            LayoutHelp.initPTR(ptrFrameLayout, LoadUtils.needRefresh(LoadUtils.TIME_LOAD_HELP), new PtrDefaultHandler() {
                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    LoadUtils.getPublished(realm, NS.CATEGORY_HELP, LoadUtils.TIME_LOAD_HELP, getContext(), false,
                            aroundLoading);
                }
            });
        }

    }

    @Override
    public void showPublishMenu(View v) {

        View menu = View.inflate(getContext(), R.layout.popupmenu_pubulish, null);
        final PopupWindow popupWindow = new PopupWindow(menu, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(getContext().getResources().
                getDrawable(R.drawable.nothing));
//        popupWindow.setAnimationStyle(R.style.popwindow_anim);

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
        startActivityForResult(intent, IntentStatic.PUBLISH);
    }

    @Override
    public void gotoPublished() {
        HelpActivity activity = (HelpActivity) getActivity();
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .hide(activity.getHelpFragment())
                .show(activity.getPersonalHelpFragment())
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
        adpter_realm.notifyDataSetChanged();
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
        if (is) {
            rules.setVisibility(View.VISIBLE);
            rules.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.translate_move_in));
        } else {
            rules.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.translate_move_out));
            rules.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rules.setVisibility(View.GONE);
                }
            }, 500);
        }
    }

    @Override
    public void setmPresenter(@Nullable HelpContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    @OnClick({R.id.back, R.id.more, R.id.collasp, R.id.mengban})
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.more:
                LayoutHelp.PermissionClick(getActivity(), new LayoutHelp.PermisionMethod() {
                    @Override
                    public void doSomething() {
                        showPublishMenu(view);
                    }
                });
                break;
            case R.id.collasp:
                clickOnRule(false);
                break;
            case R.id.mengban:
                clickOnRule(false);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentStatic.PUBLISH && resultCode == IntentStatic.PUBLISH_SUCCESS) {
            if (ptrFrameLayout != null) {
                ptrFrameLayout.autoRefresh();
            }
        }
    }
}
