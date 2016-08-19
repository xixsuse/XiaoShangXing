package com.xiaoshangxing.xiaoshang.idlesale.favoriteIdleFragment;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.wo.WoFrafment.check_photo.ImagePagerActivity;
import com.xiaoshangxing.xiaoshang.idlesale.IdleBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quchwe on 2016/8/4 0004.
 */
public class FavoriteIdleSaleAdapter extends RecyclerView.Adapter<FavoriteIdleSaleAdapter.IdleViewHoler> {

    List<IdleBean> idels;
    private final FavoriteIdleContract.View mView;

    private final Context mContext;

    private OnItemClickListener mListener;
    public FavoriteIdleSaleAdapter(@NonNull Context context, @NonNull FavoriteIdleContract.View view, List<IdleBean> idels){
        this.mContext = context;
        this.mView = view;
        this.idels = idels;
    }

    @Override
    public IdleViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        IdleViewHoler viewHoler = new IdleViewHoler(LayoutInflater.from(mContext).inflate(R.layout.rv_idle_sale,parent,false));
        return viewHoler;
    }

    @Override
    public void onBindViewHolder(final IdleViewHoler holder, final int position) {

        IdleBean idle = idels.get(position);
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
        final LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.rv_image.setLayoutManager(manager);
        holder.rv_image.setAdapter(adapter);
        holder.rv_image.setItemAnimator(new DefaultItemAnimator());
        holder.tv_price.setText("¥"+idle.getPrice());
        holder.tv_dormitory.setText(idle.getDepartment());
        holder.tv_xzcs_info.setText(idle.getText());
        holder.tv_release_time.setText(idle.getTime());
        holder.down_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.showFavoriteDialog();
            }
        });
        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>0){
                    holder.shiLiao.setClickable(true);
                    holder.shiLiao.setBackground(mContext.getResources().getDrawable(R.drawable.btn_xianzi_shiliao_green));
                    notifyItemChanged(position);
                }else {
                    holder.shiLiao.setClickable(false);
                    holder.shiLiao.setBackground(mContext.getResources().getDrawable(R.drawable.btn_xianzi_shiliao));

                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null){
                    mListener.onItemClickListener(v,position);
                }
            }
        });
        if (idle.isComplete()){
            holder.showComplete.setVisibility(View.VISIBLE);
        }else {
            holder.showComplete.setVisibility(View.GONE);
        }

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
        public ImageView down_arrow;
        public Button shiLiao;
        public ImageView showComplete;
        public EditText editText;
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
            down_arrow = (ImageView) itemView.findViewById(R.id.down_arrow);
            shiLiao = (Button)itemView.findViewById(R.id.btn_idle_shiliao);
            showComplete = (ImageView)itemView.findViewById(R.id.iv_xianzhiComplete);
            editText = (EditText)itemView.findViewById(R.id.edit);
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
}
