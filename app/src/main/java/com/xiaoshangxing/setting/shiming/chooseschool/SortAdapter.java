package com.xiaoshangxing.setting.shiming.chooseschool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SortModel;

import java.util.List;

/**
 * Created by tianyang on 2016/9/21.
 */
public class SortAdapter extends BaseAdapter implements SectionIndexer {
    private List<SortModel> list = null;
    private Context mContext;

    public SortAdapter(Context context, List<SortModel> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SortModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final SortModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_listview_sort, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.name);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            viewHolder.divider = view.findViewById(R.id.divider);
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
        return view;

    }


    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
        View divider;
    }

    public void updateListView(List<SortModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }
}
