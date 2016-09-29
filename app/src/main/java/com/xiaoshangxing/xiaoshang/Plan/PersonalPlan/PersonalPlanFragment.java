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
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;
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

/**
 * Created by FengChaoQun
 * on 2016/9/29
 */
public class PersonalPlanFragment extends BaseFragment implements PersonalPlanContract.View {
    public static final String TAG = BaseFragment.TAG + "-PersonalPlanFragment";
    @Bind(R.id.back_text)
    TextView backText;
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


    public static PersonalPlanFragment newInstance() {
        return new PersonalPlanFragment();
    }

    private View mview;
    private Realm realm;
    private View footview;
    private DotsTextView dotsTextView;
    private TextView loadingText;
    private boolean isRefreshing;
    private boolean isLoading;
    private List<Published> publisheds = new ArrayList<>();
    private PersonalPlan_Adpter adpter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.frag_myshoolhelp, null);
        ButterKnife.bind(this, mview);
        realm = Realm.getDefaultInstance();
        initView();
        return mview;
    }

    private void initView() {
        title.setText("我的计划");
        View view = new View(getContext());
        listview.addHeaderView(view);
        footview = View.inflate(getContext(), R.layout.footer, null);
        dotsTextView = (DotsTextView) footview.findViewById(R.id.dot);
        dotsTextView.start();
        loadingText = (TextView) footview.findViewById(R.id.text);
        listview.addFooterView(footview);
//        initFresh();
        refreshData();
        showNoData();
    }

    private void initFresh() {
        LayoutHelp.initPTR(ptrFrameLayout, LoadUtils.needRefresh(LoadUtils.TIME_LOAD_SELFSALE),
                new PtrDefaultHandler() {
                    @Override
                    public void onRefreshBegin(final PtrFrameLayout frame) {
                        LoadUtils.getPublished(realm, NS.CATEGORY_SALE, LoadUtils.TIME_LOAD_SELFSALE, getContext(), false,
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
            adpter.showSelectCircle(false);
            activity.setHideMenu(false);
            cancel.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showDeleteSureDialog(final int id) {
        adpter.showSelectCircle(false);
        showHideMenu(false);

        DialogUtils.DialogMenu2 dialogMenu2 = new DialogUtils.DialogMenu2(getContext());
        dialogMenu2.addMenuItem("删除");
        dialogMenu2.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
//                mPresenter.delete();
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
        LocationUtil.bottom_FillWidth(getActivity(), dialogMenu2);
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
        for (int i = 0; i <= 10; i++) {
            publisheds.add(new Published());
        }
        showNoContentText(publisheds.size() < 1);
        adpter = new PersonalPlan_Adpter(getContext(), 1, publisheds, this, (PlanActivity) getActivity());
        listview.setAdapter(adpter);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
        ButterKnife.unbind(this);
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
                break;
            case R.id.hide_delete:
                break;
        }
    }
}
