package com.xiaoshangxing;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.xiaoshangxing.network.AutoUpdate.UpdateManager;
import com.xiaoshangxing.network.UploadLogUtil;
import com.xiaoshangxing.network.netUtil.AppNetUtil;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.publicActivity.inputActivity.InputBoxLayout;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.NotifycationUtil;
import com.xiaoshangxing.utils.XSXApplication;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.utils.customView.CirecleImage;
import com.xiaoshangxing.utils.normalUtils.DeviceLog;
import com.xiaoshangxing.utils.normalUtils.NetUtils;
import com.xiaoshangxing.utils.normalUtils.SPUtils;
import com.xiaoshangxing.wo.NewsActivity.NewsActivity;
import com.xiaoshangxing.wo.WoFrafment.WoFragment;
import com.xiaoshangxing.xiaoshang.MessageNotice.MessageNoticeActivity;
import com.xiaoshangxing.xiaoshang.XiaoShangFragment.XiaoShangFragment;
import com.xiaoshangxing.yujian.ChatActivity.ChatActivity;
import com.xiaoshangxing.yujian.ChatActivity.GroupActivity;
import com.xiaoshangxing.yujian.FriendActivity.FriendActivity;
import com.xiaoshangxing.yujian.IM.kit.permission.MPermission;
import com.xiaoshangxing.yujian.IM.kit.permission.annotation.OnMPermissionDenied;
import com.xiaoshangxing.yujian.IM.kit.permission.annotation.OnMPermissionGranted;
import com.xiaoshangxing.yujian.IM.kit.reminder.ReminderItem;
import com.xiaoshangxing.yujian.IM.kit.reminder.ReminderManager;
import com.xiaoshangxing.yujian.IM.kit.reminder.SystemMessageUnreadManager;
import com.xiaoshangxing.yujian.YujianFragment.YuJianFragment;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/6/21
 */
public class MainActivity extends BaseActivity implements ReminderManager.UnreadNumChangedCallback {
    public static final String TAG = BaseActivity.TAG + "-MainActivity";
    private static final int BAIDU_READ_PHONE_STATE = 1000;
    private final int BASIC_PERMISSION_REQUEST_CODE = 100;
    @Bind(R.id.image_xiaoshang)
    ImageView imageXiaoshang;
    @Bind(R.id.xiaoshang)
    TextView xiaoshang;
    @Bind(R.id.xiaoshang_lay)
    RelativeLayout xiaoshangLay;
    @Bind(R.id.image_yujian)
    ImageView imageYujian;
    @Bind(R.id.yujian)
    TextView yujian;
    @Bind(R.id.yujian_lay)
    RelativeLayout yujianLay;
    @Bind(R.id.image_wo)
    ImageView imageWo;
    @Bind(R.id.wo)
    TextView wo;
    @Bind(R.id.wolay)
    RelativeLayout wolay;
    @Bind(R.id.radio_layout)
    LinearLayout radioLayout;
    @Bind(R.id.xiaoshang_dot)
    CirecleImage xiaoshangDot;
    @Bind(R.id.yujian_dot)
    CirecleImage yujianDot;
    @Bind(R.id.wo_dot)
    CirecleImage woDot;
    private int current;
    private WoFragment woFragment;
    private XiaoShangFragment xiaoShangFragment;
    private YuJianFragment yuJianFragment;
    private InputBoxLayout inputBoxLayout;
    private AbortableFuture<LoginInfo> loginRequest;

