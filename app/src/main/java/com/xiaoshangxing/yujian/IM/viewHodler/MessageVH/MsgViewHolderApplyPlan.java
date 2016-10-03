package com.xiaoshangxing.yujian.IM.viewHodler.MessageVH;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.PublishCache;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.yujian.IM.CustomMessage.ApplyPlanMessage;


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
        PublishCache.getPublished(String.valueOf(state_id), new PublishCache.publishedCallback() {
            @Override
            public void callback(Published published) {
                int userId = published.getUserId();
                UserInfoCache.getInstance().getHead(head, userId, context);
                UserInfoCache.getInstance().getName(name, userId);
                UserInfoCache.getInstance().getCollege(college, userId);
                text.setText(published.getText());
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
        } else {
            total_lay.setBackgroundResource(R.mipmap.apply_plan_rightblock);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtils.getAdapterPx(R.dimen.x752, context),
                    ScreenUtils.getAdapterPx(R.dimen.y379, context));
            total_lay.setLayoutParams(layoutParams);
            up_lay1.setPadding(ScreenUtils.getAdapterPx(R.dimen.x35, context),
                    ScreenUtils.getAdapterPx(R.dimen.y30, context), 0, 0);
            horizontal_line.setVisibility(View.GONE);
            bellow_lay.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onItemClick() {
        Toast.makeText(context, "点击", Toast.LENGTH_SHORT).show();
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
