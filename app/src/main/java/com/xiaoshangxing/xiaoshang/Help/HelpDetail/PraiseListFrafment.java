package com.xiaoshangxing.xiaoshang.Help.HelpDetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.data.bean.Published;
import com.xiaoshangxing.utils.customView.CirecleImage;
import com.xiaoshangxing.utils.customView.Name;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FengChaoQun
 * on 2016/7/20
 */
public class PraiseListFrafment extends Fragment {
    private RecyclerView recyclerView;
    private TextView emptyText;
    private Published published;
    private List<String> ids = new ArrayList<>();
    private int publish_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_comment_list, null);

        GetDataFromActivity activity = (GetDataFromActivity) getActivity();
        published = (Published) (activity.getData());
        if (published == null) {
            return view;
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        emptyText = (TextView) view.findViewById(R.id.empty_text);
        if (TextUtils.isEmpty(published.getPraiseUserIds())) {
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
            emptyText.setText("赶紧赞一下");
        } else {
            ids.clear();
            ids = published.getPraisePeopleOnlyIds();
            recyclerView.setAdapter(new HomeAdapter());
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(
                    getContext()).inflate(R.layout.item_praise_list_recycleview, parent,
                    false);
            MyViewHolder holder = new MyViewHolder(view);
            holder.view = view;
            holder.name = (Name) view.findViewById(R.id.name);
            holder.college = (TextView) view.findViewById(R.id.college);
            holder.headImage = (CirecleImage) view.findViewById(R.id.head_image);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            String userId = ids.get(position);
            UserInfoCache.getInstance().getHeadIntoImage(userId, holder.headImage);
            UserInfoCache.getInstance().getExIntoTextview(userId, NS.USER_NAME, holder.name);
            UserInfoCache.getInstance().getExIntoTextview(userId, NS.COLLEGE, holder.college);
            holder.headImage.setIntent_type(CirecleImage.PERSON_INFO, userId);
        }

        @Override
        public int getItemCount() {
            return ids == null ? 0 : ids.size();
//            return ids.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView college;
            Name name;
            CirecleImage headImage;
            View view;

            public MyViewHolder(View view) {
                super(view);
            }
        }
    }
}
