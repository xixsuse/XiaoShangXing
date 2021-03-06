package com.xiaoshangxing.wo.WoFrafment;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xiaoshangxing.MainActivity;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.data.bean.Published;
import com.xiaoshangxing.data.bean.PushMsg;
import com.xiaoshangxing.network.netUtil.LoadUtils;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.publicActivity.inputActivity.InputActivity;
import com.xiaoshangxing.publicActivity.inputActivity.InputBoxLayout;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.NotifycationUtil;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.utils.baseClass.BaseFragment;
import com.xiaoshangxing.utils.customView.CirecleImage;
import com.xiaoshangxing.utils.customView.LayoutHelp;
import com.xiaoshangxing.utils.customView.loadingview.DotsTextView;
import com.xiaoshangxing.utils.customView.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.customView.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.utils.imageUtils.MyGlide;
import com.xiaoshangxing.wo.NewsActivity.NewsActivity;
import com.xiaoshangxing.wo.setting.SettingActivity;
import com.xiaoshangxing.yujian.IM.uinfo.SelfInfoObserver;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.xiaoshangxing.utils.NotifycationUtil.NT_SCHOOLMATE_CHANGE;
import static com.xiaoshangxing.utils.NotifycationUtil.NT_SCHOOLMATE_NOTICE_YOU;

/**
 * Created by FengChaoQun
 * on 2016/7/3
 */
public class WoFragment extends BaseFragment implements WoContract.View, View.OnClickListener {

    public static final String TAG = BaseFragment.TAG + "-WoFragment";
    //    记录title点击时间  实现双击
    long firstClick;
    private WoContract.Presenter mPresenter;
    private View mView, divider_line, footerview;
    private RelativeLayout headView;
    private ListView listView;
    private PtrFrameLayout ptrFrameLayout;
    private RelativeLayout title;
    private boolean is_refresh = false;           //记录是否正在刷新
    private boolean is_loadMore = false;          //记录是否正在加载更多
    private boolean is_titleMove = false;          //记录是否正在移动导航栏

    private Handler handler;

    private TextView name;
    private CirecleImage headImage;
    private TextView news;
    private View news_lay;
    private ImageView newsHead;
    private DotsTextView dotsTextView;
    private TextView LoadingText;
    private TextView noContent;

    private ImageView set, publish;

    private MainActivity activity;
    private InputBoxLayout inputBoxLayout;

    private RealmResults<Published> publisheds;
    private Wo_adpter_realm adpter_realm;

    private List<PushMsg> pushMsgs = new ArrayList<>();
    private NotifycationUtil.OnNotifyChange onNotifyChange;
    private SelfInfoObserver.SelfInfoCallback selfInfoCallback;

    public static WoFragment newInstance() {
        return new WoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_wo, container, false);
        mView = view;
        setmPresenter(new WoPresenter(this, getContext()));
        handler = new Handler();
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NotifycationUtil.unRegisterObserver(onNotifyChange);
        registerSlefInfoObserver(false);
    }

    private void initView() {
        activity = (MainActivity) getActivity();
        inputBoxLayout = activity.getInputBoxLayout();
        listView = (ListView) mView.findViewById(R.id.listview);
        ptrFrameLayout = (PtrFrameLayout) mView.findViewById(R.id.reflesh_layout);
        noContent = (TextView) mView.findViewById(R.id.no_content);
        divider_line = mView.findViewById(R.id.line);
        title = (RelativeLayout) mView.findViewById(R.id.title);
        set = (ImageView) mView.findViewById(R.id.set);
        set.setOnClickListener(this);
        publish = (ImageView) mView.findViewById(R.id.publish);
        publish.setOnClickListener(this);
        //headview footview
        headView = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.util_wo_header, null);
        footerview = getActivity().getLayoutInflater().inflate(R.layout.footer, null);
        name = (TextView) headView.findViewById(R.id.name);
        headImage = (CirecleImage) headView.findViewById(R.id.head_image);
        headImage.setIntent_type(CirecleImage.PERSON_STATE, String.valueOf(TempUser.id));
        news = (TextView) headView.findViewById(R.id.news);
        news_lay = headView.findViewById(R.id.news_lay);
        newsHead = (ImageView) headView.findViewById(R.id.news_head);

        dotsTextView = (DotsTextView) footerview.findViewById(R.id.dot);
        LoadingText = (TextView) footerview.findViewById(R.id.text);

        news_lay.setOnClickListener(this);

        listView.addHeaderView(headView);
        listView.addFooterView(footerview);
        listView.setDividerHeight(0);
        //设置listview头
        initHead();

        initListview();

        //实现双击title返回第一条动态
        title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if ((System.currentTimeMillis() - firstClick) < 1000) {
                        listView.setSelection(0);
                    }
                    firstClick = System.currentTimeMillis();
                }
                return true;
            }
        });

        /*
        **describe:listview的滑动事件 向上滑显示导航栏下边的分割线
        *           当评论框可见时，隐藏评论框
        */
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideEdittext();
                return false;
            }
        });


