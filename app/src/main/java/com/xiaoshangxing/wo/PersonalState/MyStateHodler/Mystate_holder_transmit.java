package com.xiaoshangxing.wo.PersonalState.MyStateHodler;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.PublishCache;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.xiaoshang.Reward.RewardActivity;
import com.xiaoshangxing.xiaoshang.Help.HelpDetail.HelpDetailActivity;

/**
 * Created by FengChaoQun
 * on 2016/9/11
 */
public class Mystate_holder_transmit extends MyStateHodlerBase {
    private TextView shareText, text;
    private ImageView good;
    private CirecleImage head;

    @Override
    public void inflate() {
        View.inflate(view.getContext(), R.layout.mystae_holder_transmit, content);
        shareText = (TextView) view.findViewById(R.id.share_text);
        text = (TextView) view.findViewById(R.id.good_describe);
        good = (ImageView) view.findViewById(R.id.good_image);
        head = (CirecleImage) view.findViewById(R.id.person_image);
    }

    @Override
    public void refresh(Published published) {
        this.published = published;
        refreshBase();
        shareText.setText(published.getText());
        if (TextUtils.isEmpty(published.getOriginalId())) {
            return;
        }
        PublishCache.getPublished(published.getOriginalId(), new PublishCache.publishedCallback() {
            @Override
            public void callback(final Published published) {
                String userId = String.valueOf(published.getUserId());
                UserInfoCache.getInstance().getHeadIntoImage(userId, head);
                text.setText(TextUtils.isEmpty(published.getText()) ? "" : published.getText());

                content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String categry = String.valueOf(published.getCategory());
                        if (categry.equals(NS.CATEGORY_HELP)) {
                            Intent intent = new Intent(context, HelpDetailActivity.class);
                            intent.putExtra(IntentStatic.DATA, published.getId());
                            context.startActivity(intent);
                        } else if (categry.equals(NS.CATEGORY_REWARD)) {
                            Intent intent = new Intent(context, RewardActivity.class);
                            intent.putExtra(IntentStatic.DATA, published.getId());
                            context.startActivity(intent);
                        }
                    }
                });
            }
        });
    }

}
