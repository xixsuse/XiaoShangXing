package com.xiaoshangxing.xiaoshang.Reward;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.netUtil.OperateUtils;
import com.xiaoshangxing.Network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.xiaoshang.Help.HelpFragment.HelpFragment;
import com.xiaoshangxing.xiaoshang.Help.PersonalHelp.PersonalHelpFragment;
import com.xiaoshangxing.xiaoshang.Reward.PersonalReward.PersonalRewardFragment;
import com.xiaoshangxing.xiaoshang.Reward.RewardCollect.RewardCollectFragment;
import com.xiaoshangxing.xiaoshang.Reward.RewardFragment.RewardFragment;

import java.util.List;

/**
 * Created by FengChaoQun
 * on 2016/7/20
 */
public class RewardActivity extends BaseActivity implements RewardContract.View {
    public static final String TAG = BaseActivity.TAG + "-ShoolRewardActivity";
    private RewardFragment rewardFragment;
    private PersonalRewardFragment personalRewardFragment;
    private RewardCollectFragment rewardCollectFragment;
    private RewardContract.Presenter mPresenter;

    private boolean isHideMenu;//记录是否需要点击返回键隐藏菜单
    private boolean isCollect;//记录是否是collect界面在显示

    private int transmitedId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_fraglayout);
        setmPresenter(new RewardPresenter(this,this));
        BaseFragment frag = (BaseFragment) mFragmentManager.findFragmentById(R.id.main_fragment);
        if (frag != null) {
            return;
        }
        parseIntent();
    }

    private void initAllFrafments() {
        Fragment frag;

        frag = mFragmentManager.findFragmentByTag(RewardFragment.TAG);
        rewardFragment = (frag == null) ? RewardFragment.newInstance() : (RewardFragment) frag;

        frag = mFragmentManager.findFragmentByTag(PersonalRewardFragment.TAG);
        personalRewardFragment = (frag == null) ? PersonalRewardFragment.newInstance() : (PersonalRewardFragment) frag;

        frag = mFragmentManager.findFragmentByTag(RewardCollectFragment.TAG);
        rewardCollectFragment = (frag == null) ? RewardCollectFragment.newInstance() : (RewardCollectFragment) frag;


        mFragmentManager.beginTransaction().add(R.id.main_fragment,
                rewardCollectFragment, RewardCollectFragment.TAG).commit();

        mFragmentManager.beginTransaction().add(R.id.main_fragment,
                personalRewardFragment, PersonalRewardFragment.TAG).commit();

        mFragmentManager.beginTransaction().add(R.id.main_fragment,
                rewardFragment, HelpFragment.TAG).commit();

        mFragmentManager.beginTransaction()
                .hide(rewardCollectFragment)
                .hide(personalRewardFragment)
                .show(rewardFragment).commit();
    }

    private void parseIntent(){
        Fragment frag;
            switch (getIntent().getIntExtra(IntentStatic.TYPE,0)){
                case IntentStatic.OTHERS:
                    frag = mFragmentManager.findFragmentByTag(RewardFragment.TAG);
                    rewardFragment = (frag == null) ? RewardFragment.newInstance() : (RewardFragment) frag;
                    mFragmentManager.beginTransaction().add(R.id.main_fragment,
                            rewardFragment, RewardFragment.TAG).commit();
                    break;
                case IntentStatic.MINE:
                    frag = mFragmentManager.findFragmentByTag(PersonalRewardFragment.TAG);
                    personalRewardFragment = (frag == null) ? PersonalRewardFragment.newInstance() : (PersonalRewardFragment) frag;
                    mFragmentManager.beginTransaction().add(R.id.main_fragment,
                            personalRewardFragment, PersonalHelpFragment.TAG).commit();
                    break;
                default:
                    initAllFrafments();
            }
    }


    public RewardFragment getRewardFragment() {
        return rewardFragment;
    }

    public PersonalRewardFragment getPersonalRewardFragment() {
        return personalRewardFragment;
    }

    public RewardCollectFragment getRewardCollectFragment() {
        return rewardCollectFragment;
    }

    public boolean isHideMenu() {
        return isHideMenu;
    }

    public void setHideMenu(boolean hideMenu) {
        isHideMenu = hideMenu;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isHideMenu) {
                personalRewardFragment.showHideMenu(false);
                return true;
            } else if (isCollect){
                rewardCollectFragment.showHideMenu(false);
                return true;
            }
            else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    public void showTransmitDialog(final List<String> id) {
        final Dialog dialog = new Dialog(this, R.style.ActionSheetDialog);
        View dialogView = View.inflate(this, R.layout.school_help_transmit_dialog, null);
        dialog.setContentView(dialogView);

        TextView cancle = (TextView) dialogView.findViewById(R.id.cancel);
        TextView send = (TextView) dialogView.findViewById(R.id.send);
        CirecleImage head = (CirecleImage) dialogView.findViewById(R.id.head_image);
        TextView name = (TextView) dialogView.findViewById(R.id.name);
        TextView college = (TextView) dialogView.findViewById(R.id.college);
        TextView text = (TextView) dialogView.findViewById(R.id.text);
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

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OperateUtils.Tranmit(transmitedId, NS.CATEGORY_REWARD, id, RewardActivity.this, input.getText().toString(),
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
        LocationUtil.setWidth(this, dialog, getResources().getDimensionPixelSize(R.dimen.x900));
    }

    @Override
    public void showTransmitSuccess() {
        DialogUtils.Dialog_No_Button dialog_no_button =
                new DialogUtils.Dialog_No_Button(RewardActivity.this, "已分享");
        final Dialog notice_dialog = dialog_no_button.create();
        notice_dialog.show();
        LocationUtil.setWidth(RewardActivity.this, notice_dialog,
                getResources().getDimensionPixelSize(R.dimen.x420));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notice_dialog.dismiss();
            }
        }, 500);
    }

    @Override
    public void setmPresenter(@Nullable RewardContract.Presenter presenter) {
            this.mPresenter=presenter;
    }

    public void gotoSelectPerson(){
        Intent intent=new Intent(this, SelectPersonActivity.class);
        startActivityForResult(intent,SelectPersonActivity.SELECT_PERSON_CODE);
    }

    public int getTransmitedId() {
        return transmitedId;
    }

    public void setTransmitedId(int transmitedId) {
        this.transmitedId = transmitedId;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== SelectPersonActivity.SELECT_PERSON_CODE ){
            if (data!=null){
                if (data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON).size()>0){
                    showTransmitDialog(data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON));
                }else {
                    showToast("未选择联系人");
                }
            }
        }
    }
}