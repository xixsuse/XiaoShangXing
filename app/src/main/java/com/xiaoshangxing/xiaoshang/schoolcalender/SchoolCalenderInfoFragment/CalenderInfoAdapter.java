package com.xiaoshangxing.xiaoshang.schoolcalender.SchoolCalenderInfoFragment;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.loadingview.DotsTextView;
import com.xiaoshangxing.xiaoshang.RecyclerViewUtil;

import java.util.List;

/**
 * Created by quchwe on 2016/7/22 0022.
 */
public class CalenderInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ZiXunInfo> ziXunInfos;
    private final Context mContext;
    LayoutInflater inflater;
    private RecyclerViewUtil.OnItemClickListener mListener;
    public CalenderInfoAdapter(Context context,List<ZiXunInfo> ziXunInfos){
        this.mContext = context;
        this.ziXunInfos = ziXunInfos;
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public @RecyclerViewUtil.ItemType int getItemViewType(int position) {
        if (position==0){
            return RecyclerViewUtil.TYPE_HEADER;
        }else if (position>ziXunInfos.size()) {
            return RecyclerViewUtil.TYPE_FOOTER;
        }else
            return RecyclerViewUtil.TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType== RecyclerViewUtil.TYPE_HEADER){
            HeaderHolder holer = new HeaderHolder(inflater.inflate(R.layout.headview_help_list,parent,false));
            return holer;
        }else if (viewType == RecyclerViewUtil.TYPE_FOOTER){
            FootHolder holder = new FootHolder(inflater.inflate(R.layout.footer,parent,false));
            return holder;
        }else if (viewType == RecyclerViewUtil.TYPE_NORMAL) {
            CalenderInfoHolder viewHoler = new CalenderInfoHolder(inflater.inflate(R.layout.recycler_list_calender_info, parent, false));
            return viewHoler;
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {
        if (position==0&&holder1 instanceof HeaderHolder){
            HeaderHolder holer = (HeaderHolder)holder1;
        }else if (position>(ziXunInfos.size())&&holder1 instanceof FootHolder){
            FootHolder holder =(FootHolder) holder1;
            holder.dotsTextView.start();
//            if (loadMore){
//                showFooter(holder);
//            }
        }else if (position>0&&holder1 instanceof CalenderInfoHolder){
            ZiXunInfo z = ziXunInfos.get(position-1);
            CalenderInfoHolder holder = (CalenderInfoHolder)holder1;
            holder.name .setText(z.name());
            holder.position.setText("("+z.getPosition()+")");
            holder.info.setText(z.getInfo());
            holder.image.setBackgroundColor(z.getMipMap());
            if (mListener!=null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemClickListener(v,position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return ziXunInfos.size()+2;
    }

    class CalenderInfoHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView position;
        TextView info;
        CirecleImage image;
        public CalenderInfoHolder(View itemView) {
            super(itemView);
            image  = (CirecleImage)itemView.findViewById(R.id.circleView_calender_circle);
            name = (TextView) itemView.findViewById(R.id.tv_manager_name);
            position = (TextView) itemView.findViewById(R.id.tv_position);
            info = (TextView)itemView.findViewById(R.id.tv_calender_info);
        }
    }
    class HeaderHolder extends RecyclerView.ViewHolder{
        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }

    class FootHolder extends RecyclerView.ViewHolder{
        TextView loadingText ;
        DotsTextView dotsTextView;
        public FootHolder(View itemView) {
            super(itemView);
            loadingText  = (TextView) itemView.findViewById(R.id.text);
            dotsTextView = (DotsTextView) itemView.findViewById(R.id.dot);
        }
    }
    public void setOnItemClickListener(RecyclerViewUtil.OnItemClickListener listener){
        this.mListener = listener;
    }
}
