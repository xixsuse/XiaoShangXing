package com.xiaoshangxing.xiaoshang.schoolcalender.SchoolCalenderInfoFragment;

import com.xiaoshangxing.data.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quchwe on 2016/7/22 0022.
 */
public class CalenderInfoPresenter implements CalenderInfoContract.Presenter {
    List<Integer> daysHaveThings = new ArrayList<>();
    List<ZiXunInfo> ziXunInfos=new ArrayList<>();
    private CalenderInfoContract.View mView;
    private Bean bean;


    public CalenderInfoPresenter(Bean bean, CalenderInfoContract.View view){
        this.bean = bean;
        this.mView = view;
    }
    @Override
    public List<ZiXunInfo> getTask() {
        ZiXunInfo z = new ZiXunInfo();
        z.setName("今天校运会");
        z.setNumberOfLegs(20160718);
        ZiXunInfo z1 = new ZiXunInfo();
        z1.setName("今天畅游江南大学");
        z1.setNumberOfLegs(20160812);
        ZiXunInfo z2 = new ZiXunInfo();
        z2.setName("今天哈哈");
        z2.setNumberOfLegs(20160721);
        ziXunInfos.add(z);
        ziXunInfos.add(z1);
        ziXunInfos.add(z2);
        return ziXunInfos;
    }

    void setUpListeners(){
        mView.setmPresenter(this);
    }
    @Override
    public void onClickDate() {
        mView.showLoadingProgress();
        mView.showCurrentTask(bean.getTask());
        mView.showCompleteShow();
    }

    @Override
    public List<Integer> getDayHasThingsList() {
        daysHaveThings.add(20160718);
        daysHaveThings.add(20160812);
        daysHaveThings.add(20160721);
        return daysHaveThings;
    }
}

