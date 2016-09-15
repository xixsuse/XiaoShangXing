package com.xiaoshangxing.setting.newNotice.noDisturbFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.DataSetting;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

/**
 * Created by 15828 on 2016/7/13.
 */
public class NoDisturbFragment extends BaseFragment implements View.OnClickListener,AdapterView.OnItemClickListener{
    private View mView;
    private ListView listView;
    private String[] strings;
    private ArrayAdapter mAdapter;
    private TextView back;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_newnotice_nodisturb,container,false);
        strings = getResources().getStringArray(R.array.NoDisturb);
        mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_nodisturb, strings);
        listView = (ListView) mView.findViewById(R.id.list_nodisturb);
        listView.setAdapter(mAdapter);
        int i = DataSetting.getNoDisturbList(getActivity());
        listView.setItemChecked(i,true);
        listView.setOnItemClickListener(this);
        back = (TextView) mView.findViewById(R.id.nodisturb_back);
        back.setOnClickListener(this);
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
                setTime(true);
                break;
            case 2:
                SPUtils.put(getActivity(), SPUtils.NewNotice, true);
                NIMClient.toggleNotification(true);
                setTime(false);
                break;
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
    public void onClick(View v) {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
