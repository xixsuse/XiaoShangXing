package com.xiaoshangxing.yujian.IM.viewHodler;

import android.content.Context;

import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.List;

/**
 * 最近联系人列表的adapter，管理了一个callback
 */
public class RecentContactAdapter extends TAdapter<RecentContact> {

    private RecentContactsCallback callback;

    public RecentContactAdapter(Context context, List<RecentContact> items, TAdapterDelegate delegate) {
        super(context, items, delegate);
    }

    public RecentContactsCallback getCallback() {
        return callback;
    }

    public void setCallback(RecentContactsCallback callback) {
        this.callback = callback;
    }
}
