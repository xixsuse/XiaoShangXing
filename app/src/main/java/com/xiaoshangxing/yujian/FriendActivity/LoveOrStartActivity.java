package com.xiaoshangxing.yujian.FriendActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoshangxing.network.IMNetwork;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.bean.PushMsg;
import com.xiaoshangxing.data.bean.User;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.NotifycationUtil;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.utils.baseClass.IBaseView;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.yujian.Serch.NormalSerch.NormalSerch;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.Sort;
import okhttp3.ResponseBody;

import static com.xiaoshangxing.utils.NotifycationUtil.NT_IM_STARED;

/**
 * Created by FengChaoQun
 * on 2016/9/5
 */
public class LoveOrStartActivity extends BaseActivity implements IBaseView {


    public static final int LOVE = 0;
    public static final int STAR = 1;
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
    @Bind(R.id.serch_layout)
    RelativeLayout serchLayout;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.count)
    TextView count;
    private love_satr_adpter adpter;
    private int type;
    private List<User> users = new ArrayList<>();
    private NotifycationUtil.OnNotifyChange onNotifyChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_or_start);
        ButterKnife.bind(this);
        initView();
        onNotifyChange = new NotifycationUtil.OnNotifyChange() {
            @Override
            public void onChange(PushMsg pushMsg) {
                if (pushMsg.getPushType().equals(NotifycationUtil.NT_IM_STARED)) {
                    getData();
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

        back.setPadding(ScreenUtils.getAdapterPx(R.dimen.x30, this), 0, 0, 0);
        more.setVisibility(View.GONE);
        titleBottomLine.setVisibility(View.GONE);
        serchLayout.setPadding(ScreenUtils.getAdapterPx(R.dimen.x30, this), 0,
                ScreenUtils.getAdapterPx(R.dimen.x30, this), 0);

        if (!getIntent().hasExtra(IntentStatic.TYPE)) {
            showToast("跳转意图不明");
            finish();
        }

        type = getIntent().getIntExtra(IntentStatic.TYPE, LoveOrStartActivity.LOVE);
        if (type == LOVE) {
            title.setText("留心");
        } else {
            title.setText("星星");
        }
        getData();
        doWhenEnter();
    }

    private void refreshListview() {
        adpter = new love_satr_adpter(this, 1, users, type);
        listview.setAdapter(adpter);

        if (type == LoveOrStartActivity.LOVE) {
            count.setText(adpter.getCount() + "个我留心的人");
        } else {
            count.setText(adpter.getCount() + "个我的星星们");
        }
    }

    public void getData() {

        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(e.string());
                    switch (jsonObject.getInt(NS.CODE)) {
                        case 200:
                            Gson gson = new Gson();
                            users = gson.fromJson(jsonObject.getJSONArray(NS.MSG).toString(), new TypeToken<List<User>>() {
                            }.getType());
                            Log.d("users", users.toString());
                            refreshListview();
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

        ProgressSubsciber<ResponseBody> progressSubsciber = new ProgressSubsciber<>(onNext, this);
        if (type == LOVE) {
            IMNetwork.getInstance().MyFavor(progressSubsciber, String.valueOf(TempUser.id), this);
        } else {
            IMNetwork.getInstance().MyStar(progressSubsciber, String.valueOf(TempUser.id), this);
        }

    }

    /**
     * 用户进入这个页面 则删除所有本地的留心推送记录 并清除通知栏的留心通知
     */
    private void doWhenEnter() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(PushMsg.class)
                        .equalTo(NS.PUSH_TYPE, NT_IM_STARED)
                        .findAllSorted(NS.PUSH_TIME, Sort.DESCENDING)
                        .deleteAllFromRealm();
                Log.d("清除留心推送", "ok");
            }
        });
        NotifycationUtil.clearNotify(NotifycationUtil.NOTIFY_STARTED);
    }

    @OnClick({R.id.back, R.id.serch_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.serch_layout:
                NormalSerch.start(LoveOrStartActivity.this, type);
                break;
        }
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }
}
