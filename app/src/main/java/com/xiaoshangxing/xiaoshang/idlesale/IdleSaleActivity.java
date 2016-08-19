package com.xiaoshangxing.xiaoshang.idlesale;

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
import com.xiaoshangxing.xiaoshang.idlesale.IdleDetailFragment.IdleDetailContract;
import com.xiaoshangxing.xiaoshang.idlesale.IdleDetailFragment.IdleDetailFragment;
import com.xiaoshangxing.xiaoshang.idlesale.IdleDetailFragment.IdleDetailPresenter;
import com.xiaoshangxing.xiaoshang.idlesale.IdleSaleFragment.IdleSaleContract;
import com.xiaoshangxing.xiaoshang.idlesale.IdleSaleFragment.IdleSaleFragment;
import com.xiaoshangxing.xiaoshang.idlesale.IdleSaleFragment.IdleSalePresenter;
import com.xiaoshangxing.xiaoshang.idlesale.MyIdleSale.MyIdleContract;
import com.xiaoshangxing.xiaoshang.idlesale.MyIdleSale.MyIdlePresenter;
import com.xiaoshangxing.xiaoshang.idlesale.MyIdleSale.MyIdleSaleFragment;
import com.xiaoshangxing.xiaoshang.idlesale.favoriteIdleFragment.FavoriteIdleContract;
import com.xiaoshangxing.xiaoshang.idlesale.favoriteIdleFragment.FavoriteIdleFragMent;
import com.xiaoshangxing.xiaoshang.idlesale.favoriteIdleFragment.FavoriteIdlePresenter;

public class IdleSaleActivity extends BaseActivity {


    public static final String TAG = BaseActivity.TAG+"-IdleSale";
    public static final int SELECT_PERSON =10001;
    private IdleSaleContract.View IdleSaleView;
    private IdleDetailContract.View idleDetailView;
    private FavoriteIdleContract.View favoriteIdleView;
    private MyIdleContract.View myIdleView;

    boolean isHideMenu = false;

    private IdleSaleContract.Presenter IdleSalePresenter;
    private IdleDetailContract.Presenter idleDetailPresenter;
    private FavoriteIdleContract.Presenter favoriteIdlePresenter;
    private MyIdleContract.Presenter myIdlePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idle_sale);

        initAllfrag();
        initAllPresenter();

        //PlanProposeFragment tasksFragment = (PlanProposeFragment)getSupportFragmentManager().findFragmentById(R.id.fl_plan);
        BaseFragment frag = (BaseFragment) mFragmentManager.findFragmentById(R.id.fl_idle_sale);
        if (frag == null) {
            frag = getIdleSaleView();
            mFragmentManager.beginTransaction().add(R.id.fl_idle_sale, frag, BaseFragment.TAG).commit();
        }

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private void initAllPresenter() {
        IdleSalePresenter = new IdleSalePresenter(IdleSaleView,new Bean());
        idleDetailPresenter = new IdleDetailPresenter(idleDetailView);
        favoriteIdlePresenter = new FavoriteIdlePresenter(favoriteIdleView,new Bean());
        myIdlePresenter = new MyIdlePresenter(myIdleView);
    }

    private void initAllfrag() {

        Fragment frag;
        frag = mFragmentManager.findFragmentByTag(IdleSaleFragment.TAG);
        IdleSaleView = (frag == null) ? IdleSaleFragment.newInstance() : (IdleSaleFragment) frag;
        frag = mFragmentManager.findFragmentByTag(IdleDetailFragment.TAG);
        idleDetailView = (frag ==null)? IdleDetailFragment.newInstance():(IdleDetailFragment)frag;

        frag = mFragmentManager.findFragmentByTag(MyIdleSaleFragment.TAG);
        myIdleView = (frag ==null)? MyIdleSaleFragment.newInstance():(MyIdleSaleFragment)frag;

        frag = mFragmentManager.findFragmentByTag(FavoriteIdleFragMent.TAG);
        favoriteIdleView = (frag==null)? FavoriteIdleFragMent.newInstance():(FavoriteIdleFragMent)frag;
    }

    public IdleSaleFragment getIdleSaleView() {
        return (IdleSaleFragment)IdleSaleView;
    }

    public IdleDetailFragment getIdleDetailView(){
        return (IdleDetailFragment)idleDetailView;
    }

    public MyIdleSaleFragment getMyIdleView(){
        return (MyIdleSaleFragment) myIdleView;
    }

    public FavoriteIdleFragMent getFavoriteIdleView(){
        return (FavoriteIdleFragMent) favoriteIdleView;
    }
    public void setHideMenu(boolean hideMenu) {
        isHideMenu = hideMenu;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isHideMenu) {
                myIdleView.showDeleteDialog(false);
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
                        new DialogUtils.Dialog_No_Button(IdleSaleActivity.this,"已分享");
                final Dialog notice_dialog=dialog_no_button.create();
                notice_dialog.show();
                LocationUtil.setWidth(IdleSaleActivity.this, notice_dialog,
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PERSON) {
            if (data != null) {
                if (data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON).size() > 0) {
                    showTransmitDialog();
                } else {
                    Toast.makeText(IdleSaleActivity.this, "未选择联系人", Toast.LENGTH_SHORT).show();
                }
            }
        }else if (requestCode == IdleSaleFragment.TAKE_PHOTO){
            Fragment fragment = mFragmentManager.findFragmentByTag(IdleSaleFragment.TAG);
            IdleSaleFragment idleSaleFragment = fragment==null?(IdleSaleFragment)fragment: IdleSaleFragment.newInstance();
           idleSaleFragment.onActivityResult(requestCode,resultCode,data);
        }
    }
}


