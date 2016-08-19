package com.xiaoshangxing.xiaoshang.schoolcalender.addEntranceFragment;

/**
 * Created by quchwe on 2016/7/22 0022.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Bean;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.xiaoshang.schoolcalender.SchoolCalenderActivity;
import com.xiaoshangxing.xiaoshang.schoolcalender.addinfofragment.AddInfoFragment;

import java.util.List;


/**
 * Created by quchwe on 2016/7/10 0010.
 */
public class AddEntranceFragment  extends BaseFragment implements View.OnClickListener,AddEntryceContract.View{
    public static final String TAG = BaseFragment.TAG + "-AddEntranceFragment";
    private Button back;
    private TextView textTitle;
    private ImageButton ib_add;
    private RelativeLayout rl_mytitle;
    private TextView btn_calender_add_info;
    private ListView lv_managerInfo;
    private ManagerListAdapter adapter;

    private AddEntryceContract.Presenter mPresenter;
    private List<ManagerInfo> managerInfos;

    public static AddEntranceFragment newInstance(){
        return new AddEntranceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_clender_add_entry,container,false);

        setmPresenter(new Presenter(getActivity(),new Bean()));
        initView(root);
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.ib_add:

                break;
            case R.id.btn_calender_add_info:

                break;
        }
    }
    private void initView(View v) {
        back = (Button)v. findViewById(R.id.back);
        textTitle = (TextView)v. findViewById(R.id.textTitle);
        ib_add = (ImageButton) v. findViewById(R.id.ib_add);
        rl_mytitle = (RelativeLayout) v. findViewById(R.id.rl_mytitle);
        btn_calender_add_info = (TextView)v.findViewById(R.id.btn_calender_add_info);
        lv_managerInfo = (ListView) v. findViewById(R.id.lv_managerInfo);
        ib_add.setVisibility(View.GONE);
        back.setText("返回");
        textTitle.setText("添加入口");
        back.setOnClickListener(this);
        managerInfos = mPresenter.getManagerList();
        ib_add.setOnClickListener(this);
        btn_calender_add_info.setOnClickListener(this);
        adapter = new ManagerListAdapter(getActivity(),managerInfos);
        lv_managerInfo.setAdapter(adapter);

        lv_managerInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("lv","点击listView");
                if (managerInfos.get(position).getName().equals("掌上电脑")){

                }else {
                    AddInfoFragment frag = ((SchoolCalenderActivity)mActivity).getAddInfoFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                                    R.anim.slide_in_left, R.anim.slide_out_left)
                            .replace(R.id.fl_school_calender, frag,AddInfoFragment.TAG)
                            .addToBackStack(null)
                            .commit();

                    getActivity().getSupportFragmentManager().beginTransaction().remove(getActivity().getSupportFragmentManager().findFragmentByTag(AddEntranceFragment.TAG)).commit();
                }
            }
        });
    }
    @Override
    public void setmPresenter(@Nullable AddEntryceContract.Presenter presenter) {
        if (presenter==null){
            return;
        }
        mPresenter = presenter;
    }
}