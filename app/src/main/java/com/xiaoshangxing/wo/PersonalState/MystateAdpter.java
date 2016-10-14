package com.xiaoshangxing.wo.PersonalState;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.wo.PersonalState.MyStateHodler.MyStateHodlerBase;
import com.xiaoshangxing.wo.PersonalState.MyStateHodler.Mystate_holder_transmit;
import com.xiaoshangxing.wo.PersonalState.MyStateHodler.Mystate_image_text;
import com.xiaoshangxing.wo.PersonalState.MyStateHodler.Mystate_onlytext;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

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
public class MystateAdpter extends RealmBaseAdapter<Published> {
    private Context context;
    List<Published> publisheds;
    private Handler mHandler;
    Realm realm;
    private final Map<Class<?>, Integer> viewTypes;
    private Map<String, Integer> date_position = new HashMap<>();

    public MystateAdpter(@NonNull Context context, @Nullable OrderedRealmCollection<Published> data,
                         Realm realm) {
        super(context, data);
        this.publisheds = data;
        this.realm = realm;
        mHandler = new Handler();
        this.context = context;
        this.viewTypes = new HashMap<Class<?>, Integer>(getViewTypeCount());
        date_position.put(TimeUtil.getFavoriteCollectTime(NS.currentTime()), -1);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyStateHodlerBase myStateHodlerBase;
        if (convertView == null) {
            convertView = viewAtPosition(position);
        }
        myStateHodlerBase = (MyStateHodlerBase) convertView.getTag();
        myStateHodlerBase.setPosition(position);
        myStateHodlerBase.refresh(publisheds.get(position));
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
        return 3;
    }


    public View viewAtPosition(int position) {
        MyStateHodlerBase holder = null;
        View view = null;
        try {
            Class<?> viewHolder = viewHolderAtPosition(position);
            holder = (MyStateHodlerBase) viewHolder.newInstance();
            holder.setRealm(realm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        view = holder.getView(LayoutInflater.from(context));
        view.setTag(holder);
        holder.setContext(view.getContext());
        holder.setAdpter(this);
        return view;
    }

    private Class<? extends MyStateHodlerBase> viewHolderAtPosition(int position) {
        if (publisheds.get(position).getIsTransimit() == 1) {
            return Mystate_holder_transmit.class;
        } else if (TextUtils.isEmpty(publisheds.get(position).getImage())) {
            return Mystate_onlytext.class;
        } else {
            return Mystate_image_text.class;
        }
    }

    public boolean isNeedShowDate(String date, int position) {
        if (date_position.containsKey(date)) {
            return date_position.get(date) == position;
        } else {
            date_position.put(date, position);
            return true;
        }
    }
}
