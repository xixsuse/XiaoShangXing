package com.xiaoshangxing.publicActivity.Location;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 */
public class Location_adpter extends ArrayAdapter<AddressBean> {
    List<AddressBean> beans;
    private Context context;
    private int resource;
    private int selected = -1;
    private String selectedLocation;


    public Location_adpter(Context context, int resource, List<AddressBean> objects) {
        super(context, resource, objects);
        this.context = context;
        this.beans = objects;
        this.resource = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_location, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.location.setText(beans.get(position).getName());
        viewHolder.detail.setText(beans.get(position).getAddress());

        if (position == selected) {
            viewHolder.gou.setVisibility(View.VISIBLE);
        } else {
            viewHolder.gou.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public void setSelected(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }

    public void setSelectedLocation(String selectedLocation) {
        this.selectedLocation = selectedLocation;
        for (int i = 0; i < beans.size(); i++) {
            if (beans.get(i).getName().equals(selectedLocation)) {
                selected = i;
            }
        }
    }

    static class ViewHolder {
        @Bind(R.id.location)
        TextView location;
        @Bind(R.id.detail)
        TextView detail;
        @Bind(R.id.location_lay)
        LinearLayout locationLay;
        @Bind(R.id.gou)
        ImageView gou;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
