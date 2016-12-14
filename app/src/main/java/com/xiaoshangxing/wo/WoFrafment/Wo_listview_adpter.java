package com.xiaoshangxing.wo.WoFrafment;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.data.bean.Published;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.wo.WoFrafment.WoViewHolder.WoBaseHolder;
import com.xiaoshangxing.wo.WoFrafment.WoViewHolder.WoJustOneImage;
import com.xiaoshangxing.wo.WoFrafment.WoViewHolder.WoMoreImage;
import com.xiaoshangxing.wo.WoFrafment.WoViewHolder.WoNoImages;
import com.xiaoshangxing.wo.WoFrafment.WoViewHolder.WoTrasnsmit;
import com.xiaoshangxing.yujian.IM.kit.ListViewUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 */
public class Wo_listview_adpter extends ArrayAdapter<Published> {
    private final Map<Class<?>, Integer> viewTypes;
    List<Published> publisheds;
    WoFragment woFragment;
    Realm realm;
    private Context context;
    private BaseActivity activity;
    private ListView listView;

    public Wo_listview_adpter(Context context, int resource, List<Published> objects,
                              WoFragment woFragment, BaseActivity activity, Realm realm, ListView listView) {
        super(context, resource, objects);
        this.context = context;
        this.publisheds = objects;
        this.woFragment = woFragment;
        this.activity = activity;
        this.realm = realm;
        this.listView = listView;
        this.viewTypes = new HashMap<Class<?>, Integer>(getViewTypeCount());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        WoBaseHolder woBaseHolder;
        if (convertView == null) {
            convertView = viewAtPosition(position);
        }
        woBaseHolder = (WoBaseHolder) convertView.getTag();
        woBaseHolder.setPosition(position);
        woBaseHolder.refresh(publisheds.get(position));
        return convertView;
    }


    @Override
    public int getItemViewType(int position) {

        if (getViewTypeCount() == 1) {
            return 0;
        }

        Class<?> clazz = viewHolderAtPosition(position);
        if (viewTypes.containsKey(clazz)) {
            return viewTypes.get(clazz);
        } else {
            int type = viewTypes.size();
            if (type < getViewTypeCount()) {
                viewTypes.put(clazz, type);
                return type;
            }
            return 0;
        }

    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }


    public View viewAtPosition(int position) {
        WoBaseHolder holder = null;
        View view = null;
        try {
            Class<?> viewHolder = viewHolderAtPosition(position);
            holder = (WoBaseHolder) viewHolder.newInstance();
            holder.setActivity(activity);
            holder.setWoFragment(woFragment);
            holder.setRealm(realm);
//            holder.setAdpter(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        view = holder.getView(LayoutInflater.from(context));
        view.setTag(holder);
        holder.setContext(view.getContext());
        return view;
    }

    private Class<? extends WoBaseHolder> viewHolderAtPosition(int position) {
        if (publisheds.get(position).getIsTransimit() == 1) {
            return WoTrasnsmit.class;
        } else if (TextUtils.isEmpty(publisheds.get(position).getImage())) {
            return WoNoImages.class;
        } else {
            String[] images = publisheds.get(position).getImage().split(NS.SPLIT);
            if (images.length > 1) {
                return WoMoreImage.class;
            } else {
                return WoJustOneImage.class;
            }
        }
    }

    public void addData(List<Published> publisheds) {
        this.publisheds.addAll(publisheds);
        notifyDataSetChanged();
    }

    public void setData(List<Published> publisheds) {
        this.publisheds = publisheds;
        notifyDataSetChanged();
        Log.d("setData", "--" + publisheds.size());
    }

    public void removeOne(int position) {
        publisheds.remove(position);
        notifyDataSetChanged();
    }

    public void refreshItem(Published published) {
        for (int i = 0; i < publisheds.size(); i++) {
            if (publisheds.get(i).getId() == published.getId()) {
                Object tag = ListViewUtil.getViewHolderByIndex(listView, i);
                if (tag instanceof WoBaseHolder) {
                    WoBaseHolder viewHolder = (WoBaseHolder) tag;
                    viewHolder.refresh(published);
                    break;
                }
            }
        }
    }
}
