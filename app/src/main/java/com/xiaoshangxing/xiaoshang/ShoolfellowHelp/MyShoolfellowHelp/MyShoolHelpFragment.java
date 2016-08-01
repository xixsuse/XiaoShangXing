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

import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.input_activity.InputActivity;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.utils.pull_refresh.PtrHandler;
import com.xiaoshangxing.utils.pull_refresh.StoreHouseHeader;
import com.xiaoshangxing.xiaoshang.ShoolfellowHelp.ShoolfellowHelpActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private int currentItem;

    public static MyShoolHelpFragment newInstance() {
        return new MyShoolHelpFragment();
    }

    private myshoolfellow_adpter adpter;
    private List<String> list = new ArrayList<String>();
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_myshoolhelp, null);
        ButterKnife.bind(this, view);
        initFresh();
        initView();
        return view;
    }

    private void initView() {
        for (int i = 0; i <= 10; i++) {
            list.add("" + i);
        }

        View view = new View(getContext());
        listview.addHeaderView(view);

        adpter = new myshoolfellow_adpter(getContext(), 1, list, this,(ShoolfellowHelpActivity)getActivity());
        listview.setAdapter(adpter);


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
//        ptrFrameLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ptrFrameLayout.autoRefresh(true);
//            }
//        }, 100);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                    }
                }, 1500);
            }
        });
    }

    @Override
    public void setmPresenter(@Nullable MyhelpContract.Presenter presenter) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

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
        ShoolfellowHelpActivity activity=(ShoolfellowHelpActivity)getActivity();
        Intent intent=new Intent(getContext(), SelectPersonActivity.class);
        intent.putExtra(SelectPersonActivity.TRANSMIT_TYPE,SelectPersonActivity.SCHOOL_HELP_TRANSMIT);
        activity.startActivityForResult(intent,ShoolfellowHelpActivity.SELECTPERSON);
    }

    public void showDeleteSureDialog() {
        DialogUtils.DialogMenu2 dialogMenu2 = new DialogUtils.DialogMenu2(getContext());
        dialogMenu2.addMenuItem("删除");
        dialogMenu2.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {

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
                getFragmentManager().popBackStack();
                break;
            case R.id.hide_trasmit:
                adpter.showSelectCircle(false);
                showHideMenu(false);
                gotoSelectPerson();
                break;
            case R.id.hide_delete:
                adpter.showSelectCircle(false);
                showHideMenu(false);
                showDeleteSureDialog();
                break;
        }
    }

}
