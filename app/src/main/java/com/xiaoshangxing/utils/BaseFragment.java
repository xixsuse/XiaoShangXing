package com.xiaoshangxing.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/6/22
 */
public class BaseFragment extends Fragment {
    public static final String TAG = AppContracts.TAG + "-Fragment";
    protected BaseActivity mActivity;
    protected Context mContext;
    protected Realm realm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
        mContext = mActivity;
        realm = mActivity.getRealm();
    }

    /*
    **describe:显示LoadingDialog
    */

    public void showLoadingDialog(String text) {
        mActivity.showLoadingDialog(text);
    }

    /*
    **describe:关闭LoadingDialog
    */
    public void hideLoadingDialog() {
        mActivity.hideLoadingDialog();
    }

    /*
    **describe:设置LoadingDialog关闭时的回调
    */
    public void setonDismiss(LoadingDialog.onDismiss on) {
        mActivity.setonDismiss(on);
    }

    public void showToast(String toast){
        Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
    }
}
