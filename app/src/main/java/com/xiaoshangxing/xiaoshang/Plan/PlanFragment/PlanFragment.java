package com.xiaoshangxing.xiaoshang.Plan.PlanFragment;

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
import com.xiaoshangxing.xiaoshang.Plan.PlanActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by FengChaoQun
 * on 2016/9/29
 */
public class PlanFragment extends BaseFragment implements PlanContract.View {

    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.line)
    View line;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.reflesh_layout)
    PtrFrameLayout ptrFrameLayout;
    @Bind(R.id.mengban)
    View mengban;
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.collasp)
    LinearLayout collasp;
    @Bind(R.id.rules)
    RelativeLayout rules;
    private View mview;
    private Realm realm;
    private View headview, footview;
    private DotsTextView dotsTextView;
    private TextView loadingText;
    private boolean isRefreshing;
    private boolean isLoading;
    public static final String TAG = BaseFragment.TAG + "-PlanFragment";
    private List<Published> publisheds = new ArrayList<>();
    private Plan_Adpter adpter;
    private boolean isOthers;
    private String account;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.frag_sale, null);
        ButterKnife.bind(this, mview);
        realm = Realm.getDefaultInstance();
        initView();
        return mview;
    }

    @Override
    public void onDestroyView() {
        realm.close();
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static PlanFragment newInstance() {
        return new PlanFragment();
    }

    private void initView() {
        title.setText("计划发起");
        listview.setDividerHeight(0);
        content.setText(getString(R.string.launch_plan));
        headview = View.inflate(getContext(), R.layout.headview_help_list, null);
        footview = View.inflate(getContext(), R.layout.footer, null);
        dotsTextView = (DotsTextView) footview.findViewById(R.id.dot);
        dotsTextView.start();
        loadingText = (TextView) footview.findViewById(R.id.text);
        listview.addHeaderView(headview);
        listview.addFooterView(footview);
        headview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnRule(true);
            }
        });
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

        if (getActivity().getIntent().getIntExtra(IntentStatic.TYPE, IntentStatic.MINE) == IntentStatic.OTHERS) {
            this.title.setText("他的计划");
            this.more.setVisibility(View.GONE);
            headview.setVisibility(View.GONE);
            isOthers = true;
            account = getActivity().getIntent().getStringExtra(IntentStatic.EXTRA_ACCOUNT);
        }
        initFresh();
        refreshPager();
    }


    private void initFresh() {
        final LoadUtils.AroundLoading aroundLoading = new LoadUtils.AroundLoading() {
            @Override
            public void before() {
                LoadUtils.clearDatabase(NS.CATEGORY_SALE, false, true);
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
                    LoadUtils.getOthersPublished(realm, NS.CATEGORY_PLAN, Integer.parseInt(account), getContext(),
                            aroundLoading);
                }
            });
        } else {
            LayoutHelp.initPTR(ptrFrameLayout, LoadUtils.needRefresh(LoadUtils.TIME_LOAD_PLAN), new PtrDefaultHandler() {
                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    LoadUtils.getPublished(realm, NS.CATEGORY_PLAN, LoadUtils.TIME_LOAD_PLAN, getContext(), true,
                            aroundLoading);
                }
            });
        }

    }

    @OnClick({R.id.back, R.id.more, R.id.mengban, R.id.collasp})
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
            case R.id.mengban:
                clickOnRule(false);
                break;
            case R.id.collasp:
                clickOnRule(false);
                break;
        }
    }

    @Override
    public void showPublishMenu(View v) {
        View menu = View.inflate(getContext(), R.layout.popupmenu_rewardpubulish, null);
        ImageView third_image = (ImageView) menu.findViewById(R.id.third_image);
        TextView third_item = (TextView) menu.findViewById(R.id.third_item);
        third_item.setText("已加入");
        third_image.setImageResource(R.mipmap.plan_joined);
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
                gotoJoined();
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void gotoPublish() {
        Intent intent = new Intent(getContext(), InputActivity.class);
        intent.putExtra(InputActivity.EDIT_STATE, InputActivity.LANCH_PLAN);
        startActivity(intent);
    }

    @Override
    public void gotoPublished() {
        PlanActivity activity = (PlanActivity) getActivity();
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .hide(this)
                .hide(activity.getJoinedPlanFragment())
                .show(activity.getPersonalPlanFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoJoined() {
        PlanActivity activity = (PlanActivity) getActivity();
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .hide(this)
                .hide(activity.getPersonalPlanFragment())
                .show(activity.getJoinedPlanFragment())
                .addToBackStack(null)
                .commit();
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
        if (isOthers) {
            publisheds = realm.where(Published.class)
                    .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_PLAN))
                    .equalTo(NS.USER_ID, Integer.valueOf(account))
                    .findAllSorted(NS.CREATETIME, Sort.DESCENDING);
        } else {
            publisheds = realm.where(Published.class)
                    .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_PLAN))
                    .findAllSorted(NS.CREATETIME, Sort.DESCENDING);
        }

        adpter = new Plan_Adpter(getContext(), 1, getActivity(),publisheds);
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
    public void clickOnRule(boolean is) {
        if (is) {
            rules.setVisibility(View.VISIBLE);
            rules.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.verify_success_top));
        } else {
            rules.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.verify_success_top1));
            rules.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rules.setVisibility(View.GONE);
                }
            }, 800);
        }
    }

    @Override
    public void setmPresenter(@Nullable PlanContract.Presenter presenter) {

    }
}
