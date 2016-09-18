package com.xiaoshangxing.wo.WoFrafment.WoViewHolder;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.PublishCache;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.xiaoshang.ShoolReward.ShoolRewardActivity;
import com.xiaoshangxing.xiaoshang.ShoolfellowHelp.HelpDetail.HelpDetailActivity;

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
        PublishCache.getPublished(orignalId, new PublishCache.publishedCallback() {
            @Override
            public void callback(final Published published) {
                UserInfoCache.getInstance().getHead(head, published.getUserId(), context);
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
                            Intent intent = new Intent(context, ShoolRewardActivity.class);
                            intent.putExtra(IntentStatic.DATA, published.getId());
                            context.startActivity(intent);
                        }
                    }
                });
            }
        });

    }
}