    private float x1, y1, move_x, move_y;
    private Observer<StatusCode> statusCodeObserver;

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);
        setEnableRightSlide(false);
        requestBasicPermission();
        doSomethingWhenEnter();
        initInputBox();
        initAllFragments();
        onParseIntent();
        update();
        ObservalOlineState(true);
        initDot();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerMsgUnreadInfoObserver(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerMsgUnreadInfoObserver(false);
        ObservalOlineState(false);
    }

    private void initDot() {
        xiaoshangDot.setVisibility(View.GONE);
        yujianDot.setVisibility(View.GONE);
        woDot.setVisibility(View.GONE);
    }

    private void doSomethingWhenEnter() {
        TempUser.initTempUser(this);
        //第一次进入APP  收集设备信息
        if ((boolean) SPUtils.get(this, SPUtils.IS_FIRS_COME, true)) {
            DeviceLog deviceLog = new DeviceLog(this);
            deviceLog.saveDevieceLog();
        }
        SPUtils.put(this, SPUtils.IS_FIRS_COME, false);//当这个页面打开时，表明不是第一次进入APP了
        SPUtils.put(this, SPUtils.IS_QUIT, false);//当这个页面打开时，清除退出记录
        SPUtils.put(this, SPUtils.IS_NEED_GUIDE, false);//当这个页面打开时，表示已看过引导页
        //上传日志文件
        if (NetUtils.isConnected(this)) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        UploadLogUtil.doUpload();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }

    private void onParseIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            IMMessage message = (IMMessage) getIntent().getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
            switch (message.getSessionType()) {
                case P2P:
                    ChatActivity.start(this, message.getSessionId(), null, SessionTypeEnum.P2P);
                    break;
                case Team:
                    GroupActivity.start(this, message.getSessionId(), null, SessionTypeEnum.Team);
                    break;
                default:
                    break;
            }
        } else if (intent.hasExtra(IntentStatic.TYPE)) {
            switch (intent.getIntExtra(IntentStatic.TYPE, -1)) {
                case NotifycationUtil.NOTIFY_XIAOSHANG:
                    xiaoshangLay.performClick();
                    startActivity(new Intent(this, MessageNoticeActivity.class));
                    break;
                case NotifycationUtil.NOTIFY_STARTED:
                    yujianLay.performClick();
                    Intent loveIntent = new Intent(this, FriendActivity.class);
                    loveIntent.putExtra(IntentStatic.TYPE, FriendActivity.gotoStar);
                    startActivity(loveIntent);
                    break;
                case NotifycationUtil.NOTIFY_FRIEND:
                    yujianLay.performClick();
                    startActivity(new Intent(this, FriendActivity.class));
                    break;
                case NotifycationUtil.NOTIFY_WO:
                    wolay.performClick();
                    startActivity(new Intent(this, NewsActivity.class));
                    break;
                default:
                    setYUjian(true);
            }
        }
    }

    private void requestBasicPermission() {
        MPermission.with(MainActivity.this)
                .addRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_PHONE_STATE,
                        android.Manifest.permission.RECORD_AUDIO,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                )
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
//        Login();
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
    }

    private void initInputBox() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.edit_and_emot);
        inputBoxLayout = new InputBoxLayout(this, relativeLayout, this);
    }

    private void initAllFragments() {
        Fragment frag;
        frag = mFragmentManager.findFragmentByTag(WoFragment.TAG);
        woFragment = (frag == null) ? WoFragment.newInstance() : (WoFragment) frag;

        frag = mFragmentManager.findFragmentByTag(XiaoShangFragment.TAG);
        xiaoShangFragment = (frag == null) ? XiaoShangFragment.newInstance() : (XiaoShangFragment) frag;

        frag = mFragmentManager.findFragmentByTag(YuJianFragment.TAG);
        yuJianFragment = (frag == null) ? YuJianFragment.newInstance() : (YuJianFragment) frag;

        if (!xiaoShangFragment.isAdded()) {
            mFragmentManager.beginTransaction().add(R.id.main_fragment, xiaoShangFragment, XiaoShangFragment.TAG)
                    .commit();
        }
        if (!woFragment.isAdded()) {
            mFragmentManager.beginTransaction().add(R.id.main_fragment, woFragment, WoFragment.TAG)
                    .commit();
        }

        if (!yuJianFragment.isAdded()) {
            mFragmentManager.beginTransaction().add(R.id.main_fragment, yuJianFragment, YuJianFragment.TAG)
                    .commit();
        }

        setXiaoshang(true);
    }

    private void update() {
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        String version = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (!TextUtils.isEmpty(version)) {
            UpdateManager updateManager = new UpdateManager(this, version, false);
            updateManager.checkUpdateInfo();
        }

    }

    @OnClick({R.id.xiaoshang_lay, R.id.yujian_lay, R.id.wolay, R.id.emotion, R.id.normal_emot, R.id.favorite
            , R.id.send, R.id.delete_emot})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.xiaoshang_lay:
                if (current == 1) {
                    return;
                } else {
                    setXiaoshang(true);
                    setYUjian(false);
                    setWo(false);
                }

                break;
            case R.id.yujian_lay:
                if (current == 2) {
                    return;
                } else {
                    setXiaoshang(false);
                    setYUjian(true);
                    setWo(false);
                }
                break;
            case R.id.wolay:
                if (current == 3) {
                    return;
                } else {
                    setXiaoshang(false);
                    setYUjian(false);
                    setWo(true);
                }
                break;
        }
    }

    private void setWo(boolean is) {
        if (is) {
            imageWo.setImageResource(R.mipmap.wo_on);
            wo.setTextColor(getResources().getColor(R.color.green1));

            mFragmentManager.beginTransaction().hide(xiaoShangFragment).hide(yuJianFragment).show(woFragment)
                    .commitAllowingStateLoss();
            current = 3;
        } else {
            imageWo.setImageResource(R.mipmap.wo_off);
            wo.setTextColor(getResources().getColor(R.color.g0));
        }

    }

    private void setYUjian(boolean is) {
        if (is) {
            imageYujian.setImageResource(R.mipmap.yujian_on);
            yujian.setTextColor(getResources().getColor(R.color.green1));
            mFragmentManager.beginTransaction().hide(woFragment).hide(xiaoShangFragment).show(yuJianFragment)
                    .commitAllowingStateLoss();
            current = 2;
        } else {
            imageYujian.setImageResource(R.mipmap.yujian_off);
            yujian.setTextColor(getResources().getColor(R.color.g0));
        }

    }

    private void setXiaoshang(boolean is) {
        if (is) {
            imageXiaoshang.setImageResource(R.mipmap.xiaoshang_on);
            xiaoshang.setTextColor(getResources().getColor(R.color.green1));

            mFragmentManager.beginTransaction().hide(woFragment).hide(yuJianFragment).show(xiaoShangFragment)
                    .commitAllowingStateLoss();
            current = 1;
        } else {
            imageXiaoshang.setImageResource(R.mipmap.xiaoshang_off);
            xiaoshang.setTextColor(getResources().getColor(R.color.g0));
        }
    }

    public InputBoxLayout getInputBoxLayout() {
        return inputBoxLayout;
    }

    /**
     * 注册未读消息数量观察者
     */
    private void registerMsgUnreadInfoObserver(boolean register) {
        if (register) {
            ReminderManager.getInstance().registerUnreadNumChangedCallback(this);
        } else {
            ReminderManager.getInstance().unregisterUnreadNumChangedCallback(this);
        }
    }

    /**
     * 未读消息数量观察者实现
     */
    @Override
    public void onUnreadNumChanged(ReminderItem item) {
        int unread = item.unread() + SystemMessageUnreadManager.getInstance().getSysMsgUnreadCount();
//        yujianDot.setVisibility(unread > 0 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        onParseIntent();
    }

    @Override
    public String toString() {
        return TAG;
    }

    private void ObservalOlineState(boolean is) {
        if (is) {
            if (statusCodeObserver == null) {
                statusCodeObserver = new Observer<StatusCode>() {
                    @Override
                    public void onEvent(StatusCode statusCode) {
                        if (statusCode.wontAutoLogin()) {
                            if (statusCode == StatusCode.KICKOUT || statusCode == StatusCode.KICK_BY_OTHER_CLIENT) {
                                AppNetUtil.KitOut(XSXApplication.currentActivity);
                            }
                        }
                    }
                };
            }
        }
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(statusCodeObserver, is);
    }

}
