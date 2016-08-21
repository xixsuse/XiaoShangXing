package com.xiaoshangxing.yujian.YujianFragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.OnItemClickListener;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.layout.Name;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

/**
 * Created by FengChaoQun
 * on 2016/8/11
 */
public class yujianAdpter extends SwipeMenuAdapter<yujianAdpter.ViewHolder> {

    private List<Item_yujian> list;
    private OnItemClickListener mOnItemClickListener;

    public yujianAdpter(List<Item_yujian> list) {
        this.list=list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yujian_recylerview, parent, false);
    }

    @Override
    public ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new ViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setOnItemClickListener(mOnItemClickListener);
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView message,time;
        CirecleImage head;
        Name name;
        OnItemClickListener mOnItemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            message=(TextView)itemView.findViewById(R.id.message);
            time=(TextView)itemView.findViewById(R.id.time);
            head=(CirecleImage)itemView.findViewById(R.id.head_image);
            name=(Name)itemView.findViewById(R.id.name);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        public void setData(Item_yujian item_yujian) {
            this.name.setText(item_yujian.getName());
            this.message.setText(item_yujian.getMessage());
            this.time.setText(item_yujian.getTime());
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}
