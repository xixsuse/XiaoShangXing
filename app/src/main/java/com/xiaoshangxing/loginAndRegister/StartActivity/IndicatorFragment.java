package com.xiaoshangxing.loginAndRegister.StartActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.baseClass.BaseFragment;

/**
 * Created by FengChaoQun
 * on 2016/10/14
 */

public class IndicatorFragment extends BaseFragment {
    private View view;
    private ImageView top, bottom, entry;
    private int top_resousce, bottom_resouce;
    private boolean end;

    public static IndicatorFragment newInstance(int top, int bottom, boolean end) {
        final IndicatorFragment f = new IndicatorFragment();

        final Bundle args = new Bundle();
        args.putInt("top", top);
        args.putInt("bottom", bottom);
        args.putBoolean("end", end);
        f.setArguments(args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getContext(), R.layout.frag_indicator, null);
        top = (ImageView) view.findViewById(R.id.top_image);
        bottom = (ImageView) view.findViewById(R.id.bottom_image);
        entry = (ImageView) view.findViewById(R.id.entry);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        top_resousce = getArguments().getInt("top");
        bottom_resouce = getArguments().getInt("bottom");
        end = getArguments().getBoolean("end");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        top.setImageResource(top_resousce);
        bottom.setImageResource(bottom_resouce);
        entry.setVisibility(end ? View.VISIBLE : View.GONE);
        entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndicatorActivity activity = (IndicatorActivity) getActivity();
                activity.intent();
            }
        });
    }
}
