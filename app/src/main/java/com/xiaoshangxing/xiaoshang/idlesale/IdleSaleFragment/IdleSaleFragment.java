package com.xiaoshangxing.xiaoshang.idlesale.IdleSaleFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.input_activity.InputActivity;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.FileUtils;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.utils.pull_refresh.PtrHandler;
import com.xiaoshangxing.utils.pull_refresh.StoreHouseHeader;
import com.xiaoshangxing.xiaoshang.RecyclerViewUtil;
import com.xiaoshangxing.xiaoshang.idlesale.IdleBean;
import com.xiaoshangxing.xiaoshang.idlesale.IdleDetailFragment.IdleDetailFragment;
import com.xiaoshangxing.xiaoshang.idlesale.IdleSaleActivity;
import com.xiaoshangxing.xiaoshang.idlesale.MyIdleSale.MyIdleSaleFragment;
import com.xiaoshangxing.xiaoshang.idlesale.SelectImageFromSd.AllPhotoActivity.AllIPhotosActivity;
import com.xiaoshangxing.xiaoshang.idlesale.SelectImageFromSd.ImageSelectActivity.ImageDirectoryActivity;
import com.xiaoshangxing.xiaoshang.planpropose.ReleasePopUp;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by quchwe on 2016/7/30 0030.
 */
public class IdleSaleFragment extends BaseFragment implements IdleSaleContract.View ,View.OnClickListener,View.OnTouchListener{

    public static final String TAG = BaseFragment.TAG+"IdleSale";

    public static final int TAKE_PHOTO = 1;
    private IdleSaleContract.Presenter mPresenter;

    @Bind(R.id.back)
    Button back;
    @Bind(R.id.rv_idle)
    RecyclerView idleRec;
    @Bind(R.id.ib_add)
    ImageView moreImage;
    @Bind(R.id.reflesh_layout)
    PtrFrameLayout ptrFrameLayout;

