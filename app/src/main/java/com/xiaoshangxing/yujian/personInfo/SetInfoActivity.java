package com.xiaoshangxing.yujian.personInfo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.constant.FriendFieldEnum;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xiaoshangxing.network.IMNetwork;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.network.netUtil.OperateUtils;
import com.xiaoshangxing.network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.wo.setting.utils.ActionSheet;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.XSXApplication;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.utils.baseClass.IBaseView;
import com.xiaoshangxing.utils.customView.SwitchView;
import com.xiaoshangxing.utils.customView.dialog.DialogUtils;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.yujian.IM.cache.FriendDataCache;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;
import com.xiaoshangxing.yujian.report.ReportActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by 15828 on 2016/7/25.
 */
public class SetInfoActivity extends BaseActivity implements IBaseView {

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
    @Bind(R.id.StarMarkfriends)
    SwitchView StarMarkfriends;
    @Bind(R.id.mark_star)
    RelativeLayout markStar;
    @Bind(R.id.crush)
    SwitchView crush;
    @Bind(R.id.love)
    RelativeLayout love;
    @Bind(R.id.love_notice)
    TextView loveNotice;
    @Bind(R.id.bukanwo)
    SwitchView bukanwo;
    @Bind(R.id.bukanta)
    SwitchView bukanta;
    @Bind(R.id.permission)
    LinearLayout permission;
    @Bind(R.id.addtoblacklist)
    SwitchView addtoblacklist;
    @Bind(R.id.blacklist)
    RelativeLayout blacklist;
    @Bind(R.id.divider)
    ImageView divider;
    @Bind(R.id.info)
    RelativeLayout info;
    @Bind(R.id.confirm_modify)
    Button confirmModify;
    @Bind(R.id.delete)
    LinearLayout delete;
    private SwitchView starMarkfriends, addToBlackList;
    private ActionSheet mActionSheet1;
    private ActionSheet mActionSheet2;
    private String account;
    private Friend friend;
    private NimUserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_setinfo);
        ButterKnife.bind(this);

        title.setText("资料设置");
        more.setVisibility(View.GONE);

        if (getIntent().hasExtra(IntentStatic.EXTRA_ACCOUNT)) {
            account = getIntent().getStringExtra(IntentStatic.EXTRA_ACCOUNT);
            if (TextUtils.isEmpty(account)) {
                showToast("账号有误");
                finish();
                return;
            }
        } else {
            showToast("账号有误");
            finish();
            return;
        }

        if (!FriendDataCache.getInstance().isMyFriend(account) || FriendDataCache.getInstance().isInblack(account)) {
            initNotFriend();
            return;
        }

        friend = FriendDataCache.getInstance().getFriendByAccount(account);
        userInfo = NimUserInfoCache.getInstance().getUserInfo(TempUser.getId());
        if (friend == null || userInfo == null) {
            showToast("账号信息有误");
            finish();
            return;
        }

        starMarkfriends = (SwitchView) findViewById(R.id.StarMarkfriends);
        crush = (SwitchView) findViewById(R.id.crush);
        bukanwo = (SwitchView) findViewById(R.id.bukanwo);
        bukanta = (SwitchView) findViewById(R.id.bukanta);
        addToBlackList = (SwitchView) findViewById(R.id.addtoblacklist);

        setUpData();

        starMarkfriends.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                markFriend(true);
            }

            @Override
            public void toggleToOff() {
                markFriend(false);
            }
        });

        crush.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                crush(true);
            }

            @Override
            public void toggleToOff() {
                crush(false);
            }
        });

        bukanwo.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                permission(true, NS.BLOCK_CODE, bukanwo);
            }

            @Override
            public void toggleToOff() {
                permission(false, NS.REMOVE_BLOCK_CODE, bukanwo);
            }
        });

        bukanta.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                permission(true, NS.MY_BLOCK_CODE, bukanta);
            }

            @Override
            public void toggleToOff() {
                permission(false, NS.REMOVE_MY_BLOCK_CODE, bukanta);
            }
        });

        addToBlacklist();
    }

    private void initNotFriend() {
        markStar.setVisibility(View.GONE);
        love.setVisibility(View.GONE);
        loveNotice.setVisibility(View.GONE);
        permission.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        divider.setVisibility(View.VISIBLE);
        addToBlacklist();
    }

    private void addToBlacklist() {
        addToBlackList = (SwitchView) findViewById(R.id.addtoblacklist);
        addToBlackList.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                if (mActionSheet1 == null) {
                    mActionSheet1 = new ActionSheet(SetInfoActivity.this);
                    mActionSheet1.addMenuTopItem("加入黑名单，你将不再收到对方的消息，并且你们互相\n看不到对方校友圈的更新")
                            .addMenuBottomItem("确定");
                }
                mActionSheet1.show();
                WindowManager windowManager = getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = mActionSheet1.getWindow().getAttributes();
                lp.width = (display.getWidth()); //设置宽度
                mActionSheet1.getWindow().setAttributes(lp);
                mActionSheet1.setMenuBottomListener(new ActionSheet.MenuListener() {
                    @Override
                    public void onItemSelected(int position, String item) {
                        NIMClient.getService(FriendService.class).addToBlackList(account)
                                .setCallback(new RequestCallback<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        addToBlackList.setState(true);
                                        showToast("设置成功");
                                    }

                                    @Override
                                    public void onFailed(int i) {
                                        showToast("设置失败:" + i);
                                    }

                                    @Override
                                    public void onException(Throwable throwable) {
                                        showToast("设置失败:异常");
                                        throwable.printStackTrace();
                                    }
                                });
                    }

                    @Override
                    public void onCancel() {
                        addToBlackList.setState(false);
                    }
                });

            }

            @Override
            public void toggleToOff() {
                NIMClient.getService(FriendService.class).removeFromBlackList(account)
                        .setCallback(new RequestCallback<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                addToBlackList.setState(false);
                                showToast("设置成功");
                            }

                            @Override
                            public void onFailed(int i) {
                                showToast("设置失败:" + i);
                            }

                            @Override
                            public void onException(Throwable throwable) {
                                showToast("设置失败:异常");
                                throwable.printStackTrace();
                            }
                        });
            }
        });
    }

    private void markFriend(final boolean is) {
        Map<FriendFieldEnum, Object> map = new HashMap<>();
        Map<String, Object> exts = new HashMap<>();
        exts.put(NS.MARK, is);
        map.put(FriendFieldEnum.EXTENSION, exts);
        NIMClient.getService(FriendService.class).updateFriendFields(account, map)
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        starMarkfriends.setState(is);
                    }

                    @Override
                    public void onFailed(int i) {
                        showToast("操作失败:" + i);
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        showToast("操作失败:异常");
                        throwable.printStackTrace();
                    }
                });
    }

    private void crush(boolean is) {
        if (is) {
            ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
                @Override
                public void onNext(ResponseBody e) throws JSONException {
                    try {
                        JSONObject jsonObject = new JSONObject(e.string());
                        switch (jsonObject.getInt(NS.CODE)) {
                            case NS.CODE_200:
                            case 201:
                                crush.setState(true);
                                break;
                            case 408:
                                showDialog("这个月的暗恋权限次数已用完");
                                break;
                            default:
                                crush.setState(false);
                                showDialog(jsonObject.getString(NS.MSG));
                                break;
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            };
            ProgressSubsciber<ResponseBody> subsciber = new ProgressSubsciber<>(onNext, this);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(NS.USER_ID, TempUser.getId());
            jsonObject.addProperty(NS.OPPOSITE_UERID, account);
            jsonObject.addProperty(NS.CATEGORY, "1");
            jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());
            IMNetwork.getInstance().Crush(subsciber, jsonObject, this);
        } else {
            ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
                @Override
                public void onNext(ResponseBody e) throws JSONException {
                    try {
                        JSONObject jsonObject = new JSONObject(e.string());
                        switch (jsonObject.getInt(NS.CODE)) {
                            case NS.CODE_200:
                            case 201:
                                crush.setState(false);
                                break;
                            default:
                                crush.setState(true);
                                showToast(jsonObject.getString(NS.MSG));
                                break;
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            };
            ProgressSubsciber<ResponseBody> subsciber = new ProgressSubsciber<>(onNext, this);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(NS.USER_ID, TempUser.id);
            jsonObject.addProperty(NS.OPPOSITE_UERID, account);
            jsonObject.addProperty(NS.CATEGORY, "1");

            IMNetwork.getInstance().CancelFavor(subsciber, jsonObject, this);
        }

    }

    private void permission(final boolean is, String type, final SwitchView switchView) {
        List<String> accounts = new ArrayList<>();
        accounts.add(account);
        OperateUtils.SchoolCirclrPermisson(accounts, type, this, new SimpleCallBack() {
            @Override
            public void onSuccess() {
                switchView.setState(is);
            }

            @Override
            public void onError(Throwable e) {
                switchView.setState(!is);
            }

            @Override
            public void onBackData(Object o) {

            }
        });
    }

    private void showDialog(String msg) {
        final DialogUtils.Dialog_Center dialog_center = new DialogUtils.Dialog_Center(XSXApplication.currentActivity);
        Dialog dialog = dialog_center.Message(msg).
                Button("我知道了")
                .MbuttonOnClick(new DialogUtils.Dialog_Center.buttonOnClick() {
                    @Override
                    public void onButton1() {
                        dialog_center.close();
                    }

                    @Override
                    public void onButton2() {
                        dialog_center.close();
                    }
                }).create();
        dialog.show();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (ScreenUtils.getAdapterPx(R.dimen.x780, XSXApplication.currentActivity)); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }

    private void setUpData() {
        if (friend != null && friend.getExtension() != null && friend.getExtension().containsKey(NS.MARK)) {
            starMarkfriends.setState((boolean) friend.getExtension().get(NS.MARK));
        } else {
            starMarkfriends.setState(false);
        }
        getCrushState();
        addToBlackList.setState(FriendDataCache.getInstance().isInblack(account));

        //        校友圈权限
        String block = null;
        String myBlock = null;
        if (userInfo.getExtensionMap() != null) {
            block = (String) userInfo.getExtensionMap().get(NS.BLOCK);
            myBlock = (String) userInfo.getExtensionMap().get(NS.MY_BLOCK);
        }
        Log.d("block", "" + block);
        Log.d("myblock", "" + myBlock);
        if (block != null && block.contains(account)) {
            bukanwo.setState(true);
        } else {
            bukanwo.setState(false);
        }
        if (myBlock != null && myBlock.contains(account)) {
            bukanta.setState(true);
        } else {
            bukanta.setState(false);
        }
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
                            crush.setState(cruedId.equals(account));
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

    public void Report(View view) {
        Intent intent = new Intent(this, ReportActivity.class);
        intent.putExtra(IntentStatic.DATA, account);
        startActivity(intent);
    }

    public void Delete(View view) {
        if (mActionSheet2 == null) {
            mActionSheet2 = new ActionSheet(SetInfoActivity.this);
            mActionSheet2.addMenuTopItem("将联系人“" + NimUserInfoCache.getInstance()
                    .getUserDisplayName(account) + "”删除，同时删除与该联系人的聊天记录")
                    .addMenuBottomItem("确定");
        }
        mActionSheet2.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mActionSheet2.getWindow().getAttributes();
        lp.width = (display.getWidth()); //设置宽度
        mActionSheet2.getWindow().setAttributes(lp);
        mActionSheet2.setMenuBottomListener(new ActionSheet.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                OperateUtils.CancelFavor(account, SetInfoActivity.this, SetInfoActivity.this, new SimpleCallBack() {
                    @Override
                    public void onSuccess() {
                        showToast("删除成功");
                        Intent intent = new Intent();
                        intent.setAction(PersonInfoActivity.FINISH);
                        sendBroadcast(intent);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onBackData(Object o) {

                    }
                });

            }

            @Override
            public void onCancel() {

            }
        });
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(IntentStatic.DATA, crush.getState2());
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }
}
