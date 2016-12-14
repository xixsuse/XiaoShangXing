package com.xiaoshangxing.xiaoshang.MessageNotice.NoticeViewHodler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.data.bean.PushMsg;
import com.xiaoshangxing.utils.customView.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.customView.CirecleImage;
import com.xiaoshangxing.utils.customView.Name;
import com.xiaoshangxing.xiaoshang.Help.HelpDetail.HelpDetailActivity;
import com.xiaoshangxing.xiaoshang.MessageNotice.NoticeAdpter_realm;
import com.xiaoshangxing.xiaoshang.Plan.PlanDetail.PlanDetailActivity;
import com.xiaoshangxing.xiaoshang.Reward.RewardDetail.RewardDetailActivity;
import com.xiaoshangxing.xiaoshang.Sale.SaleDetail.SaleDetailsActivity;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

/**
 * Created by FengChaoQun
 * on 2016/11/16
 */

public abstract class NoticeBaseViewHolder {
    protected NoticeAdpter_realm adpter;
    protected int position;
    protected Context context;
    protected View view;
    protected PushMsg pushMsg;

    protected CirecleImage headImage;
    protected Name name;
    protected TextView college;
    protected TextView time;
    protected View responseLay;
    protected CirecleImage typeImage;
    protected FrameLayout content;
    protected View publishedConetent;
    protected EmotinText publishedText;


    public View getView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.notice_base_vh, null);
        inflateBase();
        inflateChild();
        return view;
    }


    //初始化基础
    private void inflateBase() {
        headImage = (CirecleImage) view.findViewById(R.id.head_image);
        name = (Name) view.findViewById(R.id.name);
        college = (TextView) view.findViewById(R.id.college);
        time = (TextView) view.findViewById(R.id.time);
        responseLay = view.findViewById(R.id.response);
        content = (FrameLayout) view.findViewById(R.id.content);
        publishedText = (EmotinText) view.findViewById(R.id.published_text);
        typeImage = (CirecleImage) view.findViewById(R.id.type_image);
        publishedConetent = view.findViewById(R.id.publish_content);
    }

    //初始化子类
    public abstract void inflateChild();

    public void refresh(PushMsg pushMsg) {
        setNotice(pushMsg);
        refreshBase();
        refreshChild();
    }

    //刷新基础
    protected void refreshBase() {
        initView();
        initOnclick();
    }

    //刷新子类
    protected abstract void refreshChild();

    private void initView() {
        UserInfoCache.getInstance().getHeadIntoImage(pushMsg.getUserId(), headImage);
        UserInfoCache.getInstance().getExIntoTextview(pushMsg.getUserId(), NS.COLLEGE, college);
        name.setText(pushMsg.getUserName());
        time.setText(TimeUtil.getTimeShowString(pushMsg.getPushTime(), false));
        publishedText.setText(pushMsg.getMomentText());
        switch (pushMsg.getCategory()) {
            case NS.CATEGORY_HELP:
                typeImage.setImageResource(R.mipmap.shool_help_log);
                break;
            case NS.CATEGORY_PLAN:
                typeImage.setImageResource(R.mipmap.launch_plan_log);
                break;
            case NS.CATEGORY_REWARD:
                typeImage.setImageResource(R.mipmap.school_reward_log);
                break;
            case NS.CATEGORY_SALE:
                typeImage.setImageResource(R.mipmap.xianzhi_log);
                break;
        }
    }

    private void initOnclick() {
        headImage.setIntent_type(CirecleImage.PERSON_INFO, pushMsg.getUserId());
        name.setIntent_type(Name.PERSON_INFO, pushMsg.getUserId());

        final Intent intent = new Intent();
        switch (pushMsg.getCategory()) {
            case NS.CATEGORY_HELP:
                intent.setClass(context, HelpDetailActivity.class);
                break;
            case NS.CATEGORY_PLAN:
                intent.setClass(context, PlanDetailActivity.class);
                break;
            case NS.CATEGORY_REWARD:
                intent.setClass(context, RewardDetailActivity.class);
                break;
            case NS.CATEGORY_SALE:
                intent.setClass(context, SaleDetailsActivity.class);
                break;
            default:
                return;
        }
        intent.putExtra(IntentStatic.DATA, Integer.valueOf(pushMsg.getMomentId()));
        Log.d("momentId", pushMsg.getMomentId());
        publishedConetent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(intent);
            }
        });
    }

    public NoticeAdpter_realm getAdpter() {
        return adpter;
    }

    public void setAdpter(NoticeAdpter_realm adpter) {
        this.adpter = adpter;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public PushMsg getNotice() {
        return pushMsg;
    }

    public void setNotice(PushMsg pushMsg) {
        this.pushMsg = pushMsg;
    }
}
