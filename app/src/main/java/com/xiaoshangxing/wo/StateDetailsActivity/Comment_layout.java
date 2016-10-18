package com.xiaoshangxing.wo.StateDetailsActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.CommentsBean;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/7/11
 */
public class Comment_layout {
    private Context context;
    private RelativeLayout relativeLayout;
    private CirecleImage head;
    private TextView name, time, text;

    public Comment_layout(Context context, CommentsBean commentsBean, Realm realm) {
        this.context=context;
        relativeLayout= (RelativeLayout) LayoutInflater.from(context).
                inflate(R.layout.mystate_comment_layout,null);

        head = (CirecleImage) relativeLayout.findViewById(R.id.head_image);
        name = (TextView) relativeLayout.findViewById(R.id.name);
        time = (TextView) relativeLayout.findViewById(R.id.time);
        text = (TextView) relativeLayout.findViewById(R.id.text);

        String id = String.valueOf(commentsBean.getUserId());
        head.setIntent_type(CirecleImage.PERSON_STATE, id);
        UserInfoCache.getInstance().getExIntoTextview(id, NS.USER_NAME, name);
        UserInfoCache.getInstance().getHeadIntoImage(id, head);
        time.setText(TimeUtil.getTimeShowString(commentsBean.getCreateTime(), false));
        text.setText(commentsBean.getText());
    }

    public RelativeLayout getView(){
        return relativeLayout;
    }
}
