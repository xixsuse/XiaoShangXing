package com.xiaoshangxing.wo.setting.currency.chooseBackgroundFragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.LocalDataUtils;
import com.xiaoshangxing.wo.setting.currency.chatBackground.ChatBackgroundActivity;
import com.xiaoshangxing.utils.baseClass.BaseFragment;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;

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
    private View mView;
    private GridView gridView;
    private ArrayList<RelativeLayout> completeViews = new ArrayList<RelativeLayout>();
    private List<String> data = new ArrayList<>();
    private String account;
    private ChatBackgroundActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_choosebackgd, container, false);
        ButterKnife.bind(this, mView);
        gridView = (GridView) mView.findViewById(R.id.background_gridview);
        title.setText("选择背景图");
        more.setVisibility(View.GONE);
        titleLay.setBackgroundColor(getResources().getColor(R.color.choose_background));
        titleBottomLine.setBackgroundColor(getResources().getColor(R.color.choose_background));

        activity = (ChatBackgroundActivity) getActivity();
        account = activity.getAccount();


        Adapter baseAdapter = new Adapter(data);
        gridView.setAdapter(baseAdapter);
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
        List<String> data;

        public Adapter(List<String> data) {
            super();
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size() + 1;
        }

        @Override
        public String getItem(int position) {
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

            if (position == 0) {
                Bitmap bitmap = Bitmap.createBitmap(ScreenUtils.getAdapterPx(R.dimen.x336, getContext()),
                        ScreenUtils.getAdapterPx(R.dimen.x336, getContext()),
                        Bitmap.Config.ARGB_8888);
                bitmap.eraseColor(getResources().getColor(R.color.w3));//填充颜色
//                holder.image.setBackgroundColor(getResources().getColor(R.color.w3));
                holder.image.setImageBitmap(bitmap);
                holder.textView.setText("默认");
                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LocalDataUtils.saveBackgroud(account, null, false, getContext());
                        ChatBackgroundActivity.image = null;
                        holder.textView.setVisibility(View.GONE);
                        holder.complete.setVisibility(View.VISIBLE);
                    }
                });
            }
            return convertView;
        }
    }
}
