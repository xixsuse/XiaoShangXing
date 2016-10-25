package com.xiaoshangxing.setting.privacy.privacySecondFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.setting.privacy.PermissionAdapter;
import com.xiaoshangxing.utils.BaseFragment;

import java.util.ArrayList;

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
    GridView gridView;
    private View mView;
    private PermissionAdapter adapter;
    private ArrayList<String> accounts = new ArrayList<>();
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

        adapter = new PermissionAdapter(accounts, this);
        gridView.setAdapter(adapter);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SelectPersonActivity.SELECT_PERSON_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                ArrayList<String> selected = data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON);
                if (selected.size() > 0) {
                    accounts.addAll(selected);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
