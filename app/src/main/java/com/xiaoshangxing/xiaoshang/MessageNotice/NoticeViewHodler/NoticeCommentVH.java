package com.xiaoshangxing.xiaoshang.MessageNotice.NoticeViewHodler;

import android.view.View;
import android.widget.LinearLayout;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.customView.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.NotifycationUtil;
import com.xiaoshangxing.wo.WoFrafment.Item_Comment;

/**
 * Created by FengChaoQun
 * on 2016/11/16
 */

public class NoticeCommentVH extends NoticeBaseViewHolder {
    private LinearLayout comments;
    private EmotinText comment;

    @Override
    public void inflateChild() {
        View.inflate(view.getContext(), R.layout.notice_holder_comment, content);
        comments = (LinearLayout) view.findViewById(R.id.comments);
        comment = (EmotinText) view.findViewById(R.id.comment);
    }

    @Override
    protected void refreshChild() {
        comment.setText("");

        if (pushMsg.getPushType().equals(NotifycationUtil.NT_SCHOOL_NOTICE_YOU)) {
            comment.setText("同时提到了你");
            return;
        }

        switch (pushMsg.getOperationType()) {
            case NotifycationUtil.OPERATION_COMMENT:
                Item_Comment.getComment(pushMsg.getUserId(), pushMsg.getUserName(), pushMsg.getText(), context, comment);
                break;
            case NotifycationUtil.OPERATION_PRAISE:
                comment.setText("赞了你");
                break;
            case NotifycationUtil.OPERATION_TRANSMIT:
                comment.setText("转发" + pushMsg.getCategoryName() + ":" + pushMsg.getText());
                break;
            default:
                comment.setText("未知内容");
                break;
        }

    }
}
