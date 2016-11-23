package com.xiaoshangxing.xiaoshang.Reward.RewardCollect;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.LoadUtils;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.netUtil.OperateUtils;
import com.xiaoshangxing.Network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.layout.LayoutHelp;
import com.xiaoshangxing.utils.layout.loadingview.DotsTextView;
import com.xiaoshangxing.utils.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.xiaoshang.Reward.RewardActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by FengChaoQun
 * on 2016/7/21
 */
public class RewardCollectFragment extends BaseFragment implements RewardCollectContract.View {
    public static final String TAG = BaseFragment.TAG + "-CollectFragment";

    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.reflesh_layout)
    PtrFrameLayout ptrFrameLayout;
    @Bind(R.id.hide_trasmit)
    ImageView hideTrasmit;
    @Bind(R.id.hide_delete)
    ImageView hideDelete;
    @Bind(R.id.hideMenu)
    RelativeLayout hideMenu;
    @Bind(R.id.no_content)
    TextView noContent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.double_title_refresh_listview_hidebutton, null);
        ButterKnife.bind(this, view);
        setmPresenter(new RewardCollectPresenter(this, getContext()));
        initFresh();
        initView();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d("collect", "" + isVisible());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static RewardCollectFragment newInstance() {
        return new RewardCollectFragment();
    }

    private RewardCollect_Adpter adpter;
    private List<Published> publisheds = new ArrayList<Published>();
    private View view;
    private RewardActivity activity;
    private RewardCollectContract.Presenter mPresenter;
    private View footview;
    private DotsTextView dotsTextView;
    private TextView loadingText;

    private void initView() {
        View view = new View(getContext());
        //        解决在4.3上不能设置header问题
        listview.setAdapter(null);
        listview.addHeaderView(view);
        footview = View.inflate(getContext(), R.layout.footer, null);
        dotsTextView = (DotsTextView) footview.findViewById(R.id.dot);
        dotsTextView.start();
        listview.addFooterView(footview);
        activity = (RewardActivity) getActivity();
        title.setText("收藏");
        refreshPager();
    }

    private void refreshPager() {
        publisheds = realm.where(Published.class)
                .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_REWARD))
                .equalTo(NS.COLLECT_STATU, "1")
                .findAllSorted(NS.CREATETIME, Sort.DESCENDING);
        showNoContentText(publisheds.size() < 1);
        adpter = new RewardCollect_Adpter(getContext(), 1, publisheds, this, activity);
        listview.setAdapter(adpter);
        if (publisheds.size() > 0) {
            noContent.setVisibility(View.GONE);
        } else {
            noContent.setVisibility(View.VISIBLE);
            noContent.setText("你还没有收藏的悬赏");
        }
    }

    private void initFresh() {
        LayoutHelp.initPTR(ptrFrameLayout, LoadUtils.needRefresh(LoadUtils.TIME_COLLECT_REWARD),
                new PtrDefaultHandler() {
                    @Override
                    public void onRefreshBegin(final PtrFrameLayout frame) {
                        LoadUtils.getCollected(realm, NS.COLLECT_REWARD, LoadUtils.TIME_COLLECT_REWARD, getContext(),
                                new LoadUtils.AroundLoading() {
                                    @Override
                                    public void before() {
                                        refreshPager();
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
    public void showNoData() {
        dotsTextView.stop();
        loadingText.setText("没有动态啦");
    }

    @Override
    public void showFooter() {
        dotsTextView.start();
        loadingText.setText("加载中");
    }

    public void showCollectDialog(final int id) {
        DialogUtils.DialogMenu2 dialogMenu2 = new DialogUtils.DialogMenu2(getContext());
        dialogMenu2.addMenuItem("取消收藏");
        dialogMenu2.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                OperateUtils.operate(id, getContext(), true, NS.COLLECT, true, new SimpleCallBack() {
                    @Override
                    public void onSuccess() {
                        noticeDialog("已取消收藏");
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Published published = realm.where(Published.class).equalTo(NS.ID, id).findFirst();
                                published.setCollectStatus("0");
                            }
                        });
                        refreshPager();
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
        LocationUtil.bottom_FillWidth(getActivity(), dialogMenu2);
    }

    public void noticeDialog(String message) {
        DialogUtils.Dialog_No_Button dialog_no_button = new DialogUtils.Dialog_No_Button(getActivity(), message);
        final Dialog alertDialog = dialog_no_button.create();
        alertDialog.show();
        LocationUtil.setWidth(getActivity(), alertDialog,
                getActivity().getResources().getDimensionPixelSize(R.dimen.x420));

        new Handler().postDelayed(new Runnable() {
            public void run() {
                alertDialog.dismiss();
            }
        }, 1000);
    }

    public void showHideMenu(boolean is) {
        RewardActivity activity = (RewardActivity) getActivity();
        if (is) {
            hideMenu.setVisibility(View.VISIBLE);
            activity.setCollect(true);
        } else {
            hideMenu.setVisibility(View.GONE);
            adpter.showSelectCircle(false);
            activity.setCollect(false);
        }
    }

    public void showDeleteSureDialog(final int publishedId) {
        adpter.showSelectCircle(false);
        showHideMenu(false);

        DialogUtils.DialogMenu2 dialogMenu2 = new DialogUtils.DialogMenu2(getContext());
        dialogMenu2.addMenuItem("删除");
        dialogMenu2.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                OperateUtils.deleteOnePublished(publishedId, getContext(), RewardCollectFragment.this, new SimpleCallBack() {
                    @Override
                    public void onSuccess() {
                        refreshPager();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast("删除异常");
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
        LocationUtil.bottom_FillWidth(getActivity(), dialogMenu2);
    }

    public void showNoContentText(boolean is) {
        if (is) {
            noContent.setVisibility(View.VISIBLE);
            listview.setVisibility(View.INVISIBLE);
        } else {
            noContent.setVisibility(View.INVISIBLE);
            listview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setmPresenter(@Nullable RewardCollectContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @OnClick({R.id.back, R.id.hide_trasmit, R.id.hide_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                getFragmentManager().popBackStack();
                break;
            case R.id.hide_trasmit:
//                adpter.showSelectCircle(false);
//                showHideMenu(false);
//                activity.gotoSelectPerson();
                break;
            case R.id.hide_delete:
//                showDeleteSureDialog();
                break;
        }
    }

}
