package com.xiaoshangxing.xiaoshang.MessageNotice.NoticeViewHodler;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.wo.WoFrafment.Item_Comment;

/**
 * Created by FengChaoQun
 * on 2016/11/16
 */

public class NoticeCommentVH extends NoticeBaseViewHolder {
    private LinearLayout comments;
    private TextView comment;

    @Override
    public void inflateChild() {
        View.inflate(view.getContext(), R.layout.notice_holder_comment, content);
        comments = (LinearLayout) view.findViewById(R.id.comments);
        comment = (TextView) view.findViewById(R.id.comment);
    }

    @Override
    protected void refreshChild() {
        comment.setText("");
        Item_Comment.getComment("76", "冯超群", "测试一下", context, comment);
    }
}
