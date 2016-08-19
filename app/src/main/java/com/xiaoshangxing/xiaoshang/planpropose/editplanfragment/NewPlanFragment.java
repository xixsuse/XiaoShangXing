package com.xiaoshangxing.xiaoshang.planpropose.editplanfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseFragment;

/**
 * Created by quchwe on 2016/7/23 0023.
 */
public class NewPlanFragment extends BaseFragment implements NewPlanContract.View,View.OnClickListener {
    public static final String TAG = BaseFragment.TAG+"-NewPlanFrg";

    private  NewPlanContract.Presenter mPresenter;

    private Button back;
    private TextView textTitle;
    private RelativeLayout rl_mytitle;
    private EditText et_planName;
    private EditText et_plan_info;
    private EditText et_limitNumber;
    private EditText et_planTime;
    public static NewPlanFragment newInstance() {

        Bundle args = new Bundle();

        NewPlanFragment fragment = new NewPlanFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_new_plan,container,false);

        initView(root);


        return root;
    }



    private void initView(View root) {
        back = (Button) root.findViewById(R.id.back);
        textTitle = (TextView) root.findViewById(R.id.textTitle);
        rl_mytitle = (RelativeLayout) root.findViewById(R.id.rl_mytitle);
        et_planName = (EditText) root.findViewById(R.id.et_planName);
        et_plan_info = (EditText) root.findViewById(R.id.et_plan_info);
        et_limitNumber = (EditText) root.findViewById(R.id.et_limitNumber);
        et_planTime = (EditText) root.findViewById(R.id.et_planTime);

        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void showEditComplete() {

    }

    @Override
    public void getNewPlan() {

    }

    @Override
    public void showEditError() {

    }

    @Override
    public void setmPresenter(@Nullable NewPlanContract.Presenter presenter) {
        if (presenter ==null){
            this.mPresenter = presenter;
        }
    }
}
