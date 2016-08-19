package com.xiaoshangxing.xiaoshang.schoolcalender.SchoolCalenderInfoFragment;

import android.support.annotation.Nullable;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;

import java.util.List;

/**
 * Created by quchwe on 2016/7/22 0022.
 */
public class CalenderInfoContract {
    interface View extends IBaseView<Presenter> {
        void showInfo();

        void showLoadingProgress();
        void showCurrentTask(List<ZiXunInfo> ziXunInfos);

        void showCompleteShow();

        void showCalenderInfo();

        void setmPresenter(@Nullable Presenter presenter);

        void showNoInfos();
    }
    interface Presenter extends IBasePresenter {

        List<ZiXunInfo> getTask();

        void onClickDate();

        List<Integer> getDayHasThingsList();
    }

}