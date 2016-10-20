package com.xiaoshangxing.yujian.personInfo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.constant.FriendFieldEnum;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.report.ReportActivity;
import com.xiaoshangxing.setting.DataSetting;
import com.xiaoshangxing.setting.utils.ActionSheet;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.SwitchView;
import com.xiaoshangxing.utils.normalUtils.SPUtils;
import com.xiaoshangxing.yujian.IM.cache.FriendDataCache;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/7/25.
 */
public class SetInfoActivity extends BaseActivity {

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

        if (!FriendDataCache.getInstance().isMyFriend(account)) {
            initNotFriend();
            return;
        }

        friend = FriendDataCache.getInstance().getFriendByAccount(account);
        if (friend == null) {
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
                SPUtils.put(SetInfoActivity.this, "crush", true);
                crush.setState(true);
            }

            @Override
            public void toggleToOff() {
                SPUtils.put(SetInfoActivity.this, "crush", false);
                crush.setState(false);
            }
        });
        bukanwo.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                SPUtils.put(SetInfoActivity.this, "bukanwo", true);
                bukanwo.setState(true);
            }

            @Override
            public void toggleToOff() {
                SPUtils.put(SetInfoActivity.this, "bukanwo", false);
                bukanwo.setState(false);
            }
        });
        bukanta.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                SPUtils.put(SetInfoActivity.this, "bukanta", true);
                bukanta.setState(true);
            }

            @Override
            public void toggleToOff() {
                SPUtils.put(SetInfoActivity.this, "bukanta", false);
                bukanta.setState(false);
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

    private void setUpData() {
        if (friend != null && friend.getExtension() != null && friend.getExtension().containsKey(NS.MARK)) {
            starMarkfriends.setState((boolean) friend.getExtension().get(NS.MARK));
        } else {
            starMarkfriends.setState(false);
        }
        crush.setState(DataSetting.IsCrush(this));
        bukanwo.setState(DataSetting.IsBuKanWo(this));
        bukanta.setState(DataSetting.IsBuKanTa(this));
        addToBlackList.setState(DataSetting.IsAddToBlackList(this));
    }

    public void Report(View view) {
        Intent intent = new Intent(this, ReportActivity.class);
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
                NIMClient.getService(FriendService.class).deleteFriend(account).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast("删除成功");
                        Intent intent = new Intent();
                        intent.setAction(PersonInfoActivity.FINISH);
                        sendBroadcast(intent);
                        finish();
                    }

                    @Override
                    public void onFailed(int i) {
                        showToast("删除失败:" + i);
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        showToast("删除失败:异常");
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
}
