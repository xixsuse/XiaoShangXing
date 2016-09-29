package com.xiaoshangxing.xiaoshang.Sale;

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
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.image.MyGlide;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.xiaoshang.Sale.PersonalSale.PersonalSaleFragment;
import com.xiaoshangxing.xiaoshang.Sale.SaleCollect.SaleCollectFragment;
import com.xiaoshangxing.xiaoshang.Sale.SaleFrafment.SaleFragment;

/**
 * Created by FengChaoQun
 * on 2016/9/28
 */
public class SaleActivity extends BaseActivity implements IBaseView {
    public static final String TAG = BaseActivity.TAG + "-SaleActivity";

    private SaleFragment saleFragment;
    private PersonalSaleFragment personalSaleFragment;
    private SaleCollectFragment saleCollectFragment;
    private boolean isHideMenu;//记录是否需要点击返回键隐藏菜单

    private int transmitedId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_fraglayout);
        BaseFragment frag = (BaseFragment) mFragmentManager.findFragmentById(R.id.main_fragment);
        if (frag != null) {
            return;
        }
//        parseIntent();
        initAllFrafments();
    }

    private void parseIntent() {
        Fragment frag;
        switch (getIntent().getIntExtra(IntentStatic.TYPE, 0)) {
//            case IntentStatic.OTHERS:
//                frag = mFragmentManager.findFragmentByTag(ShoolRewardFragment.TAG);
//                shoolRewardFragment = (frag == null) ? ShoolRewardFragment.newInstance() : (ShoolRewardFragment) frag;
//                mFragmentManager.beginTransaction().add(R.id.main_fragment,
//                        shoolRewardFragment, MyShoolHelpFragment.TAG).commit();
//                break;
//            case IntentStatic.MINE:
//                frag = mFragmentManager.findFragmentByTag(MyShoolRewardFragment.TAG);
//                myShoolRewardFragment = (frag == null) ? MyShoolRewardFragment.newInstance() : (MyShoolRewardFragment) frag;
//                mFragmentManager.beginTransaction().add(R.id.main_fragment,
//                        myShoolRewardFragment, MyShoolHelpFragment.TAG).commit();
//                break;
            default:
                initAllFrafments();
        }
    }

    private void initAllFrafments() {
        Fragment frag;

        frag = mFragmentManager.findFragmentByTag(SaleFragment.TAG);
        saleFragment = (frag == null) ? SaleFragment.newInstance() : (SaleFragment) frag;

        frag = mFragmentManager.findFragmentByTag(PersonalSaleFragment.TAG);
        personalSaleFragment = (frag == null) ? PersonalSaleFragment.newInstance() : (PersonalSaleFragment) frag;

        frag = mFragmentManager.findFragmentByTag(SaleCollectFragment.TAG);
        saleCollectFragment = (frag == null) ? SaleCollectFragment.newInstance() : (SaleCollectFragment) frag;

        mFragmentManager.beginTransaction().add(R.id.main_fragment,
                saleCollectFragment, SaleCollectFragment.TAG).commit();

        mFragmentManager.beginTransaction().add(R.id.main_fragment,
                personalSaleFragment, PersonalSaleFragment.TAG).commit();

        mFragmentManager.beginTransaction().add(R.id.main_fragment,
                saleFragment, SaleFragment.TAG).commit();
    }

    public SaleFragment getSaleFragment() {
        return saleFragment;
    }

    public PersonalSaleFragment getPersonalSaleFragment() {
        return personalSaleFragment;
    }

    public SaleCollectFragment getSaleCollectFragment() {
        return saleCollectFragment;
    }

    public boolean isHideMenu() {
        return isHideMenu;
    }

    public void setHideMenu(boolean hideMenu) {
        isHideMenu = hideMenu;
    }

    public void gotoSelectPerson() {
        Intent intent = new Intent(this, SelectPersonActivity.class);
        intent.putExtra(SelectPersonActivity.LIMIT, 1);
        startActivityForResult(intent, SelectPersonActivity.SELECT_PERSON_CODE);
    }

    public int getTransmitedId() {
        return transmitedId;
    }

    public void setTransmitedId(int transmitedId) {
        this.transmitedId = transmitedId;
    }

    public void showTransmitDialog(final String id) {
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
        int userId = published.getUserId();
        UserInfoCache.getInstance().getHead(head, userId, SaleActivity.this);
        UserInfoCache.getInstance().getName(name, userId);
        UserInfoCache.getInstance().getCollege(college, userId);
        text.setText(published.getText());
        if (!TextUtils.isEmpty(published.getImage())) {
            MyGlide.with(SaleActivity.this, published.getImage().split(NS.SPLIT)[0], imageView);
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
                OperateUtils.Tranmit(transmitedId, NS.CATEGORY_SALE, id, SaleActivity.this, input.getText().toString(),
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

    public void showTransmitSuccess() {
        DialogUtils.Dialog_No_Button dialog_no_button =
                new DialogUtils.Dialog_No_Button(SaleActivity.this, "已分享");
        final Dialog notice_dialog = dialog_no_button.create();
        notice_dialog.show();
        LocationUtil.setWidth(SaleActivity.this, notice_dialog,
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isHideMenu) {
                personalSaleFragment.showHideMenu(false);
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SelectPersonActivity.SELECT_PERSON_CODE) {
            if (data != null) {
                if (data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON).size() > 0) {
                    showTransmitDialog(data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON).get(0));
                } else {
                    showToast("未选择联系人");
                }
            }
        }
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }
}
