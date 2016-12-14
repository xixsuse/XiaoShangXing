package com.xiaoshangxing.wo.setting.shiming.vertify;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xiaoshangxing.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/11/18
 */

public class CheckTextviewAdpter extends ArrayAdapter<String> {
    private List<String> strings;
    private int checkedPosition = -1;
    private Context context;
    private Callback callback;

    public CheckTextviewAdpter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.strings = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_text_checkbox, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.checkbox.setChecked(position == checkedPosition);
        viewHolder.text.setText(strings.get(position));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.callback(strings.get(position));
                }
            }
        });
        return convertView;
    }

    public int getCheckedPosition() {
        return checkedPosition;
    }

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return strings.get(position);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void callback(String string);
    }

    static class ViewHolder {
        @Bind(R.id.text)
        TextView text;
        @Bind(R.id.checkbox)
        CheckBox checkbox;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
