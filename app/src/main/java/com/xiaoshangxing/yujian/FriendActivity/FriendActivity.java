package com.xiaoshangxing.yujian.FriendActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.CharacterParser;
import com.xiaoshangxing.SelectPerson.PinyinComparator;
import com.xiaoshangxing.SelectPerson.SideBar;
import com.xiaoshangxing.SelectPerson.SortModel;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.yujian.IM.cache.FriendDataCache;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;
import com.xiaoshangxing.yujian.IM.cache.TeamDataCache;
import com.xiaoshangxing.yujian.IM.kit.LoginSyncDataStatusObserver;
import com.xiaoshangxing.yujian.IM.uinfo.UserInfoHelper;
import com.xiaoshangxing.yujian.IM.uinfo.UserInfoObservable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/9/4
 */
public class FriendActivity extends BaseActivity {
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.myState)
    TextView myState;
    @Bind(R.id.title)
    RelativeLayout title;
    @Bind(R.id.listview)
    ListView sortListView;
    @Bind(R.id.dialog)
    TextView dialog;
    @Bind(R.id.sidrbar)
    SideBar sideBar;

    private FriendSortAdapter adapter;
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList = new ArrayList<>();
    private PinyinComparator pinyinComparator;
    private View group;
    private View love;
    private View star;
    private TextView group_count;
    private TextView love_count;
    private TextView star_count;

    private View headView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        sideBar.setTextView(dialog);
//        出去head
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                if (s.contains("☆")) {
                    sortListView.setSelection(0+1);
                    return;
                }
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position+1);
                }

            }
        });

        headView = View.inflate(this, R.layout.head_friend, null);
        group=headView.findViewById(R.id.group);
        love=headView.findViewById(R.id.love);
        star=headView.findViewById(R.id.star);
        sortListView.addHeaderView(headView);
        group_count=(TextView)headView.findViewById(R.id.group_count);
        love_count=(TextView)headView.findViewById(R.id.love_count);
        star_count=(TextView)headView.findViewById(R.id.star_count);
        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FriendActivity.this,LoveOrStartActivity.class);
                intent.putExtra(IntentStatic.TYPE,love_satr_adpter.LOVE);
                startActivity(intent);
            }
        });
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FriendActivity.this,LoveOrStartActivity.class);
                intent.putExtra(IntentStatic.TYPE,love_satr_adpter.STAR);
                startActivity(intent);
            }
        });
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FriendActivity.this,GroupListActivity.class);
                startActivity(intent);
            }
        });
        refreshData();

    }

    private void refreshData() {
        List<String> friends = FriendDataCache.getInstance().getMyFriendAccounts();
        String[] friendss = new String[friends.size()];
        for (int i = 0; i < friends.size(); i++) {
            friendss[i] = friends.get(i);
        }
        SourceDateList = filledData(friendss);
        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new FriendSortAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);
    }

    private List<SortModel> filledData(String[] date) {
        for (int i = 0; i < date.length; i++) {
            Log.d("datadata", "" + i + ":" + date[i]);
        }
        List<SortModel> mSortList = new ArrayList<SortModel>();
        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(NimUserInfoCache.getInstance().getUserDisplayName(date[i]));
            sortModel.setAccount(date[i]);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(sortModel.getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        Log.d("datadata", "222" + mSortList.toString());
        return mSortList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerObserver(true);
        setCount();
    }

    @Override
    protected void onPause() {
        super.onPause();
        registerObserver(false);
    }

    private void setCount(){
        group_count.setText(""+TeamDataCache.getInstance().getAllTeams().size());
    }
    /**
     * *********************************** 用户资料、好友关系变更、登录数据同步完成观察者 *******************************
     */

    private void registerObserver(boolean register) {
        if (register) {
            UserInfoHelper.registerObserver(userInfoObserver);
        } else {
            UserInfoHelper.unregisterObserver(userInfoObserver);
        }

        FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);

        LoginSyncDataStatusObserver.getInstance().observeSyncDataCompletedEvent(loginSyncCompletedObserver);
    }

    FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            NimUserInfoCache.getInstance().getUserInfoFromRemote(accounts,null);
            refreshData();
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            refreshData();
        }

        @Override
        public void onAddUserToBlackList(List<String> accounts) {
            refreshData();
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> accounts) {
            refreshData();
        }
    };

    private UserInfoObservable.UserInfoObserver userInfoObserver = new UserInfoObservable.UserInfoObserver() {
        @Override
        public void onUserInfoChanged(List<String> accounts) {
            refreshData();
        }
    };

    private Observer<Void> loginSyncCompletedObserver = new Observer<Void>() {
        @Override
        public void onEvent(Void aVoid) {
            refreshData();
        }
    };

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
