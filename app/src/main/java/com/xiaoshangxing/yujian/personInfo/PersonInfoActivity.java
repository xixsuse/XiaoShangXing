package com.xiaoshangxing.yujian.personInfo;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserServiceObserve;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xiaoshangxing.network.IMNetwork;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.network.netUtil.OperateUtils;
import com.xiaoshangxing.network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.data.bean.User;
import com.xiaoshangxing.wo.setting.personalinfo.tagView.TagViewActivity;
import com.xiaoshangxing.wo.setting.personalinfo.showheadimg.HeadImageActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.utils.baseClass.IBaseView;
import com.xiaoshangxing.utils.customView.CirecleImage;
import com.xiaoshangxing.utils.customView.ImageButtonText;
import com.xiaoshangxing.utils.customView.RoundedImageView;
import com.xiaoshangxing.utils.customView.dialog.DialogLocationAndSize;
import com.xiaoshangxing.utils.customView.dialog.DialogUtils;
import com.xiaoshangxing.utils.imageUtils.MyGlide;
import com.xiaoshangxing.wo.PersonalState.PersonalStateActivity;
import com.xiaoshangxing.yujian.ChatActivity.ChatActivity;
import com.xiaoshangxing.yujian.IM.cache.FriendDataCache;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;
import com.xiaoshangxing.yujian.pearsonalTag.PeraonalTagActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 *modified by FengChaoQun on 2016/12/24 19:22
 * description:优化代码
 */
public class PersonInfoActivity extends BaseActivity implements IBaseView, ImageButtonText.OnImageButtonTextClickListener {

