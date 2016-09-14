package com.xiaoshangxing.xiaoshang.ShoolfellowHelp.HelpDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.CommentsBean;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.input_activity.InputActivity;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.layout.Name;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import java.util.List;

/**
 * Created by FengChaoQun
 * on 2016/7/20
 */
public class CommentListFrafment extends Fragment {
    private RecyclerView recyclerView;
    private TextView emptyText;
    private boolean isCollect;//记录是否是collect界面
    private Published published;
    private List<CommentsBean> commentsBeen;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_comment_list,null);

        GetDataFromActivity activity = (GetDataFromActivity) getActivity();
        published = (Published) (activity.getData());
        if (published == null) {
            return view;
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        emptyText = (TextView) view.findViewById(R.id.empty_text);
        if (published.getComments().size() < 1) {
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
            emptyText.setText("赶紧评论一下");
        } else {
            commentsBeen = published.getComments();
            recyclerView.setAdapter(new HomeAdapter());
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
                    getContext()).inflate(R.layout.item_comment_list_recycleview, parent,
                    false);
            MyViewHolder holder = new MyViewHolder(view);
            holder.view=view;
            holder.name=(Name) view.findViewById(R.id.name);
            holder.college=(TextView)view.findViewById(R.id.college);
            holder.time=(TextView)view.findViewById(R.id.time);
            holder.text=(EmotinText) view.findViewById(R.id.text);
            holder.headImage=(CirecleImage)view.findViewById(R.id.head_image);


            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {

            final CommentsBean i = commentsBeen.get(position);
            int userId = i.getUserId();
            UserInfoCache.getInstance().getHead(holder.headImage, userId, getContext());
            UserInfoCache.getInstance().getName(holder.name, userId);
            UserInfoCache.getInstance().getCollege(holder.college, userId);
            holder.text.setText(i.getText());
            holder.time.setText(TimeUtil.getTimeShowString(i.getCreateTime(), false));

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isCollect) {
                        Intent comment_input = new Intent(getContext(), InputActivity.class);
                        comment_input.putExtra(InputActivity.EDIT_STATE, InputActivity.COMMENT);
                        comment_input.putExtra(InputActivity.COMMENT_OBJECT, holder.name.getText());
                        comment_input.putExtra(InputActivity.MOMENTID, published.getId());
                        comment_input.putExtra(InputActivity.COMMENTID, i.getId());
                        startActivity(comment_input);
                    } else {
                        Intent comment_input = new Intent(getContext(), InputActivity.class);
                        comment_input.putExtra(InputActivity.EDIT_STATE, InputActivity.COMMENT);
                        comment_input.putExtra(InputActivity.COMMENT_OBJECT, holder.name.getText());
                        comment_input.putExtra(InputActivity.MOMENTID, published.getId());
                        comment_input.putExtra(InputActivity.COMMENTID, i.getId());
                        startActivity(comment_input);
                    }
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return commentsBeen.size();
        }


        class MyViewHolder extends RecyclerView.ViewHolder
        {

            TextView college,time;
            EmotinText text;
            Name name;
            CirecleImage headImage;
            View view;

            public MyViewHolder(View view)
            {
                super(view);
            }
        }
    }
}
