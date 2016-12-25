package com.xiaoshangxing.wo.setting.privacy.forbiddenListFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.network.netUtil.OperateUtils;
import com.xiaoshangxing.network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.publicActivity.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.wo.setting.privacy.PermissionAdapter;
import com.xiaoshangxing.utils.baseClass.BaseFragment;
import com.xiaoshangxing.utils.baseClass.IBaseView;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/7/14.
 */
public class ForbiddenListFragment extends BaseFragment implements View.OnClickListener, IBaseView {
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
    @Bind(R.id.privacyfirst_gridview)
    GridView gridView;
    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
    private View mView;
    private PermissionAdapter adapter;
    private List<String> accounts = new ArrayList<>();
    private NimUserInfo userInfo;
    private List<String> origAccounts = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_privacyfist, container, false);
        ButterKnife.bind(this, mView);
        leftImage.setVisibility(View.GONE);
        leftText.setText("取消");
        title.setText("不让他(她)看我的校友圈");
        rightText.setText("完成");
        rightText.setTextColor(getResources().getColor(R.color.green1));

        back.setOnClickListener(this);
        rightText.setOnClickListener(this);

        userInfo = NimUserInfoCache.getInstance().getUserInfo(TempUser.getId());
        if (userInfo == null) {
            showToast("个人信息异常");
        } else {
            init();
        }
        return mView;
    }

    private void init() {
        if (userInfo.getExtensionMap() != null &&
                !TextUtils.isEmpty((String) userInfo.getExtensionMap().get(NS.BLOCK))) {
            String ids = (String) userInfo.getExtensionMap().get(NS.BLOCK);
            origAccounts = Arrays.asList(ids.split(NS.SPLIT));
            accounts.addAll(origAccounts);
        }
        adapter = new PermissionAdapter(accounts, this);
        gridView.setAdapter(adapter);
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
                upload();
                break;
        }
    }

    private void upload() {
        accounts = adapter.getAccounts();
        if (!accounts.isEmpty() && !origAccounts.isEmpty()) {
            ArrayList<String> add = new ArrayList<>();
            ArrayList<String> remove = new ArrayList<>();

            for (String i : origAccounts) {
                if (!accounts.contains(i)) {
                    remove.add(i);
                }
            }

            for (String i : accounts) {
                if (!origAccounts.contains(i)) {
                    add.add(i);
                }
            }

            upload(add, remove);

        } else {
            upload(accounts, origAccounts);
        }
    }

    private void upload(final List<String> add, final List<String> remove) {
        final SimpleCallBack simpleCallBack = new SimpleCallBack() {
            @Override
            public void onSuccess() {
                getActivity().getSupportFragmentManager().popBackStack();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showToast("异常");
            }

            @Override
            public void onBackData(Object o) {

            }
        };
        if (add.isEmpty() && remove.isEmpty()) {
            getActivity().getSupportFragmentManager().popBackStack();
            return;
        }
        if (!add.isEmpty() && !remove.isEmpty()) {
            OperateUtils.SchoolCirclrPermisson(add, NS.BLOCK_CODE, this, new SimpleCallBack() {
                @Override
                public void onSuccess() {
                    OperateUtils.SchoolCirclrPermisson(remove, NS.REMOVE_BLOCK_CODE, ForbiddenListFragment.this, simpleCallBack);
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    showToast("异常");
                }

                @Override
                public void onBackData(Object o) {

                }
            });
        } else if (add.isEmpty()) {
            OperateUtils.SchoolCirclrPermisson(remove, NS.REMOVE_BLOCK_CODE, this, simpleCallBack);
        } else if (remove.isEmpty()) {
            OperateUtils.SchoolCirclrPermisson(add, NS.BLOCK_CODE, this, simpleCallBack);
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

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }
}
