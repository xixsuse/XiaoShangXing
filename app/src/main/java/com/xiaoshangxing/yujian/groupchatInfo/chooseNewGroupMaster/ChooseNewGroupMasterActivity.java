package com.xiaoshangxing.yujian.groupchatInfo.chooseNewGroupMaster;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.CharacterParser;
import com.xiaoshangxing.SelectPerson.PinyinComparator;
import com.xiaoshangxing.SelectPerson.SideBar;
import com.xiaoshangxing.SelectPerson.SortModel;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.yujian.IM.NimUIKit;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;
import com.xiaoshangxing.yujian.IM.cache.TeamDataCache;
import com.xiaoshangxing.yujian.groupchatInfo.Member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 15828 on 2016/8/13.
 */
public class ChooseNewGroupMasterActivity extends BaseActivity {
    private ListView sortListView;
    private SideBar sideBar;
    private ChooseNewMasterAdapter adapter;
    private TextView dialog;
    private CharacterParser characterParser;
    private List<Member> dataList = new ArrayList<>();  //传过来的数据
    private List<SortModel> SourceDateList = new ArrayList<>();
    private PinyinComparator pinyinComparator;
    private String account;
    private Team team;
    private List<String> members = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_newgroupmaster);
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        account = getIntent().getStringExtra(IntentStatic.EXTRA_ACCOUNT);
        if (account == null) {
            showToast("账号有误");
            return;
        }
        team = TeamDataCache.getInstance().getTeamById(account);
        if (team == null) {
            showToast("账号有误");
            return;
        }

//        获取群成员列表 排除自己
        List<TeamMember> teamMembers = TeamDataCache.getInstance().getTeamMemberList(account);
        if (teamMembers != null && teamMembers.size() > 0) {
            for (int i = 0; i < teamMembers.size(); i++) {
                String teanNumberAccount = teamMembers.get(i).getAccount();
                if (TextUtils.isEmpty(teanNumberAccount) || teanNumberAccount.equals(NimUIKit.getAccount())) {
                    continue;
                }
                members.add(teanNumberAccount);
            }
        }

        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                if (s.contains("☆")) {
                    sortListView.setSelection(0);
                    return;
                }
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });
        sortListView = (ListView) findViewById(R.id.listview);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                String a = String.format("确定选择%s为新群主，你将自\n动放弃群主身份",
                        ((SortModel) adapter.getItem(position)).getName());
                final DialogUtils.Dialog_Center2 dialogUtils = new DialogUtils.Dialog_Center2(ChooseNewGroupMasterActivity.this);
                final Dialog alertDialog = dialogUtils.Message(a)
                        .Button("取消", "确定").MbuttonOnClick(new DialogUtils.Dialog_Center2.buttonOnClick() {
                            @Override
                            public void onButton1() {
                                dialogUtils.close();
                            }

                            @Override
                            public void onButton2() {
                                dialogUtils.close();
                                NIMClient.getService(TeamService.class)
                                        .transferTeam(account, members.get(position), false)
                                        .setCallback(new RequestCallback<List<TeamMember>>() {
                                            @Override
                                            public void onSuccess(List<TeamMember> teamMembers) {
                                                showToast("转让成功");
                                                finish();
                                            }

                                            @Override
                                            public void onFailed(int i) {
                                                showToast("转让失败:" + i);
                                            }

                                            @Override
                                            public void onException(Throwable throwable) {
                                                showToast("转让失败:异常");
                                                throwable.printStackTrace();
                                            }
                                        });
                            }
                        }).create();
                alertDialog.show();
//                LocationUtil.setWidth(ChooseNewGroupMasterActivity.this, alertDialog,
//                        getResources().getDimensionPixelSize(R.dimen.x780));
            }
        });
        SourceDateList = filledData(members);
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new ChooseNewMasterAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);
    }

    private List<SortModel> filledData(List<String> date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();
        for (int i = 0; i < date.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(NimUserInfoCache.getInstance().getUserDisplayName(date.get(i)));
            sortModel.setAccount(date.get(i));
            String pinyin = characterParser.getSelling(sortModel.getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }
            mSortList.add(sortModel);
        }
        return mSortList;
    }
}
