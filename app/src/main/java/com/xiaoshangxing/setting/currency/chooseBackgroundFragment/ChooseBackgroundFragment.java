package com.xiaoshangxing.setting.currency.chooseBackgroundFragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 15828 on 2016/7/15.
 */
public class ChooseBackgroundFragment extends BaseFragment implements View.OnClickListener {
    private ArrayList<Bitmap> data = new ArrayList<Bitmap>();
    private View mView;
    private TextView back;
    private GridView gridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_choosebackgd, container, false);
        back = (TextView) mView.findViewById(R.id.choosebackground_back);
        gridView = (GridView) mView.findViewById(R.id.background_gridview);

        for (int i = 0; i < 10; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.greyblock);
            data.add(bitmap);
        }

        Adapter baseAdapter = new Adapter(data);
        gridView.setAdapter(baseAdapter);
        back.setOnClickListener(this);
        return mView;
    }

    public class ViewHolder {
        public ImageView image;
        public RelativeLayout complete;
        public TextView textView;
    }

    @Override
    public void onClick(View v) {
        getActivity().getSupportFragmentManager().popBackStack();
    }


    class Adapter extends BaseAdapter {
        List<Bitmap> data;
        public Adapter(List<Bitmap> data) {
            super();
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Bitmap getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d("qqq", "  " + position);
            final ViewHolder holder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.item_background_gridview, parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.background_grida_image);
                holder.complete = (RelativeLayout) convertView.findViewById(R.id.background_grida_complete);
                holder.textView = (TextView) convertView.findViewById(R.id.background_grida_text);
//                    holder.image.setFocusable(false);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Bitmap b = data.get(position);
            holder.image.setImageBitmap(b);
            holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("qqq","0nclick...");
                        if (holder.complete.getVisibility() == View.GONE) {
                            holder.complete.setVisibility(View.VISIBLE);
                            holder.textView.setVisibility(View.GONE);
                        }
                    }
            });
            return convertView;
        }
    }


}
