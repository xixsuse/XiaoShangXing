package com.xiaoshangxing.xiaoshang.schoolcalender.addEntranceFragment;

/**
 * Created by quchwe on 2016/7/22 0022.
 */
import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;

import java.util.List;

/**
 * Created by quchwe on 2016/7/10 0010.
 */
public class AddEntryceContract {
    interface Presenter extends IBasePresenter {
        List<ManagerInfo> getManagerList();

    }
    interface View extends IBaseView<Presenter> {

    }

}