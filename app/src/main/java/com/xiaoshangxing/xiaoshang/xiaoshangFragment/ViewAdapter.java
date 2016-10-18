package com.xiaoshangxing.xiaoshang.XiaoShangFragment;


import android.view.View;

public interface ViewAdapter {

    int MAX_ELEVATION_FACTOR = 8;


    View getCardViewAt(int position);

    int getCount();
}
