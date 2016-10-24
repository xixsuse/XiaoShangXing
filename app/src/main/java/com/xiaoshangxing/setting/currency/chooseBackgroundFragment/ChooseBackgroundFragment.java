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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/7/15.
 */
public class ChooseBackgroundFragment extends BaseFragment {
    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.background_gridview)
    GridView backgroundGridview;
    private ArrayList<Bitmap> data = new ArrayList<Bitmap>();
    private View mView;
    private GridView gridView;
    private ArrayList<RelativeLayout> completeViews = new ArrayList<RelativeLayout>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_choosebackgd, container, false);
        ButterKnife.bind(this, mView);
        gridView = (GridView) mView.findViewById(R.id.background_gridview);
        title.setText("选择背景图");
        more.setVisibility(View.GONE);

        for (int i = 0; i < 10; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.greyblock);
            data.add(bitmap);
        }

        Adapter baseAdapter = new Adapter(data);
        gridView.setAdapter(baseAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < parent.getCount(); i++) {
                    completeViews.get(i).setVisibility(View.GONE);
                }
                completeViews.get(position).setVisibility(View.VISIBLE);
            }
        });
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.back)
    public void onClick() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    public class ViewHolder {
        public ImageView image;
        public RelativeLayout complete;
        public TextView textView;
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
            final ViewHolder holder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.item_background_gridview, parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.background_grida_image);
                holder.complete = (RelativeLayout) convertView.findViewById(R.id.background_grida_complete);
                holder.textView = (TextView) convertView.findViewById(R.id.background_grida_text);
                completeViews.add(holder.complete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Bitmap b = data.get(position);
            holder.image.setImageBitmap(b);
            return convertView;
        }
    }
}
