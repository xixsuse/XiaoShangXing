package com.xiaoshangxing.xiaoshang;


import android.view.View;

public interface ViewAdapter {

    int MAX_ELEVATION_FACTOR = 8;


    View getCardViewAt(int position);

    int getCount();
}
