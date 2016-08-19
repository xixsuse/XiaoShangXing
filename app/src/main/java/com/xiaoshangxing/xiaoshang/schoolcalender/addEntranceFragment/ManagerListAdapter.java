package com.xiaoshangxing.xiaoshang.schoolcalender.addEntranceFragment;

/**
 * Created by quchwe on 2016/7/22 0022.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.layout.CirecleImage;

import java.util.List;

/**
 * Created by quchwe on 2016/7/10 0010.
 */
public class ManagerListAdapter extends BaseAdapter {

    List<ManagerInfo> managerInfos;
    Context mContext;
    ViewHolder vh;

    public ManagerListAdapter(Context context,List<ManagerInfo> managerInfos){
        this.mContext = context;
        this.managerInfos = managerInfos;
    }

    @Override
    public int getCount() {
        return managerInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return managerInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ManagerInfo info = managerInfos.get(position);

        if (convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_calenr_add_entry_manager_list,parent,false);
            vh = new ViewHolder();
            vh.name = (TextView)convertView.findViewById(R.id.tv_manager_name);
            vh.cirecleImage = (CirecleImage)convertView.findViewById(R.id.lv_user_photo);
            vh.position = (TextView)convertView.findViewById(R.id.tv_manager_position);

            vh.cirecleImage.setImageBitmap(info.getBitmap());
            vh.position.setText("("+info.getPosion()+")");
            vh.name.setText(info.getName());
            convertView.setTag(vh);
        }else {
            convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder{
        TextView name;
        CirecleImage cirecleImage;
        TextView position;
    }
}

