package com.xiaoshangxing.xiaoshang.planpropose.planproposefragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.input_activity.InputActivity;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.utils.pull_refresh.PtrHandler;
import com.xiaoshangxing.utils.pull_refresh.StoreHouseHeader;
import com.xiaoshangxing.xiaoshang.planpropose.MyJoinedPlan.MyJoinedPlanFragment;
import com.xiaoshangxing.xiaoshang.planpropose.PlanList;
import com.xiaoshangxing.xiaoshang.planpropose.PlanProposeActivity;
import com.xiaoshangxing.xiaoshang.planpropose.ReleasePopUp;
import com.xiaoshangxing.xiaoshang.planpropose.myplan.MyPlanFragment;
import com.xiaoshangxing.xiaoshang.planpropose.planinfofragment.PlanInfoFragment;
import com.xiaoshangxing.xiaoshang.planpropose.shareplanfragment.SharePlanFragment;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by quchwe on 2016/7/22 0022.
 */
public class PlanProposeFragment extends BaseFragment implements PlanProposeContract.View, View.OnClickListener {


    public static final String TAG = BaseFragment.TAG+"-PlanPro";

    private PlanProposeContract.Presenter mPresenter;
    private Button back;
    private TextView textTitle;
    private ImageView ib_add;
    private RelativeLayout rl_mytitle;
    private Button btn_calender_add_info;
    private RecyclerView rv_plan_propose;
    private PlanProposeAdpter mAdapter;
    private PopupWindow mPopup;
    private List<PlanList> planLists = new ArrayList<>();
    PtrFrameLayout ptrFrameLayout;

    private boolean isRefreshing;
    private boolean isLoading;
    public static PlanProposeFragment newInstance() {
        Bundle args = new Bundle();
        PlanProposeFragment fragment = new PlanProposeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_plan_propose,container,false);
        initView(root);
        planLists = mPresenter.getPlanList();
        initFresh();
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
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                    getActivity().finish();
                break;
            case R.id.ib_add:
                    showPopupClick();
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

        LocationUtil.bottom_FillWidth(getActivity(), dialogMenu);
    }

    @Override
    public void showPopupClick() {

        ReleasePopUp r = new ReleasePopUp();
        r.popRelease(getActivity(), ib_add, R.layout.popupmenu_plan_propose,new ReleasePopUp.OnPopClickListener() {
            @Override
            public void onReadyRelease() {

                Intent intent=new Intent(getContext(), InputActivity.class);
                intent.putExtra(InputActivity.EDIT_STATE, InputActivity.LANCH_PLAN);
                startActivity(intent);
            }

            @Override
            public void onReleased() {
                PlanProposeActivity activity = (PlanProposeActivity)mActivity;
                MyPlanFragment myPlanFragment = activity.getMyPlanFragment();

//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
//                                R.anim.slide_in_left, R.anim.slide_out_left)
//                        .hide(activity.getPlanProposeFragment())
//                        .hide(activity.getmMjoinedPlanView())
//                        .hide(activity.getmPlanInfoView())
//                        .hide(activity.getSharePlanView())
//                        .hide(activity.getmNewPlanView())
//                        .show(myPlanFragment)
//                        .addToBackStack(null)
//                        .commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                                R.anim.slide_in_left, R.anim.slide_out_left)
                        .replace(R.id.fl_plan, myPlanFragment, MyPlanFragment.TAG)
                        .addToBackStack(null)
                        .commit();

            }

            @Override
            public void onJoined() {
                PlanProposeActivity activity = (PlanProposeActivity)mActivity;
                MyJoinedPlanFragment myPlanFragment = ((PlanProposeActivity)mActivity).getmMjoinedPlanView();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
//                                R.anim.slide_in_left, R.anim.slide_out_left)
//                        .hide(activity.getPlanProposeFragment())
//                        .hide(activity.getMyPlanFragment())
//                        .hide(activity.getmPlanInfoView())
//                        .hide(activity.getSharePlanView())
//                        .hide(activity.getmNewPlanView())
//                        .show(myPlanFragment)
//                        .addToBackStack(null)
//                        .commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                                R.anim.slide_in_left, R.anim.slide_out_left)
                        .replace(R.id.fl_plan, myPlanFragment, MyJoinedPlanFragment.TAG)
                        .addToBackStack(null)
                        .commit();
            }
        });
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
    public void showShareDialog(final PlanList planList) {
       final DialogUtils.DialogShowShareOtherSdk dialog = new DialogUtils.DialogShowShareOtherSdk(getActivity(),false, new DialogUtils.DialogShowShareOtherSdk.OnShareListener() {
           @Override
           public void onShareFriends() {

           }

           @Override
            public void onShareXyq() {
                SharePlanFragment frag = ((PlanProposeActivity)mActivity).getSharePlanView();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data",planList);
                frag.setArguments(bundle);
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
//                                R.anim.slide_in_left, R.anim.slide_out_left)
//                        .replace(R.id.fl_plan, frag,SharePlanFragment.TAG)
//                        .addToBackStack(PlanProposeFragment.TAG)
//                        .commit();
                Intent intent = new Intent(getContext(),InputActivity.class);
               intent.putExtra(InputActivity.TRANSMIT_TYPE, InputActivity.PUBLISH_STATE);
               startActivity(intent);

            }

            @Override
            public void onShareQQ() {

            }

            @Override
            public void onShareWeiChat() {

            }

            @Override
            public void onShareWeibo() {

            }
        });
        LocationUtil.bottom_FillWidth(getActivity(),dialog);
    }


    public boolean isRefreshing() {
        return isRefreshing;
    }

    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void refreshPager() {

    }


    private void initFresh() {
        final StoreHouseHeader header = new StoreHouseHeader(getContext());
        header.setPadding(0, getResources().getDimensionPixelSize(R.dimen.y144), 0, 20);
        header.initWithString("SWALK");
        header.setTextColor(getResources().getColor(R.color.green1));
        header.setBackgroundColor(getResources().getColor(R.color.transparent));

        header.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));

        ptrFrameLayout.setDurationToCloseHeader(2000);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
//        ptrFrameLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ptrFrameLayout.autoRefresh(false);
//            }
//        }, 100);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                isRefreshing = true;
                mPresenter.RefreshData();
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                        isRefreshing = false;
                    }
                }, 1500);
            }
        });
    }



    public void setRefreshState(boolean is) {
        isRefreshing = is;
    }


    public void setLoadState(boolean is) {
        isLoading = is;
    }



    public void autoRefresh() {

    }


    public void showNoData() {

    }

    public void showFooter() {

    }


    public void clickOnRule() {

    }

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
        ib_add = (ImageView) root.findViewById(R.id.ib_add);
        rl_mytitle = (RelativeLayout) root.findViewById(R.id.rl_mytitle);
        rv_plan_propose = (RecyclerView) root.findViewById(R.id.rv_plan_propose);
       ptrFrameLayout = (PtrFrameLayout)root.findViewById(R.id.reflesh_layout);
        back.setOnClickListener(this);
        ib_add.setOnClickListener(this);
//        btn_calender_add_info.setOnClickListener(this);


    }


}
