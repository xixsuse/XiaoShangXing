package com.xiaoshangxing.xiaoshang.MessageNotice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.xiaoshangxing.data.PushMsg;
import com.xiaoshangxing.xiaoshang.MessageNotice.NoticeViewHodler.NoticeBaseViewHolder;
import com.xiaoshangxing.xiaoshang.MessageNotice.NoticeViewHodler.NoticeCommentVH;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by FengChaoQun
 * on 2016/11/16
 */

public class NoticeAdpter extends ArrayAdapter<PushMsg> {
    private Context context;
    List<PushMsg> pushMsgs;
    private final Map<Class<?>, Integer> viewTypes;

    public NoticeAdpter(Context context, int res, List<PushMsg> pushMsgs) {
        super(context, res, pushMsgs);
        this.context = context;
        this.pushMsgs = pushMsgs;
        this.viewTypes = new HashMap<Class<?>, Integer>(getViewTypeCount());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        NoticeBaseViewHolder noticeBaseViewHolder;
        if (convertView == null) {
            convertView = viewAtPosition(position);
        }
        noticeBaseViewHolder = (NoticeBaseViewHolder) convertView.getTag();
        noticeBaseViewHolder.setPosition(position);
        noticeBaseViewHolder.refresh(pushMsgs.get(position));
        return convertView;

    }


    @Override
    public int getItemViewType(int position) {

        if (getViewTypeCount() == 1) {
            return 0;
        }

        Class<?> clazz = viewHolderAtPosition(position);
        if (viewTypes.containsKey(clazz)) {
            return viewTypes.get(clazz);
        } else {
            int type = viewTypes.size();
            if (type < getViewTypeCount()) {
                viewTypes.put(clazz, type);
                return type;
            }
            return 0;
        }

    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }


    public View viewAtPosition(int position) {
        NoticeBaseViewHolder holder = null;
        View view = null;
        try {
            Class<?> viewHolder = viewHolderAtPosition(position);
            holder = (NoticeBaseViewHolder) viewHolder.newInstance();
            holder.setAdpter(this);
            holder.setContext(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        view = holder.getView(LayoutInflater.from(context));
        view.setTag(holder);
        holder.setContext(view.getContext());
        return view;
    }

    private Class<? extends NoticeBaseViewHolder> viewHolderAtPosition(int position) {
        return NoticeCommentVH.class;
    }
}
