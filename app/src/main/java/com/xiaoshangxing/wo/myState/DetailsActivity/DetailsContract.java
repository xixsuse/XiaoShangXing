package com.xiaoshangxing.wo.myState.DetailsActivity;

import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/8/6
 */
public interface DetailsContract {

    interface View extends IBaseView<Presenter> {
        /*
        **describe:刷新数据
        */
        void refreshData();
        /*
        **describe:跳转到权限界面
        */
        void gotoPermisson();
        /*
        **describe:弹出确认删除对话框
        */
        void showSureDelete();

        /*
        **describe:关闭页面
        */
        void finishPager();
    }

    interface Presenter extends IBasePresenter {
        /*
        **describe:删除
        */
        void delete(Realm realm, Published published);
        /*
        **describe:赞
        */
        void praise();
    }
}
