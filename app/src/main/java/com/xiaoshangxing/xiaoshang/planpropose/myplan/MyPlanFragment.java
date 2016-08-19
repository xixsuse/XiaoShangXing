package com.xiaoshangxing.xiaoshang.planpropose.myplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.xiaoshang.planpropose.PlanProposeActivity;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by quchwe on 2016/7/25 0025.
 */
public class MyPlanFragment extends BaseFragment implements MyPlanContract.View,View.OnClickListener,TextWatcher {

    public static final String TAG = BaseFragment.TAG+"-MyPlan";
    private MyPlanContract.Presenter mPresenter;

    @Bind(R.id.rv_my_planList)
    RecyclerView myPlanRV;
    @Bind(R.id.back)
    Button back;
    @Bind(R.id.tv_text)
    TextView noPlanText;
    @Bind(R.id.hide_trasmit)
    ImageView transmit;

    @Bind(R.id.hide_delete)
    ImageView hide_delete;
    @Bind(R.id.hideMenu)
    RelativeLayout hideMenu;

    MyPlanAdap adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_plan,container,false);
        ButterKnife.bind(this,root);
        back.setOnClickListener(this);
        hide_delete.setOnClickListener(this);
        transmit.setOnClickListener(this);
        adapter = new MyPlanAdap(getActivity(),this,mPresenter.getMyPlanList());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myPlanRV.setLayoutManager(linearLayoutManager);
        myPlanRV.setAdapter(adapter);
        myPlanRV.setItemAnimator(new DefaultItemAnimator());
        return root;
    }

    public static MyPlanFragment newInstance() {

        Bundle args = new Bundle();

        MyPlanFragment fragment = new MyPlanFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.hide_delete:
                showCancelPlan();
                break;
            case R.id.hide_trasmit:
                Intent intent=new Intent(mContext, SelectPersonActivity.class);
                //intent.putExtra(SelectPersonActivity.TRANSMIT_TYPE, SelectPersonActivity.SCHOOL_HELP_TRANSMIT);
                PlanProposeActivity p =  (PlanProposeActivity)mContext;
                p.startActivityForResult(intent, PlanProposeActivity.SELECT_PERSON);
                break;
        }
    }

    @Override
    public void showMyPlanList() {

    }

    @Override
    public void showPlanLongClick() {

    }

    @Override
    public void setRvClick() {

    }

    @Override
    public void showDialog() {

    }

    @Override
    public void longClickPop() {

    }

    @Override
    public void showSelect() {

    }

    @Override
    public void showDeleteDialog(boolean b) {
        PlanProposeActivity planProposeActivity = (PlanProposeActivity)getActivity();
            if (b){
                hideMenu.setVisibility(View.VISIBLE);
                planProposeActivity.setHideMenu(true);
            }else {
                hideMenu.setVisibility(View.GONE);
                adapter.showSelectCircle(false);
                planProposeActivity.setHideMenu(false);
            }
    }

    @Override
    public void showNoPlan() {

    }

    @Override
    public void showCancelPlan() {
        DialogUtils.DialogMenu2 dialogMenu2 = new DialogUtils.DialogMenu2(getContext());
        dialogMenu2.addMenuItem("删除");
        dialogMenu2.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {

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
    public void setmPresenter(@NonNull MyPlanContract.Presenter presenter) {
        if (presenter!=null){
            this.mPresenter = presenter;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }



}
