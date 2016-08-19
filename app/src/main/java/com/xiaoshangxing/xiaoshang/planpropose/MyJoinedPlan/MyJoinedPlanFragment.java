package com.xiaoshangxing.xiaoshang.planpropose.MyJoinedPlan;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Bean;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.xiaoshang.RecyclerViewUtil;
import com.xiaoshangxing.xiaoshang.planpropose.PlanList;
import com.xiaoshangxing.xiaoshang.planpropose.PlanProposeActivity;
import com.xiaoshangxing.xiaoshang.planpropose.planinfofragment.PlanInfoFragment;
import com.xiaoshangxing.xiaoshang.planpropose.planproposefragment.PlanProposeAdpter;
import com.xiaoshangxing.xiaoshang.planpropose.planproposefragment.PlanProposeContract;
import com.xiaoshangxing.xiaoshang.planpropose.planproposefragment.PlanProposePresenter;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by quchwe on 2016/7/25 0025.
 */
//public class MyJoinedPlanFragment extends BaseFragment implements MyJoinedPlanContract.View,View.OnClickListener {
//
//    public static final String TAG = BaseFragment.TAG+"-MyJoined";
//
//    private MyJoinedPlanContract.Presenter mPresenter;
//
//
//    public static MyJoinedPlanFragment newInstance() {
//
//        Bundle args = new Bundle();
//
//        MyJoinedPlanFragment fragment = new MyJoinedPlanFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }
//
//    @Override
//    public void onClick(View v) {
//
//    }
//
//    @Override
//    public void showPlanList() {
//
//    }
//
//    @Override
//    public void showReleseDialog() {
//
//    }
//
//    @Override
//    public void setmPresenter(@NonNull MyJoinedPlanContract.Presenter presenter) {
//            if (presenter!=null){{
//                this.mPresenter = presenter;
//            }}
//    }
//}
public class MyJoinedPlanFragment extends BaseFragment implements PlanProposeContract.View, View.OnClickListener {

    public static final String TAG = BaseFragment.TAG+"-PlanPro";

    private PlanProposeContract.Presenter mPresenter;
    private Button back;
    private TextView textTitle;
    private ImageView ib_add;
    private RelativeLayout rl_mytitle;
    private Button btn_calender_add_info;
    private RecyclerView rv_plan_propose;
    private PopupWindow mPopup;
    private List<PlanList> planLists = new ArrayList<>();

    PlanProposeAdpter mAdapter;
    public static MyJoinedPlanFragment newInstance() {

        Bundle args = new Bundle();

        MyJoinedPlanFragment fragment = new MyJoinedPlanFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_plan_propose,container,false);
        initView(root);
        ib_add.setVisibility(View.GONE);
        setmPresenter(new PlanProposePresenter(new Bean(),this));
        planLists = mPresenter.getPlanList();
        mAdapter = new PlanProposeAdpter(getContext(),planLists,this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);


        rv_plan_propose.setLayoutManager(manager);
        rv_plan_propose.setItemAnimator(new DefaultItemAnimator());
        //mAdapter.addItemViewDelegate(new PlanProposeAdapter(getActivity()));
//        mAdapter.addItemViewDelegate(new PlanProposeAdapter(getActivity(),new PlanProposeAdapter.OnDialogListener() {
//            @Override
//            public void showReleseDialog(PlanList planList) {
//
//            }
//        }));
        rv_plan_propose.setAdapter(mAdapter);
        mPresenter.toPlanInfo();
        mPresenter.toPlanInfo();
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                    getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.ib_add:

                break;
            case R.id.btn_calender_add_info:

                break;
        }
    }

    @Override
    public void showDialog(final PlanList plan) {
        final DialogUtils.DialogMenu dialogMenu = new DialogUtils.DialogMenu(getActivity());
        dialogMenu.addMenuItem("分享");
        dialogMenu.show();
        dialogMenu.setMenuListener(new DialogUtils.DialogMenu.MenuListener() {

            @Override
            public void onItemSelected(int position, String item) {
                dialogMenu.dismiss();
                if (position==0){
                    showShareDialog(plan);
                }
            }

            @Override
            public void onCancel() {
                dialogMenu.dismiss();
            }
        });
        Log.d(TAG, "+");
        LocationUtil.bottom_FillWidth(getActivity(),dialogMenu);
    }

    @Override
    public void showPopupClick() {
    }
    @Override
    public void setRvOnClick() {
        mAdapter.setOnClickListener(new PlanProposeAdpter.OnItemClikListener(){
            @Override
            public void onItemClick(View view, int position) {
                PlanInfoFragment frag = ((PlanProposeActivity) mActivity).getmPlanInfoView();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", planLists.get(position));
                frag.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                                R.anim.slide_in_left, R.anim.slide_out_left)
                        .replace(R.id.fl_plan, frag, PlanInfoFragment.TAG)
                        .addToBackStack(PlanInfoFragment.TAG)
                        .commit();
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });
    }

    @Override
    public void sharePlan() {

    }

    @Override
    public void showShareDialog(PlanList planList) {

    }

    @Override
    public void setRefreshState(boolean is) {

    }

    @Override
    public boolean isRefreshing() {
        return false;
    }

    @Override
    public void setLoadState(boolean is) {

    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public void refreshPager() {

    }

    @Override
    public void autoRefresh() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void showFooter() {

    }

    @Override
    public void clickOnRule() {

    }

//    @Override
//    public void sharePlan() {
//
//    }
//
//    @Override
//    public void showShareDialog(final PlanList planList) {
//
//    }
//
//    @Override
//    public void setRefreshState(boolean is) {
//
//    }
//
//    @Override
//    public boolean isRefreshing() {
//        return false;
//    }
//
//    @Override
//    public void setLoadState(boolean is) {
//
//    }
//
//    @Override
//    public boolean isLoading() {
//        return false;
//    }
//
//    @Override
//    public void refreshPager() {
//
//    }
//
//    @Override
//    public void autoRefresh() {
//
//    }
//
//    @Override
//    public void showNoData() {
//
//    }
//
//    @Override
//    public void showFooter() {
//
//    }
//
//    @Override
//    public void clickOnRule() {
//
//    }

    @Override
    public void setmPresenter(@Nullable PlanProposeContract.Presenter presenter) {
        if (presenter==null){
            return;
        }
        this.mPresenter = presenter;
    }

    private void initView(View root) {
        back = (Button) root.findViewById(R.id.back);
        textTitle = (TextView) root.findViewById(R.id.textTitle);
        textTitle.setText("我加入的计划");
        ib_add = (ImageView) root.findViewById(R.id.ib_add);
        rl_mytitle = (RelativeLayout) root.findViewById(R.id.rl_mytitle);

        rv_plan_propose = (RecyclerView) root.findViewById(R.id.rv_plan_propose);

        back.setOnClickListener(this);
        ib_add.setOnClickListener(this);



    }


}
