package com.xiaoshangxing.xiaoshang.idlesale.MyIdleSale;

/**
 * Created by quchwe on 2016/8/9 0009.
 */

public class MyIdlePresenter implements MyIdleContract.Presenter {

    private final MyIdleContract.View mView;

    public MyIdlePresenter(MyIdleContract.View view){
        this.mView = view;
        mView.setmPresenter(this);
    }
}
