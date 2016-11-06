package com.xiaoshangxing.setting.currency.chatBackground;

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
import com.xiaoshangxing.data.LocalDataUtils;
import com.xiaoshangxing.utils.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/7/15.
 */
public class ChatBackgroundFragment extends BaseFragment {
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
    @Bind(R.id.ChooseBackground)
    RelativeLayout ChooseBackground;
    @Bind(R.id.chatbackground_img)
    ImageView chatbackgroundImg;
    @Bind(R.id.use_for_all)
    RelativeLayout useForAll;
    private View mView;
    private ImageView imageView;
    private ChatBackgroundActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_current_chatbackground, container, false);
        ButterKnife.bind(this, mView);
        title.setText("聊天背景");
        more.setVisibility(View.GONE);
        mActivity = (ChatBackgroundActivity) getActivity();
        imageView = (ImageView) mView.findViewById(R.id.chatbackground_img);
        mActivity.setmImageView(imageView);
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


    @OnClick({R.id.back, R.id.use_for_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.use_for_all:
                useForAll();
                break;
        }
    }

    private void useForAll() {
        LocalDataUtils.saveBackgroud(null, ChatBackgroundActivity.image, true, getContext());
    }
}
