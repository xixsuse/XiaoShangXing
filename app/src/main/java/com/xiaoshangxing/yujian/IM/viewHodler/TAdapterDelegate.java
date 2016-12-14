package com.xiaoshangxing.yujian.IM.viewHodler;

public interface TAdapterDelegate {

    public int getViewTypeCount();

    public Class<? extends TViewHolder> viewHolderAtPosition(int position);

    public boolean enabled(int position);
}