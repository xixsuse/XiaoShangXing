package com.xiaoshangxing.xiaoshang.planpropose.shareplanfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseFragment;

/**
 * Created by quchwe on 2016/7/25 0025.
 */
public class SharePlanFragment extends BaseFragment implements SharePlanContract.View,View.OnClickListener {
    public static final String TAG = BaseFragment.TAG+"-SharePlan";



    private SharePlanContract.Presenter mPresenter;



    public static SharePlanFragment newInstance() {

        Bundle args = new Bundle();

        SharePlanFragment fragment = new SharePlanFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }



    @Override
    public void showCompleted() {

    }


    @Override
    public void setmPresenter(@Nullable SharePlanContract.Presenter presenter) {

        if (presenter!=null){
            this.mPresenter = presenter;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                getActivity().getSupportFragmentManager().popBackStack();
                break;

        }

    }

}
