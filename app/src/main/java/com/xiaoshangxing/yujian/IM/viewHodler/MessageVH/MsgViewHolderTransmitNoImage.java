package com.xiaoshangxing.yujian.IM.viewHodler.MessageVH;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.PublishCache;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.xiaoshang.Help.HelpDetail.HelpDetailActivity;
import com.xiaoshangxing.xiaoshang.Plan.PlanDetail.PlanDetailActivity;
import com.xiaoshangxing.xiaoshang.Reward.RewardDetail.RewardDetailActivity;
import com.xiaoshangxing.yujian.IM.CustomMessage.TransmitMessage_NoImage;


/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderTransmitNoImage extends MsgViewHolderBase {
    private CirecleImage head;
    private TextView name, college, text, from, price;
    private LinearLayout up_lay;
    private TransmitMessage_NoImage transmitMessage_noImage;
    private int state_id;

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_transmit_noimage;
    }

    @Override
    protected void inflateContentView() {
        head = findViewById(R.id.transmit_head);
        name = findViewById(R.id.transmit_name);
        college = findViewById(R.id.transmit_college);
        text = findViewById(R.id.transmit_text);
        from = findViewById(R.id.from);
        up_lay = findViewById(R.id.up_lay);
        price = findViewById(R.id.price);
    }

    @Override
    protected void bindContentView() {
        transmitMessage_noImage = (TransmitMessage_NoImage) message.getAttachment();
        state_id = transmitMessage_noImage.getState_id();
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
                UserInfoCache.getInstance().getHeadIntoImage(userId,head);
                UserInfoCache.getInstance().getExIntoTextview(userId,NS.USER_NAME,name);
                UserInfoCache.getInstance().getExIntoTextview(userId,NS.COLLEGE,college);
                text.setText(published.getText());
                if (published.getCategory() == Integer.valueOf(NS.CATEGORY_HELP)) {
                    from.setText("分享自校友互帮");
                    price.setText("");
                    contentContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HelpDetailActivity.class);
                            intent.putExtra(IntentStatic.DATA, state_id);
                            context.startActivity(intent);
                        }
                    });
                } else if (published.getCategory() == Integer.valueOf(NS.CATEGORY_REWARD)) {
                    from.setText("分享自校内悬赏");
                    price.setText(TextUtils.isEmpty(published.getPrice()) ? "" : NS.RMB + published.getPrice());
                    contentContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, RewardDetailActivity.class);
                            intent.putExtra(IntentStatic.DATA, state_id);
                            context.startActivity(intent);
                        }
                    });
                } else if (published.getCategory() == Integer.valueOf(NS.CATEGORY_PLAN)) {
                    from.setText("分享自计划发起");
                    price.setText("");
                    contentContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, PlanDetailActivity.class);
                            intent.putExtra(IntentStatic.DATA, state_id);
                            context.startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    private void layoutDirection() {
        if (isReceivedMessage()) {
            up_lay.setBackgroundResource(R.mipmap.transmit_left);
            up_lay.setPadding(ScreenUtils.getAdapterPx(R.dimen.x48, context),
                    0, ScreenUtils.getAdapterPx(R.dimen.x52, context), 0);
        } else {
            up_lay.setBackgroundResource(R.mipmap.transmit_right);
            up_lay.setPadding(ScreenUtils.getAdapterPx(R.dimen.x24, context),
                    0, ScreenUtils.getAdapterPx(R.dimen.x72, context), 0);
        }
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
