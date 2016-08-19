package com.xiaoshangxing.xiaoshang.idlesale.IdleDetailFragment;



/**
 * Created by quchwe on 2016/8/5 0005.
 */
public class IdleDetailPresenter implements IdleDetailContract.Presenter {

    private final IdleDetailContract.View mView;

    public IdleDetailPresenter(IdleDetailContract.View view){
        this.mView = view;

        view.setmPresenter(this);
    }
}
