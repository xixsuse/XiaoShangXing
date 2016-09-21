package com.xiaoshangxing.yujian.IM.viewHodler.MessageVH;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;


/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderTransmitWithImage extends MsgViewHolderBase {
    private CirecleImage head;
    private TextView name, college, text, from;
    private LinearLayout up_lay;
    private ImageView transmit_image;

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
        layoutDirection();
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
