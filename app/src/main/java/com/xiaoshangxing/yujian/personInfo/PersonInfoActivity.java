package com.xiaoshangxing.yujian.personInfo;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xiaoshangxing.Network.IMNetwork;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.ImageButtonText;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.layout.RoundedImageView;
import com.xiaoshangxing.wo.PersonalState.PersonalStateActivity;
import com.xiaoshangxing.yujian.ChatActivity.ChatActivity;
import com.xiaoshangxing.yujian.IM.cache.FriendDataCache;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;
import com.xiaoshangxing.yujian.pearsonalTag.PeraonalTagActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;

/**
 * Created by 15828 on 2016/7/25.
 */
public class PersonInfoActivity extends BaseActivity implements IBaseView, ImageButtonText.OnImageButtonTextClickListener {

    @Bind(R.id.leftarrow)
    ImageView leftarrow;
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.mark_star)
    ImageView markStar;
    @Bind(R.id.mark_love)
    ImageView markLove;
    @Bind(R.id.head_image)
    CirecleImage headImage;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.sex)
    ImageView sex;
    @Bind(R.id.college)
    TextView college;
    @Bind(R.id.chat)
    Button chat;
    @Bind(R.id.bt1)
    ImageButtonText bt1;
    @Bind(R.id.personifo_hometown)
    TextView personifoHometown;
    @Bind(R.id.hometown)
    TextView hometown;
    @Bind(R.id.hometown_lay)
    RelativeLayout hometownLay;
    @Bind(R.id.personinfo_tag)
    TextView personinfoTag;
    @Bind(R.id.tag_content)
    TextView tagContent;
    @Bind(R.id.tag_lay)
    RelativeLayout tagLay;
    @Bind(R.id.dynamic_text)
    TextView dynamicText;
    @Bind(R.id.dynamic_image1)
    RoundedImageView dynamicImage1;
    @Bind(R.id.dynamic_image2)
    RoundedImageView dynamicImage2;
    @Bind(R.id.dynamic_image3)
    RoundedImageView dynamicImage3;
    @Bind(R.id.dynamic_image4)
    RoundedImageView dynamicImage4;
    @Bind(R.id.state_lay)
    RelativeLayout stateLay;
    @Bind(R.id.more_buttom)
    RelativeLayout moreButtom;
    private String tagContents;
    private String account;
    private User user;
    private MyBroadcastReceiver myBroadcastReceiver;
    public static final String FINISH = "FINISH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_personinfo);
        ButterKnife.bind(this);

        bt1.setmOnImageButtonTextClickListener(this);

        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FINISH);
        registerReceiver(myBroadcastReceiver, intentFilter);

        tagContents = "标签1  标签2  标签3  标签4  标签5";
        tagContent.setText(tagContents);

        account = getIntent().getStringExtra(IntentStatic.EXTRA_ACCOUNT);
        if (account == null) {
            showToast("账号有误");
            finish();
            return;
        }

        initView();
    }

    private void initView() {
        int id = Integer.valueOf(account);
        UserInfoCache.getInstance().getHead(headImage, id, this);
        UserInfoCache.getInstance().getCollege(college, id);
        UserInfoCache.getInstance().getName(name, id);

        if (FriendDataCache.getInstance().isMyFriend(account)) {
            more.setVisibility(View.VISIBLE);
            bt1.setChecked(true);
            Log.d(account, "is my friend");
        } else {
            more.setVisibility(View.GONE);
            bt1.setChecked(false);
        }

//        if (user.getSex() == 0) {
//            sex.setImageResource(R.mipmap.sex_female);
//        } else {
//            sex.setImageResource(R.mipmap.sex_male);
//        }

        NimUserInfoCache.getInstance().getUserInfoFromRemote(account, new RequestCallback<NimUserInfo>() {
            @Override
            public void onSuccess(NimUserInfo nimUserInfo) {
                Log.d("123avatar", "" + nimUserInfo.getAvatar());
                Log.d("123account", "" + nimUserInfo.getAccount());
                Log.d("123name", "" + nimUserInfo.getName());
                Log.d("123sex", "" + nimUserInfo.getGenderEnum().name());
                Log.d("123ex", "" + nimUserInfo.getExtension());
            }

            @Override
            public void onFailed(int i) {
                showToast("" + i);
            }

            @Override
            public void onException(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

    }

    public void Next() {

        if (!FriendDataCache.getInstance().isMyFriend(account)) {
            showToast(NS.ON_DEVELOPING);
            return;
        }

        Intent intent = new Intent(this, SetInfoActivity.class);
        intent.putExtra(IntentStatic.EXTRA_ACCOUNT, account);
        startActivity(intent);
    }

    public void More() {

        Intent intent = new Intent(this, MoreInfoActivity.class);
        intent.putExtra(IntentStatic.EXTRA_ACCOUNT, account);
            startActivity(intent);

//        if (DataSetting.IsFocused(this)) {
//            Intent intent = new Intent(this, MoreInfoActivity.class);
//            startActivity(intent);
//        } else {
//            final DialogUtils.Dialog_Center2 dialogUtils = new DialogUtils.Dialog_Center2(this);
//            final Dialog alertDialog = dialogUtils.Message("你未留心对方，不能查看\n对方更多资料")
//                    .Button("确定").MbuttonOnClick(new DialogUtils.Dialog_Center2.buttonOnClick() {
//                        @Override
//                        public void onButton1() {
//                            dialogUtils.close();
//                        }
//
//                        @Override
//                        public void onButton2() {
//
//                        }
//
//                    }).create();
//            alertDialog.show();
//            LocationUtil.setWidth(this, alertDialog,
//                    getResources().getDimensionPixelSize(R.dimen.x780));
//        }

    }

    //打个招呼
    public void SayHello() {
        //test
        if (account == null) {
            return;
        }
        ChatActivity.start(this, account, null, SessionTypeEnum.P2P);
    }

    //标签
    public void Tag() {
        //别人的
        Intent intent = new Intent(this, PeraonalTagActivity.class);
        startActivity(intent);
        //自己的
//        Intent intent = new Intent(this, TagViewActivity.class);
//        startActivity(intent);
    }

    @Override
    public void OnImageButtonTextClick() {
        if (!bt1.isChecked()) {
            favor();
        } else {
//            final DialogUtils.Dialog_Center2 dialogUtils = new DialogUtils.Dialog_Center2(this);
//            Dialog alertDialog = dialogUtils.Message("确定不再留心？")
//                    .Button("取消", "确定").MbuttonOnClick(new DialogUtils.Dialog_Center2.buttonOnClick() {
//                        @Override
//                        public void onButton1() {
//                            dialogUtils.close();
//                        }
//
//                        @Override
//                        public void onButton2() {
//                            bt1.setChecked(false);
//                            bt1.getImgView().setImageResource(R.mipmap.icon_liuxin);
//                            SPUtils.put(PersonInfoActivity.this, "focus", false);
//                            dialogUtils.close();
//                        }
//                    }).create();
//            alertDialog.show();
//            LocationUtil.setWidth(this, alertDialog,
//                    getResources().getDimensionPixelSize(R.dimen.x780));
        }
    }

    private void favor() {

        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(e.string());
                    switch (jsonObject.getInt(NS.CODE)) {
                        case 200:
                            showFavorSuccess();
                            break;
                        default:
                            showToast(jsonObject.getString(NS.MSG));
                            break;
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        };

        ProgressSubsciber<ResponseBody> subscriber = new ProgressSubsciber<>(onNext, this);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.USER_ID, TempUser.id);
        jsonObject.addProperty("oppositeUserId", account);
        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());


        IMNetwork.getInstance().Favor(subscriber, jsonObject, this);
    }

    private void showFavorSuccess() {
        bt1.setChecked(true);
        bt1.getImgView().setImageResource(R.mipmap.icon_liuxin_select);

        DialogUtils.Dialog_Linxin dialog_no_button =
                new DialogUtils.Dialog_Linxin(this, "已添加到我留心的人");
        final Dialog notice_dialog = dialog_no_button.create();
        notice_dialog.show();
        LocationUtil.setWidth(this, notice_dialog,
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
    protected void onDestroy() {
        unregisterReceiver(myBroadcastReceiver);
        super.onDestroy();
    }

    @OnClick({R.id.back, R.id.more, R.id.mark_star, R.id.mark_love, R.id.chat, R.id.hometown_lay, R.id.tag_lay, R.id.state_lay, R.id.more_buttom})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.more:
                Next();
                break;
            case R.id.mark_star:
                break;
            case R.id.mark_love:
                break;
            case R.id.chat:
                SayHello();
                break;
            case R.id.hometown_lay:
                break;
            case R.id.tag_lay:
                Tag();
                break;
            case R.id.state_lay:
                gotoState();
                break;
            case R.id.more_buttom:
                More();
                break;
        }
    }

    private void gotoState() {
        Intent intent = new Intent(this, PersonalStateActivity.class);
        intent.putExtra(IntentStatic.EXTRA_ACCOUNT, account);
        startActivity(intent);
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

}
