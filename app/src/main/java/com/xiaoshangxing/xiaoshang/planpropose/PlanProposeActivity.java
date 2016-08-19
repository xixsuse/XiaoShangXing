package com.xiaoshangxing.xiaoshang.planpropose;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.data.Bean;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.xiaoshang.planpropose.MyJoinedPlan.MyJoinedPlanFragment;
import com.xiaoshangxing.xiaoshang.planpropose.editplanfragment.NewPlanContract;
import com.xiaoshangxing.xiaoshang.planpropose.editplanfragment.NewPlanFragment;
import com.xiaoshangxing.xiaoshang.planpropose.editplanfragment.NewPlanPresenter;
import com.xiaoshangxing.xiaoshang.planpropose.myplan.MyPlanContract;
import com.xiaoshangxing.xiaoshang.planpropose.myplan.MyPlanFragment;
import com.xiaoshangxing.xiaoshang.planpropose.myplan.MyPlanPresenter;
import com.xiaoshangxing.xiaoshang.planpropose.planinfofragment.PlanInfoContract;
import com.xiaoshangxing.xiaoshang.planpropose.planinfofragment.PlanInfoFragment;
import com.xiaoshangxing.xiaoshang.planpropose.planinfofragment.PlanInfoPresenter;
import com.xiaoshangxing.xiaoshang.planpropose.planproposefragment.PlanProposeContract;
import com.xiaoshangxing.xiaoshang.planpropose.planproposefragment.PlanProposeFragment;
import com.xiaoshangxing.xiaoshang.planpropose.planproposefragment.PlanProposePresenter;
import com.xiaoshangxing.xiaoshang.planpropose.shareplanfragment.SharePlanContract;
import com.xiaoshangxing.xiaoshang.planpropose.shareplanfragment.SharePlanFragment;
import com.xiaoshangxing.xiaoshang.planpropose.shareplanfragment.SharePlanPresenter;

public class PlanProposeActivity extends BaseActivity {
    public static final String TAG = BaseActivity.TAG+"-PlanProp";

    public static final int SELECT_PERSON =10001;

    boolean isHideMenu = false;

    private PlanProposeContract.View mPlanproposeView;
    private PlanInfoContract.View mPlanInfoView;
    private NewPlanContract.View mNewPlanView;
    private SharePlanContract.View mSharePlanView;
    private MyPlanContract.View mMyplanView;



    private PlanInfoContract.Presenter mPlanInfoPresenter;
    private PlanProposeContract.Presenter planProposePresenter;
    private NewPlanContract.Presenter mNewPlanPresenter;
    private SharePlanContract.Presenter mSharePlanPresenter;
    private MyPlanContract.Presenter mMyPlanPresenter;
    private PlanProposeContract.View mMjoinedPlanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_propose);
        initAllfrag();
        initAllPresenter();

        //PlanProposeFragment tasksFragment = (PlanProposeFragment)getSupportFragmentManager().findFragmentById(R.id.fl_plan);
        BaseFragment frag = (BaseFragment) mFragmentManager.findFragmentById(R.id.fl_plan);
        if (frag == null) {
            frag = getPlanProposeFragment();
            mFragmentManager.beginTransaction().add(R.id.fl_plan, frag, BaseFragment.TAG).commit();
        }

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private void initAllfrag() {
        Fragment frag;
        frag = mFragmentManager.findFragmentByTag(PlanProposeFragment.TAG);
        mPlanproposeView= (frag == null) ? PlanProposeFragment.newInstance() : (PlanProposeFragment) frag;

        frag = mFragmentManager.findFragmentByTag(PlanInfoFragment.TAG);
        mPlanInfoView = (frag == null)?PlanInfoFragment.newInstance():(PlanInfoFragment)frag;

        frag = mFragmentManager.findFragmentByTag(NewPlanFragment.TAG);
        mNewPlanView = (frag==null)? NewPlanFragment.newInstance():(NewPlanFragment)frag;

        frag = mFragmentManager.findFragmentByTag(SharePlanFragment.TAG);
        mSharePlanView = (frag==null)? SharePlanFragment.newInstance():(SharePlanFragment)frag;

        frag = mFragmentManager.findFragmentByTag(MyPlanFragment.TAG);
        mMyplanView = (frag==null)? MyPlanFragment.newInstance():(MyPlanFragment)frag;

        frag = mFragmentManager.findFragmentByTag(MyJoinedPlanFragment.TAG);
        mMjoinedPlanView = (frag==null)?MyJoinedPlanFragment.newInstance():(MyJoinedPlanFragment)frag;




    }
    private void initAllPresenter(){
        planProposePresenter = new PlanProposePresenter(new Bean(),mPlanproposeView);
        mPlanInfoPresenter = new PlanInfoPresenter(new Bean(),mPlanInfoView);
        mNewPlanPresenter = new NewPlanPresenter(new Bean(),mNewPlanView);
        mSharePlanPresenter = new SharePlanPresenter(mSharePlanView,new Bean());
        mMyPlanPresenter = new MyPlanPresenter(mMyplanView,new Bean());

    }
    public PlanProposeFragment getPlanProposeFragment() {
        return (PlanProposeFragment)mPlanproposeView;
    }

    public PlanInfoFragment getmPlanInfoView() {
        return (PlanInfoFragment)mPlanInfoView;
    }

    public NewPlanFragment getmNewPlanView() {
        return (NewPlanFragment)mNewPlanView;
    }

    public MyJoinedPlanFragment getmMjoinedPlanView() {
        return (MyJoinedPlanFragment) mMjoinedPlanView;
    }

    public void setmMjoinedPlanView(PlanProposeContract.View mMjoinedPlanView) {
        this.mMjoinedPlanView = mMjoinedPlanView;
    }

    public SharePlanFragment getSharePlanView(){
        return (SharePlanFragment)mSharePlanView;
    }

    public MyPlanFragment getMyPlanFragment(){
            return (MyPlanFragment)mMyplanView;
            }

    public void setHideMenu(boolean hideMenu) {
        isHideMenu = hideMenu;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isHideMenu) {
                mMyplanView.showDeleteDialog(false);
                return true;
            }
            else {
                return super.onKeyDown(keyCode, event);
            }
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
    private void showTransmitDialog(){
        final Dialog dialog=new Dialog(this, R.style.ActionSheetDialog);
        View dialogView=View.inflate(this, R.layout.plan_propose_transmit_dialog,null);
        dialog.setContentView(dialogView);
        TextView cancle=(TextView) dialogView.findViewById(R.id.cancel);
        TextView send=(TextView) dialogView.findViewById(R.id.send);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.Dialog_No_Button dialog_no_button=
                        new DialogUtils.Dialog_No_Button(PlanProposeActivity.this,"已分享");
                final Dialog notice_dialog=dialog_no_button.create();
                notice_dialog.show();
                LocationUtil.setWidth(PlanProposeActivity.this, notice_dialog,
                        getResources().getDimensionPixelSize(R.dimen.x420));
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notice_dialog.dismiss();
                    }
                },500);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode== SELECT_PERSON ){
            if (data!=null){
                if (data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON).size()>0){
                    showTransmitDialog();
                }else {
                    Toast.makeText(PlanProposeActivity.this, "未选择联系人", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
