package com.xiaoshangxing.xiaoshang.Plan;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.network.netUtil.OperateUtils;
import com.xiaoshangxing.network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.publicActivity.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.data.bean.Published;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.utils.baseClass.BaseFragment;
import com.xiaoshangxing.utils.baseClass.IBaseView;
import com.xiaoshangxing.utils.customView.CirecleImage;
import com.xiaoshangxing.utils.customView.dialog.DialogLocationAndSize;
import com.xiaoshangxing.utils.customView.dialog.DialogUtils;
import com.xiaoshangxing.utils.imageUtils.MyGlide;
import com.xiaoshangxing.xiaoshang.Plan.JoinedPlan.JoinedPlanFragment;
import com.xiaoshangxing.xiaoshang.Plan.PersonalPlan.PersonalPlanFragment;
import com.xiaoshangxing.xiaoshang.Plan.PlanFragment.PlanFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FengChaoQun
 * on 2016/9/29
 */
public class PlanActivity extends BaseActivity implements IBaseView {
    public static final String TAG = BaseActivity.TAG + "-PlanActivity";
    private boolean isHideMenu;//记录是否需要点击返回键隐藏菜单
    private int transmitedId = -1;

    private PlanFragment planFragment;
    private JoinedPlanFragment joinedPlanFragment;
    private PersonalPlanFragment personalPlanFragment;

