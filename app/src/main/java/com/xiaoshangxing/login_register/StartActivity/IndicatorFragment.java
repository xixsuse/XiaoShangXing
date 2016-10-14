package com.xiaoshangxing.login_register.StartActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseFragment;

/**
 * Created by FengChaoQun
 * on 2016/10/14
 */

public class IndicatorFragment extends BaseFragment {
    private View view;
    private ImageView top, bottom;
    private int top_resousce, bottom_resouce;

    public static IndicatorFragment newInstance(int top, int bottom) {
        final IndicatorFragment f = new IndicatorFragment();

        final Bundle args = new Bundle();
        args.putInt("top", top);
        args.putInt("bottom", bottom);
        f.setArguments(args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getContext(), R.layout.frag_indicator, null);
        top = (ImageView) view.findViewById(R.id.top_image);
        bottom = (ImageView) view.findViewById(R.id.bottom_image);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        top_resousce = getArguments().getInt("top");
        bottom_resouce = getArguments().getInt("bottom");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        top.setImageResource(top_resousce);
        bottom.setImageResource(bottom_resouce);
    }
}
