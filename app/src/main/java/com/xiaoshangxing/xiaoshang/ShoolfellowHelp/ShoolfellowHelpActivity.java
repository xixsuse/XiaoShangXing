package com.xiaoshangxing.xiaoshang.ShoolfellowHelp;

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
import android.widget.Toast;

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
import com.xiaoshangxing.xiaoshang.ShoolfellowHelp.MyShoolfellowHelp.MyShoolHelpFragment;
import com.xiaoshangxing.xiaoshang.ShoolfellowHelp.ShoolfellowHelpFragment.ShoolfellowHelpFragment;

/**
 * Created by FengChaoQun
 * on 2016/7/20
 */
public class ShoolfellowHelpActivity extends BaseActivity implements HelpContract.View {
    public static final String TAG = BaseActivity.TAG + "-ShoolfellowHelpActivity";

    private ShoolfellowHelpFragment shoolfellowHelpFragment;
    private MyShoolHelpFragment myShoolHelpFragment;
    private HelpContract.Presenter mPresenter;
    public static final int OTHERS=10002;
    public static final int MINE=10003;

    private boolean isHideMenu;//记录是否需要点击返回键隐藏菜单

    private int transmitedId = -1;//记录要转发的动态id


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_fraglayout);
        setmPresenter(new HelpPresenter(this, this));
        BaseFragment frag = (BaseFragment) mFragmentManager.findFragmentById(R.id.main_fragment);
        if (frag != null) {
            return;
        }
        parseIntent();
    }

    private void parseIntent(){
        Fragment frag;
        switch (getIntent().getIntExtra(IntentStatic.TYPE,0)){
            case OTHERS:
                frag = mFragmentManager.findFragmentByTag(ShoolfellowHelpFragment.TAG);
                shoolfellowHelpFragment = (frag == null) ? ShoolfellowHelpFragment.newInstance() :
                        (ShoolfellowHelpFragment) frag;
                frag = getShoolfellowHelpFragment();
                mFragmentManager.beginTransaction().add(R.id.main_fragment,
                        frag, ShoolfellowHelpFragment.TAG).commit();
                break;
            case MINE:
                frag = mFragmentManager.findFragmentByTag(MyShoolHelpFragment.TAG);
                myShoolHelpFragment = (frag == null) ? MyShoolHelpFragment.newInstance() : (MyShoolHelpFragment) frag;
                frag = getMyShoolHelpFragment();
                mFragmentManager.beginTransaction().add(R.id.main_fragment,
                        frag, MyShoolHelpFragment.TAG).commit();
                break;
            default:
                initAllFragments();
        }
    }

    private void initAllFragments() {
        Fragment frag;

        frag = mFragmentManager.findFragmentByTag(ShoolfellowHelpFragment.TAG);
        shoolfellowHelpFragment = (frag == null) ? ShoolfellowHelpFragment.newInstance() : (ShoolfellowHelpFragment) frag;

        frag = mFragmentManager.findFragmentByTag(MyShoolHelpFragment.TAG);
        myShoolHelpFragment = (frag == null) ? MyShoolHelpFragment.newInstance() : (MyShoolHelpFragment) frag;

        frag = getMyShoolHelpFragment();
        mFragmentManager.beginTransaction().add(R.id.main_fragment,
                frag, MyShoolHelpFragment.TAG).commit();
        frag = getShoolfellowHelpFragment();
        mFragmentManager.beginTransaction().add(R.id.main_fragment,
                frag, ShoolfellowHelpFragment.TAG).commit();
    }

    public ShoolfellowHelpFragment getShoolfellowHelpFragment() {
        return shoolfellowHelpFragment;
    }

    public MyShoolHelpFragment getMyShoolHelpFragment() {
        return myShoolHelpFragment;
    }

    public boolean isHideMenu() {
        return isHideMenu;
    }

    public void setHideMenu(boolean hideMenu) {
        isHideMenu = hideMenu;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isHideMenu) {
                myShoolHelpFragment.showHideMenu(false);
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public void showTransmitDialog(final String id) {
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
        int userId = published.getUserId();
        UserInfoCache.getInstance().getHead(head, userId, ShoolfellowHelpActivity.this);
        UserInfoCache.getInstance().getName(name, userId);
        UserInfoCache.getInstance().getCollege(college, userId);
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
                OperateUtils.Tranmit(transmitedId, NS.CATEGORY_REWARD, id, ShoolfellowHelpActivity.this, input.getText().toString(),
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
                new DialogUtils.Dialog_No_Button(ShoolfellowHelpActivity.this, "已分享");
        final Dialog notice_dialog = dialog_no_button.create();
        notice_dialog.show();
        LocationUtil.setWidth(ShoolfellowHelpActivity.this, notice_dialog,
                getResources().getDimensionPixelSize(R.dimen.x420));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notice_dialog.dismiss();
            }
        }, 500);
    }

    public void gotoSelectPerson(){
        Intent intent=new Intent(this, SelectPersonActivity.class);
        intent.putExtra(SelectPersonActivity.LIMIT, 1);
        startActivityForResult(intent,SelectPersonActivity.SELECT_PERSON_CODE);
    }

    @Override
    public void setmPresenter(@Nullable HelpContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    public int getTransmitedId() {
        return transmitedId;
    }

    public void setTransmitedId(int transmitedId) {
        this.transmitedId = transmitedId;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode== SelectPersonActivity.SELECT_PERSON_CODE ){
            if (data!=null){
                if (data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON).size()>0){
                    showTransmitDialog(data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON).get(0));
                }else {
                    Toast.makeText(ShoolfellowHelpActivity.this, "未选择联系人", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
