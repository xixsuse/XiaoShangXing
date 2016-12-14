package com.xiaoshangxing.wo.PersonalState.MyStateHodler;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.bean.Published;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.wo.PersonalState.MystateAdpter;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/9/11
 */
public abstract class MyStateHodlerBase {
    public Published published;
    public int position;
    /**
     * context
     */
    protected Context context;
    protected BaseActivity activity;
    protected MystateAdpter adpter;
    /**
     * list item view
     */
    protected View view, left_lay;
    protected Realm realm;
    protected FrameLayout content;
    private TextView day, month, location;

    public MyStateHodlerBase() {
    }

    public View getView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.mystate_holder_base, null);
        inflateBase();
        inflate();
        return view;
    }

    public abstract void inflate();

    private void inflateBase() {
        day = (TextView) view.findViewById(R.id.day);
        month = (TextView) view.findViewById(R.id.month);
        location = (TextView) view.findViewById(R.id.location);
        content = (FrameLayout) view.findViewById(R.id.content);
        left_lay = view.findViewById(R.id.left_lay);
    }

    public abstract void refresh(Published published);

    protected void refreshBase() {
        String data = TimeUtil.getFavoriteCollectTime(published.getCreateTime());
        if (adpter.isNeedShowDate(data, position)) {
            String[] datas = data.split("-");
            if (datas.length == 2) {
                day.setText(datas[1]);
                month.setText(TimeUtil.transferCNmonth(datas[0]));
            } else if (datas.length == 3) {
                day.setText(datas[2]);
                month.setText(TimeUtil.transferCNmonth(datas[0]));
            }
        } else {
            day.setText("");
            month.setText("");
        }

        String location_text = published.getLocation();
        if (TextUtils.isEmpty(location_text)) {
            location.setVisibility(View.GONE);
        } else {
            location.setText(location_text);
            location.setVisibility(View.VISIBLE);
        }
        if (position == 0) {
            left_lay.setPadding(0, ScreenUtils.getAdapterPx(R.dimen.y110, context), 0, 0);
            content.setPadding(0, ScreenUtils.getAdapterPx(R.dimen.y110, context), 0, 0);
        } else {
            if (adpter.isNeedShowDate(data, position)) {
                left_lay.setPadding(0, ScreenUtils.getAdapterPx(R.dimen.y76, context), 0, 0);//左侧布局考虑到textview的间隔 比右侧少点
                content.setPadding(0, ScreenUtils.getAdapterPx(R.dimen.y86, context), 0, 0);
            } else {
                left_lay.setPadding(0, ScreenUtils.getAdapterPx(R.dimen.y14, context), 0, 0);
                content.setPadding(0, ScreenUtils.getAdapterPx(R.dimen.y14, context), 0, 0);
            }

        }
    }

    public MystateAdpter getAdpter() {
        return adpter;
    }

    public void setAdpter(MystateAdpter adpter) {
        this.adpter = adpter;
    }

    public Published getPublished() {
        return published;
    }

    public void setPublished(Published published) {
        this.published = published;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public BaseActivity getActivity() {
        return activity;
    }

    public void setActivity(BaseActivity activity) {
        this.activity = activity;
    }

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