    public static final String FINISH = "FINISH";
    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
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
    private NimUserInfo user;
    private MyBroadcastReceiver myBroadcastReceiver;
    private List<User> loves = new ArrayList<>();
    private boolean isStar;
    private boolean isLove;
    private Observer<List<NimUserInfo>> observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_personinfo);
        ButterKnife.bind(this);

        title.setText("个人资料");
        titleBottomLine.setVisibility(View.GONE);
        bt1.setmOnImageButtonTextClickListener(this);

        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FINISH);
        registerReceiver(myBroadcastReceiver, intentFilter);

        account = getIntent().getStringExtra(IntentStatic.ACCOUNT);
        if (account == null) {
            showToast("账号有误");
            finish();
            return;
        }

        initView();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(myBroadcastReceiver);
        oberverUserInfo(false);
        super.onDestroy();
    }

    private void initView() {

        user = NimUserInfoCache.getInstance().getUserInfo(account);
        if (user == null) {
            NimUserInfoCache.getInstance().getUserInfoFromRemote(account, new RequestCallback<NimUserInfo>() {
                @Override
                public void onSuccess(NimUserInfo nimUserInfo) {
                    user = nimUserInfo;
                    refreshPager();
                }

                @Override
                public void onFailed(int i) {
                    showToast("获取用户信息失败:" + i);
                }

                @Override
                public void onException(Throwable throwable) {
                    showToast("获取用户信息失败:异常");
                    throwable.printStackTrace();
                }
            });
        } else {
            refreshPager();
        }

        oberverUserInfo(true);

        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonInfoActivity.this, HeadImageActivity.class);
                intent.putExtra(IntentStatic.ACCOUNT, account);
                startActivity(intent);
            }
        });

    }

    private void refreshPager() {

        UserInfoCache.getInstance().getHeadIntoImage(account, headImage);
        UserInfoCache.getInstance().getExIntoTextview(account, NS.COLLEGE, college);
        UserInfoCache.getInstance().getExIntoTextview(account, NS.USER_NAME, name);
        UserInfoCache.getInstance().getExIntoTextview(account, NS.HOMETOWN, hometown, "未填写");

        if (TextUtils.isEmpty(user.getAvatar())) {
            headImage.setVisibility(View.GONE);
        }

        if (user.getExtensionMap() == null) {
            hometownLay.setVisibility(View.GONE);
        } else {
            String homeString = String.valueOf(user.getExtensionMap().get(NS.HOMETOWN));
            if (TextUtils.isEmpty(homeString) || homeString.equals("null")) {
                hometownLay.setVisibility(View.GONE);
            }
        }

        if (user.getGenderEnum() == GenderEnum.FEMALE) {
            sex.setImageResource(R.mipmap.sex_female);
        } else if (user.getGenderEnum() == GenderEnum.MALE) {
            sex.setImageResource(R.mipmap.sex_male);
        } else {
            sex.setVisibility(View.GONE);
        }

        tagContent.setText("");
        tagContents = (String) user.getExtensionMap().get(NS.LABEL);
        if (!TextUtils.isEmpty(tagContents)) {
            String[] strings = tagContents.split(NS.SPLIT);
            for (String i : strings) {
                tagContent.append(i + " ");
            }
        } else {
            tagLay.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(String.valueOf(user.getExtensionMap().get(NS.HOMETOWN)))) {
            hometownLay.setVisibility(View.GONE);
        }

        getImages();
        getCrushState();

        if (TempUser.isMine(account)) {
            more.setVisibility(View.GONE);
            bt1.setChecked(true);
            bt1.getImgView().setImageResource(R.mipmap.icon_liuxin_select);
            bt1.setText("留心");
            isStar = true;
            return;
        }

        if (FriendDataCache.getInstance().isMyFriend(account)) {
            bt1.setChecked(true);
            bt1.getImgView().setImageResource(R.mipmap.icon_liuxin_select);
            bt1.setText("取消");
            isStar = true;
            Friend friend = FriendDataCache.getInstance().getFriendByAccount(account);
            if (friend != null && friend.getExtension() != null && friend.getExtension().containsKey(NS.MARK)
                    && (boolean) friend.getExtension().get(NS.MARK)) {
                markStar.setVisibility(View.VISIBLE);
            }
        } else {
            bt1.setChecked(false);
            bt1.getImgView().setImageResource(R.mipmap.icon_liuxin);
            bt1.setText("留心");
            getStar();
        }

    }

    private void oberverUserInfo(boolean is) {
        if (observer == null) {
            observer = new Observer<List<NimUserInfo>>() {
                @Override
                public void onEvent(List<NimUserInfo> nimUserInfos) {
                    for (NimUserInfo userInfo : nimUserInfos) {
                        if (userInfo.getAccount().equals(String.valueOf(TempUser.id))) {
                            user = userInfo;
                            refreshPager();
                        }
                    }
                }
            };
        }
        NIMClient.getService(UserServiceObserve.class).observeUserInfoUpdate(observer, is);
    }

    private void getImages() {

        Subscriber<ResponseBody> subscriber = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    switch (jsonObject.getInt(NS.CODE)) {
                        case NS.CODE_200:
                            if (!TextUtils.isEmpty(jsonObject.getString(NS.MSG))) {
                                String[] strings = jsonObject.getString(NS.MSG).split(NS.SPLIT);
                                initPictures(Arrays.asList(strings));
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.USER_ID, account);
        jsonObject.addProperty(NS.CATEGORY, NS.CATEGORY_STATE);

        IMNetwork.getInstance().GetImages(subscriber, jsonObject, this);

    }

    private void initPictures(List<String> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            switch (i) {
                case 0:
                    MyGlide.withBitmap(this, arrayList.get(i), dynamicImage1);
                    break;
                case 1:
                    MyGlide.withBitmap(this, arrayList.get(i), dynamicImage2);
                    break;
                case 2:
                    MyGlide.withBitmap(this, arrayList.get(i), dynamicImage3);
                    break;
                case 3:
                    MyGlide.withBitmap(this, arrayList.get(i), dynamicImage4);
                    break;
            }
        }
    }

    private void getStar() {
        Subscriber<ResponseBody> subscriber = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    switch (jsonObject.getInt(NS.CODE)) {
                        case 200:
                            Gson gson = new Gson();
                            loves = gson.fromJson(jsonObject.getJSONArray(NS.MSG).toString(), new TypeToken<List<User>>() {
                            }.getType());
                            for (User user : loves) {
                                if (user.getId() == Integer.valueOf(account)) {
                                    isStar = true;
                                    bt1.setChecked(true);
                                    bt1.getImgView().setImageResource(R.mipmap.icon_liuxin_select);
                                }
                            }
                            break;
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        IMNetwork.getInstance().MyFavor(subscriber, String.valueOf(TempUser.id), this);
    }

    private void getCrushState() {
        Subscriber<ResponseBody> subscriber = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    switch (jsonObject.getInt(NS.CODE)) {
                        case NS.CODE_200:
                            String cruedId = jsonObject.getString(NS.MSG).split(NS.SPLIT)[0];
                            isLove = cruedId.equals(account);
                            markLove.setVisibility(isLove ? View.VISIBLE : View.INVISIBLE);
                            break;
                        default:
                            Log.w("getCrush", jsonObject.getString(NS.MSG));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.USER_ID, TempUser.getId());
        jsonObject.addProperty(NS.CATEGORY, "1");
        IMNetwork.getInstance().GetCrush(subscriber, jsonObject, this);
    }

    public void Next() {
        Intent intent = new Intent(this, SetInfoActivity.class);
        intent.putExtra(IntentStatic.ACCOUNT, account);
        startActivityForResult(intent, IntentStatic.SIMPLE_CODE);
    }

    public void More() {

        if (isStar) {
            Intent intent = new Intent(this, MoreInfoActivity.class);
            intent.putExtra(IntentStatic.ACCOUNT, account);
            startActivity(intent);
        } else {
            final DialogUtils.Dialog_Center2 dialogUtils = new DialogUtils.Dialog_Center2(this);
            final Dialog alertDialog = dialogUtils.Message("你未留心对方，不能查看\n对方更多资料")
                    .Button("确定").MbuttonOnClick(new DialogUtils.Dialog_Center2.buttonOnClick() {
                        @Override
                        public void onButton1() {
                            dialogUtils.close();
                        }

                        @Override
                        public void onButton2() {

                        }

                    }).create();
            alertDialog.show();
            DialogLocationAndSize.setWidth(alertDialog, R.dimen.x780);
        }

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
        if (TempUser.isMine(account)) {
            Intent intent = new Intent(this, TagViewActivity.class);
            intent.putExtra(IntentStatic.DATA, tagContents);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, PeraonalTagActivity.class);
            intent.putExtra(IntentStatic.DATA, tagContents);
            startActivity(intent);
        }
    }

    @Override
    public void OnImageButtonTextClick() {

        if (TempUser.isMine(account)) {
            return;
        }

        if (!bt1.isChecked()) {
            favor();
        } else {
            final DialogUtils.Dialog_Center2 dialogUtils = new DialogUtils.Dialog_Center2(this);
            Dialog alertDialog = dialogUtils.Message("确定不再留心？")
                    .Button("取消", "确定").MbuttonOnClick(new DialogUtils.Dialog_Center2.buttonOnClick() {
                        @Override
                        public void onButton1() {
                            dialogUtils.close();
                        }

                        @Override
                        public void onButton2() {
                            OperateUtils.CancelFavor(account, PersonInfoActivity.this, PersonInfoActivity.this, new SimpleCallBack() {
                                @Override
                                public void onSuccess() {
                                    bt1.setChecked(false);
                                    bt1.getImgView().setImageResource(R.mipmap.icon_liuxin);
                                    bt1.setText("留心");
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onBackData(Object o) {

                                }
                            });

                            dialogUtils.close();
                        }
                    }).create();
            alertDialog.show();
            DialogLocationAndSize.setWidth(alertDialog, R.dimen.x780);
        }
    }

    private void favor() {
        OperateUtils.Favor(account, this, this, new SimpleCallBack() {
            @Override
            public void onSuccess() {
                showFavorSuccess();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onBackData(Object o) {

            }
        });
    }

    private void showFavorSuccess() {
        bt1.setChecked(true);
        bt1.getImgView().setImageResource(R.mipmap.icon_liuxin_select);
        bt1.setText("取消");

        DialogUtils.Dialog_Linxin dialog_no_button =
                new DialogUtils.Dialog_Linxin(this, "已添加到我留心的人");
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
        intent.putExtra(IntentStatic.ACCOUNT, account);
        startActivity(intent);
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }

    public boolean isLove() {
        return isLove;
    }

    public void setLove(boolean love) {
        isLove = love;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentStatic.SIMPLE_CODE:
                if (data == null) {
                    return;
                }
                isLove = data.getBooleanExtra(IntentStatic.DATA, false);
                markLove.setVisibility(isLove ? View.VISIBLE : View.INVISIBLE);
                break;
        }
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

}
