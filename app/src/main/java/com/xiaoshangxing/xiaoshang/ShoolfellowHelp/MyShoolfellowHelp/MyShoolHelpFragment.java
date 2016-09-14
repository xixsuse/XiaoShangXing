package com.xiaoshangxing.xiaoshang.ShoolfellowHelp.MyShoolfellowHelp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.Network.LoadUtils;
import com.xiaoshangxing.Network.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.loadingview.DotsTextView;
import com.xiaoshangxing.utils.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.utils.pull_refresh.PtrHandler;
import com.xiaoshangxing.utils.pull_refresh.StoreHouseHeader;
import com.xiaoshangxing.xiaoshang.ShoolReward.ShoolRewardActivity;
import com.xiaoshangxing.xiaoshang.ShoolfellowHelp.ShoolfellowHelpActivity;

import java.util.ArrayList;
import java.util.List;

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
public class MyShoolHelpFragment extends BaseFragment implements MyhelpContract.View {

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

    public static MyShoolHelpFragment newInstance() {
        return new MyShoolHelpFragment();
    }

    private myshoolfellow_adpter adpter;
    private List<String> list = new ArrayList<String>();
    private View view;
    private MyhelpContract.Presenter mPresenter;
    private View  footview;
    private DotsTextView dotsTextView;
    private TextView loadingText;
    private Realm realm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_myshoolhelp, null);
        ButterKnife.bind(this, view);
        realm = Realm.getDefaultInstance();
        setmPresenter(new MyHelpPresenter(this, getContext(), realm));
        initFresh();
        initView();
        return view;
    }

    private void initView() {
        View view = new View(getContext());
        listview.addHeaderView(view);
        footview = View.inflate(getContext(), R.layout.footer, null);
        dotsTextView = (DotsTextView) footview.findViewById(R.id.dot);
        dotsTextView.start();
        loadingText = (TextView) footview.findViewById(R.id.text);
        listview.addFooterView(footview);
        refreshData();
    }

    @Override
    public void refreshData() {
        if (LoadUtils.needRefresh(LoadUtils.TIME_LOAD_SELFHELP)) {
            ptrFrameLayout.autoRefresh();
        }

        RealmResults<Published> publisheds = realm.where(Published.class)
                .equalTo(NS.USER_ID, TempUser.id)
                .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_HELP))
                .findAll().sort(NS.ID, Sort.DESCENDING);
        MyHelpAdapter myHelpAdapter = new MyHelpAdapter(getContext(), publisheds, this, realm);
        listview.setAdapter(myHelpAdapter);
        showNoData();
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
        final StoreHouseHeader header = new StoreHouseHeader(getContext());
        header.setPadding(0, getResources().getDimensionPixelSize(R.dimen.y144), 0, 20);
        header.initWithString("SWALK");
        header.setTextColor(getResources().getColor(R.color.green1));
        header.setBackgroundColor(getResources().getColor(R.color.transparent));

        header.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));

        ptrFrameLayout.setDurationToCloseHeader(2000);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPresenter.refreshData(frame);
            }
        });
    }

    @Override
    public void setmPresenter(@Nullable MyhelpContract.Presenter presenter) {
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
        ShoolfellowHelpActivity activity=(ShoolfellowHelpActivity)getActivity();
        if (is) {
            hideMenu.setVisibility(View.VISIBLE);
            activity.setHideMenu(true);
        } else {
            hideMenu.setVisibility(View.GONE);
            adpter.showSelectCircle(false);
            activity.setHideMenu(false);
        }
    }

    private void gotoSelectPerson(){
        adpter.showSelectCircle(false);
        showHideMenu(false);
        ShoolfellowHelpActivity activity=(ShoolfellowHelpActivity)getActivity();
        Intent intent=new Intent(getContext(), SelectPersonActivity.class);
        activity.startActivityForResult(intent, SelectPersonActivity.SELECT_PERSON_CODE);
    }

    public void showDeleteSureDialog() {
        adpter.showSelectCircle(false);
        showHideMenu(false);

        DialogUtils.DialogMenu2 dialogMenu2 = new DialogUtils.DialogMenu2(getContext());
        dialogMenu2.addMenuItem("删除");
        dialogMenu2.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                mPresenter.delete();
            }

            @Override
            public void onCancel() {

            }
        });
        dialogMenu2.initView();
        dialogMenu2.show();
        LocationUtil.bottom_FillWidth(getActivity(), dialogMenu2);
    }

    public void showNoContentText(boolean is){
        if (is){
            noContent.setVisibility(View.VISIBLE);
            listview.setVisibility(View.INVISIBLE);
        }else {
            noContent.setVisibility(View.INVISIBLE);
            listview.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.back, R.id.hide_trasmit, R.id.hide_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (getActivity().getIntent().getIntExtra(IntentStatic.TYPE,0)== ShoolRewardActivity.MINE){
                    getActivity().finish();
                }else {
                    getFragmentManager().popBackStack();
                }
                break;
            case R.id.hide_trasmit:
                gotoSelectPerson();
                break;
            case R.id.hide_delete:
                showDeleteSureDialog();
                break;
        }
    }

}
