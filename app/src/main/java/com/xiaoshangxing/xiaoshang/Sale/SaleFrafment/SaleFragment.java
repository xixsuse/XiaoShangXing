package com.xiaoshangxing.xiaoshang.Sale.SaleFrafment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.bean.Published;
import com.xiaoshangxing.network.netUtil.LoadUtils;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.network.netUtil.OperateUtils;
import com.xiaoshangxing.network.netUtil.SimpleCallBack;
import com.xiaoshangxing.publicActivity.album.AlbumActivity;
import com.xiaoshangxing.publicActivity.inputActivity.InputActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseFragment;
import com.xiaoshangxing.utils.customView.LayoutHelp;
import com.xiaoshangxing.utils.customView.RuleUtil;
import com.xiaoshangxing.utils.customView.dialog.DialogLocationAndSize;
import com.xiaoshangxing.utils.customView.dialog.DialogUtils;
import com.xiaoshangxing.utils.customView.loadingview.DotsTextView;
import com.xiaoshangxing.utils.customView.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.customView.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.utils.normalUtils.FileUtils;
import com.xiaoshangxing.utils.normalUtils.KeyBoardUtils;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.xiaoshang.Sale.PersonalSale.PersonalSaleFragment;
import com.xiaoshangxing.xiaoshang.Sale.SaleActivity;
import com.xiaoshangxing.xiaoshang.Sale.SaleCollect.SaleCollectFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by FengChaoQun
 * on 2016/9/28
 */
