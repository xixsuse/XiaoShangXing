package com.xiaoshangxing.xiaoshang.Sale.SaleCollect;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.xiaoshangxing.data.bean.Published;
import com.xiaoshangxing.utils.baseClass.BaseFragment;
import com.xiaoshangxing.utils.customView.LayoutHelp;
import com.xiaoshangxing.utils.customView.dialog.DialogLocationAndSize;
import com.xiaoshangxing.utils.customView.dialog.DialogUtils;
import com.xiaoshangxing.utils.customView.loadingview.DotsTextView;
import com.xiaoshangxing.utils.customView.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.customView.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.utils.normalUtils.KeyBoardUtils;
import com.xiaoshangxing.xiaoshang.Sale.SaleActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import io.realm.Sort;

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
    PtrFrameLayout ptrFrameLayout;
    @Bind(R.id.hide_trasmit)
    ImageView hideTrasmit;
    @Bind(R.id.hide_delete)
    ImageView hideDelete;
    @Bind(R.id.hideMenu)
    RelativeLayout hideMenu;
    @Bind(R.id.no_content)
    TextView noContent;

    private View footview, headview;
    private DotsTextView dotsTextView;
    private TextView loadingText;
    private SaleActivity activity;
    private SaleCollect_Adpter_realm adpter_realm;
    private RealmResults<Published> publisheds;
    private View view;

    public static SaleCollectFragment newInstance() {
        return new SaleCollectFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.double_title_refresh_listview_hidebutton, null);
        ButterKnife.bind(this, view);
        initFresh();
        initView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void initView() {
        headview = new View(getContext());
//        解决在4.3上不能设置header问题
        listview.setAdapter(null);
        listview.addHeaderView(headview);
        footview = View.inflate(getContext(), R.layout.footer, null);
        dotsTextView = (DotsTextView) footview.findViewById(R.id.dot);
        dotsTextView.start();
        listview.addFooterView(footview);
        activity = (SaleActivity) getActivity();
        title.setText("收藏");
        refreshPager();
    }

    private void refreshPager() {
        publisheds = realm.where(Published.class)
                .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_SALE))
                .equalTo(NS.COLLECT_STATU, "1")
                .findAllSorted(NS.CREATETIME, Sort.DESCENDING);
        showNoContentText(publisheds.size() < 1);
        adpter_realm = new SaleCollect_Adpter_realm(getContext(), publisheds, this, activity);
        listview.setAdapter(adpter_realm);
        if (publisheds.size() > 0) {
            noContent.setVisibility(View.GONE);
        } else {
            noContent.setVisibility(View.VISIBLE);
            noContent.setText("你还没有收藏的闲置");
        }
    }

    private void initFresh() {
        LayoutHelp.initPTR(ptrFrameLayout, LoadUtils.needRefresh(LoadUtils.TIME_COLLECT_SALE),
                new PtrDefaultHandler() {
                    @Override
                    public void onRefreshBegin(final PtrFrameLayout frame) {
                        LoadUtils.getCollected(realm, NS.COLLECT_SALE, LoadUtils.TIME_COLLECT_SALE, getContext(),
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
                                        refreshPager();
                                    }

                                    @Override
                                    public void error() {
                                        frame.refreshComplete();
                                    }
                                });
                    }
                });
    }

    @OnClick({R.id.back, R.id.cancel, R.id.hide_trasmit, R.id.hide_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                getFragmentManager().popBackStack();
                break;
            case R.id.cancel:
//                adpter.showSelectCircle(false);
//                showHideMenu(false);
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
        if (is) {
            noContent.setVisibility(View.VISIBLE);
            listview.setVisibility(View.INVISIBLE);
        } else {
            noContent.setVisibility(View.INVISIBLE);
            listview.setVisibility(View.VISIBLE);
        }
    }

    public void showEdittext(boolean is, EditText editText) {
        if (is) {
            editText.setFocusable(true);
            editText.requestFocus();
            editText.setFocusableInTouchMode(true);
        } else {
            KeyBoardUtils.closeKeybord(editText, getContext());
        }
    }

    @Override
    public void showCollectDialog(final int id) {
        DialogUtils.DialogMenu2 dialogMenu2 = new DialogUtils.DialogMenu2(getContext());
        dialogMenu2.addMenuItem("取消收藏");
        dialogMenu2.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                OperateUtils.operateWithLoad(id, getContext(), true, NS.COLLECT, true, SaleCollectFragment.this,
                        new SimpleCallBack() {
                            @Override
                            public void onSuccess() {
                                noticeDialog("已取消收藏");
//                        realm.executeTransaction(new Realm.Transaction() {
//                            @Override
//                            public void execute(Realm realm) {
//                                Published published = realm.where(Published.class).equalTo(NS.ID, id).findFirst();
//                                published.setCollectStatus("0");
//                            }
//                        });
//                        refreshPager();
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
        DialogLocationAndSize.bottom_FillWidth(getActivity(), dialogMenu2);
    }

    @Override
    public void noticeDialog(String message) {
        DialogUtils.Dialog_No_Button dialog_no_button = new DialogUtils.Dialog_No_Button(getActivity(), message);
        final Dialog alertDialog = dialog_no_button.create();
        alertDialog.show();
        DialogLocationAndSize.setWidth(alertDialog, R.dimen.x420);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                alertDialog.dismiss();
            }
        }, 1000);
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
    public void setmPresenter(@Nullable SaleCollectContract.Presenter presenter) {

    }
}
