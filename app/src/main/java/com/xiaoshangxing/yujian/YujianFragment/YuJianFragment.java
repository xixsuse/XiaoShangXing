package com.xiaoshangxing.yujian.YujianFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.ListViewDecoration;
import com.xiaoshangxing.utils.OnItemClickListener;
import com.xiaoshangxing.utils.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.utils.pull_refresh.PtrHandler;
import com.xiaoshangxing.utils.pull_refresh.StoreHouseHeader;
import com.xiaoshangxing.yujian.chatInfo.ChatInfoActivity;
import com.xiaoshangxing.yujian.xiaoyou.XiaoYouActivity;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/8/11
 */

public class YuJianFragment extends BaseFragment {

    public static final String TAG = BaseFragment.TAG + "-YuJianFragment";

    @Bind(R.id.schoolfellow)
    ImageView schoolfellow;
    @Bind(R.id.serch_layout)
    RelativeLayout serchLayout;
    @Bind(R.id.friend)
    ImageView friend;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.recyclerView)
    SwipeMenuRecyclerView recyclerView;
    @Bind(R.id.reflesh_layout)
    PtrFrameLayout ptrFrameLayout;
    @Bind(R.id.no_net_lay)
    FrameLayout noNetLay;

    public static YuJianFragment newInstance() {
        return new YuJianFragment();
    }

    private View mView;
    private List<Item_yujian> list = new ArrayList<>();
    private yujianAdpter adpter;
    private MyReceiver myReceiver;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = View.inflate(getContext(), R.layout.frag_yujian, null);
        ButterKnife.bind(this, mView);
        initView();
        initFresh();
        return mView;
    }

    private void initView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new ListViewDecoration(getResources().getDimensionPixelSize(R.dimen.x208), 0));
// 设置菜单创建器。
        recyclerView.setSwipeMenuCreator(swipeMenuCreator);
        recyclerView.setSwipeMenuItemClickListener(menuItemClickListener);

        for (int i = 0; i < 20; i++) {
            Item_yujian item_yujian = new Item_yujian();
            item_yujian.setMessage("message..." + i);
            item_yujian.setName("name..." + i);
            item_yujian.setTime("time..." + i);
            list.add(item_yujian);
        }
        adpter = new yujianAdpter(list);
        adpter.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(adpter);
        recyclerView.setLongPressDragEnabled(true);
        recyclerView.setOnItemMoveListener(onItemMoveListener);

        myReceiver = new MyReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(myReceiver, mFilter);

    }

    private void initFresh() {
        StoreHouseHeader header = new StoreHouseHeader(getContext());
        header.setPadding(0, getResources().getDimensionPixelSize(R.dimen.y144), 0, 20);
        header.initWithString("SWALK");
        header.setTextColor(getResources().getColor(R.color.green1));
        header.setBackgroundColor(getResources().getColor(R.color.w0));
        header.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
        ptrFrameLayout.setDurationToCloseHeader(2000);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.disableWhenHorizontalMove(true);
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
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                    }
                }, 1500);
            }
        });
    }

    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
                    .setBackgroundColor(getResources().getColor(R.color.red2))
                    .setText("删除") // 文字，还可以设置文字颜色，大小等。。
                    .setTextColor(Color.WHITE)
                    .setTextSize(16)
                    .setWidth(getResources().getDimensionPixelSize(R.dimen.y250))
                    .setHeight(getResources().getDimensionPixelSize(R.dimen.y192));
            swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
        }
    };

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Toast.makeText(mContext, "我是第" + position + "条。", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 菜单点击监听。
     */
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        /**
         * Item的菜单被点击的时候调用。
         * @param closeable       closeable. 用来关闭菜单。
         * @param adapterPosition adapterPosition. 这个菜单所在的item在Adapter中position。
         * @param menuPosition    menuPosition. 这个菜单的position。比如你为某个Item创建了2个MenuItem，那么这个position可能是是 0、1，
         * @param direction       如果是左侧菜单，值是：SwipeMenuRecyclerView#LEFT_DIRECTION，如果是右侧菜单，值是：SwipeMenuRecyclerView#RIGHT_DIRECTION.
         */
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();// 关闭被点击的菜单。

//            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
//                Toast.makeText(mContext, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
//            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
//                Toast.makeText(mContext, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
//            }
            Log.d("position", "" + adapterPosition);
            list.remove(adapterPosition);
            adpter.notifyDataSetChanged();
        }
    };

    private OnItemMoveListener onItemMoveListener = new OnItemMoveListener() {
        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            // 当Item被拖拽的时候。
            Collections.swap(list, fromPosition, toPosition);
            adpter.notifyItemMoved(fromPosition, toPosition);
            return true;// 返回true表示处理了，返回false表示你没有处理。
        }

        @Override
        public void onItemDismiss(int position) {
            // 当Item被滑动删除掉的时候，在这里是无效的，因为这里没有启用这个功能。
            // 使用Menu时就不用使用这个侧滑删除啦，两个是冲突的。
        }
    };

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            //Toast.makeText(context, intent.getAction(), 1).show();
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//            NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo activeInfo = manager.getActiveNetworkInfo();
            try {
                if (activeInfo == null) {
                    noNetLay.setVisibility(View.VISIBLE);
                } else {
                    noNetLay.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

//        Toast.makeText(context, "mobile:"+mobileInfo.isConnected()+"\n"+"wifi:"+wifiInfo.isConnected()
//                +"\n"+"active:"+activeInfo.getTypeName(), Toast.LENGTH_SHORT).show();
        }  //如果无网络连接activeInfo为null

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(myReceiver);
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.schoolfellow, R.id.serch_layout, R.id.friend})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.schoolfellow:
                Intent fellow_intent=new Intent(getContext(), XiaoYouActivity.class);
                startActivity(fellow_intent);
                break;
            case R.id.serch_layout:
                break;
            case R.id.friend:
                Intent chatIntent=new Intent(getContext(), ChatInfoActivity.class);
                startActivity(chatIntent);
                break;
        }
    }
}
