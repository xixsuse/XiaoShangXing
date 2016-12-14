package com.xiaoshangxing.xiaoshang.Reward.RewardFragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.data.bean.Published;
import com.xiaoshangxing.utils.AppContracts;
import com.xiaoshangxing.utils.customView.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.customView.CirecleImage;
import com.xiaoshangxing.utils.customView.LayoutHelp;
import com.xiaoshangxing.utils.customView.Name;
import com.xiaoshangxing.xiaoshang.Reward.RewardActivity;
import com.xiaoshangxing.xiaoshang.Reward.RewardDetail.RewardDetailActivity;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import java.util.List;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 */
public class Reward_adpter_realm extends RealmBaseAdapter<Published> {
    List<Published> publisheds;
    private Context context;
    private RewardFragment fragment;
    private RewardActivity activity;

    public Reward_adpter_realm(Context context, RealmResults<Published> objects,
                               RewardFragment fragment, RewardActivity activity) {
        super(context, objects);
        this.context = context;
        this.publisheds = objects;
        this.fragment = fragment;
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final mystate_viewholder viewholder;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shoolreward_listview, null);
            viewholder = new mystate_viewholder();
            viewholder.headImage = (CirecleImage) convertView.findViewById(R.id.head_image);
            viewholder.name = (Name) convertView.findViewById(R.id.name);
            viewholder.college = (TextView) convertView.findViewById(R.id.college);
            viewholder.time = (TextView) convertView.findViewById(R.id.time);
            viewholder.text = (EmotinText) convertView.findViewById(R.id.text);
            viewholder.price = (TextView) convertView.findViewById(R.id.price);
            viewholder.button = (ImageView) convertView.findViewById(R.id.button);
            viewholder.down_arrow = (ImageView) convertView.findViewById(R.id.down_arrow);
            viewholder.finish = (ImageView) convertView.findViewById(R.id.finish);
            convertView.setTag(viewholder);

        } else {
            viewholder = (mystate_viewholder) convertView.getTag();
        }

        viewholder.button.setVisibility(View.GONE);

        final Published published = publisheds.get(position);

        String userId = String.valueOf(published.getUserId());
        UserInfoCache.getInstance().getHeadIntoImage(userId, viewholder.headImage);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.USER_NAME, viewholder.name);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.COLLEGE, viewholder.college);
        viewholder.time.setText(TimeUtil.getSimplePublishedTime(published.getCreateTime()));
        viewholder.text.setText(published.getText());
        viewholder.price.setText(AppContracts.RMB + published.getPrice());
        viewholder.headImage.setIntent_type(CirecleImage.PERSON_INFO, userId);

        viewholder.down_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.showCollectDialog(published.getId(), published.isCollected());
            }
        });

//        viewholder.button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (fragment.isRefreshing()) {
//                    return;
//                }
//
//                View menu = View.inflate(context, R.layout.shoolfellow_popupmenu, null);
//                final PopupWindow popupWindow = new PopupWindow(menu, ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT);
//                popupWindow.setFocusable(true);
//                popupWindow.setBackgroundDrawable(context.getResources().
//                        getDrawable(R.drawable.circular_12_b3));
//                popupWindow.setAnimationStyle(R.style.popwindow_anim);
//
//                menu.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//                int mShowMorePopupWindowWidth = menu.getMeasuredWidth();
//                int mShowMorePopupWindowHeight = menu.getMeasuredHeight();
//                int heightMoreBtnView = v.getHeight();
//
//                popupWindow.setOutsideTouchable(true);
//                popupWindow.setTouchable(true);
//
//                popupWindow.showAsDropDown(v, -mShowMorePopupWindowWidth - context.getResources().getDimensionPixelSize(R.dimen.x6),
//                        -(mShowMorePopupWindowHeight + heightMoreBtnView) / 2);
//
//                View transmit = menu.findViewById(R.id.transmit);
//                View comment = menu.findViewById(R.id.comment);
//                View praise = menu.findViewById(R.id.praise);
//                final TextView praiseOrCancle = (TextView) menu.findViewById(R.id.praiseOrCancel);
//                praiseOrCancle.setText(Published_Help.isPraised(published) ? "取消" : "赞");
//
//                transmit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        activity.setTransmitedId(published.getId());
//                        activity.gotoSelectPerson();
//                        popupWindow.dismiss();
//                        popupWindow.dismiss();
//                    }
//                });
//
//                comment.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(context, InputActivity.class);
//                        intent.putExtra(InputActivity.EDIT_STATE, InputActivity.COMMENT);
//                        intent.putExtra(InputActivity.MOMENTID, published.getId());
//                        context.startActivity(intent);
//                        popupWindow.dismiss();
//                    }
//                });
//
//                praise.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        OperateUtils.operateWithLoad(published.getId(), context, true, NS.PRAISE, Published_Help.isPraised(published),
//                                fragment, new SimpleCallBack() {
//                                    @Override
//                                    public void onSuccess() {
//                                        if (praiseOrCancle.getText().equals("赞")) {
//                                            praiseOrCancle.setText("取消");
//                                            Toast.makeText(context, "赞", Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            praiseOrCancle.setText("赞");
//                                            Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable e) {
//                                    }
//
//                                    @Override
//                                    public void onBackData(Object o) {
//
//                                    }
//                                });
//                        popupWindow.dismiss();
//                    }
//                });
//            }
//        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutHelp.PermissionClick(activity, new LayoutHelp.PermisionMethod() {
                    @Override
                    public void doSomething() {
                        Intent intent = new Intent(context, RewardDetailActivity.class);
                        intent.putExtra(IntentStatic.DATA, published.getId());
                        context.startActivity(intent);
                    }
                });
            }
        });


        if (!published.isAlive()) {
            viewholder.finish.setVisibility(View.VISIBLE);
            viewholder.price.setTextColor(context.getResources().getColor(R.color.g0));
        } else {
            viewholder.finish.setVisibility(View.GONE);
            viewholder.price.setTextColor(context.getResources().getColor(R.color.red1));
        }

        return convertView;
    }

    private static class mystate_viewholder {
        private CirecleImage headImage;
        private TextView college, time, price;
        private EmotinText text;
        private Name name;
        private ImageView button, down_arrow, finish;
    }
}
