package com.xiaoshangxing.setting.privacy.privacyFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.SwitchView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/7/14.
 */
public class PrivacyFragment extends BaseFragment {
    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    private View mView;
    private SwitchView privacy_allow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_privacy, container, false);
        ButterKnife.bind(this, mView);
        title.setText("隐私");
        more.setVisibility(View.GONE);

        privacy_allow = (SwitchView) mView.findViewById(R.id.privacy_allow);
        privacy_allow.setState(true);
        privacy_allow.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                privacy_allow.setState(true);
            }

            @Override
            public void toggleToOff() {
                privacy_allow.setState(false);
            }
        });


        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setCloseActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.back)
    public void onClick() {
        getActivity().finish();
    }
}
