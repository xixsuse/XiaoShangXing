package com.xiaoshangxing.xiaoshang.schoolcalender.addinfofragment;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.xiaoshang.schoolcalender.SchoolCalenderInfoFragment.ZiXunInfo;

import java.util.List;

/**
 * Created by quchwe on 2016/7/12 0012.
 */
public class AddInfoContract {
    interface Presenter extends IBasePresenter {
        void saveTask(ZiXunInfo z);
        List<ZiXunInfo> getTask();

        List<Integer> getDaysHaveThings();
    }
    interface View extends IBaseView<Presenter> {
        void showAddTask();
        void showDateSelect();

    }
}