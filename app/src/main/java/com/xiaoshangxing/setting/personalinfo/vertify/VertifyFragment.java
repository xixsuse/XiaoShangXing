package com.xiaoshangxing.setting.personalinfo.vertify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseFragment;


/**
 * Created by 15828 on 2016/7/12.
 */
public class VertifyFragment extends BaseFragment implements View.OnClickListener{
    private View mView;
    private TextView back;
    private Button submit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_personalinfo_vertify,container,false);
        back = (TextView) mView.findViewById(R.id.vertify_back);
        submit = (Button) mView.findViewById(R.id.vertify_submit);
        back.setOnClickListener(this);
        submit.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.vertify_back:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.vertify_submit:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                                R.anim.slide_in_left, R.anim.slide_out_left)
                        .replace(R.id.setting_personinfo_Content,new VertifySucessFragment())
                        .commit();
                break;
            default:
                break;
        }
    }
}
