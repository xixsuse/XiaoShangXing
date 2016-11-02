package com.xiaoshangxing.xiaoshang.Reward.PersonalReward;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.netUtil.OperateUtils;
import com.xiaoshangxing.Network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.layout.Name;
import com.xiaoshangxing.xiaoshang.Reward.RewardActivity;
import com.xiaoshangxing.xiaoshang.Reward.RewardDetail.RewardDetailActivity;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import java.util.List;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 */
public class PersonalReward_adpter extends ArrayAdapter<Published> {
    private Context context;
    List<Published> publisheds;
    private PersonalRewardFragment fragment;
    private boolean showselect;
    private RewardActivity activity;

    public PersonalReward_adpter(Context context, int resource, List<Published> objects,
                                 PersonalRewardFragment fragment, RewardActivity activity) {
        super(context, resource, objects);
        this.context = context;
        this.publisheds = objects;
        this.fragment = fragment;
        this.activity=activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final mystate_viewholder viewholder;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_myshoolreward_listview, null);
            viewholder = new mystate_viewholder();
            viewholder.headImage = (CirecleImage) convertView.findViewById(R.id.head_image);
            viewholder.name = (Name) convertView.findViewById(R.id.name);
            viewholder.college = (TextView) convertView.findViewById(R.id.college);
            viewholder.time = (TextView) convertView.findViewById(R.id.time);
            viewholder.text = (EmotinText) convertView.findViewById(R.id.text);
            viewholder.price = (TextView) convertView.findViewById(R.id.price);
            viewholder.iscomplete = (CheckBox) convertView.findViewById(R.id.iscomplete);
            viewholder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            viewholder.parentLay = (LinearLayout) convertView.findViewById(R.id.parent_lay);
            convertView.setTag(viewholder);

        } else {
            viewholder = (mystate_viewholder) convertView.getTag();
        }

        final Published published = publisheds.get(position);

        //完成状态
        viewholder.iscomplete.setChecked(published.isAlive());
        viewholder.iscomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleCallBack simpleCallBack = new SimpleCallBack() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        viewholder.iscomplete.setChecked(published.isAlive());
                    }

                    @Override
                    public void onBackData(Object o) {
                        Published published1 = (Published) o;
                        viewholder.iscomplete.setChecked((published1.isAlive()));
                    }
                };
                OperateUtils.ChangeStatu(published.getId(), published.getStatus(), context, true, simpleCallBack);
            }
        });

        viewholder.checkBox.setChecked(false);
        if (showselect) {
            viewholder.iscomplete.setVisibility(View.INVISIBLE);
            viewholder.checkBox.setVisibility(View.VISIBLE);
        } else {
            viewholder.iscomplete.setVisibility(View.VISIBLE);
            viewholder.checkBox.setVisibility(View.GONE);
        }

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showMenu(v, viewholder, published.getId());
                viewholder.parentLay.setBackground(context.getResources()
                        .getDrawable(R.drawable.circular_8_g1_nostroke));
                return true;
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showselect) {
                    viewholder.checkBox.performClick();
                } else {
                    Intent intent = new Intent(context, RewardDetailActivity.class);
                    intent.putExtra(IntentStatic.DATA, published.getId());
                    context.startActivity(intent);
                }
            }
        });


        viewholder.checkBox.setChecked(false);

        String userId = String.valueOf(published.getUserId());
        UserInfoCache.getInstance().getHeadIntoImage(userId, viewholder.headImage);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.USER_NAME, viewholder.name);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.COLLEGE, viewholder.college);
        viewholder.time.setText(TimeUtil.getTimeShowString(published.getCreateTime(), false));
        viewholder.text.setText(published.getText());
        viewholder.price.setText(NS.RMB + published.getPrice());
        viewholder.headImage.setIntent_type(CirecleImage.PERSON_INFO, userId);
        return convertView;
    }


    private static class mystate_viewholder {
        private CirecleImage headImage;
        private TextView college, time, price;
        private Name name;
        private EmotinText text;
        private CheckBox checkBox, iscomplete;
        private LinearLayout parentLay;
    }

    private void showMenu(View v, final mystate_viewholder viewholder, final int publishId) {
        int[] xy = new int[2];
        v.getLocationInWindow(xy);
        View menu;
        if (xy[1] <= context.getResources().getDimensionPixelSize(R.dimen.y300)) {
            menu = View.inflate(getContext(), R.layout.popup_myhelp_up, null);
        } else {
            menu = View.inflate(getContext(), R.layout.popup_myhelp_bottom, null);
        }

        final PopupWindow popupWindow = new PopupWindow(menu, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(getContext().getResources().
                getDrawable(R.drawable.nothing));
        popupWindow.setAnimationStyle(R.style.popwindow_anim);

        menu.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int mShowMorePopupWindowWidth = menu.getMeasuredWidth();
        int mShowMorePopupWindowHeight = menu.getMeasuredHeight();
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);

        if (xy[1] <= context.getResources().getDimensionPixelSize(R.dimen.y300)) {
            popupWindow.showAsDropDown(v,
                    -mShowMorePopupWindowWidth / 2 + v.getWidth() / 2,
                    0);
        } else {
            popupWindow.showAsDropDown(v,
                    -mShowMorePopupWindowWidth / 2 + v.getWidth() / 2,
                    -mShowMorePopupWindowHeight - v.getHeight());
        }

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                viewholder.parentLay.setBackground(context.getResources()
                        .getDrawable(R.drawable.et_circular_8_w0));

            }
        });

        final TextView transmit = (TextView) menu.findViewById(R.id.transmit);
        TextView delete = (TextView) menu.findViewById(R.id.delete);
        TextView more = (TextView) menu.findViewById(R.id.more);

        transmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setTransmitedId(publishId);
                activity.gotoSelectPerson();
                popupWindow.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                fragment.showDeleteSureDialog(publishId);
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.showHideMenu(true);
                showSelectCircle(true);
                popupWindow.dismiss();
            }
        });

    }

    public void showSelectCircle(boolean is) {
        showselect = is;
        notifyDataSetChanged();
    }
}
