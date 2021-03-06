package com.xiaoshangxing.xiaoshang.Sale.SaleCollect;

import com.xiaoshangxing.utils.baseClass.IBasePresenter;
import com.xiaoshangxing.utils.baseClass.IBaseView;

/**
 * Created by FengChaoQun
 * on 2016/7/23
 */
public class SaleCollectContract {
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
        **describe:弹出取消收藏对话框
        */
        void showCollectDialog(int id);

        /*
        **describe:提示弹窗
        */
        void noticeDialog(String message);

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
        **describe:取消收藏
        */
        void unCollect();

        /*
        **describe:加载更多
        */
        void loadMore();
    }
}
