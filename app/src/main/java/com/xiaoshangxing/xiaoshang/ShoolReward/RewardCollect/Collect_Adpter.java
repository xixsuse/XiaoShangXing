package com.xiaoshangxing.xiaoshang.ShoolReward.RewardCollect;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.layout.Name;
import com.xiaoshangxing.xiaoshang.ShoolReward.RewardDetail.RewardDetailActivity;
import com.xiaoshangxing.xiaoshang.ShoolReward.ShoolRewardActivity;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import java.util.List;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 */
public class Collect_Adpter extends ArrayAdapter<Published> {
    private Context context;
    List<Published> publisheds;
    private CollectFragment fragment;
    private boolean showselect;
    private ShoolRewardActivity activity;


    public Collect_Adpter(Context context, int resource, List<Published> objects,
                          CollectFragment fragment, ShoolRewardActivity activity) {
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
            viewholder.cardview = (CardView) convertView.findViewById(R.id.cardview);
            convertView.setTag(viewholder);

        } else {
            viewholder = (mystate_viewholder) convertView.getTag();
        }

        final Published published = publisheds.get(position);
        String id = String.valueOf(published.getUserId());
        UserInfoCache.getInstance().getHeadIntoImage(id, viewholder.headImage);
        UserInfoCache.getInstance().getExIntoTextview(id, NS.USER_NAME, viewholder.name);
        UserInfoCache.getInstance().getExIntoTextview(id, NS.COLLEGE, viewholder.college);
//        UserInfoCache.getInstance().getHead(viewholder.headImage, published.getUserId(), context);
//        UserInfoCache.getInstance().getName(viewholder.name, published.getUserId());
//        UserInfoCache.getInstance().getCollege(viewholder.college, published.getUserId());
        viewholder.time.setText(TimeUtil.getTimeShowString(published.getCreateTime(), false));
        viewholder.text.setText(published.getText());
        viewholder.price.setText(NS.RMB + published.getPrice());

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
                viewholder.cardview.setBackground(context.getResources()
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

        return convertView;
    }


    private static class mystate_viewholder {
        private CirecleImage headImage;
        private TextView college, time, price;
        private Name name;
        private EmotinText text;
        private CheckBox checkBox;
        private ImageView button, down_arrow;
        private CardView cardview;
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
                viewholder.cardview.setBackground(context.getResources()
                        .getDrawable(R.drawable.circular_8_w0_nostroke));
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
