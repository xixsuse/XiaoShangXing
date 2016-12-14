package com.xiaoshangxing.wo.setting.newNotice.noDisturbFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.xiaoshangxing.R;
import com.xiaoshangxing.wo.setting.DataSetting;
import com.xiaoshangxing.utils.baseClass.BaseFragment;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/7/13.
 */
public class NoDisturbFragment extends BaseFragment implements AdapterView.OnItemClickListener {
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
    @Bind(R.id.list_nodisturb)
    ListView listNodisturb;
    private View mView;
    private ListView listView;
    private String[] strings;
    private ArrayAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_newnotice_nodisturb, container, false);
        ButterKnife.bind(this, mView);
        title.setText("消息免打扰");
        more.setVisibility(View.GONE);
        strings = getResources().getStringArray(R.array.NoDisturb);
        mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_nodisturb, strings);
        listView = (ListView) mView.findViewById(R.id.list_nodisturb);
        listView.setAdapter(mAdapter);
        initSelect();
        listView.setOnItemClickListener(this);
        return mView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SPUtils.put(getContext(), SPUtils.NoDisturb, position);
        switch (position) {
            case 0:
                SPUtils.put(getActivity(), SPUtils.NewNotice, false);
                NIMClient.toggleNotification(false);
                setTime(false);
                break;
            case 1:
                SPUtils.put(getActivity(), SPUtils.NewNotice, true);
                NIMClient.toggleNotification(true);
                setTime(true);
                break;
            case 2:
                SPUtils.put(getActivity(), SPUtils.NewNotice, true);
                NIMClient.toggleNotification(true);
                setTime(false);
                break;
        }
    }

    private void initSelect() {
        if (DataSetting.IsAcceptedNews(getContext())) {
            StatusBarNotificationConfig config = DataSetting.getStatusConfig();
            if (TextUtils.isEmpty(config.downTimeBegin)) {
                listView.setItemChecked(2, true);
            } else {
                listView.setItemChecked(1, true);
            }
        } else {
            listView.setItemChecked(0, true);
        }
    }

    private void setTime(boolean is) {
        StatusBarNotificationConfig config = DataSetting.getStatusConfig();
        if (is) {
            config.downTimeBegin = "22:00";
            config.downTimeEnd = "08:00";
        } else {
            config.downTimeBegin = null;
            config.downTimeEnd = null;
        }
        config.downTimeToggle = is;
        NIMClient.updateStatusBarNotificationConfig(config);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.back)
    public void onClick() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

}
