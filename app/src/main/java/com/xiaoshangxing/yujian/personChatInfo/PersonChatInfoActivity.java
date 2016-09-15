package com.xiaoshangxing.yujian.personChatInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.Team;
import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.data.TopChat;
import com.xiaoshangxing.setting.currency.chatBackground.ChatBackgroundActivity;
import com.xiaoshangxing.setting.utils.ActionSheet;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.SwitchView;
import com.xiaoshangxing.yujian.IM.TeamCreateHelper;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;
import com.xiaoshangxing.yujian.YujianFragment.YuJianFragment;
import com.xiaoshangxing.yujian.groupchatInfo.Member;
import com.xiaoshangxing.yujian.groupchatInfo.MyGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by tianyang on 2016/8/22.
 */
public class PersonChatInfoActivity extends BaseActivity implements IBaseView {

    @Bind(R.id.person_chatinfo_gridview)
    MyGridView mGridview;
    @Bind(R.id.chatinfo_leftarrow)
    ImageView chatinfoLeftarrow;
    @Bind(R.id.chatinfo_back)
    TextView chatinfoBack;
    @Bind(R.id.topChat)
    SwitchView topChat;
    @Bind(R.id.noDisturb)
    SwitchView noDisturb;
    @Bind(R.id.SetChatBackGroundView)
    RelativeLayout SetChatBackGroundView;
    @Bind(R.id.CleanChatRecordView)
    RelativeLayout CleanChatRecordView;
    private ActionSheet mActionSheet1;
    private PersonalAdapter adapter;
    private List<Member> data = new ArrayList<>();
    private String account;
    private Realm realm;
    private boolean notice;

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_personchatinfo);
        ButterKnife.bind(this);

        account = getIntent().getStringExtra(IntentStatic.EXTRA_ACCOUNT);
        String headPath = NimUserInfoCache.getInstance().getHeadImage(account);
        String name = NimUserInfoCache.getInstance().getUserDisplayName(account);
        for (int i = 0; i < 1; i++) {
            Member member = new Member();
            member.setHeadPath(headPath);
            member.setName(name);
            member.setAccount(account);
            data.add(member);
        }
        adapter = new PersonalAdapter(this, data, this, account);
        mGridview.setAdapter(adapter);
        realm = Realm.getDefaultInstance();
        ZhiDing();
        notice = NIMClient.getService(FriendService.class).isNeedMessageNotify(account);
        MessageMute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void ZhiDing() {

        TopChat mytopChat = realm.where(TopChat.class).equalTo("account", account).findFirst();
        if (mytopChat == null) {
            topChat.setState(false);
        } else {
            topChat.setState(true);
        }

        topChat.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        TopChat topchat = new TopChat();
                        topchat.setAccount(account);
                        realm.copyToRealmOrUpdate(topchat);
                        Log.d("copy_to_realm", realm.where(TopChat.class).toString());
                        topChat.setState(true);

                        NIMClient.getService(MsgService.class).queryRecentContacts().
                                setCallback(new RequestCallbackWrapper<List<RecentContact>>() {

                                    @Override
                                    public void onResult(int code, List<RecentContact> recents, Throwable exception) {
                                        if (code != ResponseCode.RES_SUCCESS || recents == null) {
                                            return;
                                        }
                                        for (int i = 0; i < recents.size(); i++) {
                                            if (recents.get(i).getContactId().equals(account)) {
                                                recents.get(i).setTag(YuJianFragment.RECENT_TAG_STICKY);
                                                NIMClient.getService(MsgService.class).updateRecent(recents.get(i));
                                                Log.d("置顶", "--" + recents.get(i).getContactId() + "tag" + recents.get(i).getTag());
                                            }
                                        }
                                    }
                                });
                        showToast("置顶成功");

                    }
                });
            }

            @Override
            public void toggleToOff() {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        TopChat mytopChat = realm.where(TopChat.class).equalTo("account", account).findFirst();
                        if (mytopChat != null) {
                            mytopChat.deleteFromRealm();
                        }
                        topChat.setState(false);
                        NIMClient.getService(MsgService.class).queryRecentContacts().
                                setCallback(new RequestCallbackWrapper<List<RecentContact>>() {

                                    @Override
                                    public void onResult(int code, List<RecentContact> recents, Throwable exception) {
                                        if (code != ResponseCode.RES_SUCCESS || recents == null) {
                                            return;
                                        }
                                        for (int i = 0; i < recents.size(); i++) {
                                            if (recents.get(i).getContactId().equals(account)) {
                                                recents.get(i).setTag(0);
                                                NIMClient.getService(MsgService.class).updateRecent(recents.get(i));
                                                Log.d("置顶", "--" + recents.get(i).getContactId() + "tag" + recents.get(i).getTag());
                                            }
                                        }
                                    }
                                });
                        showToast("取消置顶");
                    }
                });
            }
        });
    }

    //    消息免打扰
    private void MessageMute() {
        noDisturb.setState(!notice);
        Log.d("消息免打扰", "" + !notice);
        noDisturb.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                NIMClient.getService(FriendService.class).setMessageNotify(account, false)
                        .setCallback(new RequestCallback<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showToast("设置成功,现在不接受该用户消息通知");
                                noDisturb.setState(true);
                            }

                            @Override
                            public void onFailed(int i) {
                                showToast("设置失败:" + i);
                            }

                            @Override
                            public void onException(Throwable throwable) {
                                showToast("设置失败:异常");
                            }
                        });

            }

            @Override
            public void toggleToOff() {
                NIMClient.getService(FriendService.class).setMessageNotify(account, true)
                        .setCallback(new RequestCallback<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showToast("设置成功,现在接受该用户消息通知");
                                noDisturb.setState(false);
                            }

                            @Override
                            public void onFailed(int i) {
                                showToast("设置失败:" + i);
                            }

                            @Override
                            public void onException(Throwable throwable) {
                                showToast("设置失败:异常");
                            }
                        });
            }
        });
    }

    public void Back(View view) {
        finish();
    }

    public void SetChatBackGround(View view) {
        Intent intent = new Intent(this, ChatBackgroundActivity.class);
        startActivity(intent);
    }

    public void CleanChatRecord(View view) {
        if (mActionSheet1 == null) {
            mActionSheet1 = new ActionSheet(this);
            mActionSheet1.addMenuBottomItem("清空聊天记录");
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
                NIMClient.getService(MsgService.class).clearChattingHistory(account, SessionTypeEnum.P2P);
                Toast.makeText(PersonChatInfoActivity.this, item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SelectPersonActivity.SELECT_PERSON_CODE) {
            if (data == null) {
                Toast.makeText(PersonChatInfoActivity.this, "没有选择联系人", Toast.LENGTH_SHORT).show();
                return;
            }
            ArrayList<String> arrayList = data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON);
            if (arrayList == null || arrayList.size() == 0) {
//                Toast.makeText(PersonChatInfoActivity.this, "没有选择联系人", Toast.LENGTH_SHORT).show();
            } else {
                arrayList.add(account);
                Log.d("select account", arrayList.toString());
                TeamCreateHelper.createAdvancedTeam(PersonChatInfoActivity.this,
                        arrayList, this, new RequestCallback<Team>() {
                            @Override
                            public void onSuccess(Team team) {

                            }

                            @Override
                            public void onFailed(int i) {

                            }

                            @Override
                            public void onException(Throwable throwable) {

                            }
                        });
            }
        }
    }
}
