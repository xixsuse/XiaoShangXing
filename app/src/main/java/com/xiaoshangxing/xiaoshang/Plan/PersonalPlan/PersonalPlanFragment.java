package com.xiaoshangxing.xiaoshang.Plan.PersonalPlan;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.DialogLocationAndSize;
import com.xiaoshangxing.utils.layout.LayoutHelp;
import com.xiaoshangxing.utils.layout.loadingview.DotsTextView;
import com.xiaoshangxing.utils.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.xiaoshang.Plan.PlanActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by FengChaoQun
 * on 2016/9/29
 */
public class PersonalPlanFragment extends BaseFragment implements PersonalPlanContract.View {
    public static final String TAG = BaseFragment.TAG + "-PersonalPlanFragment";
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.cancel)
    LinearLayout cancel;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
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
        mview = inflater.inflate(R.layout.double_title_refresh_listview_hidebutton, null);
        ButterKnife.bind(this, mview);
        initView();
        return mview;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static PersonalPlanFragment newInstance() {
        return new PersonalPlanFragment();
    }

    private View mview;
    private View footview;
    private DotsTextView dotsTextView;
    private TextView loadingText;
    private boolean isRefreshing;
    private boolean isLoading;
    private PlanActivity activity;
    private RealmResults<Published> publisheds;
    private PersonalPlan_Adpter_realm adpter_realm;

    private void initView() {
        title.setText("我的计划");
        View view = new View(getContext());
        listview.addHeaderView(view);
        listview.setDividerHeight(0);
        footview = View.inflate(getContext(), R.layout.footer, null);
        dotsTextView = (DotsTextView) footview.findViewById(R.id.dot);
        dotsTextView.start();
        loadingText = (TextView) footview.findViewById(R.id.text);
        listview.addFooterView(footview);
        activity = (PlanActivity) getActivity();
        initFresh();
        refreshData();
        showNoData();
    }

    private void initFresh() {
        LayoutHelp.initPTR(ptrFrameLayout, LoadUtils.needRefresh(LoadUtils.TIME_LOAD_SELFPLAN),
                new PtrDefaultHandler() {
                    @Override
                    public void onRefreshBegin(final PtrFrameLayout frame) {
                        LoadUtils.getPublished(realm, NS.CATEGORY_PLAN, LoadUtils.TIME_LOAD_SELFPLAN, getContext(), true,
                                new LoadUtils.AroundLoading() {
                                    @Override
                                    public void before() {
                                    }

                                    @Override
                                    public void complete() {
                                        frame.refreshComplete();
                                    }

                                    @Override
                                    public void onSuccess() {
                                        refreshData();
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
    public void showHideMenu(boolean is) {
        PlanActivity activity = (PlanActivity) getActivity();
        if (is) {
            hideMenu.setVisibility(View.VISIBLE);
            activity.setHideMenu(true);
            cancel.setVisibility(View.VISIBLE);
            back.setVisibility(View.GONE);
        } else {
            hideMenu.setVisibility(View.GONE);
            adpter_realm.showSelectCircle(false);
            activity.setHideMenu(false);
            cancel.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showDeleteSureDialog(final int id) {
        adpter_realm.showSelectCircle(false);
        showHideMenu(false);

        DialogUtils.DialogMenu2 dialogMenu2 = new DialogUtils.DialogMenu2(getContext());
        dialogMenu2.addMenuItem("删除");
        dialogMenu2.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                OperateUtils.deleteOnePublished(id, getContext(), PersonalPlanFragment.this, new SimpleCallBack() {
                    @Override
                    public void onSuccess() {
                        refreshData();
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
        DialogLocationAndSize.bottom_FillWidth(getActivity(), dialogMenu2);
    }

    public void showDeleteSureDialog2(final List<String> ids) {
        adpter_realm.showSelectCircle(false);
        showHideMenu(false);

        DialogUtils.DialogMenu2 dialogMenu2 = new DialogUtils.DialogMenu2(getContext());
        dialogMenu2.addMenuItem("删除");
        dialogMenu2.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                OperateUtils.deletePublisheds(ids, getContext(), PersonalPlanFragment.this, new SimpleCallBack() {
                    @Override
                    public void onSuccess() {
                        showToast("删除成功");
                        refreshData();
                        adpter_realm.getSelectIds().clear();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast("删除异常");
                        refreshData();
                        adpter_realm.getSelectIds().clear();
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
    public void refreshData() {
        publisheds = realm.where(Published.class)
                .equalTo(NS.USER_ID, TempUser.id)
                .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_PLAN))
                .findAll().sort(NS.CREATETIME, Sort.DESCENDING);
        showNoContentText(publisheds.size() < 1);
        adpter_realm = new PersonalPlan_Adpter_realm(getContext(), publisheds, this, (PlanActivity) getActivity());
        listview.setAdapter(adpter_realm);
        if (publisheds.size() > 0) {
            noContent.setVisibility(View.GONE);
        } else {
            noContent.setVisibility(View.VISIBLE);
            noContent.setText("你还没有发布计划");
        }
    }

    @Override
    public void showNoData() {
        dotsTextView.stop();
        loadingText.setText("没有更多啦");
    }

    @Override
    public void showFooter() {
        dotsTextView.start();
        loadingText.setText("加载中");
    }

    @Override
    public void setmPresenter(@Nullable PersonalPlanContract.Presenter presenter) {

    }

    @OnClick({R.id.back, R.id.cancel, R.id.hide_trasmit, R.id.hide_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                getFragmentManager().popBackStack();
                break;
            case R.id.cancel:
                showHideMenu(false);
                break;
            case R.id.hide_trasmit:
                if (adpter_realm.getSelectIds().isEmpty()) {
                    return;
                }
                adpter_realm.showSelectCircle(false);
                showHideMenu(false);
                activity.setPublishIdsForTransmit(adpter_realm.getSelectIds());
                activity.gotoSelectOnePerson();
                break;
            case R.id.hide_delete:
                if (adpter_realm.getSelectIds().size() == 0) {
                    showToast("请选择要删除的内容");
                    return;
                }
                showDeleteSureDialog2(adpter_realm.getSelectIds());
                break;
        }
    }
}
