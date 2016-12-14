package com.xiaoshangxing.wo.setting.personalinfo.vertify.vertifyAgreementFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.baseClass.BaseFragment;

/**
 * Created by 15828 on 2016/7/15.
 */
public class VertifyAgreementFragment extends BaseFragment {
    private View mView;
    private Button agree;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_vertifyagreement, container, false);
        agree = (Button) mView.findViewById(R.id.agree);
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return mView;
    }
}
