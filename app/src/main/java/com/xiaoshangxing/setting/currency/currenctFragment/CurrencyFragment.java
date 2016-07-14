package com.xiaoshangxing.setting.currency.currenctFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseFragment;

/**
 * Created by 15828 on 2016/7/14.
 */
public class CurrencyFragment extends BaseFragment implements View.OnClickListener{
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_currency, container, false);


        return mView;
    }

    @Override
    public void onClick(View v) {

    }
}
