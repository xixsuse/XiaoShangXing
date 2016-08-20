package com.xiaoshangxing.setting.currency.chatBackground;

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

/**
 * Created by 15828 on 2016/7/15.
 */
public class ChatBackgroundFragment extends BaseFragment implements View.OnClickListener{
    private View mView;
    private TextView back;
    private ImageView imageView;
    private ChatBackgroundActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_current_chatbackground, container, false);
        mActivity = (ChatBackgroundActivity) getActivity();
        back = (TextView) mView.findViewById(R.id.chatbackground_back);
        imageView = (ImageView) mView.findViewById(R.id.chatbackground_img);
        mActivity.setmImageView(imageView);
        back.setOnClickListener(this);
        return mView;
    }

    @Override
    public void onClick(View v) {
        getActivity().finish();
    }
}
