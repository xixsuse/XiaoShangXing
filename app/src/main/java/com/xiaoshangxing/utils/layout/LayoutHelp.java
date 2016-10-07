package com.xiaoshangxing.utils.layout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.animation.AnimationUtils;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.setting.shiming.ShenheActivity;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;
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
        header.initWithString("SMATE");
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

    public static void PermissionClick(final Activity activity, PermisionMethod permisionMethod) {
        if (!TempUser.isRealName) {
            final DialogUtils.Dialog_Center dialogUtils = new DialogUtils.Dialog_Center(activity);
            final Dialog alertDialog = dialogUtils.Message("你还没有实名认证，\n是否需要前往验证。")
                    .Button("取消", "立即验证").MbuttonOnClick(new DialogUtils.Dialog_Center.buttonOnClick() {
                        @Override
                        public void onButton1() {
                            dialogUtils.close();
                        }

                        @Override
                        public void onButton2() {
                            Intent intent = new Intent(activity, ShenheActivity.class);
                            activity.startActivity(intent);
                            dialogUtils.close();
                        }
                    }).create();
            alertDialog.show();
            LocationUtil.setWidth(activity, alertDialog,
                    activity.getResources().getDimensionPixelSize(R.dimen.x780));
        } else {
            permisionMethod.doSomething();
        }
    }

    public interface PermisionMethod {
        void doSomething();
    }
}
