package com.xiaoshangxing.wo.myState.MyStateHodler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.input_activity.InputActivity;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.wo.myState.MystateAdpter;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/9/11
 */
public abstract class MyStateHodlerBase {
    public Published published;
    /**
     * context
     */
    protected Context context;

    protected BaseActivity activity;

    protected MystateAdpter adpter;

    /**
     * list item view
     */
    protected View view;

    protected Realm realm;

    public int position;

    private TextView day, month, location;
    private View publish;
    protected FrameLayout content;

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
        publish = view.findViewById(R.id.publish);
        content = (FrameLayout) view.findViewById(R.id.content);
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

        location.setText(published.getLocation());
        if (TempUser.isMine(String.valueOf(published.getUserId())) && position == 0) {
            publish.setVisibility(View.VISIBLE);
            publish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent publish_intent = new Intent(getContext(), InputActivity.class);
                    publish_intent.putExtra(InputActivity.LIMIT, 9);
                    publish_intent.putExtra(InputActivity.EDIT_STATE, InputActivity.PUBLISH_STATE);
                    context.startActivity(publish_intent);
                }
            });
        } else {
            publish.setVisibility(View.GONE);
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
