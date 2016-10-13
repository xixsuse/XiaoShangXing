package com.xiaoshangxing.yujian.IM.viewHodler;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xiaoshangxing.R;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.image.MyGlide;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;
import com.xiaoshangxing.yujian.IM.cache.TeamDataCache;
import com.xiaoshangxing.yujian.IM.kit.ImageKit.imageview.HeadImageView;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;
import com.xiaoshangxing.yujian.IM.uinfo.UserInfoHelper;
import com.xiaoshangxing.yujian.YujianFragment.YuJianFragment;

public abstract class RecentViewHolder extends TViewHolder implements OnClickListener {

    protected FrameLayout portraitPanel;

    protected HeadImageView imgHead;

    protected TextView tvNickname;

    protected EmotinText tvMessage;

    protected TextView tvUnread;

    protected RelativeLayout unread_less;

    protected ImageView unread_more;

    protected View unreadIndicator;

    protected TextView tvDatetime;

    // 消息发送错误状态标记，目前没有逻辑处理
    protected ImageView imgMsgStatus;

    protected RecentContact recent;

    protected View bottomLine;
    protected View topLine;

    protected abstract String getContent();

    public void refresh(Object item) {
        recent = (RecentContact) item;

        updateBackground();

        loadPortrait();

        updateNewIndicator();

        updateNickLabel(UserInfoHelper.getUserTitleName(recent.getContactId(), recent.getSessionType()));

        updateMsgLabel();
    }

    public void refreshCurrentItem() {
        if (recent != null) {
            refresh(recent);
        }
    }

    private void updateBackground() {
        topLine.setVisibility(isFirstItem() ? View.GONE : View.VISIBLE);
        bottomLine.setVisibility(isLastItem() ? View.VISIBLE : View.GONE);
        if ((recent.getTag() & YuJianFragment.RECENT_TAG_STICKY) == 0) {
            view.setBackgroundResource(R.drawable.nim_list_item_selector);
        } else {
            view.setBackgroundResource(R.drawable.nim_recent_contact_sticky_selecter);
        }
    }

    protected void loadPortrait() {
        // 设置头像
        if (recent.getSessionType() == SessionTypeEnum.P2P) {
//            imgHead.loadBuddyAvatar(recent.getContactId());
            NimUserInfo userInfo = NimUserInfoCache.getInstance().getUserInfo(recent.getContactId());
            if (userInfo != null) {
                MyGlide.with_app_log(context, userInfo.getAvatar(), imgHead);
            }
        } else if (recent.getSessionType() == SessionTypeEnum.Team) {
            Team team = TeamDataCache.getInstance().getTeamById(recent.getContactId());
            imgHead.loadTeamIconByTeam(team);
        }
    }

    private void updateNewIndicator() {
        int unreadNum = recent.getUnreadCount();
//        tvUnread.setVisibility(unreadNum > 0 ? View.VISIBLE : View.GONE);
//        tvUnread.setText(unreadCountShowRule(unreadNum));
        if (unreadNum > 0 && unreadNum < 100) {
            unread_less.setVisibility(View.VISIBLE);
            tvUnread.setText("" + unreadNum);
            unread_more.setVisibility(View.GONE);
        } else if (unreadNum > 99) {
            unread_less.setVisibility(View.GONE);
            unread_more.setVisibility(View.VISIBLE);
        } else {
            unread_less.setVisibility(View.GONE);
            unread_more.setVisibility(View.GONE);
        }
    }

    private void updateMsgLabel() {
        // 显示消息具体内容
//        MoonUtil.identifyFaceExpressionAndTags(context, tvMessage, getContent(), ImageSpan.ALIGN_BOTTOM, 0.45f);
        if (TextUtils.isEmpty(getContent())) {
//            Log.d("创建计划群", "无邀请人");
            return;
        }

        tvMessage.setText(getContent());

        MsgStatusEnum status = recent.getMsgStatus();
        switch (status) {
            case fail:
                imgMsgStatus.setImageResource(R.mipmap.nim_g_ic_failed_small);
                imgMsgStatus.setVisibility(View.VISIBLE);
                break;
            case sending:
                imgMsgStatus.setImageResource(R.mipmap.nim_recent_contact_ic_sending);
                imgMsgStatus.setVisibility(View.VISIBLE);
                break;
            default:
                imgMsgStatus.setVisibility(View.GONE);
                break;
        }

        String timeString = TimeUtil.getTimeShowString(recent.getTime(), true);
        tvDatetime.setText(timeString);
        if (!TextUtils.isEmpty(timeString) && timeString.equals("1970-01-01")) {
            tvDatetime.setVisibility(View.GONE);
        } else {
            tvDatetime.setVisibility(View.VISIBLE);
        }
    }

    protected void updateNickLabel(String nick) {
        tvNickname.setText(nick);
    }

    protected int getResId() {
        return R.layout.nim_recent_contact_list_item;
    }

    public void inflate() {
        this.portraitPanel = (FrameLayout) view.findViewById(R.id.portrait_panel);
        this.imgHead = (HeadImageView) view.findViewById(R.id.img_head);
        this.tvNickname = (TextView) view.findViewById(R.id.tv_nickname);
        this.tvMessage = (EmotinText) view.findViewById(R.id.tv_message);
        this.tvUnread = (TextView) view.findViewById(R.id.unread_number_tip);
        this.unreadIndicator = view.findViewById(R.id.new_message_indicator);
        this.tvDatetime = (TextView) view.findViewById(R.id.tv_date_time);
        this.imgMsgStatus = (ImageView) view.findViewById(R.id.img_msg_status);
        this.bottomLine = view.findViewById(R.id.bottom_line);
        this.topLine = view.findViewById(R.id.top_line);
        this.unread_less = (RelativeLayout) view.findViewById(R.id.unread_less_lay);
        this.unread_more = (ImageView) view.findViewById(R.id.unread_more);
    }

    protected String unreadCountShowRule(int unread) {
        unread = Math.min(unread, 99);
        return String.valueOf(unread);
    }

    protected RecentContactsCallback getCallback() {
        return ((RecentContactAdapter) getAdapter()).getCallback();
    }

    @Override
    public void onClick(View v) {

    }
}