    private boolean isRefreshing;
    private boolean isLoading = false;
    public static IdleSaleFragment newInstance() {

        Bundle args = new Bundle();

        IdleSaleFragment fragment = new IdleSaleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    List<IdleBean> idles = new ArrayList<>();
    Uri came_photo_path = null;
    IdleSaleAdapter adapter;
    //手指向右滑动时的最小速度
    private static final int XSPEED_MIN = 200;

    //手指向右滑动时的最小距离
    private static final int XDISTANCE_MIN = 150;

    //记录手指按下时的横坐标。
    private float xDown;

    //记录手指移动时的横坐标。
    private float xMove;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_idel_sale,container,false);
        ButterKnife.bind(this,root);
        initList();
       adapter = new IdleSaleAdapter(getActivity(),this,idles);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        idleRec.setLayoutManager(manager);
        idleRec.setAdapter(adapter);
        idleRec.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnItemClickListener(new RecyclerViewUtil.OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                IdleSaleActivity activity = (IdleSaleActivity)getActivity();

                IdleDetailFragment idleDetailFragment = activity.getIdleDetailView();
//                getFragmentManager().beginTransaction()
//                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
//                                R.anim.slide_in_left, R.anim.slide_out_left)
//                        .replace(R.id.fl_idle_sale,)
//                        .show(activity.getIdleDetailView())
//                        .addToBackStack(IdleDetailFragment.TAG)
//                        .commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                                R.anim.slide_in_left, R.anim.slide_out_left)
                        .replace(R.id.fl_idle_sale, idleDetailFragment,IdleDetailFragment.TAG)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onItemLongClickListener(View v, int position) {

            }
        });
        idleRec.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                    int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    Log.d("wqv ", totalItemCount + "");
                    Log.d("fir ", firstVisibleItem + "");
                    Log.d("la", lastVisibleItem + "");
                    if (!isLoading && totalItemCount == (lastVisibleItem + firstVisibleItem + 1)) {
                        Log.d("wqv ", "怎么样你才会被调用 啊，卧槽");
                        adapter.setLoadMore(true, lastVisibleItem);

//                    //new ArticleTask(mActivity).execute(mAdapter.getBottomArticleId());
//                    View v  = layoutManager.findViewByPosition(totalItemCount-1);
////                    recyclerView.getRootView()
//                    MyAdapter.FootHolder holder =  (MyAdapter.FootHolder)recyclerView.getChildViewHolder(v);
//                    myAdapter.showFooter(holder);
                        isLoading = true;
                    }

                }
            }
        });
        initFresh();
        return root;
    }

    @Override
    public void showPop() {
        ReleasePopUp r = new ReleasePopUp();
        r.popRelease(getActivity(), moreImage, R.layout.popupmenu_rewardpubulish,new ReleasePopUp.OnPopClickListener() {
            @Override
            public void onReadyRelease() {
             showReleseDialog();

            }

            @Override
            public void onReleased() {
                MyIdleSaleFragment myPlanFragment = ((IdleSaleActivity)mActivity).getMyIdleView();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                                R.anim.slide_in_left, R.anim.slide_out_left)
                        .replace(R.id.fl_idle_sale,myPlanFragment,MyIdleSaleFragment.TAG)
                        .addToBackStack(IdleSaleFragment.TAG)
                        .commit();

            }

            @Override
            public void onJoined() {

            }
        });
    }

    private void initList(){
        IdleBean id= new IdleBean();
        id.setTime("20分钟前");
        id.setText("这是闲置的说明，最多可以显示三行这是闲置的说明，最多可以显示三行这是闲置的说明，最多可以显示三行这是闲置的说明，最多可以显示三行");
        id.setComplete(false);
        id.setDepartment("李园");
        id.setAcademy("设计学院");
        id.setPrice("1000");
        idles.add(id);
        id.setComplete(true);
        idles.add(id);
        idles.add(id);
        idles.add(id);
        idles.add(id);
        idles.add(id);
        idles.add(id);
        idles.add(id);
        idles.add(id);
        idles.add(id);
        idles.add(id);



    }
    @Override
    public void showReleseDialog() {
        DialogUtils.DialogMenu2 dialogMenu2 = new DialogUtils.DialogMenu2(getContext());
        dialogMenu2.addMenuItem("拍照");
        dialogMenu2.addMenuItem("从手机相册选择");
        dialogMenu2.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                if (position==0){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    came_photo_path = FileUtils.newPhotoPath();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, came_photo_path);
                    intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                    startActivityForResult(intent, TAKE_PHOTO);
                }if (position ==1){
                    getActivity().startActivity(new Intent(getContext(), AllIPhotosActivity.class));
                }
            }

            @Override
            public void onCancel() {

            }
        });
        dialogMenu2.initView();
        dialogMenu2.show();
        LocationUtil.bottom_FillWidth(getActivity(), dialogMenu2);
    }

    public void noticeDialog(String message) {
        DialogUtils.Dialog_No_Button dialog_no_button = new DialogUtils.Dialog_No_Button(getActivity(), message);
        final Dialog alertDialog = dialog_no_button.create();
        alertDialog.show();
        LocationUtil.setWidth(getActivity(), alertDialog,
                getActivity().getResources().getDimensionPixelSize(R.dimen.x420));

        new Handler().postDelayed(new Runnable() {
            public void run() {
                alertDialog.dismiss();
            }
        }, 1000);
    }

    @Override
    public void showFavoriteDialog() {
        DialogUtils.DialogMenu2 dialogMenu2 = new DialogUtils.DialogMenu2(getContext());
        dialogMenu2.addMenuItem("收藏");
        dialogMenu2.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                noticeDialog("已收藏");
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
    public void setmPresenter(@NonNull IdleSaleContract.Presenter presenter) {

        mPresenter = presenter;
    }

    @OnClick({R.id.back, R.id.ib_add})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.ib_add:
                showPop();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PHOTO&&resultCode== Activity.RESULT_OK){
            takePhoto();

        }
    }

    private void takePhoto(){

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(came_photo_path);
        getActivity().sendBroadcast(intent);
        ArrayList<String> imagePaths = new ArrayList<>();
        imagePaths.add(came_photo_path.toString());
        Intent k = new Intent(getContext(), InputActivity.class);
        k.putExtra(InputActivity.EDIT_STATE, InputActivity.XIANZHI);
        k.putStringArrayListExtra("path",imagePaths);
        startActivity(k);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        idles.clear();
    }
    public boolean isRefreshing() {
        return isRefreshing;
    }

    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void refreshPager() {

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
                mPresenter.RefreshData();
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



    public void setRefreshState(boolean is) {
        isRefreshing = is;
    }


    public void setLoadState(boolean is) {
        isLoading = is;
    }



    public void autoRefresh() {

    }


    public void showNoData() {

    }

    public void showFooter() {

    }


    public void clickOnRule() {

    }
    private VelocityTracker mVelocityTracker;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();;
                //活动的距离
                int distanceX = (int) (xMove - xDown);
                //获取顺时速度
                int xSpeed = getScrollVelocity();
                //当滑动的距离大于我们设定的最小距离且滑动的瞬间速度大于我们设定的速度时，返回到上一个activity
                if(distanceX > XDISTANCE_MIN && xSpeed > XSPEED_MIN) {
                   getActivity().getSupportFragmentManager().popBackStack();
                }
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return true;
    }
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     * 获取手指在content界面滑动的速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }
}
