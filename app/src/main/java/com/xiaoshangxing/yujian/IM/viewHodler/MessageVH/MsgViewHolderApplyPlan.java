package com.xiaoshangxing.yujian.IM.viewHodler.MessageVH;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.TeamService;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.netUtil.OperateUtils;
import com.xiaoshangxing.Network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.PublishCache;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.xiaoshang.Plan.PlanDetail.PlanDetailActivity;
import com.xiaoshangxing.yujian.IM.CustomMessage.ApplyPlanMessage;
import com.xiaoshangxing.yujian.IM.CustomMessage.CustomNotificationMessage;

import java.util.ArrayList;


/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderApplyPlan extends MsgViewHolderBase {
    private CirecleImage head;
    private TextView name, college, text, state;
    private LinearLayout total_lay;
    private View agree, refuse, horizontal_line, bellow_lay;
    private RelativeLayout up_lay1;
    private ApplyPlanMessage applyPlanMessage;
    private int state_id;
    private String group_account;

    @Override
    protected int getContentResId() {
        return R.layout.apply_plan_right;
    }

    @Override
    protected void inflateContentView() {
        head = findViewById(R.id.head_image);
        name = findViewById(R.id.name);
        college = findViewById(R.id.college);
        text = findViewById(R.id.text);
        state = findViewById(R.id.state_text);
        total_lay = findViewById(R.id.total_lay);
        agree = findViewById(R.id.agree);
        refuse = findViewById(R.id.refuse);
        horizontal_line = findViewById(R.id.horizontal_line);
        bellow_lay = findViewById(R.id.bellow_lay);
        up_lay1 = findViewById(R.id.up_lay1);
    }

    @Override
    protected void bindContentView() {
        applyPlanMessage = (ApplyPlanMessage) message.getAttachment();
        state_id = applyPlanMessage.getState_id();
        layoutDirection();
        PublishCache.getPublished(String.valueOf(state_id), new SimpleCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onBackData(Object o) {
                Published published = (Published) o;
                String userId = String.valueOf(published.getUserId());
                UserInfoCache.getInstance().getHeadIntoImage(userId, head);
                UserInfoCache.getInstance().getExIntoTextview(userId, NS.USER_NAME, name);
                UserInfoCache.getInstance().getExIntoTextview(userId, NS.COLLEGE, college);
                text.setText(published.getText());
                group_account = published.getGroupNo();
            }
        });
    }

    private void layoutDirection() {
        if (isReceivedMessage()) {
            total_lay.setBackgroundResource(R.mipmap.apply_plan_leftblock);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtils.getAdapterPx(R.dimen.x752, context),
                    ScreenUtils.getAdapterPx(R.dimen.y523, context));
            total_lay.setLayoutParams(layoutParams);
            up_lay1.setPadding(ScreenUtils.getAdapterPx(R.dimen.x55, context),
                    ScreenUtils.getAdapterPx(R.dimen.y30, context), 0, 0);

            horizontal_line.setVisibility(View.VISIBLE);
            bellow_lay.setVisibility(View.VISIBLE);
            state.setText("申请加入计划");
            initClick();
        } else {
            total_lay.setBackgroundResource(R.mipmap.apply_plan_rightblock);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtils.getAdapterPx(R.dimen.x752, context),
                    ScreenUtils.getAdapterPx(R.dimen.y379, context));
            total_lay.setLayoutParams(layoutParams);
            up_lay1.setPadding(ScreenUtils.getAdapterPx(R.dimen.x35, context),
                    ScreenUtils.getAdapterPx(R.dimen.y30, context), 0, 0);
            horizontal_line.setVisibility(View.GONE);
            bellow_lay.setVisibility(View.GONE);
            state.setText("我已申请加入计划,等待对方回应...");
        }
    }

    private void initClick() {
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMMessage imMessage = null;
                CustomNotificationMessage customNotificationMessage = new CustomNotificationMessage();
                customNotificationMessage.setNotificationType(CustomNotificationText.ApllyPlan_REFUSE);
                imMessage = MessageBuilder.createCustomMessage(message.getFromAccount(), SessionTypeEnum.P2P, customNotificationMessage);
                moduleProxy.sendMessage(imMessage);
            }
        });

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OperateUtils.operate(state_id, context, true, NS.JOIN, false, new SimpleCallBack() {
                    @Override
                    public void onSuccess() {
                        if (TextUtils.isEmpty(group_account)) {
                            Toast.makeText(context, "群号异常", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ArrayList<String> arrayList = new ArrayList<String>();
                        arrayList.add(message.getSessionId());
                        NIMClient.getService(TeamService.class).addMembers(group_account, arrayList).setCallback(new RequestCallback<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                IMMessage imMessage = null;
                                CustomNotificationMessage customNotificationMessage = new CustomNotificationMessage();
                                customNotificationMessage.setNotificationType(CustomNotificationText.ApllyPlan_PASS);
                                imMessage = MessageBuilder.createCustomMessage(message.getFromAccount(), SessionTypeEnum.P2P, customNotificationMessage);
                                moduleProxy.sendMessage(imMessage);
                            }

                            @Override
                            public void onFailed(int i) {
                                Toast.makeText(context, "拉入计划群失败" + i, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onException(Throwable throwable) {
                                Toast.makeText(context, "拉入计划群异常", Toast.LENGTH_SHORT).show();
                                throwable.printStackTrace();
                            }
                        });

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onBackData(Object o) {

                    }
                });
            }
        });
    }

    // 内容区域点击事件响应处理。
    @Override
    protected void onItemClick() {
        Intent intent = new Intent(context, PlanDetailActivity.class);
        intent.putExtra(IntentStatic.DATA, state_id);
        context.startActivity(intent);
    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }

    protected String getDisplayText() {
        return message.getContent();
    }
}
