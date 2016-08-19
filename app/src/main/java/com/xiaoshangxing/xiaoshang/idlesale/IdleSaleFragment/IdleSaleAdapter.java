package com.xiaoshangxing.xiaoshang.idlesale.IdleSaleFragment;


import android.content.Context;
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

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.loadingview.DotsTextView;
import com.xiaoshangxing.xiaoshang.RecyclerViewUtil;
import com.xiaoshangxing.xiaoshang.idlesale.IdleBean;
import com.xiaoshangxing.xiaoshang.idlesale.IdleImageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quchwe on 2016/8/4 0004.
 */
public class IdleSaleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<IdleBean> idels;
    private final IdleSaleContract.View mView;
    public boolean loadMore = false;
    private final Context mContext;
    private LayoutInflater inflater;
    private RecyclerViewUtil.OnItemClickListener mListener;
    public IdleSaleAdapter(@NonNull Context context,@NonNull IdleSaleContract.View view,List<IdleBean> idels){
        this.mContext = context;
        this.mView = view;
        this.idels = idels;
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public @RecyclerViewUtil.ItemType int getItemViewType(int position) {
        if (position==0){
            return RecyclerViewUtil.TYPE_HEADER;
        }else if (position>idels.size()) {
            return RecyclerViewUtil.TYPE_FOOTER;
        }else
            return RecyclerViewUtil.TYPE_NORMAL;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType== RecyclerViewUtil.TYPE_HEADER){
            HeaderHoler holer = new HeaderHoler(inflater.inflate(R.layout.headview_help_list,parent,false));
            return holer;
        }else if (viewType == RecyclerViewUtil.TYPE_FOOTER){
            FootHolder holder = new FootHolder(inflater.inflate(R.layout.footer,parent,false));
            return holder;
        }else if (viewType ==RecyclerViewUtil.TYPE_NORMAL){
            IdleViewHoler viewHoler = new IdleViewHoler(inflater.inflate(R.layout.rv_idle_sale,parent,false));
            return viewHoler;
        }
     return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder1, final int position) {
                if (position==0&&holder1 instanceof HeaderHoler){
                   HeaderHoler holer = (HeaderHoler)holder1;
                    holer.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mView.clickOnRule();
                        }
                    });
                }else if (position>idels.size()&&holder1 instanceof FootHolder){
                    FootHolder holder =(FootHolder) holder1;

                    if (loadMore){
                        showFooter(holder);
                    }else {
                        holder.itemView.setVisibility(View.GONE);
                    }
                }else if (position>0&&holder1 instanceof IdleViewHoler) {
                    IdleBean idle = idels.get(position-1);
                    final IdleViewHoler holder = (IdleViewHoler)holder1;
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
                    IdleImageAdapter adapter = new IdleImageAdapter(mContext, imageUrls,R.layout.rv_idle_image);
                    final LinearLayoutManager manager = new LinearLayoutManager(mContext);
                    manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    holder.rv_image.setLayoutManager(manager);
                    holder.rv_image.setAdapter(adapter);
                    holder.rv_image.setItemAnimator(new DefaultItemAnimator());
                    holder.tv_price.setText("¥" + idle.getPrice());
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
                            if (s.toString().length() > 0) {
                                holder.shiLiao.setClickable(true);
                                holder.shiLiao.setBackground(mContext.getResources().getDrawable(R.drawable.btn_xianzi_shiliao_green));
                            } else {
                                holder.shiLiao.setClickable(false);
                                holder.shiLiao.setBackground(mContext.getResources().getDrawable(R.drawable.btn_xianzi_shiliao));
                            }
                        }
                    });
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mListener != null) {
                                mListener.onItemClickListener(v, position);
                            }
                        }
                    });
                    if (idle.isComplete()) {
                        holder.showComplete.setVisibility(View.VISIBLE);
                    } else {
                        holder.showComplete.setVisibility(View.GONE);
                    }
                }
    }

    public void setOnItemClickListener(RecyclerViewUtil.OnItemClickListener listener){
        this.mListener = listener;
    }
    @Override
    public int getItemCount() {
        return idels.size()+2;
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
            editText = (EditText) itemView.findViewById(R.id.edit);
        
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
        holder.itemView.setVisibility(View.VISIBLE);
        holder.dotsTextView.showAndPlay();
        holder.loadingText.setText("加载中");
    }

    public void setLoadMore(boolean loadMore,int pos) {
        this.loadMore = loadMore;
        notifyItemChanged(pos);
    }

}
