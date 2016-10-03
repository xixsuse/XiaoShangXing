package com.xiaoshangxing.xiaoshang.Sale.SaleCollect;

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

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.netUtil.OperateUtils;
import com.xiaoshangxing.Network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.loadingview.DotsTextView;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.xiaoshang.Sale.SaleActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/9/28
 */
public class SaleCollectFragment extends BaseFragment implements SaleCollectContract.View {
    public static final String TAG = BaseFragment.TAG + "-SaleCollectFragment";
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
    PtrFrameLayout refleshLayout;
    @Bind(R.id.hide_trasmit)
    ImageView hideTrasmit;
    @Bind(R.id.hide_delete)
    ImageView hideDelete;
    @Bind(R.id.hideMenu)
    RelativeLayout hideMenu;
    @Bind(R.id.no_content)
    TextView noContent;

    private View footview;
    private DotsTextView dotsTextView;
    private TextView loadingText;
    private SaleActivity activity;
    private SaleCollect_Adpter adpter;
    private List<Published> publisheds = new ArrayList<>();

    public static SaleCollectFragment newInstance() {
        return new SaleCollectFragment();
    }

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_myshoolreward, null);
        ButterKnife.bind(this, view);
//        setmPresenter(new CollectPresenter(this,getContext()));
//        initFresh();
        initView();
        return view;
    }

    private void initView() {
        View view = new View(getContext());
        listview.addHeaderView(view);
        footview = View.inflate(getContext(), R.layout.footer, null);
        dotsTextView = (DotsTextView) footview.findViewById(R.id.dot);
        dotsTextView.start();
        listview.addFooterView(footview);
        activity = (SaleActivity) getActivity();
        title.setText("收藏");
        initlistview();
    }

    private void initlistview() {
        for (int i = 0; i <= 10; i++) {
            publisheds.add(new Published());
        }
        adpter = new SaleCollect_Adpter(getContext(), 1, publisheds, this, activity);
        listview.setAdapter(adpter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.back, R.id.cancel, R.id.hide_trasmit, R.id.hide_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                break;
            case R.id.cancel:
                break;
            case R.id.hide_trasmit:
                break;
            case R.id.hide_delete:
                break;
        }
    }

    @Override
    public void showHideMenu(boolean is) {

    }

    @Override
    public void showDeleteSureDialog() {

    }

    @Override
    public void showNoContentText(boolean is) {

    }

    @Override
    public void showCollectDialog(final int id) {
        DialogUtils.DialogMenu2 dialogMenu2 = new DialogUtils.DialogMenu2(getContext());
        dialogMenu2.addMenuItem("收藏");
        dialogMenu2.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                OperateUtils.operate(id, getContext(), true, NS.COLLECT,false, new SimpleCallBack() {
                    @Override
                    public void onSuccess() {
                        noticeDialog("已收藏");
                    }

                    @Override
                    public void onError(Throwable e) {
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

    @Override
    public void noticeDialog(String message) {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void showFooter() {

    }

    @Override
    public void setmPresenter(@Nullable SaleCollectContract.Presenter presenter) {

    }
}
