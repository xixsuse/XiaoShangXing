package com.xiaoshangxing.xiaoshang.Help.PersonalHelp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import com.xiaoshangxing.xiaoshang.Help.HelpDetail.HelpDetailActivity;
import com.xiaoshangxing.xiaoshang.Help.HelpActivity;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;

/**
 * Created by FengChaoQun
 * on 2016/9/10
 */
public class PersonalHelpAdapter extends RealmBaseAdapter<Published> {
    private Context context;
    List<Published> publisheds;
    private PersonalHelpFragment fragment;
    private boolean showselect;
    private HelpActivity activity;
    Realm realm;

    public PersonalHelpAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Published> data,
                               PersonalHelpFragment fragment, Realm realm, HelpActivity activity) {
        super(context, data);
        this.publisheds = data;
        this.fragment = fragment;
        this.realm = realm;
        this.context = context;
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

        final Published published = publisheds.get(position);

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
                showMenu(v, position);
                v.setBackgroundColor(context.getResources().getColor(R.color.g1));
                return true;
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showselect) {
                    viewholder.checkBox.performClick();
                } else {
                    Intent intent = new Intent(context, HelpDetailActivity.class);
                    intent.putExtra(IntentStatic.DATA, published.getId());
                    context.startActivity(intent);
                }
            }
        });
        String userId = String.valueOf(published.getUserId());
        UserInfoCache.getInstance().getHeadIntoImage(userId, viewholder.headImage);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.USER_NAME, viewholder.name);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.COLLEGE, viewholder.college);
        viewholder.time.setText(TimeUtil.getTimeShowString(published.getCreateTime(), false));
        viewholder.text.setText(published.getText());

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

        return convertView;
    }


    private static class mystate_viewholder {
        private CirecleImage headImage;
        private TextView college, time;
        private Name name;
        private EmotinText text;
        private CheckBox checkBox, iscomplete;
    }

    private void showMenu(View v, final int position) {

        final View view = v;
        int[] xy = new int[2];
        v.getLocationInWindow(xy);
        View menu;
        if (xy[1] <= context.getResources().getDimensionPixelSize(R.dimen.y300)) {
            menu = View.inflate(context, R.layout.popup_myhelp_up, null);
        } else {
            menu = View.inflate(context, R.layout.popup_myhelp_bottom, null);
        }

        final PopupWindow popupWindow = new PopupWindow(menu, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(context.getResources().
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
                activity.setTransmitedId(publisheds.get(position).getId());
                activity.gotoSelectPerson();
//                Intent intent = new Intent(context, SelectPersonActivity.class);
//                intent.putExtra(SelectPersonActivity.LIMIT, 1);

//                activity.startActivityForResult(intent, SelectPersonActivity.SELECT_PERSON_CODE);
                popupWindow.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                fragment.showDeleteSureDialog(publisheds.get(position).getId());
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