    private String account;
    private List<String> publishIdsForTransmit = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_fraglayout);
        BaseFragment frag = (BaseFragment) mFragmentManager.findFragmentById(R.id.main_fragment);
        if (frag != null) {
            return;
        }
        parseIntent();
        initAllFrafments();
    }

    private void parseIntent() {
        Fragment frag;
        switch (getIntent().getIntExtra(IntentStatic.TYPE, 0)) {
            case IntentStatic.OTHERS:
                frag = mFragmentManager.findFragmentByTag(PlanFragment.TAG);
                planFragment = (frag == null) ? PlanFragment.newInstance() : (PlanFragment) frag;
                mFragmentManager.beginTransaction().add(R.id.main_fragment,
                        planFragment, PlanFragment.TAG).commit();
                account = getIntent().getStringExtra(IntentStatic.EXTRA_ACCOUNT);
                break;
            case IntentStatic.MINE:
                frag = mFragmentManager.findFragmentByTag(PersonalPlanFragment.TAG);
                personalPlanFragment = (frag == null) ? PersonalPlanFragment.newInstance() : (PersonalPlanFragment) frag;
                mFragmentManager.beginTransaction().add(R.id.main_fragment,
                        personalPlanFragment, PersonalPlanFragment.TAG).commit();
                break;
            default:
                initAllFrafments();
        }
    }

    private void initAllFrafments() {
        Fragment frag;

        frag = mFragmentManager.findFragmentByTag(PlanFragment.TAG);
        planFragment = (frag == null) ? PlanFragment.newInstance() : (PlanFragment) frag;

        frag = mFragmentManager.findFragmentByTag(JoinedPlanFragment.TAG);
        joinedPlanFragment = (frag == null) ? JoinedPlanFragment.newInstance() : (JoinedPlanFragment) frag;

        frag = mFragmentManager.findFragmentByTag(PersonalPlanFragment.TAG);
        personalPlanFragment = (frag == null) ? PersonalPlanFragment.newInstance() : (PersonalPlanFragment) frag;

        mFragmentManager.beginTransaction().add(R.id.main_fragment,
                joinedPlanFragment, JoinedPlanFragment.TAG).commit();

        mFragmentManager.beginTransaction().add(R.id.main_fragment,
                personalPlanFragment, PersonalPlanFragment.TAG).commit();

        mFragmentManager.beginTransaction().add(R.id.main_fragment,
                planFragment, PlanFragment.TAG).commit();

        mFragmentManager.beginTransaction()
                .hide(joinedPlanFragment)
                .hide(personalPlanFragment)
                .show(planFragment).commit();
    }

    public boolean isHideMenu() {
        return isHideMenu;
    }

    public void setHideMenu(boolean hideMenu) {
        isHideMenu = hideMenu;
    }

    public void gotoSelectPerson() {
        Intent intent = new Intent(this, SelectPersonActivity.class);
        startActivityForResult(intent, SelectPersonActivity.SELECT_PERSON_CODE);
    }

    public void gotoSelectOnePerson() {
        Intent intent = new Intent(this, SelectPersonActivity.class);
        intent.putExtra(SelectPersonActivity.LIMIT, 1);
        startActivityForResult(intent, SelectPersonActivity.SELECT_PERSON_CODE_2);
    }

    public int getTransmitedId() {
        return transmitedId;
    }

    public void setTransmitedId(int transmitedId) {
        this.transmitedId = transmitedId;
    }

    public void showTransmitDialog(final List<String> id) {
        final Dialog dialog = new Dialog(this, R.style.ActionSheetDialog);
        View dialogView = View.inflate(this, R.layout.school_sale_transmit_dialog, null);
        dialog.setContentView(dialogView);

        TextView cancle = (TextView) dialogView.findViewById(R.id.cancel);
        TextView send = (TextView) dialogView.findViewById(R.id.send);
        CirecleImage head = (CirecleImage) dialogView.findViewById(R.id.head_image);
        TextView name = (TextView) dialogView.findViewById(R.id.name);
        TextView college = (TextView) dialogView.findViewById(R.id.college);
        TextView text = (TextView) dialogView.findViewById(R.id.text);
        ImageView imageView = (ImageView) dialogView.findViewById(R.id.image);
        final EditText input = (EditText) dialogView.findViewById(R.id.input);

        Published published = realm.where(Published.class).equalTo(NS.ID, transmitedId).findFirst();
        if (published == null) {
            showToast("信息有误");
            return;
        }
        String userId = String.valueOf(published.getUserId());
        UserInfoCache.getInstance().getHeadIntoImage(userId, head);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.USER_NAME, name);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.COLLEGE, college);
        text.setText(published.getText());
        if (!TextUtils.isEmpty(published.getImage())) {
            MyGlide.with(PlanActivity.this, published.getImage().split(NS.SPLIT)[0], imageView);
        }

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OperateUtils.Tranmit(transmitedId, NS.CATEGORY_PLAN, id, PlanActivity.this, input.getText().toString(),
                        new SimpleCallBack() {
                            @Override
                            public void onSuccess() {
                                showTransmitSuccess();
                                setTransmitedId(-1);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onBackData(Object o) {

                            }
                        });
                dialog.dismiss();
            }
        });
        dialog.show();
        DialogLocationAndSize.setWidth(dialog, R.dimen.x900);
    }

    /**
     * description:转发多天动态给一个好友
     *
     * @param userId 好友id
     * @return
     */

    public void showTransmitDialog2(final String userId) {

        if (publishIdsForTransmit.isEmpty()) {
            return;
        }

        final Dialog dialog = new Dialog(this, R.style.ActionSheetDialog);
        View dialogView = View.inflate(this, R.layout.transmit_dialog_2, null);
        dialog.setContentView(dialogView);

        TextView cancle = (TextView) dialogView.findViewById(R.id.cancel);
        TextView send = (TextView) dialogView.findViewById(R.id.send);
        CirecleImage head = (CirecleImage) dialogView.findViewById(R.id.head_image);
        TextView name = (TextView) dialogView.findViewById(R.id.name);
        TextView count = (TextView) dialogView.findViewById(R.id.count);
        final EditText input = (EditText) dialogView.findViewById(R.id.input);

        UserInfoCache.getInstance().getHeadIntoImage(userId, head);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.USER_NAME, name);
        count.setText("[逐条转发]共" + publishIdsForTransmit.size() + "条信息");
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OperateUtils.TranmitMoreToOne(publishIdsForTransmit, NS.CATEGORY_PLAN, userId, PlanActivity.this, input.getText().toString(),
                        new SimpleCallBack() {
                            @Override
                            public void onSuccess() {
                                showTransmitSuccess();
                                publishIdsForTransmit.clear();
                            }

                            @Override
                            public void onError(Throwable e) {
                                publishIdsForTransmit.clear();
                            }

                            @Override
                            public void onBackData(Object o) {

                            }
                        });
                dialog.dismiss();
            }
        });
        dialog.show();
        DialogLocationAndSize.setWidth(dialog, R.dimen.x900);
    }

    public void showTransmitSuccess() {
        DialogUtils.Dialog_No_Button dialog_no_button =
                new DialogUtils.Dialog_No_Button(PlanActivity.this, "已分享");
        final Dialog notice_dialog = dialog_no_button.create();
        notice_dialog.show();
        DialogLocationAndSize.setWidth(notice_dialog, R.dimen.x420);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notice_dialog.dismiss();
            }
        }, 500);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isHideMenu) {
                personalPlanFragment.showHideMenu(false);
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    public PlanFragment getPlanFragment() {
        return planFragment;
    }

    public JoinedPlanFragment getJoinedPlanFragment() {
        return joinedPlanFragment;
    }

    public PersonalPlanFragment getPersonalPlanFragment() {
        return personalPlanFragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SelectPersonActivity.SELECT_PERSON_CODE) {
            if (data != null) {
                if (data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON).size() > 0) {
                    showTransmitDialog(data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON));
                } else {
                    showToast("未选择联系人");
                }
            }
        } else if (requestCode == SelectPersonActivity.SELECT_PERSON_CODE_2) {
            if (data != null) {
                if (data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON).size() == 1) {
                    showTransmitDialog2(data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON).get(0));
                } else {
                    showToast("未选择联系人");
                }
            }
        }
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }

    public List<String> getPublishIdsForTransmit() {
        return publishIdsForTransmit;
    }

    public void setPublishIdsForTransmit(List<String> publishIdsForTransmit) {
        this.publishIdsForTransmit = publishIdsForTransmit;
    }
}
