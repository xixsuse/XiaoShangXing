package com.xiaoshangxing.xiaoshang.idlesale.IdleSaleFragment;

import android.support.annotation.NonNull;

import com.xiaoshangxing.data.Bean;
import com.xiaoshangxing.xiaoshang.idlesale.IdleBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quchwe on 2016/7/30 0030.
 */
public class IdleSalePresenter implements IdleSaleContract.Presenter {

    private final IdleSaleContract.View mView;
    private final Bean bean;

    public IdleSalePresenter(@NonNull IdleSaleContract.View view,@NonNull Bean bean){
        this.mView = view;
        this.bean = bean;
        mView.setmPresenter(this);

    }
    @Override
    public List<IdleBean> getList() {
        List<IdleBean> dataList = new ArrayList<>();
        return dataList;
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void RefreshData() {

    }
}
