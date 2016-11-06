package com.xiaoshangxing.setting.newNotice.newNoticeFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.DataSetting;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.SwitchView;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/7/13.
 */
public class NewNoticeFrament extends BaseFragment {
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
    private SwitchView newnotice_accept, newnotice_inform, newnotice_sound, newnotice_vibration, newnotice_renew;
    private RelativeLayout layout1, layout4;
    private LinearLayout layout2, layout3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_newnotice, container, false);
        ButterKnife.bind(this, mView);
        newnotice_accept = (SwitchView) mView.findViewById(R.id.newnotice_accept);
        newnotice_inform = (SwitchView) mView.findViewById(R.id.newnotice_inform);
        newnotice_sound = (SwitchView) mView.findViewById(R.id.newnotice_sound);
        newnotice_vibration = (SwitchView) mView.findViewById(R.id.newnotice_vibration);
        newnotice_renew = (SwitchView) mView.findViewById(R.id.newnotice_renew);

        layout1 = (RelativeLayout) mView.findViewById(R.id.newnotice_lay1);
        layout2 = (LinearLayout) mView.findViewById(R.id.newnotice_lay2);
        layout3 = (LinearLayout) mView.findViewById(R.id.newnotice_lay3);
        layout4 = (RelativeLayout) mView.findViewById(R.id.newnotice_lay4);

        title.setText("新消息通知");
        more.setVisibility(View.GONE);

        setUpData();

        newnotice_accept.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                SPUtils.put(getActivity(), SPUtils.NewNotice, true);
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.VISIBLE);
                newnotice_accept.setState(true);
                NIMClient.toggleNotification(true);

            }

            @Override
            public void toggleToOff() {
                SPUtils.put(getActivity(), SPUtils.NewNotice, false);
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.GONE);
                layout3.setVisibility(View.GONE);
                layout4.setVisibility(View.GONE);
                newnotice_accept.setState(false);
                NIMClient.toggleNotification(false);
            }
        });

        newnotice_inform.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                newnotice_inform.setState(true);
                StatusBarNotificationConfig config = DataSetting.getStatusConfig();
                config.hideContent = false;
                DataSetting.setStatusConfig(config);
                NIMClient.updateStatusBarNotificationConfig(config);
            }

            @Override
            public void toggleToOff() {
                newnotice_inform.setState(false);
                StatusBarNotificationConfig config = DataSetting.getStatusConfig();
                config.hideContent = true;
                DataSetting.setStatusConfig(config);
                NIMClient.updateStatusBarNotificationConfig(config);
            }

        });

        newnotice_sound.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                newnotice_sound.setState(true);
                StatusBarNotificationConfig config = DataSetting.getStatusConfig();
                config.ring = true;
                DataSetting.setStatusConfig(config);
                NIMClient.updateStatusBarNotificationConfig(config);
            }

            @Override
            public void toggleToOff() {
                newnotice_sound.setState(false);
                StatusBarNotificationConfig config = DataSetting.getStatusConfig();
                config.ring = false;
                DataSetting.setStatusConfig(config);
                NIMClient.updateStatusBarNotificationConfig(config);
            }
        });

        newnotice_vibration.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                newnotice_vibration.setState(true);
                StatusBarNotificationConfig config = DataSetting.getStatusConfig();
                config.vibrate = true;
                DataSetting.setStatusConfig(config);
                NIMClient.updateStatusBarNotificationConfig(config);
            }

            @Override
            public void toggleToOff() {
                newnotice_vibration.setState(false);
                StatusBarNotificationConfig config = DataSetting.getStatusConfig();
                config.vibrate = false;
                DataSetting.setStatusConfig(config);
                NIMClient.updateStatusBarNotificationConfig(config);
            }
        });

        newnotice_renew.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                SPUtils.put(getActivity(), SPUtils.NewsForXiaoyou, true);
                newnotice_renew.setState(true);
            }

            @Override
            public void toggleToOff() {
                SPUtils.put(getActivity(), SPUtils.NewsForXiaoyou, false);
                newnotice_renew.setState(false);
            }
        });

        return mView;
    }

    private void setUpData() {
        StatusBarNotificationConfig config = DataSetting.getStatusConfig();
        newnotice_accept.setState(DataSetting.IsAcceptedNews(getActivity()));
        newnotice_inform.setState(!config.hideContent);
        newnotice_sound.setState(config.ring);
        newnotice_vibration.setState(config.vibrate);
        newnotice_renew.setState(DataSetting.IsRenewed(getActivity()));
        if (newnotice_accept.getState2()) {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.VISIBLE);
            layout4.setVisibility(View.VISIBLE);
        }
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

    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
        }
    }
}


