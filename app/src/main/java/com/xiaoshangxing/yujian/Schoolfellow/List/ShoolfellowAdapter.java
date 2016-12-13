package com.xiaoshangxing.yujian.Schoolfellow.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.image.MyGlide;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.yujian.Schoolfellow.ItemBean.BaseItemBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/10/22
 */

public class ShoolfellowAdapter extends BaseAdapter {
    private Context context;

    //所有的数据
    private List<BaseItemBean> allItems;

    //要展示的数据
    private List<BaseItemBean> showItems;

    private onItemClick onItemClick;

    public ShoolfellowAdapter.onItemClick getOnItemClick() {
        return onItemClick;
    }

    public void setOnItemClick(ShoolfellowAdapter.onItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public ShoolfellowAdapter(Context context, List<BaseItemBean> allItems, ListView listView) {
        this.context = context;
        this.allItems = ItemHelp.sort(allItems);
        this.showItems = ItemHelp.filterVisibleItem(allItems);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                }
                if (onItemClick != null) {
                    onItemClick.click(position - 1, showItems.get(position - 1));
                }
            }
        });
    }

    @Override
    public int getCount() {
        return showItems.size();
    }

    @Override
    public Object getItem(int position) {
        return showItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_schoolfellow_list, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        initView(position, viewHolder);

        return convertView;
    }

    private void initView(int position, ViewHolder viewHolder) {
        BaseItemBean baseItemBean = showItems.get(position);
        LinearLayout.LayoutParams params;
        switch (baseItemBean.getType()) {
            case BaseItemBean.COLLEGE:
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ScreenUtils.getAdapterPx(R.dimen.y150, context));
                viewHolder.topLay.setLayoutParams(params);
                MyGlide.with_default_college(context, baseItemBean.getImage(), viewHolder.headImage);
                viewHolder.name.setText(baseItemBean.getShowName());
                viewHolder.name.setPadding(ScreenUtils.getAdapterPx(R.dimen.x24, context), 0, 0, 0);
                viewHolder.ex.setText(TextUtils.isEmpty(baseItemBean.getExText()) ? "" : baseItemBean.getExText());
                viewHolder.ex.setPadding(ScreenUtils.getAdapterPx(R.dimen.x24, context), 0, 0, 0);
                viewHolder.headImage.setVisibility(View.VISIBLE);
                viewHolder.ex.setVisibility(View.VISIBLE);
                break;
            case BaseItemBean.PROFESSION:
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ScreenUtils.getAdapterPx(R.dimen.y144, context));
                viewHolder.topLay.setLayoutParams(params);
                viewHolder.name.setText(baseItemBean.getShowName());
                viewHolder.name.setPadding(0, 0, 0, 0);
                viewHolder.headImage.setVisibility(View.GONE);
                viewHolder.ex.setVisibility(View.GONE);
                break;
            case BaseItemBean.GRADE:
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ScreenUtils.getAdapterPx(R.dimen.y120, context));
                viewHolder.topLay.setLayoutParams(params);
                viewHolder.name.setText(baseItemBean.getShowName());
                viewHolder.name.setPadding(0, 0, 0, 0);
                viewHolder.headImage.setVisibility(View.GONE);
                viewHolder.ex.setVisibility(View.GONE);
                break;
            case BaseItemBean.PERSON:
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ScreenUtils.getAdapterPx(R.dimen.y126, context));
                viewHolder.topLay.setLayoutParams(params);
                viewHolder.name.setText(baseItemBean.getShowName());
                viewHolder.name.setPadding(ScreenUtils.getAdapterPx(R.dimen.x12, context), 0, 0, 0);
                viewHolder.headImage.setVisibility(View.VISIBLE);
                MyGlide.with_default_head(context, baseItemBean.getImage(), viewHolder.headImage);
                viewHolder.ex.setVisibility(View.VISIBLE);
                viewHolder.ex.setPadding(ScreenUtils.getAdapterPx(R.dimen.x12, context), 0, 0, 0);
                viewHolder.ex.setText(TextUtils.isEmpty(baseItemBean.getExText()) ? "" : baseItemBean.getExText());
                break;
        }

        viewHolder.topLay.setBackgroundColor(baseItemBean.isExpand() ? context.getResources().getColor(R.color.w3)
                : context.getResources().getColor(R.color.w0));
        if (position != showItems.size() - 1 && (showItems.get(position + 1).getType() != baseItemBean.getType())) {
            viewHolder.divider.setVisibility(View.VISIBLE);
            viewHolder.divider2.setVisibility(View.GONE);
        } else {
            viewHolder.divider2.setVisibility(View.VISIBLE);
            viewHolder.divider.setVisibility(View.GONE);
        }
    }

    public void addData(int position, BaseItemBean baseItemBean) {
        BaseItemBean temp = showItems.get(position);
        temp.setExpand(true);
        int index = allItems.indexOf(temp);
        List<BaseItemBean> chidren = baseItemBean.getChildren();
        for (int i = 0; i < chidren.size(); i++) {
            allItems.add(index + i + 1, chidren.get(i));
        }
        showItems = ItemHelp.filterVisibleItem(allItems);
        notifyDataSetChanged();
    }

    public void removeChildrens(int position) {
        BaseItemBean temp = showItems.get(position);
        for (int i = 0; i < temp.getChildren().size(); i++) {
            allItems.remove(temp.getChildren().get(i));
        }
        showItems = ItemHelp.filterVisibleItem(allItems);
        notifyDataSetChanged();
    }

    public void collasp(int positon) {
        BaseItemBean baseItemBean = showItems.get(positon);
//        baseItemBean.setExpand(false);
        ItemHelp.Collasp(baseItemBean);
        showItems = ItemHelp.filterVisibleItem(allItems);
        notifyDataSetChanged();
    }

    public interface onItemClick {
        void click(int position, BaseItemBean itemBean);
    }


    static class ViewHolder {
        @Bind(R.id.head_image)
        CirecleImage headImage;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.ex)
        TextView ex;
        @Bind(R.id.top_lay)
        LinearLayout topLay;
        @Bind(R.id.divider)
        ImageView divider;
        @Bind(R.id.divider2)
        ImageView divider2;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
