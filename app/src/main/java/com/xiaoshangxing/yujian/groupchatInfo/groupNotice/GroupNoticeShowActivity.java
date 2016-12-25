package com.xiaoshangxing.yujian.groupchatInfo.groupNotice;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nimlib.sdk.team.model.Team;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.utils.customView.CirecleImage;
import com.xiaoshangxing.yujian.IM.cache.TeamDataCache;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/8/13.
 */
public class GroupNoticeShowActivity extends BaseActivity {
    @Bind(R.id.groupNoticeShow_leftarrow)
    ImageView groupNoticeShowLeftarrow;
    @Bind(R.id.groupNoticeShow_back)
    TextView groupNoticeShowBack;
    @Bind(R.id.head_image)
    CirecleImage headImage;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.view1)
    View view1;
    @Bind(R.id.view2)
    View view2;
    @Bind(R.id.groupNoticeShow_edit)
    TextView groupNoticeShowEdit;
    @Bind(R.id.NoticeContent)
    TextView NoticeContent;
    private String teamAccount;
    private Team team;
    private String ownerAccount;
    private TeamDataCache.TeamDataChangedObserver observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_groupnotice_show);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObserver(false);
    }

    private void initView() {
        teamAccount = getIntent().getStringExtra(IntentStatic.ACCOUNT);
        if (TextUtils.isEmpty(teamAccount)) {
            showToast("群信息有误");
            return;
        }
        team = TeamDataCache.getInstance().getTeamById(teamAccount);
        if (team == null) {
            showToast("群信息有误");
            return;
        }
        registerObserver(true);
        refresh();
    }

    private void refresh() {

        if (TextUtils.isEmpty(team.getAnnouncement())) {
            showToast("没有公告");
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(team.getAnnouncement());
            ownerAccount = jsonObject.getString(NS.CREATOR);
            UserInfoCache.getInstance().getHeadIntoImage(ownerAccount, headImage);
            UserInfoCache.getInstance().getExIntoTextview(ownerAccount, NS.USER_NAME, name);
            headImage.setIntent_type(CirecleImage.PERSON_INFO, ownerAccount);
            time.setText(TimeUtil.getTimeShowString(jsonObject.getLong(NS.TIME), false));
            if (TextUtils.isEmpty(jsonObject.getString(NS.CONTENT))) {
                NoticeContent.setText("");
            } else {
                NoticeContent.setText(jsonObject.getString(NS.CONTENT));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (ownerAccount.equals(TempUser.getId())) {
            groupNoticeShowEdit.setVisibility(View.VISIBLE);
        } else {
            groupNoticeShowEdit.setVisibility(View.GONE);
        }
    }

    private void registerObserver(boolean is) {
        if (observer == null) {
            observer = new TeamDataCache.TeamDataChangedObserver() {
                @Override
                public void onUpdateTeams(List<Team> teams) {
                    for (Team team1 : teams) {
                        if (team1.getId().equals(teamAccount)) {
                            team = team1;
                            refresh();
                            break;
                        }
                    }
                }

                @Override
                public void onRemoveTeam(Team team) {

                }
            };
        }
        if (is) {
            TeamDataCache.getInstance().registerTeamDataChangedObserver(observer);
        } else {
            TeamDataCache.getInstance().unregisterTeamDataChangedObserver(observer);
        }
    }

    @OnClick({R.id.groupNoticeShow_back, R.id.groupNoticeShow_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.groupNoticeShow_back:
                finish();
                break;
            case R.id.groupNoticeShow_edit:
                Intent intent = new Intent(this, GroupNoticeEditActivity.class);
                intent.putExtra(IntentStatic.ACCOUNT, teamAccount);
                startActivity(intent);
                break;
        }
    }
}
