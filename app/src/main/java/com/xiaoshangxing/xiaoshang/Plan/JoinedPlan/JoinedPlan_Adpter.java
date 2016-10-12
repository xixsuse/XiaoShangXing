package com.xiaoshangxing.xiaoshang.Plan.JoinedPlan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.JoinedPlan;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.layout.LayoutHelp;
import com.xiaoshangxing.utils.layout.Name;
import com.xiaoshangxing.xiaoshang.Plan.PlanDetail.PlanDetailActivity;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 */
public class JoinedPlan_Adpter extends ArrayAdapter<JoinedPlan> {
    private Context context;
    List<JoinedPlan> joinedPlen;
    protected Handler handler;
    private Activity activity;

    public JoinedPlan_Adpter(Context context, int resource, Activity activity, List<JoinedPlan> objects) {
        super(context, resource, objects);
        this.context = context;
        this.joinedPlen = objects;
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_plan_adapter, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        final JoinedPlan published = joinedPlen.get(position);

        UserInfoCache.getInstance().getHead(viewHolder.headImage, published.getUserId(), context);
        UserInfoCache.getInstance().getName(viewHolder.name, published.getUserId());
        UserInfoCache.getInstance().getCollege(viewHolder.college, published.getUserId());
        viewHolder.time.setText(TimeUtil.getTimeShowString(published.getCreateTime(), false));
        viewHolder.text.setText(published.getText());
        viewHolder.planName.setText(published.getPlanName());
        viewHolder.complete.setVisibility(published.isAlive() ? View.GONE : View.VISIBLE);

        if (TextUtils.isEmpty(published.getPersonLimit())) {
            viewHolder.peopleLimit.setText("不限人数");
        } else {
            viewHolder.peopleLimit.setText((published.getJoinCount() + "-" + published.getPersonLimit()));
        }

        viewHolder.joinedCount.setText("" + published.getJoinCount());

        if (!TextUtils.isEmpty(published.getPersonLimit())) {
            if (published.getPraiseCount() == Integer.valueOf(published.getPersonLimit())) {
                viewHolder.full.setVisibility(View.VISIBLE);
            } else {
                viewHolder.full.setVisibility(View.INVISIBLE);
            }
        } else {
            viewHolder.full.setVisibility(View.INVISIBLE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutHelp.PermissionClick(activity, new LayoutHelp.PermisionMethod() {
                    @Override
                    public void doSomething() {
                        Intent intent = new Intent(context, PlanDetailActivity.class);
                        intent.putExtra(IntentStatic.DATA, published.getId());
                        context.startActivity(intent);
                    }
                });
            }
        });

        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.name)
        Name name;
        @Bind(R.id.college)
        TextView college;
        @Bind(R.id.down_arrow)
        ImageView downArrow;
        @Bind(R.id.complete)
        ImageView complete;
        @Bind(R.id.plan_name)
        TextView planName;
        @Bind(R.id.text)
        EmotinText text;
        @Bind(R.id.up_lay)
        LinearLayout upLay;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.people_limit)
        TextView peopleLimit;
        @Bind(R.id.joined_count)
        TextView joinedCount;
        @Bind(R.id.full)
        TextView full;
        @Bind(R.id.head_image)
        CirecleImage headImage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
