package com.xiaoshangxing.xiaoshang.Plan.PersonalPlan;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.network.netUtil.OperateUtils;
import com.xiaoshangxing.network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.data.bean.Published;
import com.xiaoshangxing.utils.customView.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.customView.CirecleImage;
import com.xiaoshangxing.utils.customView.Name;
import com.xiaoshangxing.xiaoshang.Plan.PlanActivity;
import com.xiaoshangxing.xiaoshang.Plan.PlanDetail.PlanDetailActivity;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 */
public class PersonalPlan_Adpter extends ArrayAdapter<Published> {
    protected Handler handler;
    List<Published> publisheds;
    private Context context;
    private PersonalPlanFragment fragment;
    private PlanActivity activity;
    private boolean showselect;
    private List<String> selectIds = new ArrayList<>();

    public PersonalPlan_Adpter(Context context, int resource, List<Published> objects,
                               PersonalPlanFragment fragment, PlanActivity activity) {
        super(context, resource, objects);
        this.context = context;
        this.publisheds = objects;
        this.fragment = fragment;
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_personalplan_adapter, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Published published = publisheds.get(position);

        String userId = String.valueOf(published.getUserId());
        UserInfoCache.getInstance().getHeadIntoImage(userId, viewHolder.headImage);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.USER_NAME, viewHolder.name);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.COLLEGE, viewHolder.college);
        viewHolder.time.setText(TimeUtil.getSimplePublishedTime(published.getCreateTime()));
        viewHolder.text.setText(published.getText());
        viewHolder.planName.setText(published.getPlanName());
        viewHolder.headImage.setIntent_type(CirecleImage.PERSON_INFO, userId);

        if (TextUtils.isEmpty(published.getPersonLimit())) {
            viewHolder.peopleLimit.setText("不限人数");
        } else {
            viewHolder.peopleLimit.setText((published.getJoinCount() + "-" + published.getPersonLimit()));
        }
        viewHolder.joinedCount.setText("" + published.getJoinCount());

        if (!TextUtils.isEmpty(published.getPersonLimit())) {
            if (published.getPraiseCount() == Integer.valueOf(published.getPersonLimit())) {
                viewHolder.full.setVisibility(View.VISIBLE);
            } else {
                viewHolder.full.setVisibility(View.INVISIBLE);
            }
        } else {
            viewHolder.full.setVisibility(View.INVISIBLE);
        }

        //完成状态
        viewHolder.complete.setVisibility(published.isAlive() ? View.GONE : View.VISIBLE);
        viewHolder.iscomplete.setChecked(published.isAlive());
        viewHolder.iscomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleCallBack simpleCallBack = new SimpleCallBack() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        viewHolder.iscomplete.setChecked(published.isAlive());
                    }

                    @Override
                    public void onBackData(Object o) {
                        Published published1 = (Published) o;
                        viewHolder.iscomplete.setChecked((published1.isAlive()));
                    }
                };
                OperateUtils.ChangeStatu(published.getId(), published.getStatus(), context, true, fragment, simpleCallBack);
            }
        });

        if (showselect) {
            viewHolder.iscomplete.setVisibility(View.INVISIBLE);
            viewHolder.checkbox.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iscomplete.setVisibility(View.VISIBLE);
            viewHolder.checkbox.setVisibility(View.GONE);
        }

        final String publishId = String.valueOf(published.getId());

        if (selectIds.contains(publishId)) {
            viewHolder.checkbox.setChecked(true);
        } else {
            viewHolder.checkbox.setChecked(false);
        }

        viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!selectIds.contains(publishId)) {
                        selectIds.add(publishId);
                    }
                } else {
                    selectIds.remove(publishId);
                }
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showMenu(viewHolder.upLay, position);
                viewHolder.upLay.setBackgroundColor(context.getResources().getColor(R.color.g1));
                return true;
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showselect) {
                    viewHolder.checkbox.performClick();
                } else {
                    Intent intent = new Intent(context, PlanDetailActivity.class);
                    intent.putExtra(IntentStatic.DATA, published.getId());
                    context.startActivity(intent);
                }
            }
        });

        return convertView;
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
                view.setBackgroundResource(R.drawable.et_circular_4_white);
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

    public List<String> getSelectIds() {
        return selectIds;
    }

    public void setSelectIds(List<String> selectIds) {
        this.selectIds = selectIds;
    }


    static class ViewHolder {
        @Bind(R.id.name)
        Name name;
        @Bind(R.id.college)
        TextView college;
        @Bind(R.id.down_arrow)
        ImageView downArrow;
        @Bind(R.id.complete)
        ImageView complete;
        @Bind(R.id.plan_name)
        TextView planName;
        @Bind(R.id.text)
        EmotinText text;
        @Bind(R.id.iscomplete)
        CheckBox iscomplete;
        @Bind(R.id.checkbox)
        CheckBox checkbox;
        @Bind(R.id.up_lay)
        LinearLayout upLay;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.people_limit)
        TextView peopleLimit;
        @Bind(R.id.joined_count)
        TextView joinedCount;
        @Bind(R.id.full)
        TextView full;
        @Bind(R.id.head_image)
        CirecleImage headImage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
