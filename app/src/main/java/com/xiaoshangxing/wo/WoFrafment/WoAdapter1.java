package com.xiaoshangxing.wo.WoFrafment;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoshangxing.Network.NS;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.wo.WoFrafment.WoViewHolder.WoBaseHolder;
import com.xiaoshangxing.wo.WoFrafment.WoViewHolder.WoJustOneImage;
import com.xiaoshangxing.wo.WoFrafment.WoViewHolder.WoMoreImage;
import com.xiaoshangxing.wo.WoFrafment.WoViewHolder.WoNoImages;
import com.xiaoshangxing.wo.WoFrafment.WoViewHolder.WoTrasnsmit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;

/**
 * Created by FengChaoQun
 * on 2016/9/10
 */
public class WoAdapter1 extends RealmBaseAdapter<Published> {
    private Context context;
    List<Published> publisheds;
    WoFragment woFragment;
    private BaseActivity activity;
    private Handler mHandler;
    Realm realm;
    private final Map<Class<?>, Integer> viewTypes;

    public WoAdapter1(@NonNull Context context, @Nullable OrderedRealmCollection<Published> data,
                      WoFragment woFragment, BaseActivity activity, Realm realm) {
        super(context, data);
        this.publisheds = data;
        this.woFragment = woFragment;
        this.activity = activity;
        this.realm = realm;
        mHandler = new Handler();
        this.context = context;
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
            holder.setAdapter1(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        view = holder.getView(LayoutInflater.from(context));
        view.setTag(holder);
        holder.setContext(view.getContext());
        Log.d("new view", "----ok");
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
}
