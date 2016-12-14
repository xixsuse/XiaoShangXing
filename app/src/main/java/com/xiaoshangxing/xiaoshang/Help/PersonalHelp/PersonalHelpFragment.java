package com.xiaoshangxing.xiaoshang.Help.PersonalHelp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.network.netUtil.LoadUtils;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.network.netUtil.OperateUtils;
import com.xiaoshangxing.network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.publicActivity.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.bean.Published;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseFragment;
import com.xiaoshangxing.utils.customView.LayoutHelp;
import com.xiaoshangxing.utils.customView.dialog.DialogLocationAndSize;
import com.xiaoshangxing.utils.customView.dialog.DialogUtils;
import com.xiaoshangxing.utils.customView.loadingview.DotsTextView;
import com.xiaoshangxing.utils.customView.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.customView.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.xiaoshang.Help.HelpActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by FengChaoQun
 * on 2016/7/21
 */
public class PersonalHelpFragment extends BaseFragment implements PersonalhelpContract.View {

    public static final String TAG = BaseFragment.TAG + "-MyShoolHelpFragment";
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.cancel)
    LinearLayout cancel;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
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
    private View view;
    private PersonalhelpContract.Presenter mPresenter;
    private View footview;
    private DotsTextView dotsTextView;
    private TextView loadingText;
    private PersonalHelpAdapter adapter;
    private HelpActivity activity;

    public static PersonalHelpFragment newInstance() {
        return new PersonalHelpFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.double_title_refresh_listview_hidebutton, null);
        ButterKnife.bind(this, view);
        setmPresenter(new PersonalHelpPresenter(this, getContext(), realm));
        initView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void initView() {
        title.setText("我的互帮");
        View view = new View(getContext());
        listview.addHeaderView(view);
        footview = View.inflate(getContext(), R.layout.footer, null);
        dotsTextView = (DotsTextView) footview.findViewById(R.id.dot);
        dotsTextView.start();
        loadingText = (TextView) footview.findViewById(R.id.text);
        listview.addFooterView(footview);
        activity = (HelpActivity) getActivity();
        initFresh();
        refreshData();
        showNoData();
    }

    @Override
    public void refreshData() {
        RealmResults<Published> publisheds = realm.where(Published.class)
                .equalTo(NS.USER_ID, TempUser.id)
                .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_HELP))
                .findAll().sort(NS.ID, Sort.DESCENDING);
        showNoContentText(publisheds.size() < 1);
        adapter = new PersonalHelpAdapter(getContext(), publisheds, this, realm, activity);
        listview.setAdapter(adapter);
        if (publisheds.size() > 0) {
            noContent.setVisibility(View.GONE);
        } else {
            noContent.setVisibility(View.VISIBLE);
            noContent.setText("你还没有发布互帮");
        }
    }

    @Override
    public void showNoData() {
        dotsTextView.stop();
        loadingText.setText("没有更多啦");
    }

    @Override
    public void showFooter() {
        dotsTextView.start();
        loadingText.setText("加载中");
    }

    private void initFresh() {
        LayoutHelp.initPTR(ptrFrameLayout, LoadUtils.needRefresh(LoadUtils.TIME_LOAD_SELFHELP),
                new PtrDefaultHandler() {
                    @Override
                    public void onRefreshBegin(final PtrFrameLayout frame) {
                        mPresenter.refreshData(frame);
                        LoadUtils.getPublished(realm, NS.CATEGORY_HELP, LoadUtils.TIME_LOAD_SELFHELP, getContext(), true,
                                new LoadUtils.AroundLoading() {
                                    @Override
                                    public void before() {
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
    public void setmPresenter(@Nullable PersonalhelpContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showHideMenu(boolean is) {
        HelpActivity activity = (HelpActivity) getActivity();
        if (is) {
            hideMenu.setVisibility(View.VISIBLE);
            activity.setHideMenu(true);
            cancel.setVisibility(View.VISIBLE);
            back.setVisibility(View.GONE);
        } else {
            hideMenu.setVisibility(View.GONE);
            adapter.showSelectCircle(false);
            activity.setHideMenu(false);
            cancel.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
        }
    }

    private void gotoSelectPerson() {
        adapter.showSelectCircle(false);
        showHideMenu(false);
        HelpActivity activity = (HelpActivity) getActivity();
        Intent intent = new Intent(getContext(), SelectPersonActivity.class);
        intent.putExtra(SelectPersonActivity.LIMIT, 1);
        activity.startActivityForResult(intent, SelectPersonActivity.SELECT_PERSON_CODE);
    }

    public void showDeleteSureDialog(final int publishId) {
        adapter.showSelectCircle(false);
        showHideMenu(false);

        DialogUtils.DialogMenu2 dialogMenu2 = new DialogUtils.DialogMenu2(getContext());
        dialogMenu2.addMenuItem("删除");
        dialogMenu2.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                OperateUtils.deleteOnePublished(publishId, getContext(), PersonalHelpFragment.this, new SimpleCallBack() {
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
        DialogLocationAndSize.bottom_FillWidth(getActivity(), dialogMenu2);
    }

    public void showDeleteSureDialog2(final List<String> ids) {
        adapter.showSelectCircle(false);
        showHideMenu(false);

        DialogUtils.DialogMenu2 dialogMenu2 = new DialogUtils.DialogMenu2(getContext());
        dialogMenu2.addMenuItem("删除");
        dialogMenu2.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                Log.d("selecte2", ids.toString());
                OperateUtils.deletePublisheds(ids, getContext(), PersonalHelpFragment.this, new SimpleCallBack() {
                    @Override
                    public void onSuccess() {
                        showToast("删除成功");
                        refreshData();
                        adapter.getSelectIds().clear();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast("删除异常");
                        refreshData();
                        adapter.getSelectIds().clear();
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
        DialogLocationAndSize.bottom_FillWidth(getActivity(), dialogMenu2);
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

    @OnClick({R.id.back, R.id.hide_trasmit, R.id.hide_delete, R.id.cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (getActivity().getIntent().getIntExtra(IntentStatic.TYPE, -1) == IntentStatic.MINE) {
                    getActivity().finish();
                } else {
                    getFragmentManager().popBackStack();
                }
                break;
            case R.id.hide_trasmit:
                if (adapter.getSelectIds().isEmpty()) {
                    return;
                }
                adapter.showSelectCircle(false);
                showHideMenu(false);
                activity.setPublishIdsForTransmit(adapter.getSelectIds());
                activity.gotoSelectOnePerson();
                break;
            case R.id.hide_delete:
                if (adapter.getSelectIds().size() == 0) {
                    showToast("请选择要删除的内容");
                    return;
                }
                showDeleteSureDialog2(adapter.getSelectIds());
                break;
            case R.id.cancel:
                showHideMenu(false);
                break;
        }
    }
}
