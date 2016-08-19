package com.xiaoshangxing.xiaoshang.idlesale.MyIdleSale;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.xiaoshangxing.xiaoshang.idlesale.IdleBean;
import com.xiaoshangxing.xiaoshang.idlesale.IdleSaleActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by quchwe on 2016/8/7 0007.
 */

public class MyIdleSaleFragment extends BaseFragment implements View.OnClickListener,MyIdleContract.View {

    public static final String TAG = BaseFragment.TAG+"-MyIdle";

    private MyIdleContract.Presenter mPresenter;
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
    List<IdleBean> idles = new ArrayList<>();
    MyIdleAdapter adapter;
    public static MyIdleSaleFragment newInstance() {

        Bundle args = new Bundle();

        MyIdleSaleFragment fragment = new MyIdleSaleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_my_idle,container,false);
        ButterKnife.bind(this,root);
        back.setOnClickListener(this);
        hide_delete.setOnClickListener(this);
        transmit.setOnClickListener(this);
        initList();
        adapter = new MyIdleAdapter(getActivity(),this,idles);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myPlanRV.setLayoutManager(linearLayoutManager);
        myPlanRV.setAdapter(adapter);
        myPlanRV.setItemAnimator(new DefaultItemAnimator());
        return root;
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
               // intent.putExtra(SelectPersonActivity.TRANSMIT_TYPE, SelectPersonActivity.SCHOOL_HELP_TRANSMIT);
                IdleSaleActivity p =  (IdleSaleActivity) mContext;
                p.startActivityForResult(intent, IdleSaleActivity.SELECT_PERSON);
                break;
        }
    }
    private void initList(){
        IdleBean id= new IdleBean();
        id.setTime("20分钟前");
        id.setText("这是闲置的说明，最多可以显示三行这是闲置的说明，最多可以显示三行这是闲置的说明，最多可以显示三行这是闲置的说明，最多可以显示三行");
        id.setComplete(false);
        id.setDepartment("李园");
        id.setAcademy("设计学院");
        id.setPrice("1000");
        idles.add(id);
        id.setComplete(true);
        idles.add(id);


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
        IdleSaleActivity planProposeActivity = (IdleSaleActivity) getActivity();
        if (b){
            hideMenu.setVisibility(View.VISIBLE);
            planProposeActivity.setHideMenu(true);
        }else {
            hideMenu.setVisibility(View.GONE);
            adapter.showSelectCircle(false, 0);
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
    public void setmPresenter(@Nullable MyIdleContract.Presenter presenter) {
            this.mPresenter = presenter;
    }
}
