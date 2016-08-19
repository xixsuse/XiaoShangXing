package com.xiaoshangxing.xiaoshang.idlesale.IdleDetailFragment;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;

/**
 * Created by quchwe on 2016/8/5 0005.
 */
public interface IdleDetailContract {
    interface Presenter extends IBasePresenter {

    }
    interface View extends IBaseView<Presenter> {
        void showPop();
        void showShareDialog();
        void showShareXYQDialog();
    }
}
