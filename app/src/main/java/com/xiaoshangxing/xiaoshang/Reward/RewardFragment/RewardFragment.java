package com.xiaoshangxing.xiaoshang.Reward.RewardFragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.bean.Published;
import com.xiaoshangxing.network.netUtil.LoadUtils;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.network.netUtil.OperateUtils;
import com.xiaoshangxing.network.netUtil.SimpleCallBack;
import com.xiaoshangxing.publicActivity.inputActivity.InputActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseFragment;
import com.xiaoshangxing.utils.customView.LayoutHelp;
import com.xiaoshangxing.utils.customView.RuleUtil;
import com.xiaoshangxing.utils.customView.dialog.DialogLocationAndSize;
import com.xiaoshangxing.utils.customView.dialog.DialogUtils;
import com.xiaoshangxing.utils.customView.loadingview.DotsTextView;
import com.xiaoshangxing.utils.customView.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.customView.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.xiaoshang.Reward.RewardActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by FengChaoQun
 * on 2016/7/20
 */
public class RewardFragment extends BaseFragment implements RewardContract.View {

    public static final String TAG = BaseFragment.TAG + "-ShoolRewardFragment";
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.reflesh_layout)
    PtrFrameLayout ptrFrameLayout;
    @Bind(R.id.no_content)
    TextView noContent;
    @Bind(R.id.mengban)
    View mengban;
    @Bind(R.id.rule_image)
    ImageView ruleImage;
    @Bind(R.id.rule_button)
    ImageView ruleButton;
    @Bind(R.id.wrap_view)
    RelativeLayout wrapView;
    @Bind(R.id.rules)
    RelativeLayout rules;
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


    private View mview;
    private Reward_adpter_realm adpter1;
    private View headview, footview;
    private RewardContract.Presenter mPresenter;
    private DotsTextView dotsTextView;
    private TextView loadingText;
    private boolean isRefreshing;
    private boolean isLoading;
    private RealmResults<Published> realmResults;
    private String account;
    private boolean isOthers;
    private RuleUtil ruleUtil;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.title_anounce_refresh_listview, null);
        ButterKnife.bind(this, mview);
        setmPresenter(new RewardPresenter(this, getContext()));
        initFresh();
        initView();
        refreshPager();
        return mview;
    }

    @Override
    public void onResume() {
        super.onResume();
        setCloseActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static RewardFragment newInstance() {
        return new RewardFragment();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setCloseActivity();
        }
    }

    private void initView() {
        title.setText(R.string.shoolreward);
        leftText.setText(R.string.xiaoshang);
        more.setImageResource(R.mipmap.add);
        ruleImage.setImageResource(R.mipmap.gonggao_xs);
        noContent.setText("还没有人发布悬赏");
        headview = new View(getContext());
        footview = View.inflate(getContext(), R.layout.footer, null);
        dotsTextView = (DotsTextView) footview.findViewById(R.id.dot);
        dotsTextView.start();
        loadingText = (TextView) footview.findViewById(R.id.text);
        listview.addHeaderView(headview);
        listview.addFooterView(footview);
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
        if (getActivity().getIntent().getIntExtra(IntentStatic.TYPE, 0) == IntentStatic.OTHERS) {
            this.title.setText("他的悬赏");
            this.more.setVisibility(View.GONE);
            headview.setVisibility(View.GONE);
            account = getActivity().getIntent().getStringExtra(IntentStatic.ACCOUNT);
            isOthers = true;
            listview.setPadding(0, 0, 0, 0);
            rules.setVisibility(View.GONE);
        } else {
            listview.setPadding(0, ScreenUtils.getAdapterPx(R.dimen.y96, getContext()), 0, 0);
            ruleUtil = new RuleUtil(mview, getActivity());
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
                refreshPager();
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
                    LoadUtils.getPersonalPublished(realm, NS.CATEGORY_REWARD, Integer.parseInt(account), getContext(),
                            aroundLoading);
                }
            });
        } else {
            LayoutHelp.initPTR(ptrFrameLayout, LoadUtils.needRefresh(LoadUtils.TIME_LOAD_REWARD, realm), new PtrDefaultHandler() {
                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    LoadUtils.getPublished(realm, NS.CATEGORY_REWARD, LoadUtils.TIME_LOAD_REWARD, getContext(), false,
                            aroundLoading);
                }
            });
        }
    }

    @OnClick({R.id.back, R.id.more})
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
        }
    }

    @Override
    public boolean isRefreshing() {
        return isRefreshing;
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void showPublishMenu(View v) {
        View menu = View.inflate(getContext(), R.layout.popupmenu_rewardpubulish, null);
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
        View collect = menu.findViewById(R.id.collect);
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
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCollect();
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void gotoCollect() {
        RewardActivity activity = (RewardActivity) getActivity();
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .hide(activity.getRewardFragment())
//                .hide(activity.getPersonalRewardFragment())
                .show(activity.getRewardCollectFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoPublish() {
        Intent intent = new Intent(getContext(), InputActivity.class);
        intent.putExtra(InputActivity.EDIT_STATE, InputActivity.SHOOL_REWARD);
        startActivityForResult(intent, IntentStatic.PUBLISH);
    }

    @Override
    public void gotoPublished() {
        RewardActivity activity = (RewardActivity) getActivity();
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .hide(activity.getRewardFragment())
//                .hide(activity.getRewardCollectFragment())
                .show(activity.getPersonalRewardFragment())
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
        if (isOthers) {
            realmResults = realm.where(Published.class)
                    .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_REWARD))
                    .equalTo(NS.USER_ID, Integer.valueOf(account))
                    .findAllSorted(NS.CREATETIME, Sort.DESCENDING);
        } else {
            realmResults = realm.where(Published.class)
                    .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_REWARD))
                    .findAllSorted(NS.CREATETIME, Sort.DESCENDING);
        }
        adpter1 = new Reward_adpter_realm(getContext(), realmResults, this, (RewardActivity) getActivity());
        listview.setAdapter(adpter1);
        if (realmResults.size() > 0) {
            noContent.setVisibility(View.GONE);
        } else {
            noContent.setVisibility(View.VISIBLE);
            noContent.setText(isOthers ? "他还没有发布悬赏" : "还没有人发布悬赏");
        }
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
    }

    public boolean needCollespRule() {
        return ruleUtil.needhideRules();
    }

    @Override
    public void showCollectDialog(final int id, final boolean isCancle) {
        DialogUtils.DialogMenu2 dialogMenu2 = new DialogUtils.DialogMenu2(getContext());
        dialogMenu2.addMenuItem(isCancle ? "取消收藏" : "收藏");
        dialogMenu2.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                OperateUtils.operateWithLoad(id, getContext(), true, NS.COLLECT, isCancle, RewardFragment.this,
                        new SimpleCallBack() {
                            @Override
                            public void onSuccess() {
                                noticeDialog(isCancle ? "已取消收藏" : "已收藏");
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onBackData(Object o) {

                            }
                        });
            }

            @Override
            public void onCancel() {

            }
        });
        dialogMenu2.initView();
        dialogMenu2.show();
        DialogLocationAndSize.bottom_FillWidth(getActivity(), dialogMenu2);
    }

    @Override
    public void noticeDialog(String message) {
        DialogUtils.Dialog_No_Button dialog_no_button = new DialogUtils.Dialog_No_Button(getActivity(), message);
        final Dialog alertDialog = dialog_no_button.create();
        alertDialog.show();
        DialogLocationAndSize.setWidth(alertDialog, R.dimen.x420);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                alertDialog.dismiss();
            }
        }, 1000);
    }

    @Override
    public void setmPresenter(@Nullable RewardContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentStatic.PUBLISH && resultCode == IntentStatic.PUBLISH_SUCCESS) {
            if (ptrFrameLayout != null) {
                ptrFrameLayout.autoRefresh();
            }
        }
    }


}
