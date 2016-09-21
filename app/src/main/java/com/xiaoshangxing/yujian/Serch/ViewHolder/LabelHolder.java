package com.xiaoshangxing.yujian.Serch.ViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.yujian.Serch.AbsContactViewHolder;
import com.xiaoshangxing.yujian.Serch.ContactDataAdapter;
import com.xiaoshangxing.yujian.Serch.LabelItem;


public class LabelHolder extends AbsContactViewHolder<LabelItem> {

    private TextView name;

    @Override
    public void refresh(ContactDataAdapter contactAdapter, int position, LabelItem item) {
        this.name.setText(item.getText());
    }

    @Override
    public View inflate(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.nim_contacts_abc_item, null);
        this.name = (TextView) view.findViewById(R.id.tv_nickname);
        return view;
    }

}
