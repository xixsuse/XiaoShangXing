package com.xiaoshangxing.xiaoshang.idlesale.favoriteIdleFragment;

import android.support.annotation.NonNull;

import com.xiaoshangxing.data.Bean;
import com.xiaoshangxing.xiaoshang.idlesale.IdleBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quchwe on 2016/8/9 0009.
 */

public class FavoriteIdlePresenter implements FavoriteIdleContract.Presenter {
    private final FavoriteIdleContract.View mView;
    private final Bean bean;

    public FavoriteIdlePresenter(@NonNull FavoriteIdleContract.View view, @NonNull Bean bean){
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

}
