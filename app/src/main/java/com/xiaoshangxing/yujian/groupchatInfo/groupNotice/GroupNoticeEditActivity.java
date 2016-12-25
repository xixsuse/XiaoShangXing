package com.xiaoshangxing.yujian.groupchatInfo.groupNotice;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.utils.customView.dialog.DialogLocationAndSize;
import com.xiaoshangxing.utils.customView.dialog.DialogUtils;
import com.xiaoshangxing.yujian.IM.cache.TeamDataCache;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * modified by FengChaoQun on 2016/12/24 19:16
 * description:优化代码
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
                if (editText.getText().toString().equals(team.getAnnouncement())) {
                    finish.setAlpha((float) 0.5);
                    finish.setEnabled(false);
                } else {
                    finish.setAlpha(1);
                    finish.setEnabled(true);
                }
            }
        });
    }

    private void parseData() {
        account = getIntent().getStringExtra(IntentStatic.ACCOUNT);
        if (account == null) {
            Toast.makeText(GroupNoticeEditActivity.this, "群数据异常", Toast.LENGTH_SHORT).show();
            finish();
        }

        team = TeamDataCache.getInstance().getTeamById(account);
        if (team == null) {
            Toast.makeText(GroupNoticeEditActivity.this, "群数据异常", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (!TextUtils.isEmpty(team.getAnnouncement())) {
            try {
                JSONObject jsonObject = new JSONObject(team.getAnnouncement());
                editText.setText(jsonObject.getString(NS.CONTENT));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
                                dialogUtils.close();
                            }
                        }).create();
                alertDialog.show();
                DialogLocationAndSize.setWidth(alertDialog, R.dimen.x780);
                break;
        }
    }

    private void save() {
        showLoadingDialog("正在保存");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.CONTENT, editText.getText().toString());
        jsonObject.addProperty(NS.CREATOR, TempUser.getId());
        jsonObject.addProperty(NS.TIME, NS.currentTime());
        NIMClient.getService(TeamService.class).updateTeam(account, TeamFieldEnum.Announcement, jsonObject.toString()).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                hideLoadingDialog();
                noticeDialog("已保存");
            }

            @Override
            public void onFailed(int code) {
                if (code == ResponseCode.RES_TEAM_ENACCESS) {
                    Toast.makeText(GroupNoticeEditActivity.this, "没有权限", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GroupNoticeEditActivity.this, "保存失败:" + code,
                            Toast.LENGTH_SHORT).show();
                }
                hideLoadingDialog();
            }

            @Override
            public void onException(Throwable exception) {
                hideLoadingDialog();
            }
        });
    }

    public void noticeDialog(String message) {
        DialogUtils.Dialog_No_Button dialog_no_button = new DialogUtils.Dialog_No_Button(this, message);
        final Dialog alertDialog = dialog_no_button.create();
        alertDialog.show();
        DialogLocationAndSize.setWidth(alertDialog, R.dimen.x420);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                alertDialog.dismiss();
                finish();
            }
        }, 1000);
    }

}
