package com.xiaoshangxing.wo.WoFrafment;

import android.content.Context;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.TempUser;

/**
 * Created by FengChaoQun
 * on 2016/9/10
 */
public class Published_Help {

    /**
     * description:解析点赞人 并显示到校友圈
     *
     * @param published    动态
     * @param linearLayout 显示点赞人的父布局
     * @return
     */

    public static void buildPrasiPeople(Published published, LinearLayout linearLayout, Context context) {

        linearLayout.removeAllViews();

        if (TextUtils.isEmpty(published.getPraiseUserIds())) {
            return;
        }

        PraisePeople praisePeople = new PraisePeople(context);

        for (String i : published.getPraiseUserIds().split(NS.SPLIT)) {

            String[] id_name = i.split(NS.SPLIT2);
            if (id_name.length >= 2) {
                praisePeople.addName(id_name[1], id_name[0]);
            }
        }

        linearLayout.addView(praisePeople.getTextView());
    }

    /**
     * description:判断是否已经赞过
     *
     * @param published 动态
     * @return true 为已经赞过
     */

    public static boolean isPraised(Published published) {
        if (TextUtils.isEmpty(published.getPraiseUserIds())) {
            return false;
        }

        return published.getPraisePeopleOnlyIds().contains(String.valueOf(TempUser.id));
    }
}
