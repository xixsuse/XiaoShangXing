package com.xiaoshangxing.yujian.chatInfo.setBackground;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.currency.CurrencyActivity;
import com.xiaoshangxing.utils.BaseFragment;


public class SetBackgroundFragment extends BaseFragment implements View.OnClickListener{
    private View mView;
    private TextView back;
    private ImageView imageView;
    private SetBackgroundActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_current_chatbackground, container, false);
        mActivity = (SetBackgroundActivity) getActivity();
        back = (TextView) mView.findViewById(R.id.chatbackground_back);
        imageView = (ImageView) mView.findViewById(R.id.chatbackground_img);
        mActivity.setmImageView(imageView);
        back.setOnClickListener(this);
        return mView;
    }

    @Override
    public void onClick(View v) {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
