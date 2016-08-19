package com.xiaoshangxing.xiaoshang.idlesale.favoriteIdleFragment;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.xiaoshang.idlesale.IdleBean;

import java.util.List;

/**
 * Created by quchwe on 2016/8/9 0009.
 */

public interface FavoriteIdleContract {
    interface Presenter extends IBasePresenter {
        List<IdleBean> getList();
        void sendMessage(String message);

    }
    interface View extends IBaseView<Presenter> {
        void showShare();
        void showFavoriteDialog();

    }
}
