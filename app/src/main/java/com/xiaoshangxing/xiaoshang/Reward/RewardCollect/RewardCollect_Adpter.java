package com.xiaoshangxing.xiaoshang.Reward.RewardCollect;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.data.bean.Published;
import com.xiaoshangxing.utils.customView.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.customView.CirecleImage;
import com.xiaoshangxing.utils.customView.Name;
import com.xiaoshangxing.xiaoshang.Reward.RewardActivity;
import com.xiaoshangxing.xiaoshang.Reward.RewardDetail.RewardDetailActivity;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import java.util.List;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 */
public class RewardCollect_Adpter extends ArrayAdapter<Published> {
    List<Published> publisheds;
    private Context context;
    private RewardCollectFragment fragment;
    private boolean showselect;
    private RewardActivity activity;


    public RewardCollect_Adpter(Context context, int resource, List<Published> objects,
                                RewardCollectFragment fragment, RewardActivity activity) {
        super(context, resource, objects);
        this.context = context;
        this.publisheds = objects;
        this.fragment = fragment;
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final mystate_viewholder viewholder;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shoolreward_collect_listview, null);
            viewholder = new mystate_viewholder();
            viewholder.headImage = (CirecleImage) convertView.findViewById(R.id.head_image);
            viewholder.name = (Name) convertView.findViewById(R.id.name);
            viewholder.college = (TextView) convertView.findViewById(R.id.college);
            viewholder.time = (TextView) convertView.findViewById(R.id.time);
            viewholder.text = (EmotinText) convertView.findViewById(R.id.text);
            viewholder.price = (TextView) convertView.findViewById(R.id.price);
            viewholder.button = (ImageView) convertView.findViewById(R.id.button);
            viewholder.down_arrow = (ImageView) convertView.findViewById(R.id.down_arrow);
            viewholder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            viewholder.parentLay = (LinearLayout) convertView.findViewById(R.id.parent_lay);
            viewholder.finish = (ImageView) convertView.findViewById(R.id.finish);
            convertView.setTag(viewholder);

        } else {
            viewholder = (mystate_viewholder) convertView.getTag();
        }

        final Published published = publisheds.get(position);
        String id = String.valueOf(published.getUserId());
        UserInfoCache.getInstance().getHeadIntoImage(id, viewholder.headImage);
        UserInfoCache.getInstance().getExIntoTextview(id, NS.USER_NAME, viewholder.name);
        UserInfoCache.getInstance().getExIntoTextview(id, NS.COLLEGE, viewholder.college);
        viewholder.time.setText(TimeUtil.getSimplePublishedTime(published.getCreateTime()));
        viewholder.text.setText(published.getText());
        viewholder.price.setText(NS.RMB + published.getPrice());
        viewholder.headImage.setIntent_type(CirecleImage.PERSON_INFO, id);

        if (showselect) {
            viewholder.checkBox.setVisibility(View.VISIBLE);
        } else {
            viewholder.checkBox.setVisibility(View.GONE);
        }

        viewholder.down_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.showCollectDialog(published.getId());
            }
        });

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
                Intent intent = new Intent(context, RewardDetailActivity.class);
                intent.putExtra(IntentStatic.DATA, published.getId());
                context.startActivity(intent);
            }
        });

        viewholder.checkBox.setChecked(false);

        if (!published.isAlive()) {
            viewholder.finish.setVisibility(View.VISIBLE);
            viewholder.price.setTextColor(context.getResources().getColor(R.color.g0));
        } else {
            viewholder.finish.setVisibility(View.GONE);
            viewholder.price.setTextColor(context.getResources().getColor(R.color.red1));
        }

        return convertView;
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

    private static class mystate_viewholder {
        private CirecleImage headImage;
        private TextView college, time, price;
        private Name name;
        private EmotinText text;
        private CheckBox checkBox;
        private ImageView button, down_arrow, finish;
        private LinearLayout parentLay;
    }

}
