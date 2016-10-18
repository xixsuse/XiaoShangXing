package com.xiaoshangxing.xiaoshang.Reward.PersonalReward;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.LoadUtils;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.netUtil.OperateUtils;
import com.xiaoshangxing.Network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.layout.LayoutHelp;
import com.xiaoshangxing.utils.layout.loadingview.DotsTextView;
import com.xiaoshangxing.utils.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.xiaoshang.Reward.RewardActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by FengChaoQun
 * on 2016/7/21
 */
public class PersonalRewardFragment extends BaseFragment implements PersonalRewardContract.View {
    public static final String TAG = BaseFragment.TAG + "-MyShoolHelpFragment";
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.reflesh_layout)
    PtrFrameLayout ptrFrameLayout;
    @Bind(R.id.hide_trasmit)
    ImageView hideTrasmit;
    @Bind(R.id.hide_delete)
    ImageView hideDelete;
    @Bind(R.id.hideMenu)
    RelativeLayout hideMenu;
    @Bind(R.id.no_content)
    TextView noContent;
    @Bind(R.id.cancel)
    LinearLayout cancel;

    public static PersonalRewardFragment newInstance() {
        return new PersonalRewardFragment();
    }

    private PersonalReward_adpter adpter;
    private View view;
    private RewardActivity activity;
    private PersonalRewardContract.Presenter mPresenter;
    private View footview;
    private DotsTextView dotsTextView;
    private TextView loadingText;
    private Realm realm;
    RealmResults<Published> publisheds;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.double_title_refresh_listview_hidebutton, null);
        ButterKnife.bind(this, view);
        realm = Realm.getDefaultInstance();
        setmPresenter(new PersonalRewardPresenter(this, getContext(), realm));
        initView();
        initFresh();
        return view;
    }

    private void initView() {
        title.setText(R.string.myreward);
        noContent.setText("你还没有发布悬赏,快去发布你的互帮吧");
        View view = new View(getContext());
        footview = View.inflate(getContext(), R.layout.footer, null);
        dotsTextView = (DotsTextView) footview.findViewById(R.id.dot);
        dotsTextView.start();
        loadingText = (TextView) footview.findViewById(R.id.text);
        listview.addHeaderView(view);
        listview.addFooterView(footview);
        activity = (RewardActivity) getActivity();
        refreshData();
    }

    @Override
    public void refreshData() {
        publisheds = realm.where(Published.class)
                .equalTo(NS.USER_ID, TempUser.id)
                .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_REWARD))
                .findAll().sort(NS.CREATETIME, Sort.DESCENDING);
        showNoContentText(publisheds.size() < 1);
        adpter = new PersonalReward_adpter(getContext(), 1, publisheds, this, (RewardActivity) getActivity());
        listview.setAdapter(adpter);
    }

    private void initFresh() {
        LayoutHelp.initPTR(ptrFrameLayout, LoadUtils.needRefresh(LoadUtils.TIME_LOAD_SELFREWARD),
                new PtrDefaultHandler() {
                    @Override
                    public void onRefreshBegin(final PtrFrameLayout frame) {
                        LoadUtils.getPublished(realm, NS.CATEGORY_REWARD, LoadUtils.TIME_LOAD_SELFREWARD, getContext(),true,
                                new LoadUtils.AroundLoading() {
                                    @Override
                                    public void before() {
                                        LoadUtils.clearDatabase(NS.CATEGORY_STATE, true, true);
                                    }

                                    @Override
                                    public void complete() {
                                        frame.refreshComplete();
                                    }

                                    @Override
                                    public void onSuccess() {
                                        refreshData();
                                    }

                                    @Override
                                    public void error() {
                                        frame.refreshComplete();
                                    }
                                });
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
        ButterKnife.unbind(this);
    }

    @Override
    public void showNoData() {
        dotsTextView.stop();
        loadingText.setText("没有动态啦");
    }

    @Override
    public void showFooter() {
        dotsTextView.start();
        loadingText.setText("加载中");
    }

    @Override
    public void showHideMenu(boolean is) {
        RewardActivity activity = (RewardActivity) getActivity();
        if (is) {
            hideMenu.setVisibility(View.VISIBLE);
            activity.setHideMenu(true);
            cancel.setVisibility(View.VISIBLE);
            back.setVisibility(View.GONE);
        } else {
            hideMenu.setVisibility(View.GONE);
            adpter.showSelectCircle(false);
            activity.setHideMenu(false);
            cancel.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showDeleteSureDialog(final int publishedId) {
        adpter.showSelectCircle(false);
        showHideMenu(false);

        DialogUtils.DialogMenu2 dialogMenu2 = new DialogUtils.DialogMenu2(getContext());
        dialogMenu2.addMenuItem("删除");
        dialogMenu2.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
//                mPresenter.delete();
                OperateUtils.deleteOnePublished(publishedId, getContext(), PersonalRewardFragment.this, new SimpleCallBack() {
                    @Override
                    public void onSuccess() {
                        refreshData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast("删除异常");
                    }

                    @Override
                    public void onBackData(Object o) {

                    }
                });
            }

            @Override
            public void onCancel() {

            }
        });
        dialogMenu2.initView();
        dialogMenu2.show();
        LocationUtil.bottom_FillWidth(getActivity(), dialogMenu2);
    }

    public void showNoContentText(boolean is) {
        if (is) {
            noContent.setVisibility(View.VISIBLE);
            listview.setVisibility(View.INVISIBLE);
        } else {
            noContent.setVisibility(View.INVISIBLE);
            listview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setmPresenter(@Nullable PersonalRewardContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @OnClick({R.id.back, R.id.hide_trasmit, R.id.hide_delete, R.id.cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (getActivity().getIntent().getIntExtra(IntentStatic.TYPE, 0) == IntentStatic.MINE) {
                    getActivity().finish();
                } else {
                    getFragmentManager().popBackStack();
                }
                break;
            case R.id.hide_trasmit:
                adpter.showSelectCircle(false);
                showHideMenu(false);
                activity.gotoSelectPerson();
                break;
            case R.id.hide_delete:
//                showDeleteSureDialog();
                break;
            case R.id.cancel:
                showHideMenu(false);
                break;
        }
    }

}
