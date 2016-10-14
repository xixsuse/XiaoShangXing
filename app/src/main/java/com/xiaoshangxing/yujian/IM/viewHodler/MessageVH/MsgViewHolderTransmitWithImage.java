package com.xiaoshangxing.yujian.IM.viewHodler.MessageVH;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.PublishCache;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.image.MyGlide;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.xiaoshang.Sale.SaleActivity;
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
        PublishCache.getPublished(String.valueOf(state_id), new PublishCache.publishedCallback() {
            @Override
            public void callback(Published published) {
                int userId = published.getUserId();
                UserInfoCache.getInstance().getHead(head, userId, context);
                UserInfoCache.getInstance().getName(name, userId);
                UserInfoCache.getInstance().getCollege(college, userId);
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
                        Intent intent = new Intent(context, SaleActivity.class);
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