public class SaleFragment extends BaseFragment implements SaleContract.View {
    public static final String TAG = BaseFragment.TAG + "-SaleFragment";
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.reflesh_layout)
    PtrFrameLayout ptrFrameLayout;
    @Bind(R.id.no_content)
    TextView noContent;
    @Bind(R.id.mengban)
    View mengban;
    @Bind(R.id.rule_image)
    ImageView ruleImage;
    @Bind(R.id.rule_button)
    ImageView ruleButton;
    @Bind(R.id.wrap_view)
    RelativeLayout wrapView;
    @Bind(R.id.rules)
    RelativeLayout rules;
    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;


    private View mview;
    private View headview, footview;
    private DotsTextView dotsTextView;
    private TextView loadingText;
    private boolean isRefreshing;
    private boolean isLoading;

    private Sale_Adpter_realm adpter_realm;
    private RealmResults<Published> publisheds;
    private InputMethodManager imm;
    private Uri came_photo_path;
    private List<String> select_image_urls = new ArrayList<String>();//选择的图片
    private String account;
    private boolean isOthers;
    private RuleUtil ruleUtil;

    public static SaleFragment newInstance() {
        return new SaleFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.title_anounce_refresh_listview, null);
        ButterKnife.bind(this, mview);
        initView();
        return mview;
    }

    @Override
    public void onResume() {
        super.onResume();
        setCloseActivity();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setCloseActivity();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void setmPresenter(@Nullable SaleContract.Presenter presenter) {

    }

    private void initView() {
        title.setText("闲置出售");
        leftText.setText(R.string.xiaoshang);
        more.setImageResource(R.mipmap.add);
        ruleImage.setImageResource(R.mipmap.gonggao_xz);
        headview = new View(getContext());
        footview = View.inflate(getContext(), R.layout.footer, null);
        dotsTextView = (DotsTextView) footview.findViewById(R.id.dot);
        dotsTextView.start();
        loadingText = (TextView) footview.findViewById(R.id.text);
        listview.addHeaderView(headview);
        listview.addFooterView(footview);
        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(),
                            0);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(),
//                            0);
//                }
            }
        });

        if (getActivity().getIntent().getIntExtra(IntentStatic.TYPE, IntentStatic.MINE) == IntentStatic.OTHERS) {
            this.title.setText("他的闲置");
            this.more.setVisibility(View.GONE);
            headview.setVisibility(View.GONE);
            account = getActivity().getIntent().getStringExtra(IntentStatic.ACCOUNT);
            isOthers = true;
            listview.setPadding(0, 0, 0, 0);
            rules.setVisibility(View.GONE);
        } else {
            listview.setPadding(0, ScreenUtils.getAdapterPx(R.dimen.y96, getContext()), 0, 0);
            ruleUtil = new RuleUtil(mview, getActivity());
        }
        initFresh();
        refreshPager();
    }

    private void initFresh() {
        final LoadUtils.AroundLoading aroundLoading = new LoadUtils.AroundLoading() {
            @Override
            public void before() {
            }

            @Override
            public void complete() {
                ptrFrameLayout.refreshComplete();
            }

            @Override
            public void onSuccess() {
                refreshPager();
            }

            @Override
            public void error() {
                ptrFrameLayout.refreshComplete();
            }
        };

        if (isOthers) {
            LayoutHelp.initPTR(ptrFrameLayout, true, new PtrDefaultHandler() {
                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    LoadUtils.getPersonalPublished(realm, NS.CATEGORY_SALE, Integer.parseInt(account), getContext(),
                            aroundLoading);
                }
            });
        } else {
            LayoutHelp.initPTR(ptrFrameLayout, LoadUtils.needRefresh(LoadUtils.TIME_LOAD_SALE), new PtrDefaultHandler() {
                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    LoadUtils.getPublished(realm, NS.CATEGORY_SALE, LoadUtils.TIME_LOAD_SALE, getContext(), false,
                            aroundLoading);
                }
            });
        }

    }

    @Override
    public void showPublishMenu(View v) {
        View menu = View.inflate(getContext(), R.layout.popupmenu_rewardpubulish, null);
        final PopupWindow popupWindow = new PopupWindow(menu, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(getContext().getResources().
                getDrawable(R.drawable.nothing));
//        popupWindow.setAnimationStyle(R.style.popwindow_anim);

        menu.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int mShowMorePopupWindowWidth = menu.getMeasuredWidth();

        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);

        popupWindow.showAsDropDown(v,
                -mShowMorePopupWindowWidth + this.getResources().getDimensionPixelSize(R.dimen.x30) + v.getWidth(),
                3);

        View publish = menu.findViewById(R.id.publish);
        View published = menu.findViewById(R.id.published);
        View collect = menu.findViewById(R.id.collect);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPublishType();
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
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCollect();
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void gotoPublish() {
        if (select_image_urls.isEmpty()) {
            return;
        }
        Intent intent = new Intent(getContext(), InputActivity.class);
        intent.putExtra(InputActivity.EDIT_STATE, InputActivity.XIANZHI);
        intent.putExtra(InputActivity.LIMIT, 3);
        intent.putExtra(InputActivity.SELECT_IMAGE_URLS, (ArrayList<String>) select_image_urls);
        startActivityForResult(intent, IntentStatic.PUBLISH);
    }

    @Override
    public void gotoPublished() {
        SaleActivity activity = (SaleActivity) getActivity();
        PersonalSaleFragment fragment = activity.getPersonalSaleFragment();
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .hide(this)
//                .hide(activity.getSaleCollectFragment())
                .show(fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoCollect() {
        SaleActivity activity = (SaleActivity) getActivity();
        SaleCollectFragment fragment = activity.getSaleCollectFragment();
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .hide(this)
//                .hide(activity.getPersonalSaleFragment())
                .show(fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showCollectDialog(final int id, final boolean isCancle) {
        DialogUtils.DialogMenu2 dialogMenu2 = new DialogUtils.DialogMenu2(getContext());
        dialogMenu2.addMenuItem(isCancle ? "取消收藏" : "收藏");
        dialogMenu2.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                OperateUtils.operateWithLoad(id, getContext(), true, NS.COLLECT, isCancle, SaleFragment.this,
                        new SimpleCallBack() {
                            @Override
                            public void onSuccess() {
                                noticeDialog(isCancle ? "已取消收藏" : "已收藏");
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
    public void setRefreshState(boolean is) {
        isRefreshing = is;
    }

    @Override
    public boolean isRefreshing() {
        return isRefreshing;
    }

    @Override
    public void setLoadState(boolean is) {
        isLoading = is;
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void refreshPager() {
        if (isOthers) {
            publisheds = realm.where(Published.class)
                    .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_SALE))
                    .equalTo(NS.USER_ID, Integer.valueOf(account))
                    .findAllSorted(NS.CREATETIME, Sort.DESCENDING);
        } else {
            publisheds = realm.where(Published.class)
                    .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_SALE))
                    .findAllSorted(NS.CREATETIME, Sort.DESCENDING);
        }
        adpter_realm = new Sale_Adpter_realm(getContext(), publisheds, this, (SaleActivity) getActivity());
        listview.setAdapter(adpter_realm);
        if (publisheds.size() > 0) {
            noContent.setVisibility(View.GONE);
        } else {
            noContent.setVisibility(View.VISIBLE);
            noContent.setText(isOthers ? "他还没有发布闲置" : "还没有人发布闲置");
        }
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
    public void clickOnRule(boolean is) {
//        if (is) {
//            rules.setVisibility(View.VISIBLE);
//            rules.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.translate_move_in));
//        } else {
//            rules.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.translate_move_out));
//            rules.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    rules.setVisibility(View.GONE);
//                }
//            }, 500);
//        }
    }

    public boolean needCollespRule() {
        return ruleUtil.needhideRules();
    }

    @Override
    public void showEdittext(boolean is, EditText editText) {
        if (is) {
            editText.setFocusable(true);
            editText.requestFocus();
            editText.setFocusableInTouchMode(true);
//            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//            KeyBoardUtils.openKeybord(editText, getContext());
        } else {
            KeyBoardUtils.closeKeybord(editText, getContext());
        }
    }

    @Override
    public void showPublishType() {
        DialogUtils.DialogMenu2 dialogMenu2 = new DialogUtils.DialogMenu2(getContext());
        dialogMenu2.addMenuItem("拍照");
        dialogMenu2.addMenuItem("从手机相册选择");
        dialogMenu2.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                if (position == 0) {
                    openCamera();
                }
                if (position == 1) {
                    Intent album_intent = new Intent(getContext(), AlbumActivity.class);
                    album_intent.putExtra(AlbumActivity.LIMIT, 3);
                    album_intent.putExtra(AlbumActivity.SELECTED, (ArrayList<String>) select_image_urls);
                    startActivityForResult(album_intent, InputActivity.SELECT_PHOTO_FROM_ALBUM);
                }
            }

            @Override
            public void onCancel() {

            }
        });
        dialogMenu2.initView();
        dialogMenu2.show();
        DialogLocationAndSize.bottom_FillWidth(getActivity(), dialogMenu2);
    }

    private void openCamera() {
        came_photo_path = FileUtils.newPhotoPath();
        IntentStatic.openCamera(getActivity(), came_photo_path, InputActivity.TAKE_PHOTO);
    }

    public void moveListview(int px) {
        listview.smoothScrollBy(px, Math.abs(px));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == IntentStatic.PUBLISH && resultCode == IntentStatic.PUBLISH_SUCCESS) {
            if (ptrFrameLayout != null) {
                ptrFrameLayout.autoRefresh();
            }
        }

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        select_image_urls.clear();
        switch (requestCode) {
            case InputActivity.TAKE_PHOTO:
                List<String> temp = new ArrayList<String>();
                temp.add(came_photo_path.getPath());
                Log.d("came_photo_path", came_photo_path.toString());
                select_image_urls.add(came_photo_path.getPath());
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(came_photo_path);
                getActivity().sendBroadcast(intent);
                break;
            case InputActivity.SELECT_PHOTO_FROM_ALBUM:
                select_image_urls = data.getStringArrayListExtra(InputActivity.SELECT_IMAGE_URLS);
                break;
        }
        gotoPublish();
    }

    @OnClick({R.id.back, R.id.more})
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.more:
                LayoutHelp.PermissionClick(getActivity(), new LayoutHelp.PermisionMethod() {
                    @Override
                    public void doSomething() {
                        showPublishMenu(view);
                    }
                });
                break;
        }
    }

}
