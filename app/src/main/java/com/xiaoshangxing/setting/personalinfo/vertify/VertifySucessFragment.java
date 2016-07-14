package com.xiaoshangxing.setting.personalinfo.vertify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseFragment;

/**
 * Created by 15828 on 2016/7/12.
 */
public class VertifySucessFragment extends BaseFragment implements View.OnClickListener{
    private View mView;
    private TextView back;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_personalinfo_vertifysucess,container,false);
        back = (TextView) mView.findViewById(R.id.vertifysucess_back);
        return mView;
    }

    @Override
    public void onClick(View v) {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
