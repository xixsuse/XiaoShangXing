package com.xiaoshangxing.xiaoshang.ShoolReward.RewardDetail;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;

/**
 * Created by FengChaoQun
 * on 2016/7/23
 */
public class RewardContract {
    public interface  view extends IBaseView<Presenter>{
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
        **describe:控制输入框
        */
        void showInputBox(boolean is);
    }

    public interface Presenter extends IBasePresenter{

        /*
        **describe:转发 评论 赞
        */
        void transmit();
        void comment();
        void praise();

        /*
        **describe:收藏与否
        */
        void collect();
    }
}
