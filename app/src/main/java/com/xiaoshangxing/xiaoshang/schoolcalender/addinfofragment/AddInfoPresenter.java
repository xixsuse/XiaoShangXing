package com.xiaoshangxing.xiaoshang.schoolcalender.addinfofragment;

import com.xiaoshangxing.xiaoshang.schoolcalender.SchoolCalenderInfoFragment.ZiXunInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quchwe on 2016/7/12 0012.
 */
public class AddInfoPresenter implements AddInfoContract.Presenter {

    AddInfoContract.View view;

    List<Integer> daysHaveThings = new ArrayList<>();
    public AddInfoPresenter(){

    }
    @Override
    public void saveTask(ZiXunInfo z) {

    }



    @Override
    public List<ZiXunInfo> getTask() {


        return null;
    }

    @Override
    public List<Integer> getDaysHaveThings(){
        daysHaveThings.add(20160718);
        daysHaveThings.add(20160812);
        daysHaveThings.add(20160721);
        return daysHaveThings;
    }
}

