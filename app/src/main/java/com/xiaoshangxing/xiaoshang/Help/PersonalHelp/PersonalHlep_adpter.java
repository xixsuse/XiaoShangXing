package com.xiaoshangxing.xiaoshang.Help.PersonalHelp;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.publicActivity.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.utils.customView.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.customView.CirecleImage;
import com.xiaoshangxing.utils.customView.Name;
import com.xiaoshangxing.xiaoshang.Help.HelpActivity;
import com.xiaoshangxing.xiaoshang.Reward.RewardDetail.RewardDetailActivity;

import java.util.List;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 */
public class PersonalHlep_adpter extends ArrayAdapter<String> {
    List<String> strings;
    private Context context;
    private int resource;
    private PersonalHelpFragment fragment;
    private boolean showselect;
    private HelpActivity activity;


    public PersonalHlep_adpter(Context context, int resource, List<String> objects,
                               PersonalHelpFragment fragment, HelpActivity activity) {
        super(context, resource, objects);
        this.context = context;
        this.strings = objects;
        this.resource = resource;
        this.fragment = fragment;
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final mystate_viewholder viewholder;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_myshoolfellowhelp_listview, null);
            viewholder = new mystate_viewholder();
            viewholder.headImage = (CirecleImage) convertView.findViewById(R.id.head_image);
            viewholder.name = (Name) convertView.findViewById(R.id.name);
            viewholder.college = (TextView) convertView.findViewById(R.id.college);
            viewholder.time = (TextView) convertView.findViewById(R.id.time);
            viewholder.text = (EmotinText) convertView.findViewById(R.id.text);
            viewholder.iscomplete = (CheckBox) convertView.findViewById(R.id.iscomplete);
            viewholder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(viewholder);

        } else {
            viewholder = (mystate_viewholder) convertView.getTag();
        }

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
                showMenu(v, -1);
                v.setBackgroundColor(context.getResources().getColor(R.color.g1));
                return true;
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RewardDetailActivity.class);
                context.startActivity(intent);
            }
        });

        viewholder.checkBox.setChecked(false);

        return convertView;
    }

    private void showMenu(View v, final int id) {

        final View view = v;
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
                view.setAlpha(1);
                view.setBackgroundColor(context.getResources().getColor(R.color.w0));
            }
        });

        final TextView transmit = (TextView) menu.findViewById(R.id.transmit);
        TextView delete = (TextView) menu.findViewById(R.id.delete);
        TextView more = (TextView) menu.findViewById(R.id.more);

        transmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SelectPersonActivity.class);
                activity.startActivityForResult(intent, SelectPersonActivity.SELECT_PERSON_CODE);
                popupWindow.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                fragment.showDeleteSureDialog(id);
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
        private TextView college, time;
        private Name name;
        private EmotinText text;
        private CheckBox checkBox, iscomplete;
    }

}
