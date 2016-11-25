package com.xiaoshangxing.wo.PersonalState.MyStateHodler;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.PublishCache;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.xiaoshang.Help.HelpDetail.HelpDetailActivity;
import com.xiaoshangxing.xiaoshang.Plan.PlanDetail.PlanDetailActivity;
import com.xiaoshangxing.xiaoshang.Reward.RewardDetail.RewardDetailActivity;
import com.xiaoshangxing.xiaoshang.Sale.SaleDetail.SaleDetailsActivity;

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
        PublishCache.getPublished(published.getOriginalId(), new SimpleCallBack() {
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
                text.setText(TextUtils.isEmpty(published.getText()) ? "" : published.getText());

                content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String categry = String.valueOf(published.getCategory());
                        Intent intent = new Intent();
                        switch (categry) {
                            case NS.CATEGORY_HELP:
                                intent.setClass(context, HelpDetailActivity.class);
                                break;
                            case NS.CATEGORY_REWARD:
                                intent.setClass(context, RewardDetailActivity.class);
                                break;
                            case NS.CATEGORY_PLAN:
                                intent.setClass(context, PlanDetailActivity.class);
                                break;
                            case NS.CATEGORY_SALE:
                                intent.setClass(context, SaleDetailsActivity.class);
                                break;
                        }
                        intent.putExtra(IntentStatic.DATA, published.getId());
                        context.startActivity(intent);
                    }
                });
            }
        });
    }
}
