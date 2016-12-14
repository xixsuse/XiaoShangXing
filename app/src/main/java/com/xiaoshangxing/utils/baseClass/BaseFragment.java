package com.xiaoshangxing.utils.baseClass;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.xiaoshangxing.utils.AppContracts;
import com.xiaoshangxing.utils.customView.dialog.LoadingDialog;

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
    private boolean isDefaultPopFragment = true;//标记是否默认右滑关闭fragment

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
        mContext = mActivity;
        realm = mActivity.getRealm();
    }

    @Override
    public void onResume() {
        super.onResume();
        setDefaultRightSlide();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setDefaultRightSlide();
        }
    }

    /*
    **describe:设置默认右滑响应事件
    */
    private void setDefaultRightSlide() {
        if (isDefaultPopFragment) {
            setRightSlide(new BaseActivity.RightSlide() {
                @Override
                public boolean rightSlide() {
                    getFragmentManager().popBackStack();
                    return true;
                }
            });
        }
    }

    /*
    **describe:设置右滑关闭当前activity
    */
    public void setCloseActivity() {
        setRightSlide(new BaseActivity.RightSlide() {
            @Override
            public boolean rightSlide() {
                mActivity.finish();
                return true;
            }
        });
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

    public void showToast(String toast) {
        Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
    }

    public void setRightSlide(BaseActivity.RightSlide rightSlide) {
        mActivity.setRightSlide(rightSlide);
    }

    public boolean isDefaultPopFragment() {
        return isDefaultPopFragment;
    }

    public void setDefaultPopFragment(boolean defaultPopFragment) {
        isDefaultPopFragment = defaultPopFragment;
    }

}
