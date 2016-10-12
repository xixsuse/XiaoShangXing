package com.xiaoshangxing.xiaoshang.Plan.PlanDetail;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.netUtil.OperateUtils;
import com.xiaoshangxing.Network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.input_activity.InputActivity;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.layout.Name;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/9/29
 */
public class PlanDetailActivity extends BaseActivity implements IBaseView {
    public static final String TAG = BaseFragment.TAG + "-PlanDetailActivity";
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.head_image)
    CirecleImage headImage;
    @Bind(R.id.name)
    Name name;
    @Bind(R.id.college)
    TextView college;
    @Bind(R.id.text)
    EmotinText text;
    @Bind(R.id.joined_count)
    TextView joinedCount;
    @Bind(R.id.full)
    TextView full;
    @Bind(R.id.orange)
    CirecleImage orange;
    @Bind(R.id.plan_name)
    TextView planName;
    @Bind(R.id.r4)
    CirecleImage r4;
    @Bind(R.id.people_limit)
    TextView peopleLimit;
    @Bind(R.id.blue6)
    CirecleImage blue6;
    @Bind(R.id.data)
    TextView data;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.up_lay)
    LinearLayout upLay;
    @Bind(R.id.invite)
    Button invite;
    @Bind(R.id.apply)
    Button apply;
    @Bind(R.id.launch_people)
    Name launchPeople;
    @Bind(R.id.complete)
    ImageView complete;

    private int published_id;
    private Published published;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plandetail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        if (!getIntent().hasExtra(IntentStatic.DATA)) {
            showToast("动态id错误");
            finish();
            return;
        }
        published_id = getIntent().getIntExtra(IntentStatic.DATA, -1);
        published = realm.where(Published.class).equalTo(NS.ID, published_id).findFirst();
        if (published == null) {
            showToast("没有该动态的消息");
            finish();
            return;
        }
        refreshPager();
    }

    private void refreshPager() {
        int userId = published.getUserId();
        UserInfoCache.getInstance().getHead(headImage, userId, this);
        UserInfoCache.getInstance().getName(name, userId);
        UserInfoCache.getInstance().getCollege(college, userId);
        time.setText(TimeUtil.getTimeShowString(published.getCreateTime(), false));
        text.setText(published.getText());
        joinedCount.setText("" + published.getJoinCount());
        planName.setText(published.getPlanName());
        complete.setVisibility(published.isAlive() ? View.GONE : View.VISIBLE);

        if (TextUtils.isEmpty(published.getPersonLimit())) {
            peopleLimit.setText("不限人数");
        } else {
            peopleLimit.setText(("0-" + published.getPersonLimit()));
        }

        if (!TextUtils.isEmpty(published.getPersonLimit())) {
            if (published.getPraiseCount() == Integer.valueOf(published.getPersonLimit())) {
                full.setVisibility(View.VISIBLE);
            } else {
                full.setVisibility(View.INVISIBLE);
            }
        } else {
            full.setVisibility(View.INVISIBLE);
        }

        data.setText(TimeUtil.getPlanDate(published.getCreateTime()) + "-" +
                TimeUtil.getPlanDate(Long.valueOf(published.getValidationDate())));
        UserInfoCache.getInstance().getName(launchPeople, userId);
    }

    public void showShareDialog() {
        final Dialog dialog = new Dialog(this, R.style.ActionSheetDialog);
        View view = View.inflate(this, R.layout.util_help_share_dialog, null);
        dialog.setContentView(view);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        Button button = (Button) view.findViewById(R.id.cancel);
        View xsx = view.findViewById(R.id.xsx);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        xsx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OperateUtils.Share(PlanDetailActivity.this, InputActivity.SHOOL_REWARD, published_id);
                dialog.dismiss();
            }
        });
        dialog.show();
        LocationUtil.bottom_FillWidth(this, dialog);
    }

    private void showTransmitDialog(final String id) {
        final Dialog dialog = new Dialog(this, R.style.ActionSheetDialog);
        View dialogView = View.inflate(this, R.layout.school_help_transmit_dialog, null);
        dialog.setContentView(dialogView);

        TextView cancle = (TextView) dialogView.findViewById(R.id.cancel);
        TextView send = (TextView) dialogView.findViewById(R.id.send);
        CirecleImage head = (CirecleImage) dialogView.findViewById(R.id.head_image);
        TextView name = (TextView) dialogView.findViewById(R.id.name);
        TextView college = (TextView) dialogView.findViewById(R.id.college);
        TextView text = (TextView) dialogView.findViewById(R.id.text);
        final EditText input = (EditText) dialogView.findViewById(R.id.input);

        int userId = published.getUserId();
        UserInfoCache.getInstance().getHead(head, userId, PlanDetailActivity.this);
        UserInfoCache.getInstance().getName(name, userId);
        UserInfoCache.getInstance().getCollege(college, userId);
        text.setText(published.getText());

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OperateUtils.Tranmit(published_id, NS.CATEGORY_PLAN, id, PlanDetailActivity.this, input.getText().toString(),
                        new SimpleCallBack() {
                            @Override
                            public void onSuccess() {
                                showTransmitSuccess("已分享");
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

    public void showTransmitSuccess(String text) {
        DialogUtils.Dialog_No_Button dialog_no_button =
                new DialogUtils.Dialog_No_Button(PlanDetailActivity.this, text);
        final Dialog notice_dialog = dialog_no_button.create();
        notice_dialog.show();
        LocationUtil.setWidth(PlanDetailActivity.this, notice_dialog,
                getResources().getDimensionPixelSize(R.dimen.x420));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notice_dialog.dismiss();
            }
        }, 500);
    }

    public void gotoSelectPeson() {
        Intent intent_selectperson = new Intent(PlanDetailActivity.this, SelectPersonActivity.class);
        intent_selectperson.putExtra(SelectPersonActivity.LIMIT, 1);
        startActivityForResult(intent_selectperson, SelectPersonActivity.SELECT_PERSON_CODE);
    }

    @OnClick({R.id.back, R.id.more, R.id.invite, R.id.apply})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.more:
                showShareDialog();
                break;
            case R.id.invite:
                gotoSelectPeson();
                break;
            case R.id.apply:
                apply();
                break;
        }
    }

    private void apply() {

        if (published.getUserId() == TempUser.id) {
            showToast("不能申请加入自己发起的计划");
            return;
        }

        if (published.getStatus() == 0) {
            showToast("计划已结束,不能申请");
            return;
        }

        OperateUtils.Tranmit(published_id, NS.APPLY_PLAN, String.valueOf(published.getUserId()), PlanDetailActivity.this, "",
                new SimpleCallBack() {
                    @Override
                    public void onSuccess() {
                        showTransmitSuccess("已申请");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onBackData(Object o) {

                    }
                });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SelectPersonActivity.SELECT_PERSON_CODE) {
            if (data != null) {
                if (data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON).size() > 0) {
                    List<String> list = data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON);
                    showTransmitDialog(list.get(0));
                } else {
                    Toast.makeText(PlanDetailActivity.this, "未选择联系人", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }
}
