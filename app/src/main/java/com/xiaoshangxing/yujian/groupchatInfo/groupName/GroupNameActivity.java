package com.xiaoshangxing.yujian.groupchatInfo.groupName;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.yujian.IM.cache.TeamDataCache;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/8/12.
 */
public class GroupNameActivity extends BaseActivity {
    @Bind(R.id.groupChatName_save)
    TextView save;
    @Bind(R.id.groupChatName_edittext)
    EditText editText;
    @Bind(R.id.groupChatName_clear)
    ImageView clear;

    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_groupname);
        ButterKnife.bind(this);

        account = getIntent().getStringExtra(IntentStatic.ACCOUNT);
        if (account == null) {
            Toast.makeText(GroupNameActivity.this, "群id有误", Toast.LENGTH_SHORT).show();
            finish();
        }

        editText.setHint(TeamDataCache.getInstance().getTeamName(account));

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
                    save.setAlpha(1);
                    save.setClickable(true);
                    clear.setVisibility(View.VISIBLE);
                } else {
                    save.setAlpha((float) 0.5);
                    save.setClickable(false);
                    clear.setVisibility(View.GONE);
                }
            }
        });

    }


    @OnClick({R.id.groupChatName_back, R.id.groupChatName_save, R.id.groupChatName_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.groupChatName_back:
                finish();
                break;
            case R.id.groupChatName_save:
                saveName();
                break;
            case R.id.groupChatName_clear:
                editText.setText("");
                break;
        }
    }

    private void saveName() {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            Toast.makeText(this, "名字不能为空", Toast.LENGTH_SHORT).show();
        } else {
            char[] s = editText.getText().toString().toCharArray();
            int i;
            for (i = 0; i < s.length; i++) {
                if (String.valueOf(s[i]).equals(" ")) {
                    Toast.makeText(this, "名字不能含有空格", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            if (i == s.length) {
                saveTeamProperty();
            }
        }
    }

    /**
     * 保存设置
     */
    private void saveTeamProperty() {
        NIMClient.getService(TeamService.class).updateTeam(account, TeamFieldEnum.Name, editText.getText().toString()).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                Toast.makeText(GroupNameActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailed(int code) {
                if (code == ResponseCode.RES_TEAM_ENACCESS) {
                    Toast.makeText(GroupNameActivity.this, "没有权限", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GroupNameActivity.this, "保存失败:" + code,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
            }
        });
    }
}
