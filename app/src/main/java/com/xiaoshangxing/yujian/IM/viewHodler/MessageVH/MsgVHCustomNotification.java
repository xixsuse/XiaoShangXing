package com.xiaoshangxing.yujian.IM.viewHodler.MessageVH;

import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotFilter.MoonUtil;


public class MsgVHCustomNotification extends MsgViewHolderBase {

    protected TextView notificationTextView;


    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_notification;
    }

    @Override
    protected void inflateContentView() {
        notificationTextView = (TextView) view.findViewById(R.id.message_item_notification_label);
    }

    @Override
    protected void bindContentView() {
        handleTextNotification(getDisplayText());
    }

    protected String getDisplayText() {

        return CustomNotificationText.getNotificationText(message);
    }

    private void handleTextNotification(String text) {
        MoonUtil.identifyFaceExpressionAndATags(context, notificationTextView, text, ImageSpan.ALIGN_BOTTOM);
        notificationTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected boolean isMiddleItem() {
        return true;
    }
}

