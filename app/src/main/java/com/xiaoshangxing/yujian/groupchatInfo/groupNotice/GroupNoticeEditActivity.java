package com.xiaoshangxing.yujian.groupchatInfo.groupNotice;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogLocationAndSize;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.yujian.IM.cache.SimpleCallback;
import com.xiaoshangxing.yujian.IM.cache.TeamDataCache;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 15828 on 2016/8/12.
 */
public class GroupNoticeEditActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.groupNoticeEdit_back)
    TextView back;
    @Bind(R.id.groupNoticeEdit_finish)
    TextView finish;
    @Bind(R.id.groupNotice_editText)
    EditText editText;

    private String account;
    private Team team;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_groupnotice_edit);
        ButterKnife.bind(this);
        finish.setEnabled(false);
        back.setOnClickListener(this);
        finish.setOnClickListener(this);

        parseData();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    finish.setAlpha(1);
                    finish.setEnabled(true);
                } else {
                    finish.setAlpha((float) 0.5);
                    finish.setEnabled(false);
                }
            }
        });


    }

    private void parseData() {
        account = getIntent().getStringExtra(IntentStatic.EXTRA_ACCOUNT);
        if (account == null) {
            Toast.makeText(GroupNoticeEditActivity.this, "群数据异常", Toast.LENGTH_SHORT).show();
            finish();
        }

        TeamDataCache.getInstance().fetchTeamById(account, new SimpleCallback<Team>() {
            @Override
            public void onResult(boolean success, Team result) {
                if (success) {
                    team = result;
                    editText.setText(result.getAnnouncement());
                } else {
                    showToast("获取公告失败");
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.groupNoticeEdit_back:
                if (!editText.getText().toString().equals("")) {
                    final DialogUtils.Dialog_Center2 dialogUtils = new DialogUtils.Dialog_Center2(this);
                    final Dialog alertDialog = dialogUtils.Message("退出本次编辑？")
                            .Button("继续编辑", "退出").MbuttonOnClick(new DialogUtils.Dialog_Center2.buttonOnClick() {
                                @Override
                                public void onButton1() {
                                    dialogUtils.close();
                                }

                                @Override
                                public void onButton2() {
                                    finish();
                                }
                            }).create();
                    alertDialog.show();
                    DialogLocationAndSize.setWidth(alertDialog, R.dimen.x780);
                } else {
                    finish();
                }
                break;
            case R.id.groupNoticeEdit_finish:
                final DialogUtils.Dialog_Center2 dialogUtils = new DialogUtils.Dialog_Center2(this);
                final Dialog alertDialog = dialogUtils.Message("该公告会通知全部群成员，是否\n发布？")
                        .Button("取消", "发布").MbuttonOnClick(new DialogUtils.Dialog_Center2.buttonOnClick() {
                            @Override
                            public void onButton1() {
                                dialogUtils.close();
                            }

                            @Override
                            public void onButton2() {
                                save();
                            }
                        }).create();
                alertDialog.show();
                DialogLocationAndSize.setWidth(alertDialog, R.dimen.x780);
                break;
        }
    }

    private void save() {
        NIMClient.getService(TeamService.class).updateTeam(account, TeamFieldEnum.Announcement, editText.getText().toString()).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                Toast.makeText(GroupNoticeEditActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailed(int code) {
                if (code == ResponseCode.RES_TEAM_ENACCESS) {
                    Toast.makeText(GroupNoticeEditActivity.this, "没有权限", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GroupNoticeEditActivity.this, "保存失败:" + code,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
            }
        });
    }



}
