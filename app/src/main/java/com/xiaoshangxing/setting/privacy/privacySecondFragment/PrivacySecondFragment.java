package com.xiaoshangxing.setting.privacy.privacySecondFragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.privacy.PrivacyGridAdapter;
import com.xiaoshangxing.utils.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/7/14.
 */
public class PrivacySecondFragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_text)
    TextView rightText;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.privacySecond_gridview)
    GridView privacySecondGridview;
    private View mView;
    private GridView gridView;
    private List<Bitmap> data = new ArrayList<>();
    private boolean isAdded = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_privacysecond, container, false);
        ButterKnife.bind(this, mView);
        leftImage.setVisibility(View.GONE);
        leftText.setText("取消");
        title.setText("不看他(她)看我的校友圈");
        rightText.setText("完成");
        rightText.setTextColor(getResources().getColor(R.color.green1));
        rightText.setAlpha(0.5f);

        gridView = (GridView) mView.findViewById(R.id.privacySecond_gridview);
        final Bitmap bitmap1 = BitmapFactory.decodeResource(getActivity().getResources(), R.mipmap.cirecleimage_default);
        final PrivacyGridAdapter adapter = new PrivacyGridAdapter(getActivity(), data);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (data.size() != 0) {
                    if (position == adapter.getCount() - 1) {
                        for (int i = 0; i < adapter.getCount() - 2; i++) {
                            if (adapter.getRedDeleteViews().get(i).getVisibility() == View.GONE) {
                                adapter.getRedDeleteViews().get(i).setVisibility(View.VISIBLE);
                                isAdded = false;
                            } else {
                                adapter.getRedDeleteViews().get(i).setVisibility(View.GONE);
                                isAdded = true;
                            }
                        }
                    } else if (position == adapter.getCount() - 2) {
                        if (isAdded) data.add(bitmap1);
                    } else {
                        if (!isAdded) {
                            data.remove(position);
                            if (data.size() != 0)
                                adapter.getRedDeleteViews().get(adapter.getCount() - 2).setVisibility(View.GONE);
                            else {
                                rightText.setAlpha((float) 0.5);
                                rightText.setClickable(false);
                                adapter.getRedDeleteViews().get(adapter.getCount() - 1).setVisibility(View.GONE);
                            }

                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    rightText.setAlpha(1);
                    rightText.setClickable(true);
                    isAdded = true;
                    data.add(bitmap1);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.back, R.id.right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.right_text:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }
}
