package com.xiaoshangxing.xiaoshang.Help.HelpDetail;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.CommentsBean;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
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
    private GetDataFromActivity activity;
    private Handler handler = new Handler();
    private boolean isTuch = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_comment_list,null);

        activity = (GetDataFromActivity) getActivity();
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
            commentsBeen = published.getComments()/*.sort(NS.CREATETIME, Sort.DESCENDING)*/;
            recyclerView.setAdapter(new HomeAdapter());
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isTuch) {
                    activity.hideInputBox();
                }
            }
        });
        emptyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.hideInputBox();
            }
        });

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
            String id = String.valueOf(i.getUserId());
            UserInfoCache.getInstance().getHeadIntoImage(id, holder.headImage);
            UserInfoCache.getInstance().getExIntoTextview(id, NS.USER_NAME, holder.name);
            UserInfoCache.getInstance().getExIntoTextview(id, NS.COLLEGE, holder.college);

            holder.text.setText(i.getText());
            holder.time.setText(TimeUtil.getTimeShowString(i.getCreateTime(), false));
            holder.headImage.setIntent_type(CirecleImage.PERSON_INFO, String.valueOf(userId));

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    activity.comment(i.getId());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            final int[] xy = new int[2];
                            v.getLocationOnScreen(xy);
                            final View mv = v;
                            int editextLocation = activity.getInputBox().getEdittext_height();
                            int destination = xy[1] + mv.getHeight() - editextLocation;
                            if (destination < 0) {
                                return;
                            }

                            isTuch = false;
                            recyclerView.smoothScrollBy(destination, Math.abs(destination));
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    isTuch = true;
                                }
                            }, Math.abs(destination) + 100);
                        }
                    }, 300);
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
