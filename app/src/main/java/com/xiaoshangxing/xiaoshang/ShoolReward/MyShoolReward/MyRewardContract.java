package com.xiaoshangxing.xiaoshang.ShoolReward.MyShoolReward;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;

/**
 * Created by FengChaoQun
 * on 2016/7/23
 */
public class MyRewardContract {
    public interface View extends IBaseView<Presenter> {
        /*
        **describe:控制隐藏菜单的显示
        */
        void showHideMenu(boolean is);

        /*
        **describe:弹出删除对话框
        */
        void showDeleteSureDialog();

        /*
        **describe:没有内容时
        */
        void showNoContentText(boolean is);
        /*
        **describe:刷新数据
        */
        void refreshData();
        /*
        **describe:没有数据了
        */
        void showNoData();

        /*
       **describe:显示listview尾
       */
        void showFooter();
    }

    public interface Presenter extends IBasePresenter {
        /*
        **describe:转发 删除
        */
        void transmit();
        void delete();
        /*
        **describe:结束或取消
        */
        void completeOrCancle();
        /*
        **describe:加载数据
        */
        void refreshData();
        /*
        **describe:加载更多
        */
        void LoadMore();
    }
}
