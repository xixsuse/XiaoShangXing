package com.xiaoshangxing.xiaoshang.Help.PersonalHelp;

import android.content.Intent;
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
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;
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
import com.xiaoshangxing.xiaoshang.Help.HelpActivity;

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
public class PersonalShoolHelpFragment extends BaseFragment implements PersonalhelpContract.View {

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


    public static PersonalShoolHelpFragment newInstance() {
        return new PersonalShoolHelpFragment();
    }

    private View view;
    private PersonalhelpContract.Presenter mPresenter;
    private View footview;
    private DotsTextView dotsTextView;
    private TextView loadingText;
    private Realm realm;
    private PersonalHelpAdapter personalHelpAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.double_title_refresh_listview_hidebutton, null);
        ButterKnife.bind(this, view);
        realm = Realm.getDefaultInstance();
        setmPresenter(new PersonalHelpPresenter(this, getContext(), realm));
        initView();
        return view;
    }

    private void initView() {
        title.setText("我的互帮");
        noContent.setText("你还没有发布互帮,快去发布你的互帮吧");
        View view = new View(getContext());
        listview.addHeaderView(view);
        footview = View.inflate(getContext(), R.layout.footer, null);
        dotsTextView = (DotsTextView) footview.findViewById(R.id.dot);
        dotsTextView.start();
        loadingText = (TextView) footview.findViewById(R.id.text);
        listview.addFooterView(footview);
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
        personalHelpAdapter = new PersonalHelpAdapter(getContext(), publisheds, this, realm, (HelpActivity) getActivity());
        listview.setAdapter(personalHelpAdapter);
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
                                        LoadUtils.clearDatabase(NS.CATEGORY_HELP, true, true);
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
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
        ButterKnife.unbind(this);
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
            personalHelpAdapter.showSelectCircle(false);
            activity.setHideMenu(false);
            cancel.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
        }
    }

    private void gotoSelectPerson() {
        personalHelpAdapter.showSelectCircle(false);
        showHideMenu(false);
        HelpActivity activity = (HelpActivity) getActivity();
        Intent intent = new Intent(getContext(), SelectPersonActivity.class);
        intent.putExtra(SelectPersonActivity.LIMIT, 1);
        activity.startActivityForResult(intent, SelectPersonActivity.SELECT_PERSON_CODE);
    }

    public void showDeleteSureDialog(final int publishId) {
        personalHelpAdapter.showSelectCircle(false);
        showHideMenu(false);

        DialogUtils.DialogMenu2 dialogMenu2 = new DialogUtils.DialogMenu2(getContext());
        dialogMenu2.addMenuItem("删除");
        dialogMenu2.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                OperateUtils.deleteOnePublished(publishId, getContext(), PersonalShoolHelpFragment.this, new SimpleCallBack() {
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

    @OnClick({R.id.back, R.id.hide_trasmit, R.id.hide_delete, R.id.cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (getActivity().getIntent().getIntExtra(IntentStatic.TYPE, IntentStatic.MINE) == IntentStatic.MINE) {
                    getActivity().finish();
                } else {
                    getFragmentManager().popBackStack();
                }
                break;
            case R.id.hide_trasmit:
                gotoSelectPerson();
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
