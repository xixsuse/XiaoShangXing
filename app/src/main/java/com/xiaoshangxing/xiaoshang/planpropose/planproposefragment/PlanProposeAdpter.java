package com.xiaoshangxing.xiaoshang.planpropose.planproposefragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.loadingview.DotsTextView;

import com.xiaoshangxing.xiaoshang.RecyclerViewUtil;
import com.xiaoshangxing.xiaoshang.planpropose.PlanList;

import java.util.List;

import static com.xiaoshangxing.xiaoshang.RecyclerViewUtil.TYPE_FOOTER;
import static com.xiaoshangxing.xiaoshang.RecyclerViewUtil.TYPE_HEADER;
import static com.xiaoshangxing.xiaoshang.RecyclerViewUtil.TYPE_NORMAL;

/**
 * Created by quchwe on 2016/8/15 0015.
 */

public class PlanProposeAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public boolean loadMore = false;
    private OnItemClikListener mListener;
    private List<PlanList> mPlanList;
    private LayoutInflater inflater;
    private final Context mContext;
    private final PlanProposeContract.View mView;

    public PlanProposeAdpter(Context context,List<PlanList> lists,PlanProposeContract.View mView){
        this.mContext = context;
        this.mPlanList = lists;
        this.inflater = LayoutInflater.from(mContext);
        this.mView = mView;
    }
    @Override
    public  @RecyclerViewUtil.ItemType int getItemViewType(int position) {
        if (position==0){
            return TYPE_HEADER;
        }else if (position>mPlanList.size()) {
            return TYPE_FOOTER;
        }else
            return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==TYPE_HEADER){
            HeaderHoler holer = new HeaderHoler(inflater.inflate(R.layout.headview_help_list,parent,false));
            return holer;
        }else if (viewType ==TYPE_FOOTER){
            FootHolder holder = new FootHolder(inflater.inflate(R.layout.footer,parent,false));
            return holder;
        }else if (viewType==TYPE_NORMAL){
            PlanProposeViewHolder holder = new PlanProposeViewHolder(inflater.inflate(R.layout.rv_plan_propose,parent,false));
            return  holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {
        if (position==0&&holder1 instanceof HeaderHoler){
            HeaderHoler holer = (HeaderHoler)holder1;
            holder1.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mView.clickOnRule();
                }
            });
        }else if (position>0&&holder1 instanceof PlanProposeViewHolder){
             final PlanList planList = mPlanList.get(position-1);

            PlanProposeViewHolder holder = (PlanProposeViewHolder)holder1;
            if (mListener!=null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemClick(v,position-1);
                    }
                });
            }
            holder.headImage.setImageResource(R.mipmap.cirecleimage_default);
            holder.name.setText(planList.getName());
            holder.academy.setText(planList.getAcademy());
            holder.planInfo.setText(planList.getText());
            holder.time.setText(planList.getTime());
            holder.planName.setText("#"+planList.getPlanName());
            holder.planLimtNumber.setText("0-"+planList.getLimitPepoleNumber()+"人");
            holder.participateNumber.setText(planList.getPaticipateNumber()+"");
            if (planList.isCompleted()){
                holder.showComplete.setVisibility(View.VISIBLE);

            }else {
                holder.showComplete.setVisibility(View.GONE);
            }


            holder.showFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("wq","wq");
                    mView.showDialog(planList);
                }
            });
            if (planList.isFull()){
                holder.isFull.setVisibility(View.VISIBLE);
                holder.participateNumber.setTextColor(mContext.getResources().getColor(R.color.g0));
            }else {
                holder.isFull.setVisibility(View.INVISIBLE);
                holder.participateNumber.setTextColor(mContext.getResources().getColor(R.color.green1));
            }
        }else if (position>mPlanList.size()&&holder1 instanceof FootHolder){
            FootHolder holder =(FootHolder) holder1;
            holder.dotsTextView.start();
            if (loadMore){
                showFooter(holder);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mPlanList.size()+2;
    }

    class PlanProposeViewHolder extends RecyclerView.ViewHolder{
        CirecleImage headImage;
        TextView name;
        TextView academy;
        TextView planInfo;
        TextView time;
        TextView planName;
        TextView planLimtNumber;
        TextView participateNumber;
        ImageButton showFavorite;
        TextView isFull;
        ImageView showComplete;


        public PlanProposeViewHolder(View itemView) {
            super(itemView);
            headImage = (CirecleImage) itemView.findViewById(R.id.iv_headImage);
            name = (TextView)itemView.findViewById(R.id.tv_name);
            planName = (TextView)itemView.findViewById(R.id.tv_plan_name);
            academy = (TextView) itemView.findViewById(R.id.tv_plan_list_academy);
            time = (TextView)itemView.findViewById(R.id.tv_list_plan_time);
            planInfo = (TextView)itemView.findViewById(R.id.tv_plan_info);
            planLimtNumber = (TextView)itemView.findViewById(R.id.tv_list_plan_renshu);
            participateNumber = (TextView)itemView.findViewById(R.id.tv_participate_number);
            showFavorite = (ImageButton) itemView.findViewById(R.id.ib_reward_downArrow);
            isFull = (TextView)itemView.findViewById(R.id.tv_full);
            showComplete = (ImageView)itemView.findViewById(R.id.iv_complete);
        }
    }
    class HeaderHoler extends RecyclerView.ViewHolder{
        public HeaderHoler(View itemView) {
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

    public void showFooter(FootHolder holder) {
        holder.dotsTextView.start();
        holder.loadingText.setText("加载中");
    }

    public void setLoadMore(boolean loadMore,int pos) {
        this.loadMore = loadMore;
        notifyItemChanged(pos);
    }

    public interface OnItemClikListener{
        void onItemClick(View v, int position);
        void onItemLongClick(View v, int position);
    }

    public void setOnClickListener(OnItemClikListener listener){
        this.mListener = listener;
    }
}
