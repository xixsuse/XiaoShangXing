package com.xiaoshangxing.xiaoshang.Reward;

import android.app.Dialog;

import com.xiaoshangxing.utils.baseClass.IBasePresenter;
import com.xiaoshangxing.utils.baseClass.IBaseView;

/**
 * Created by FengChaoQun
 * on 2016/8/6
 */
public interface RewardContract {

    interface View extends IBaseView<Presenter> {

        /*
        **describe:弹出转发成功对话框
        */
        void showTransmitSuccess();
    }

    interface Presenter extends IBasePresenter {
        /*
        **describe:转发到校友圈
        */
        void transmit(Dialog dialog);
    }
}
