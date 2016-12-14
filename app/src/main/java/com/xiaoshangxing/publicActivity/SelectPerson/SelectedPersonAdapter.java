package com.xiaoshangxing.publicActivity.SelectPerson;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.utils.customView.CirecleImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FengChaoQun
 * on 2016/7/25
 */
public class SelectedPersonAdapter extends RecyclerView.Adapter<SelectedPersonAdapter.MyViewHolder> {
    private Context context;
    private List<String> selectPerson = new ArrayList<String>();

    private SelectPersonActivity activity;
    private boolean isGroup;

    public SelectedPersonAdapter(Context context, List<String> list, SelectPersonActivity activity, boolean isGroup) {
        this.context = context;
        this.selectPerson = list;
        this.activity = activity;
        this.isGroup = isGroup;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycle_select_person, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.view = view;
        viewHolder.head = (CirecleImage) view.findViewById(R.id.head_image);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (!isGroup) {
            UserInfoCache.getInstance().getHeadIntoImage(selectPerson.get(position), holder.head);
        }
    }

    @Override
    public int getItemCount() {
        return selectPerson.size();
    }


    public int getSelectCount() {
        return selectPerson.size();
    }

    public List<String> getSelectPerson() {
        return selectPerson;
    }

    public void setSelectPerson(List<String> selectPerson) {
        this.selectPerson = selectPerson;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CirecleImage head;
        View view;

        public MyViewHolder(View view) {
            super(view);
        }
    }


}
