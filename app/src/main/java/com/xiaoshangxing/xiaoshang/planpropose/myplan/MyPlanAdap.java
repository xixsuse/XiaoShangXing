package com.xiaoshangxing.xiaoshang.planpropose.myplan;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.xiaoshang.planpropose.PlanList;
import com.xiaoshangxing.xiaoshang.planpropose.PlanProposeActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by quchwe on 2016/8/3 0003.
 */
public class MyPlanAdap extends RecyclerView.Adapter<MyPlanAdap.MyPlanViewHolder> {


    private final Context mContext;
    private final MyPlanContract.View mView;
   private List<PlanList> mPlanLists;
    private boolean isSelected = false;

    public MyPlanAdap(@NonNull Context context, @NonNull MyPlanContract.View view, List<PlanList> planLists){
        this.mContext = context;
        this.mView = view;
        this.mPlanLists = planLists;

    }
    @Override
    public MyPlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyPlanViewHolder viewHolder = new MyPlanViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_plan_propose,
                parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyPlanViewHolder holder, final int position) {
        final PlanList myPlan = mPlanLists.get(position);

        holder.headImage.setImageResource(R.mipmap.cirecleimage_default);
        holder.myName.setText(myPlan.getName());
        holder.academy.setText(myPlan.getAcademy());
        holder.planName.setText("#"+myPlan.getPlanName());
        holder.planInfo.setText(myPlan.getText());
        holder.limitPeopleNumber.setText("0-"+myPlan.getLimitPepoleNumber());
        holder.participatePeopleNumber.setText(myPlan.getPaticipateNumber()+"");
        holder.time.setText(myPlan.getTime());
        holder.completeOrCancel.setVisibility(View.VISIBLE);
        holder.showCompleteImage.setVisibility(View.GONE);
        if (myPlan.isCompleted()){

            holder.completeOrCancel.setBackground(mContext.getResources().getDrawable(R.mipmap.cirecleimage_default));
        }else {

            holder.completeOrCancel.setBackground(mContext.getResources().getDrawable(R.mipmap.cancle));
        }

        if (myPlan.getLimitPepoleNumber()!=null&&myPlan.getLimitPepoleNumber()==myPlan.getPaticipateNumber()) {
            holder.participatePeopleNumber.setTextColor(mContext.getResources().getColor(R.color.g0));

        }else holder.participatePeopleNumber.setTextColor(mContext.getResources().getColor(R.color.green1));



        if (isSelected){
            holder.completeOrCancel.setVisibility(View.GONE);
            holder.selectCheckBox.setVisibility(View.VISIBLE);
            holder.selectCheckBox.setChecked(true);
        }else {
            holder.completeOrCancel.setVisibility(View.VISIBLE);
            holder.selectCheckBox.setVisibility(View.GONE);
            holder.selectCheckBox.setChecked(false);
        }
        if (myPlan.isCompleted()) {
            holder.completeOrCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myPlan.setCompleted(false);
                    notifyItemChanged(position);
                }
            });
        }else if (!myPlan.isCompleted()){
            holder.completeOrCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myPlan.setCompleted(true);
                    notifyItemChanged(position);
                }
            });
        }
        holder.planFrame.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                int pos = holder.getLayoutPosition();
//                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
//                    removeData(pos);
                holder.nameInfo.setBackgroundColor(mContext.getResources().getColor(R.color.g1));
                holder.userInfo.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.plan_list_background_grey));
                showMenu(v,holder);

                return true;
            }
        });
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });


        }


    }


    @Override
    public int getItemCount() {
        return mPlanLists.size();
    }

    class MyPlanViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.ll_name)
        LinearLayout nameInfo;
        @Bind(R.id.userInfo)
        LinearLayout userInfo;
        @Bind(R.id.fl_plan_list)
        FrameLayout planFrame;
       @Bind(R.id.iv_headImage)
        ImageView headImage;
        @Bind(R.id.tv_name)
        TextView myName;
        @Bind(R.id.tv_plan_name)
        TextView planName;
        @Bind(R.id.tv_plan_info)
        TextView planInfo;
        @Bind(R.id.tv_plan_list_academy)
        TextView academy;
        @Bind(R.id.tv_list_plan_time)
        TextView time;
        @Bind(R.id.btn_complete)
        ImageButton completeOrCancel;
        @Bind(R.id.checkbox)
        CheckBox selectCheckBox;
        @Bind(R.id.iv_complete)
        ImageView showCompleteImage;
        @Bind(R.id.tv_participate_number)
        TextView participatePeopleNumber;
        @Bind(R.id.tv_list_plan_renshu)
        TextView limitPeopleNumber;
        @Bind(R.id.ib_reward_downArrow)

        ImageButton showDialog;
        public MyPlanViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public void showSelectCircle(boolean selected){
        isSelected = selected;
        notifyDataSetChanged();
    }
    public void showMenu(View v, final MyPlanViewHolder viewHolder){
        final View view=v;
        int []xy=new int[2];
        v.getLocationInWindow(xy);
        Log.d("y",""+xy[1]);
        View menu;
        if (xy[1]<=viewHolder.planFrame.getHeight()){
            menu= View.inflate(mContext, R.layout.popup_myhelp_up,null);
        }else {
            menu= View.inflate(mContext, R.layout.popup_myhelp_bottom,null);
        }


        final PopupWindow popupWindow=new PopupWindow(menu, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(mContext.getResources().
                getDrawable(R.drawable.nothing));
        popupWindow.setAnimationStyle(R.style.popwindow_anim);

        menu.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int mShowMorePopupWindowWidth = menu.getMeasuredWidth();
        int mShowMorePopupWindowHeight=menu.getMeasuredHeight();
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);


        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                viewHolder.userInfo.setBackgroundDrawable(mContext.getResources()
                        .getDrawable(R.drawable.plan_list_background));
                viewHolder.nameInfo.setBackgroundColor(mContext.getResources().getColor(R.color.w0));

            }
        });

        if (xy[1]<=mContext.getResources().getDimensionPixelSize(R.dimen.y310)){
            popupWindow.showAsDropDown(v,
                    -mShowMorePopupWindowWidth/2+v.getWidth()/2,
                    0);
        }else {
            popupWindow.showAsDropDown(v,
                    -mShowMorePopupWindowWidth/2+v.getWidth()/2,
                    -mShowMorePopupWindowHeight-v.getHeight());
        }



        final TextView transmit=(TextView)menu.findViewById(R.id.transmit);
        TextView delete=(TextView)menu.findViewById(R.id.delete);
        TextView more=(TextView)menu.findViewById(R.id.more);

        transmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, SelectPersonActivity.class);
               // intent.putExtra(SelectPersonActivity.TRANSMIT_TYPE, SelectPersonActivity.SCHOOL_HELP_TRANSMIT);
                PlanProposeActivity p =  (PlanProposeActivity)mContext;
                p.startActivityForResult(intent, PlanProposeActivity.SELECT_PERSON);
                popupWindow.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                mView.showCancelPlan();

            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.showDeleteDialog(true);
                showSelectCircle(true);
                popupWindow.dismiss();
            }
        });

    }


    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
