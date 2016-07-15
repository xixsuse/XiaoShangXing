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

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseFragment;

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
        mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_report, strings);
        listView = (ListView) mView.findViewById(R.id.list_nodisturb);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        back = (TextView) mView.findViewById(R.id.nodisturb_back);
        back.setOnClickListener(this);
        return mView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
