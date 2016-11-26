package com.xiaoshangxing.wo.WoFrafment.WoViewHolder;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.PublishCache;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.xiaoshang.Help.HelpDetail.HelpDetailActivity;
import com.xiaoshangxing.xiaoshang.Plan.PlanDetail.PlanDetailActivity;
import com.xiaoshangxing.xiaoshang.Reward.RewardDetail.RewardDetailActivity;
import com.xiaoshangxing.xiaoshang.Sale.SaleDetail.SaleDetailsActivity;

/**
 * Created by FengChaoQun
 * on 2016/9/10
 */
public class WoTrasnsmit extends WoBaseHolder {
    private CirecleImage head;
    private EmotinText text;

    @Override
    public void inflate() {
        View.inflate(view.getContext(), R.layout.wo_viewholder_transmit, content);
        head = (CirecleImage) view.findViewById(R.id.transmit_image);
        text = (EmotinText) view.findViewById(R.id.transmit_text);
    }

    @Override
    public void refresh(final Published item) {
        setPublished(item);
        refreshBase();
        String orignalId = item.getOriginalId();
        if (TextUtils.isEmpty(orignalId)) {
            return;
        }
        PublishCache.getPublished(orignalId, new SimpleCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onBackData(Object o) {
                final Published published = (Published) o;
                String userId = String.valueOf(published.getUserId());
                UserInfoCache.getInstance().getHeadIntoImage(userId, head);
                text.setText(published.getText());
                content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int categry = published.getCategory();
                        if (categry == Integer.valueOf(NS.CATEGORY_HELP)) {
                            Intent help_intent = new Intent(context, HelpDetailActivity.class);
                            help_intent.putExtra(IntentStatic.DATA, published.getId());
                            context.startActivity(help_intent);
                        } else if (categry == Integer.valueOf(NS.CATEGORY_REWARD)) {
                            Intent intent = new Intent(context, RewardDetailActivity.class);
                            intent.putExtra(IntentStatic.DATA, published.getId());
                            context.startActivity(intent);
                        } else if (categry == Integer.valueOf(NS.CATEGORY_PLAN)) {
                            Intent intent = new Intent(context, PlanDetailActivity.class);
                            intent.putExtra(IntentStatic.DATA, published.getId());
                            context.startActivity(intent);
                        } else if (categry == Integer.valueOf(NS.CATEGORY_SALE)) {
                            Intent intent = new Intent(context, SaleDetailsActivity.class);
                            intent.putExtra(IntentStatic.DATA, published.getId());
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "类型异常" + categry, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
