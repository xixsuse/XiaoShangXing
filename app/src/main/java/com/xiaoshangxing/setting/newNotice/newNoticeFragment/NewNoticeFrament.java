package com.xiaoshangxing.setting.newNotice.newNoticeFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.SwitchView;

/**
 * Created by 15828 on 2016/7/13.
 */
public class NewNoticeFrament extends BaseFragment {
    private View mView;
    private SwitchView accept;
    private RelativeLayout layout1, layout4;
    private LinearLayout layout2, layout3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_newnotice, container, false);
        accept = (SwitchView) mView.findViewById(R.id.newnotice_accept);
        layout1 = (RelativeLayout) mView.findViewById(R.id.newnotice_lay1);
        layout2 = (LinearLayout) mView.findViewById(R.id.newnotice_lay2);
        layout3 = (LinearLayout) mView.findViewById(R.id.newnotice_lay3);
        layout4 = (RelativeLayout) mView.findViewById(R.id.newnotice_lay4);
        accept.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.VISIBLE);
                //   accept.toggleSwitch(true);
                accept.setState(true);
            }

            @Override
            public void toggleToOff() {
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.GONE);
                layout3.setVisibility(View.GONE);
                layout4.setVisibility(View.GONE);
                // accept.toggleSwitch(false);
                accept.setState(false);
            }
        });
        return mView;
    }
}


