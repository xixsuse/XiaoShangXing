package com.xiaoshangxing.utils.layout;

import android.content.Context;
import android.view.animation.AnimationUtils;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.utils.pull_refresh.PtrHandler;
import com.xiaoshangxing.utils.pull_refresh.StoreHouseHeader;

/**
 * Created by FengChaoQun
 * on 2016/9/20
 */
public class LayoutHelp {

    /**
     * description:初始化下拉刷新组件
     *
     * @param frame           下拉刷新组件
     * @param ptrHandler      下拉刷新时加载方法
     * @param needAutoRefresh 是否需要自动刷新
     * @return
     */

    public static void initPTR(PtrFrameLayout frame, boolean needAutoRefresh, PtrHandler ptrHandler) {
        Context context = frame.getContext();
        StoreHouseHeader header = new StoreHouseHeader(context);
        header.setPadding(0, context.getResources().getDimensionPixelSize(R.dimen.y144), 0, 20);
        header.initWithString("SWALK");
        header.setTextColor(context.getResources().getColor(R.color.green1));
        header.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        header.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
        frame.setDurationToCloseHeader(2000);
        frame.setHeaderView(header);
        frame.addPtrUIHandler(header);
        frame.setPtrHandler(ptrHandler);
        if (needAutoRefresh) {
            frame.autoRefresh();
        }
    }
}
