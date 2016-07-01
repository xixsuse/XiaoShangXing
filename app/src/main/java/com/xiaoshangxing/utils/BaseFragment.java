package com.xiaoshangxing.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by FengChaoQun
 * on 2016/6/22
 */
public class BaseFragment extends Fragment {
    public static final String TAG = AppContracts.TAG + "-Fragment";
    protected BaseActivity mActivity;
    protected Context mContext;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
        mContext = mActivity;
    }

}
