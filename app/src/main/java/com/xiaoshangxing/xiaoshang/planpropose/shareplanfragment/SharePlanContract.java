package com.xiaoshangxing.xiaoshang.planpropose.shareplanfragment;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;

/**
 * Created by quchwe on 2016/7/25 0025.
 */
public interface SharePlanContract {
    interface Presenter extends IBasePresenter {

        void saveShareInfo(String shareInfo);
    }
    interface View extends IBaseView<Presenter> {
        void showCompleted();


    }
}