//      初始化刷新布局
        initFresh();

        footerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "正在更新内容", Toast.LENGTH_SHORT).show();
            }
        });

        onNotifyChange = new NotifycationUtil.OnNotifyChange() {
            @Override
            public void onChange(PushMsg pushMsg) {
                if (pushMsg.getPushType().equals(NotifycationUtil.NT_SCHOOLMATE_NOTICE_YOU)
                        || pushMsg.getPushType().equals(NotifycationUtil.NT_SCHOOLMATE_CHANGE)) {
                    Log.d("pushMes", "change");
                    setNews();
                }
            }
        };
        NotifycationUtil.registerObserver(onNotifyChange);
        registerSlefInfoObserver(true);
    }

    private void registerSlefInfoObserver(boolean is) {
        if (selfInfoCallback == null) {
            selfInfoCallback = new SelfInfoObserver.SelfInfoCallback() {
                @Override
                public void onCallback(NimUserInfo userInfo) {
                    UserInfoCache.getInstance().getHeadIntoImage(TempUser.getId(), headImage);
                    UserInfoCache.getInstance().getExIntoTextview(TempUser.getId(), NS.USER_NAME, name);
                }
            };
        }
        SelfInfoObserver.getInstance().registerObserver(selfInfoCallback, is);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.news_lay:
                gotoNews();
                break;
            case R.id.set:
                gotoSet();
                break;
            case R.id.publish:
                LayoutHelp.PermissionClick(getActivity(), new LayoutHelp.PermisionMethod() {
                    @Override
                    public void doSomething() {
                        gotopublish();
                    }
                });
                break;
        }
    }

    @Override
    public void setName(String name) {
        this.name.setText(name);
    }

    @Override
    public void setHead(String path) {
        MyGlide.with_defaul_image(getContext(), path, headImage);
    }

    @Override
    public void setNews() {
        pushMsgs = realm.where(PushMsg.class).not().equalTo("isRead", "1")
                .beginGroup()
                .equalTo(NS.PUSH_TYPE, NT_SCHOOLMATE_CHANGE)
                .or().equalTo(NS.PUSH_TYPE, NT_SCHOOLMATE_NOTICE_YOU)
                .endGroup()
                .findAllSorted(NS.PUSH_TIME, Sort.DESCENDING);
        if (pushMsgs.isEmpty()) {
            news_lay.setVisibility(View.GONE);
        } else {
            news_lay.setVisibility(View.VISIBLE);
            news.setText(pushMsgs.size() + "条新消息");
            String userId = pushMsgs.get(0).getUserId();
            UserInfoCache.getInstance().getHeadIntoImage(userId, newsHead);
            Log.d("unreadMsg", pushMsgs.toString());
        }
    }

    private void initHead() {
        UserInfoCache.getInstance().getHeadIntoImage(TempUser.getId(), headImage);
        UserInfoCache.getInstance().getExIntoTextview(TempUser.getId(), NS.USER_NAME, name);
        setNews();
    }

    private void initListview() {
        publisheds = realm.where(Published.class)
                .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_STATE))
                .findAll().sort(NS.CREATETIME, Sort.DESCENDING);

        if (publisheds.size() > 0) {
            noContent.setVisibility(View.GONE);
        } else {
            noContent.setVisibility(View.VISIBLE);
            noContent.setText("还没有人发布动态");
        }

        adpter_realm = new Wo_adpter_realm(getContext(),
                publisheds, this, (BaseActivity) getActivity(), realm, listView);
        listView.setAdapter(adpter_realm);
    }

    private void initFresh() {
        LayoutHelp.initPTR(ptrFrameLayout, LoadUtils.needRefresh(LoadUtils.TIME_LOAD_STATE, realm),
                new PtrDefaultHandler() {
                    @Override
                    public void onRefreshBegin(final PtrFrameLayout frame) {
                        LoadUtils.getPublished(realm, NS.CATEGORY_STATE, LoadUtils.TIME_LOAD_STATE, getContext(), false,
                                new LoadUtils.AroundLoading() {
                                    @Override
                                    public void before() {
                                        divider_line.setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void complete() {
                                        frame.refreshComplete();
                                    }

                                    @Override
                                    public void onSuccess() {
                                        initListview();
                                    }

                                    @Override
                                    public void error() {
                                        frame.refreshComplete();
                                    }
                                });
                    }

                });
    }

    public void showEdittext(Context context) {
        inputBoxLayout.showOrHideLayout(true);
    }

    @Override
    public void hideEdittext() {
        inputBoxLayout.showOrHideLayout(false);
    }

    public void moveListview(int px) {
        listView.smoothScrollBy(px, Math.abs(px));
    }

    public int get_Editext_Height() {
        return inputBoxLayout.getEdittext_height();
    }

    @Override
    public void setmPresenter(@Nullable WoContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void gotoSet() {
        Intent setIntent = new Intent(getContext(), SettingActivity.class);
        startActivity(setIntent);
    }

    @Override
    public void gotopublish() {
        Intent publish_intent = new Intent(getContext(), InputActivity.class);
        publish_intent.putExtra(InputActivity.LIMIT, 9);
        publish_intent.putExtra(InputActivity.EDIT_STATE, InputActivity.PUBLISH_STATE);
        startActivityForResult(publish_intent, IntentStatic.PUBLISH);
    }

    @Override
    public void gotoNews() {
        Intent new_intent = new Intent(getContext(), NewsActivity.class);
        startActivity(new_intent);
    }

    @Override
    public void showFooter() {
        dotsTextView.start();
    }

    @Override
    public void showNoData() {
        dotsTextView.stop();
        LoadingText.setText("没有动态啦");
    }

    @Override
    public void setEditCallback(InputBoxLayout.CallBack callback) {
        inputBoxLayout.setCallBack(callback);
    }

    @Override
    public String getEdittextText() {
        return null;
    }

    @Override
    public void hideTitle() {
        if (!is_titleMove) {
            int[] xy = new int[2];
            title.getLocationOnScreen(xy);
            if (xy[1] >= 0) {
                ValueAnimator animator = ValueAnimator.ofInt(0, title.getHeight());
                animator.setDuration(1000);

                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int curValue = (int) animation.getAnimatedValue();
                        title.layout(0, -curValue, title.getWidth(), -curValue + title.getHeight());
                    }
                });
                animator.start();
                is_titleMove = true;
                divider_line.setVisibility(View.INVISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        is_titleMove = false;
                    }
                }, 1000);
            }
        }
    }

    @Override
    public void showTitle() {
        if (!is_titleMove) {
            int[] xy = new int[2];
            title.getLocationOnScreen(xy);
            if (xy[1] < -10) {
                ValueAnimator animator = ValueAnimator.ofInt(-title.getHeight(), 0);
                animator.setDuration(1000);

                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int curValue = (int) animation.getAnimatedValue();
                        title.layout(0, curValue, title.getWidth(), curValue + title.getHeight());
                    }
                });
                animator.start();
                is_titleMove = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        is_titleMove = false;
                        divider_line.setVisibility(View.VISIBLE);
                    }
                }, 1000);
            }
        }
    }

    @Override
    public void setRefreshState(boolean is) {
        is_refresh = is;
    }

    @Override
    public void setLoadState(boolean is) {
        is_loadMore = is;
    }

    public boolean is_loadMore() {
        return is_loadMore;
    }

    public boolean is_refresh() {
        return is_refresh;
    }

    @Override
    public void refreshPager() {
    }

    public void deleteOne(int position) {
        adpter_realm.notifyDataSetChanged();
        if (position != adpter_realm.getCount()) {
            listView.setSelection(position);
        }
    }

    @Override
    public Realm getRealm() {
        return realm;
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentStatic.PUBLISH && resultCode == IntentStatic.PUBLISH_SUCCESS) {
            if (ptrFrameLayout != null) {
                ptrFrameLayout.autoRefresh();
            }
        }
    }
}
