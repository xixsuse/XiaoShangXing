package com.xiaoshangxing.xiaoshang.ShoolfellowHelp.HelpDetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.xiaoshang.ShoolReward.RewardDetail.RewardDetailActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by FengChaoQun
 * on 2016/7/20
 */
public class CommentListFrafment extends Fragment {
    private RecyclerView recyclerView;
    private TextView emptyText;
    private boolean isCollect;//记录是否是collect界面
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_comment_list,null);
        recyclerView=(RecyclerView) view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new HomeAdapter());
        emptyText=(TextView)view.findViewById(R.id.empty_text);
        Random random=new Random();
        if (random.nextInt(4)==1){
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
            emptyText.setText("赶紧评论一下");
        }



        return view;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view=LayoutInflater.from(
                    getContext()).inflate(R.layout.item_praise_list_recycleview, parent,
                    false);
            MyViewHolder holder = new MyViewHolder(view);
            holder.view=view;
            holder.name=(TextView)view.findViewById(R.id.name);
            holder.college=(TextView)view.findViewById(R.id.college);
            holder.time=(TextView)view.findViewById(R.id.time);
            holder.text=(TextView)view.findViewById(R.id.text);
            holder.headImage=(CirecleImage)view.findViewById(R.id.head_image);


            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isCollect){
                        RewardDetailActivity activity=(RewardDetailActivity) getActivity();
                        activity.showOrHideInputBox();
                    }else {
                        HelpDetailActivity activity=(HelpDetailActivity) getActivity();
                        activity.showOrHideInputBox();
                    }


                }
            });
            emptyText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HelpDetailActivity activity=(HelpDetailActivity) getActivity();
                    activity.showInputBox(false);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {

        }

        @Override
        public int getItemCount()
        {
            return 100;
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {

            TextView name,college,time,text;
            CirecleImage headImage;
            View view;

            public MyViewHolder(View view)
            {
                super(view);
            }
        }
    }
}
