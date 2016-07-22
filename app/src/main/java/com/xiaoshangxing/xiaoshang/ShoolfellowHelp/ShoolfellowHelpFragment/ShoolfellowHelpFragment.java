package com.xiaoshangxing.xiaoshang.ShoolfellowHelp.ShoolfellowHelpFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.loadingview.DotsTextView;
import com.xiaoshangxing.utils.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.utils.pull_refresh.PtrHandler;
import com.xiaoshangxing.utils.pull_refresh.StoreHouseHeader;
import com.xiaoshangxing.xiaoshang.ShoolfellowHelp.MyShoolfellowHelp.MyShoolHelpFragment;
import com.xiaoshangxing.xiaoshang.ShoolfellowHelp.ShoolfellowHelpActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/7/20
 */
public class ShoolfellowHelpFragment extends BaseFragment implements ShoolHelpContract.View{

    public static final String TAG = BaseFragment.TAG + "-ShoolfellowHelpFragment";
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.myState)
    TextView myState;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.reflesh_layout)
    PtrFrameLayout ptrFrameLayout;

    private View mview;
    private shoolfellow_adpter adpter;
    private List<String> list = new ArrayList<String>();
    private View headview, footview;

    public static ShoolfellowHelpFragment newInstance() {
        return new ShoolfellowHelpFragment();
    }

    private boolean isRefreshing;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.frag_shoolfellowhelp, null);
        ButterKnife.bind(this, mview);
        initFresh();
        initView();
        return mview;
    }

    private void initView() {
        for (int i = 0; i <= 10; i++) {
            list.add("" + i);
        }
        adpter = new shoolfellow_adpter(getContext(), 1, list, this);
        listview.setAdapter(adpter);

        headview = View.inflate(getContext(), R.layout.headview_help_list, null);
        footview = View.inflate(getContext(), R.layout.footer, null);
        DotsTextView dotsTextView = (DotsTextView) footview.findViewById(R.id.dot);
        dotsTextView.start();
        listview.addHeaderView(headview);
        listview.addFooterView(footview);
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
//                ptrFrameLayout.autoRefresh(false);
//            }
//        }, 100);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                isRefreshing = true;
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                        isRefreshing = false;
                    }
                }, 1500);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.back, R.id.more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.more:
                showPublishMenu(view);
                break;
        }
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public void showPublishMenu(View v) {

        View menu = View.inflate(getContext(), R.layout.popupmenu_pubulish, null);
        final PopupWindow popupWindow = new PopupWindow(menu, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(getContext().getResources().
                getDrawable(R.drawable.nothing));
        popupWindow.setAnimationStyle(R.style.popwindow_anim);

        menu.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int mShowMorePopupWindowWidth = menu.getMeasuredWidth();

        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);

        popupWindow.showAsDropDown(v,
                -mShowMorePopupWindowWidth + this.getResources().getDimensionPixelSize(R.dimen.x30) + v.getWidth(),
                3);

        View publish = menu.findViewById(R.id.publish);
        View published = menu.findViewById(R.id.published);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        published.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPublished();
                popupWindow.dismiss();
            }
        });
    }



    @Override
    public void gotoPublish() {

    }

    @Override
    public void gotoPublished() {
        ShoolfellowHelpActivity activity = (ShoolfellowHelpActivity) getActivity();
        MyShoolHelpFragment fragment = activity.getMyShoolHelpFragment();
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .replace(R.id.main_fragment, fragment, MyShoolHelpFragment.TAG)
                .addToBackStack(null).commit();
    }

    @Override
    public void toastErro() {

    }

    @Override
    public void setmPresenter(@Nullable ShoolHelpContract.Presenter presenter) {

    }
}
