package com.xiaoshangxing.wo.NewsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.PushMsg;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.NotifycationUtil;
import com.xiaoshangxing.wo.StateDetailsActivity.DetailsActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.xiaoshangxing.utils.NotifycationUtil.NOTIFY_WO;
import static com.xiaoshangxing.utils.NotifycationUtil.NT_SCHOOLMATE_CHANGE;
import static com.xiaoshangxing.utils.NotifycationUtil.NT_SCHOOLMATE_NOTICE_YOU;

/**
 * Created by FengChaoQun
 * on 2016/7/11
 */
public class NewsActivity extends BaseActivity implements NewsContract.View {


    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_text)
    TextView rightText;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.listview)
    ListView listview;
    private NewsContract.Presenter mPresenter;
    private newsAdapter_realm adapter_realm;
    private RealmResults<PushMsg> pushMsgs;
    private NotifycationUtil.OnNotifyChange onNotifyChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        setmPresenter(new NewsPresenter(this, this));
        initView();
        doWhenEnter();
        initData();
        onNotifyChange = new NotifycationUtil.OnNotifyChange() {
            @Override
            public void onChange(PushMsg pushMsg) {
                if (pushMsg.getPushType().equals(NotifycationUtil.NT_SCHOOLMATE_NOTICE_YOU)
                        || pushMsg.getPushType().equals(NotifycationUtil.NT_SCHOOLMATE_CHANGE)) {
                    initData();
                    doWhenEnter();
                }
            }
        };
        NotifycationUtil.registerObserver(onNotifyChange);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotifycationUtil.unRegisterObserver(onNotifyChange);
    }

    private void initView() {
        title.setText("消息");
        rightText.setText("清空");
        titleBottomLine.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        pushMsgs = realm.where(PushMsg.class)
                .equalTo(NS.PUSH_TYPE, NT_SCHOOLMATE_CHANGE)
                .or().equalTo(NS.PUSH_TYPE, NT_SCHOOLMATE_NOTICE_YOU)
                .findAllSorted(NS.PUSH_TIME, Sort.DESCENDING);
        if (pushMsgs.isEmpty()) {
            return;
        }
        adapter_realm = new newsAdapter_realm(this, pushMsgs);
        listview.setAdapter(adapter_realm);
    }

    /**
     * 用户进入这个页面 则将本地的有关推送记录置为已读 并清除通知栏的有关通知
     */
    private void doWhenEnter() {
        final List<PushMsg> pushMsgs = realm.where(PushMsg.class).not().equalTo("isRead", "1")
                .beginGroup()
                .equalTo(NS.PUSH_TYPE, NT_SCHOOLMATE_CHANGE)
                .or().equalTo(NS.PUSH_TYPE, NT_SCHOOLMATE_NOTICE_YOU)
                .endGroup()
                .findAllSorted(NS.PUSH_TIME, Sort.DESCENDING);
        if (!pushMsgs.isEmpty()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (PushMsg i : pushMsgs) {
                        i.setIsRead("1");
                    }
                }
            });
        }
        NotifycationUtil.clearNotify(NOTIFY_WO);
    }


    @Override
    public void cleanData() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(PushMsg.class).equalTo(NS.PUSH_TYPE, NT_SCHOOLMATE_CHANGE)
                        .or().equalTo(NS.PUSH_TYPE, NT_SCHOOLMATE_NOTICE_YOU)
                        .findAllSorted(NS.PUSH_TIME, Sort.DESCENDING)
                        .deleteAllFromRealm();
                Log.d("清空校友圈推送信息", "ok");
                initData();
            }
        });
    }

    @Override
    public void showCleanDialog() {
        DialogUtils.DialogMenu2 dialogMenu2 = new DialogUtils.DialogMenu2(this);
        dialogMenu2.addMenuItem("清空所有消息");
        dialogMenu2.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                mPresenter.cleanData();
            }

            @Override
            public void onCancel() {

            }
        });
        dialogMenu2.initView();
        dialogMenu2.show();
        LocationUtil.bottom_FillWidth(this, dialogMenu2);
    }

    @Override
    public void clickOnClean() {
        if (!adapter_realm.isEmpty()) {
            showCleanDialog();
        }
    }

    @Override
    public void gotoDetail() {
        Intent detai_intent = new Intent(this, DetailsActivity.class);
        startActivity(detai_intent);
    }

    @Override
    public void setmPresenter(@Nullable NewsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @OnClick({R.id.back, R.id.right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right_text:
                clickOnClean();
                break;
        }
    }
}
