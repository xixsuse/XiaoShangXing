package com.xiaoshangxing.xiaoshang.planpropose.planinfofragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.xiaoshang.planpropose.PlanList;
import com.xiaoshangxing.xiaoshang.planpropose.PlanProposeActivity;

/**
 * Created by quchwe on 2016/7/23 0023.
 */
public class PlanInfoFragment extends BaseFragment implements PlanInfoContract.View,View.OnClickListener{
    public static final String TAG = BaseFragment.TAG+"-PlanInfo";

    private PlanInfoContract.Presenter mPresenter;

    public static PlanInfoFragment newInstance() {

        Bundle args = new Bundle();

        PlanInfoFragment fragment = new PlanInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private Button back;
    private TextView textTitle;
    private CirecleImage iv_headImage;
    private TextView tv_name;
    private TextView tv_academy;
    private TextView tv_plan_info;
    private TextView tv_joinNumber;
    private TextView tv_plan_name;
    private TextView tv_people_number_limit;
    private TextView tv_plan_time_limit;
    private TextView tv_faqiname;
    private TextView tv_faqiTime;
    private Button btn_inviteFriends;
    private Button btn_joinPlan;
    private LinearLayout ll_plan_info_twoButton;

    private PlanList mPlanList;
    private ImageView showCompleted;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_plan_info,container,false);
        initView(root);

        mPlanList = (PlanList)getArguments().getSerializable("data");
        //mPlanList = mPresenter.getPlanList();
        if (mPlanList.isFull()){
            tv_joinNumber.setTextColor(getResources().getColor(R.color.g0));
            ll_plan_info_twoButton.setVisibility(View.GONE);
        }else {
            tv_joinNumber.setTextColor(getResources().getColor(R.color.green1));
            ll_plan_info_twoButton.setVisibility(View.VISIBLE);
        }
        if (mPlanList.isCompleted()){
            showCompleted.setVisibility(View.VISIBLE);
        }
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                    getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btn_inviteFriends:
                    onInviteFriendsClick();
                break;
            case R.id.btn_joinPlan:

                break;
        }
    }

    @Override
    public void onJoinPlanClick() {

    }

    @Override
    public void onInviteFriendsClick() {
        Intent intent=new Intent(mContext, SelectPersonActivity.class);
        //intent.putExtra(SelectPersonActivity.TRANSMIT_TYPE, SelectPersonActivity.SCHOOL_HELP_TRANSMIT);
        PlanProposeActivity p =  (PlanProposeActivity)mContext;
        p.startActivityForResult(intent, PlanProposeActivity.SELECT_PERSON);
    }

    @Override
    public void showFullPeople() {

    }

    @Override
    public void setViewInfo() {
        iv_headImage.setImageBitmap(mPlanList.getBitmap());

        tv_name.setText(mPlanList.getName());
        tv_academy.setText(mPlanList.getAcademy());
        tv_plan_name.setText(mPlanList.getPlanName());
        tv_plan_info.setText(mPlanList.getText());

        if (mPlanList.getLimitPepoleNumber()!=null&&mPlanList.getLimitPepoleNumber()==mPlanList.getPaticipateNumber()){
            tv_joinNumber.setTextColor(getResources().getColor(R.color.g0));
            ll_plan_info_twoButton.setVisibility(View.GONE);
        }
        tv_joinNumber.setText(mPlanList.getPaticipateNumber());
        tv_plan_time_limit.setText(mPlanList.getLimitTime());
        tv_faqiname.setText(mPlanList.getName());
        tv_faqiTime.setText(mPlanList.getPlanProposeTime());
    }

    @Override
    public void setmPresenter(@Nullable PlanInfoContract.Presenter presenter) {
        if (presenter!=null){
            this.mPresenter = presenter;
        }
    }
    private void initView(View root) {
        showCompleted = (ImageView)root.findViewById(R.id.iv_showComplete);
        back = (Button) root.findViewById(R.id.back);
        textTitle = (TextView) root.findViewById(R.id.textTitle);
        iv_headImage = (CirecleImage) root.findViewById(R.id.iv_headImage);
        tv_name = (TextView) root.findViewById(R.id.tv_name);
        tv_academy = (TextView) root.findViewById(R.id.tv_academy);
        tv_plan_info = (TextView) root.findViewById(R.id.tv_plan_info);
        tv_joinNumber = (TextView) root.findViewById(R.id.tv_joinNumber);
        tv_plan_name = (TextView) root.findViewById(R.id.tv_plan_name);
        tv_people_number_limit = (TextView) root.findViewById(R.id.tv_people_number_limit);
        tv_plan_time_limit = (TextView) root.findViewById(R.id.tv_plan_time_limit);
        tv_faqiname = (TextView) root.findViewById(R.id.tv_faqiname);
        tv_faqiTime = (TextView) root.findViewById(R.id.tv_faqiTime);
        btn_inviteFriends = (Button) root.findViewById(R.id.btn_inviteFriends);
        btn_joinPlan = (Button) root.findViewById(R.id.btn_joinPlan);
        ll_plan_info_twoButton = (LinearLayout) root.findViewById(R.id.ll_plan_info_twoButton);

        back.setOnClickListener(this);
        btn_inviteFriends.setOnClickListener(this);
        btn_joinPlan.setOnClickListener(this);
    }
}
