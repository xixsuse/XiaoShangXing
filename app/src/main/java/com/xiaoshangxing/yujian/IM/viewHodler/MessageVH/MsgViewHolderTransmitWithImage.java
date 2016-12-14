package com.xiaoshangxing.yujian.IM.viewHodler.MessageVH;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.PublishCache;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.data.bean.Published;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.customView.CirecleImage;
import com.xiaoshangxing.utils.imageUtils.MyGlide;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.xiaoshang.Sale.SaleDetail.SaleDetailsActivity;
import com.xiaoshangxing.yujian.IM.CustomMessage.TransmitMessage_WithImage;


/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderTransmitWithImage extends MsgViewHolderBase {
    private CirecleImage head;
    private TextView name, college, text, from;
    private LinearLayout up_lay;
    private ImageView transmit_image;
    private TransmitMessage_WithImage transmitMessage_withImage;
    private int state_id;

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_transmit_withimage;
    }

    @Override
    protected void inflateContentView() {
        head = findViewById(R.id.transmit_head);
        name = findViewById(R.id.transmit_name);
        college = findViewById(R.id.transmit_college);
        text = findViewById(R.id.transmit_text);
        from = findViewById(R.id.from);
        up_lay = findViewById(R.id.up_lay);
        transmit_image = findViewById(R.id.transmit_image);
    }

    @Override
    protected void bindContentView() {
        transmitMessage_withImage = (TransmitMessage_WithImage) message.getAttachment();
        state_id = transmitMessage_withImage.getState_id();
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
                from.setText("分享自闲置出售");
                String images = published.getImage();
                if (!TextUtils.isEmpty(images)) {
                    String imagePath[] = images.split(NS.SPLIT);
                    MyGlide.with(context, imagePath[0], transmit_image);
                }
                contentContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, SaleDetailsActivity.class);
                        intent.putExtra(IntentStatic.DATA, state_id);
                        context.startActivity(intent);
                    }
                });
            }
        });
    }

    private void layoutDirection() {
        if (isReceivedMessage()) {
            up_lay.setBackgroundResource(R.mipmap.transmit_left);
            up_lay.setPadding(ScreenUtils.getAdapterPx(R.dimen.x48, context),
                    ScreenUtils.getAdapterPx(R.dimen.y18, context), ScreenUtils.getAdapterPx(R.dimen.x52, context), 0);
        } else {
            up_lay.setBackgroundResource(R.mipmap.transmit_right);
            up_lay.setPadding(ScreenUtils.getAdapterPx(R.dimen.x24, context),
                    ScreenUtils.getAdapterPx(R.dimen.y18, context), ScreenUtils.getAdapterPx(R.dimen.x72, context), 0);
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
