package com.xiaoshangxing.yujian.chatInfo.chooseNewGroupMaster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SortModel;
import com.xiaoshangxing.utils.layout.CirecleImage;

import java.util.List;

public class ChooseNewMasterAdapter extends BaseAdapter implements SectionIndexer {
    private List<SortBean> list = null;
    private Context mContext;

    public ChooseNewMasterAdapter(Context mContext, List<SortBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void updateListView(List<SortBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final SortBean mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_listview_selectnewmaster, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.name);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            viewHolder.divider = view.findViewById(R.id.divider);
            viewHolder.headImg = (CirecleImage) view.findViewById(R.id.head_image);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        int section = getSectionForPosition(position);

        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.divider.setVisibility(View.GONE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
            viewHolder.divider.setVisibility(View.VISIBLE);
        }

        viewHolder.tvTitle.setText(this.list.get(position).getName());
        viewHolder.headImg.setImageBitmap(this.list.get(position).getBitmap() );

        return view;

    }


    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
        CirecleImage headImg;
        View divider;
    }


    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }


    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}