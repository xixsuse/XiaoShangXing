package com.xiaoshangxing.yujian.IM.viewHodler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.xiaoshangxing.yujian.ChatActivity.ModuleProxy;

import io.realm.Realm;

public abstract class TViewHolder {
    /**
     * context
     */
    protected Context context;

    /**
     * list item view
     */
    protected View view;

    /**
     * adapter providing data
     */
    protected TAdapter adapter;

    /**
     * index of item
     */
    protected int position;

    protected ModuleProxy moduleProxy;


    public TViewHolder() {

    }

    protected void setContext(Context context) {

        this.context = context;
    }

    protected void setAdapter(TAdapter adapter) {
        this.adapter = adapter;
    }

    protected TAdapter getAdapter() {
        return this.adapter;
    }

    protected void setPosition(int position) {
        this.position = position;
    }

    public View getView(LayoutInflater inflater) {
        int resId = getResId();
        view = inflater.inflate(resId, null);
        inflate();
        return view;
    }

    public boolean isFirstItem() {
        return position == 0;
    }

    public boolean isLastItem() {
        return position == adapter.getCount() - 1;
    }

    protected abstract int getResId();

    protected abstract void inflate();

    protected abstract void refresh(Object item);

    public ModuleProxy getModuleProxy() {
        return moduleProxy;
    }

    public void setModuleProxy(ModuleProxy moduleProxy) {
        this.moduleProxy = moduleProxy;
    }

    public void destory() {

    }

    protected <T extends View> T findView(int resId) {
        return (T) (view.findViewById(resId));
    }
}