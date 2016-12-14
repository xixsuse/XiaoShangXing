package com.xiaoshangxing.xiaoshang.Help.HelpDetail;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.xiaoshangxing.utils.baseClass.IBasePresenter;
import com.xiaoshangxing.utils.baseClass.IBaseView;

/**
 * Created by FengChaoQun
 * on 2016/7/23
 */
public class HelpDetailContract {
    public interface View extends IBaseView<Presenter> {
        /*
        **describe:滑块滑动到指定位置
        */
        void moveToPosition(int position);

        /*
        **describe:滑块闪现到指定位置
        */
        void moveImediate(int position);

        /*
        **describe:弹出分享对话框
        */
        void showShareDialog();

        /*
        **describe:跳转到评论页面
        */
        void gotoInput();

        /*
        **describe:跳转到选人界面
        */
        void gotoSelectPeson();

        /*
        **describe:设置赞或取消
        */
        void setPraise();

        /*
        **describe:设置 评论 转发 赞 的数目
        */
        void setCount();

        /*
        **describe:弹出转发成功对话框
        */
        void showTransmitSuccess();
    }

    public interface Presenter extends IBasePresenter {
        /*
       **describe:转发  赞 分享到校友圈
       */
        void transmit(IMMessage imMessage, IMMessage text);

        void praise();

        void share();

    }
}
