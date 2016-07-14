package com.xiaoshangxing.setting.privacy.privacyFistFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseFragment;

/**
 * Created by 15828 on 2016/7/14.
 */
public class PrivacyFistFragment extends BaseFragment implements View.OnClickListener {
    private View mView;
    private TextView cancel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_privacyfist, container, false);
        cancel = (TextView) mView.findViewById(R.id.privacyfirst_cancel);
        cancel.setOnClickListener(this);
        return mView;
    }

    @Override
    public void onClick(View v) {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
