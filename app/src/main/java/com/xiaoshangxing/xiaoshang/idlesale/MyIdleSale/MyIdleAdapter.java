package com.xiaoshangxing.xiaoshang.idlesale.MyIdleSale;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.wo.WoFrafment.check_photo.ImagePagerActivity;
import com.xiaoshangxing.xiaoshang.idlesale.IdleBean;
import com.xiaoshangxing.xiaoshang.idlesale.IdleSaleActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quchwe on 2016/8/7 0007.
 */

public class MyIdleAdapter extends RecyclerView.Adapter<MyIdleAdapter.IdleViewHoler> {

    List<IdleBean> idels;
    private final MyIdleContract.View mView;

    private final Context mContext;
    boolean isSelected = false;

    private OnItemClickListener mListener;
    public MyIdleAdapter(@NonNull Context context, @NonNull MyIdleContract.View view, List<IdleBean> idels){
        this.mContext = context;
        this.mView = view;
        this.idels = idels;
    }

    @Override
    public IdleViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
       IdleViewHoler viewHoler = new IdleViewHoler(LayoutInflater.from(mContext).inflate(R.layout.rv_my_idle_sale,parent,false));
        return viewHoler;
    }

    @Override
    public void onBindViewHolder(final IdleViewHoler holder, final int position) {

        final IdleBean idle = idels.get(position);
        holder.iv_headImage.setImageResource(R.mipmap.cirecleimage_default);
        holder.tv_xzcs_name.setText(idle.getName());
        holder.tv_academy_name.setText(idle.getAcademy());
        String[] urls2 = {"http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg",
                "http://img.my.csdn.net/uploads/201410/19/1413698867_8323.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383290_1042.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383275_3977.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383264_3954.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383264_4787.jpg"
        };
        final ArrayList<String> imageUrls = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            imageUrls.add(urls2[i]);
        }
        IdleImageAdapter adapter = new IdleImageAdapter(mContext,imageUrls);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.rv_image.setLayoutManager(manager);
        holder.rv_image.setAdapter(adapter);
        holder.rv_image.setItemAnimator(new DefaultItemAnimator());
        holder.tv_price.setText("¥"+idle.getPrice());
        holder.tv_dormitory.setText(idle.getDepartment());
        holder.tv_xzcs_info.setText(idle.getText());
        holder.tv_release_time.setText(idle.getTime());
        if (isSelected){
            holder.complete.setVisibility(View.GONE);
            holder.selcetedCheck.setVisibility(View.VISIBLE);
            holder.selcetedCheck.setChecked(true);
        }else {
            holder.complete.setVisibility(View.VISIBLE);
            holder.selcetedCheck.setVisibility(View.GONE);
            holder.selcetedCheck.setChecked(false);
        }
        if (idle.isComplete()){
            holder.complete.setImageResource(R.mipmap.quxiao2);
        }else {
            holder.complete.setImageResource(R.mipmap.yimai);
        }
        holder.complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idle.isComplete()){
                    holder.complete.setImageResource(R.mipmap.yimai);
                    idle.setComplete(false);

                }else {
                    holder.complete.setImageResource(R.mipmap.quxiao2);
                    idle.setComplete(false);
                }
                notifyItemChanged(position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showMenu(v,holder,position);
                holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.g1));
                return true;
            }
        });

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
    @Override
    public int getItemCount() {
        return idels.size();
    }

    class IdleViewHoler extends RecyclerView.ViewHolder{
        public CirecleImage iv_headImage;
        public TextView tv_xzcs_info;
        public TextView tv_xzcs_name;
        public TextView tv_academy_name;
        public TextView tv_release_time;
        public TextView tv_dormitory;
        public TextView tv_price;
        public RecyclerView rv_image;
        public  ImageView complete;
        public CheckBox selcetedCheck;

        public IdleViewHoler(View itemView) {
            super(itemView);
            iv_headImage = (CirecleImage) itemView.findViewById(R.id.iv_headImage);

            tv_xzcs_info = (TextView) itemView.findViewById(R.id.tv_xzcs_info);

            tv_xzcs_name = (TextView) itemView.findViewById(R.id.tv_xzcs_name);

            tv_academy_name = (TextView) itemView.findViewById(R.id.tv_academy_name);
            tv_release_time = (TextView) itemView.findViewById(R.id.tv_release_time);
            tv_dormitory = (TextView) itemView.findViewById(R.id.tv_dormitory);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            rv_image = (RecyclerView) itemView.findViewById(R.id.rv_image);
             complete = (ImageView)itemView.findViewById(R.id.iv_idle_shiliao);
            selcetedCheck = (CheckBox)itemView.findViewById(R.id.checkbox);


        }
    }


    public class IdleImageAdapter extends RecyclerView.Adapter<IdleImageAdapter.ImageViewHolder>{

        final ArrayList<String> bitmaps;
        final Context mContext;

        public IdleImageAdapter(Context context,ArrayList<String> bitmaps){
            this.mContext = context;
            this.bitmaps = bitmaps;
        }


        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           ImageViewHolder viewHolder = new ImageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_idle_image,parent,false));
            return viewHolder;
        }



        @Override
        public void onBindViewHolder(ImageViewHolder holder, final int position) {


//            if (bitmaps.size()==1){
//                //holder.image.setImageResource(R.mipmap.sunluyang);
//                ViewGroup.LayoutParams params = holder.image.getLayoutParams();
//                params.height = R.dimen.y276;
//                params.width = R.dimen.x278;
//                holder.image.setLayoutParams(params);
//            }
//            else {
//                ViewGroup.LayoutParams params = holder.image.getLayoutParams();
//                params.height =276;
//                params.width = 276;
//                holder.image.setLayoutParams(params);
//            }
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ImagePagerActivity.class);
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, bitmaps);
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                    mContext.startActivity(intent);
                }
            });
            Glide.with(mContext).
                    load(bitmaps.get(position))
                    .animate(R.anim.fade_in)
                    .into(holder.image);
        }



        @Override
        public int getItemCount() {
            return bitmaps.size();
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder{

            public ImageView image;
            public ImageViewHolder(View itemView) {
                super(itemView);
                image = (ImageView)itemView.findViewById(R.id.iv_image_idle);
            }
        }

    }
    public interface OnItemClickListener{
        void onItemClickListener(View v, int position);
        void onItemLongClickListener(View v, int position);
    }

    public void showSelectCircle(boolean selected, int pos){
        isSelected = selected;
        notifyItemChanged(pos);
    }
    public void showMenu(View v, final IdleViewHoler viewHolder, final int pos){
        final View view=v;
        int []xy=new int[2];
        v.getLocationInWindow(xy);
        Log.d("y",""+xy[1]);
        View menu;
        if (xy[1]<=viewHolder.itemView.getHeight()){
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
                viewHolder.itemView.setBackgroundColor(mContext.getResources()
                        .getColor(R.color.w0));

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
                IdleSaleActivity p =  (IdleSaleActivity) mContext;
                p.startActivityForResult(intent, IdleSaleActivity.SELECT_PERSON);
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
                showSelectCircle(true,pos);
                popupWindow.dismiss();
            }
        });

    }
}
