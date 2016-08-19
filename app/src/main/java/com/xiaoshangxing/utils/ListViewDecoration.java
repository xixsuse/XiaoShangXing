package com.xiaoshangxing.utils;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xiaoshangxing.R;
import com.yanzhenjie.recyclerview.swipe.ResCompat;

/**
 * Created by FengChaoQun
 * on 2016/8/11
 */
public class ListViewDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDrawable;

    private int margingleft, margingright;
    public ListViewDecoration(int left,int right) {
        mDrawable = ResCompat.getDrawable(XSXApplication.getInstance(), R.drawable.divider_recycler);
        this.margingleft =left;
        this.margingright =right;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            // 以下计算主要用来确定绘制的位置
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDrawable.getIntrinsicHeight();
            mDrawable.setBounds(left+margingleft, top, right-margingright, bottom);
            mDrawable.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, mDrawable.getIntrinsicHeight());
    }
}

