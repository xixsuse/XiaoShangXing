package com.xiaoshangxing.yujian.FriendActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.xiaoshangxing.Network.IMNetwork;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.CharacterParser;
import com.xiaoshangxing.SelectPerson.PinyinComparator;
import com.xiaoshangxing.SelectPerson.SideBar;
import com.xiaoshangxing.SelectPerson.SortModel;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.yujian.IM.cache.FriendDataCache;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;
import com.xiaoshangxing.yujian.IM.cache.TeamDataCache;
import com.xiaoshangxing.yujian.IM.kit.LoginSyncDataStatusObserver;
import com.xiaoshangxing.yujian.IM.uinfo.UserInfoHelper;
import com.xiaoshangxing.yujian.IM.uinfo.UserInfoObservable;
import com.xiaoshangxing.yujian.Serch.GlobalSearchActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by FengChaoQun
 * on 2016/9/4
 */
public class FriendActivity extends BaseActivity {


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

    private View headView, serch;

    private List<User> loves = new ArrayList<>();
    private List<User> stars = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        title.setText("好友");
        more.setVisibility(View.GONE);
        titleBottomLine.setVisibility(View.GONE);

        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        sideBar.setTextView(dialog);
//        出去head
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                if (s.contains("☆")) {
                    sortListView.setSelection(0 + 1);
                    return;
                }
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position + 1);
                }

            }
        });

        headView = View.inflate(this, R.layout.head_friend, null);
        group = headView.findViewById(R.id.group);
        love = headView.findViewById(R.id.love);
        star = headView.findViewById(R.id.star);
        serch = headView.findViewById(R.id.serch_layout);
        sortListView.addHeaderView(headView);
        group_count = (TextView) headView.findViewById(R.id.group_count);
        love_count = (TextView) headView.findViewById(R.id.love_count);
        star_count = (TextView) headView.findViewById(R.id.star_count);
        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendActivity.this, LoveOrStartActivity.class);
                intent.putExtra(IntentStatic.TYPE, LoveOrStartActivity.LOVE);
                startActivity(intent);
            }
        });
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendActivity.this, LoveOrStartActivity.class);
                intent.putExtra(IntentStatic.TYPE, LoveOrStartActivity.STAR);
                startActivity(intent);
            }
        });
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendActivity.this, GroupListActivity.class);
                startActivity(intent);
            }
        });
        serch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalSearchActivity.start(FriendActivity.this);
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
        getLoveCount();
        getStarCount();
    }

    private void getLoveCount() {

        Subscriber<ResponseBody> subscriber = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    switch (jsonObject.getInt(NS.CODE)) {
                        case 200:
                            Gson gson = new Gson();
                            loves = gson.fromJson(jsonObject.getJSONArray(NS.MSG).toString(), new TypeToken<List<User>>() {
                            }.getType());
                            love_count.setText("" + loves.size());
                            break;
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        IMNetwork.getInstance().MyFavor(subscriber, String.valueOf(TempUser.id), this);

    }

    private void getStarCount() {
        Subscriber<ResponseBody> subscriber = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    switch (jsonObject.getInt(NS.CODE)) {
                        case 200:
                            Gson gson = new Gson();
                            stars = gson.fromJson(jsonObject.getJSONArray(NS.MSG).toString(), new TypeToken<List<User>>() {
                            }.getType());
                            star_count.setText("" + stars.size());
                            break;
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        IMNetwork.getInstance().MyStar(subscriber, String.valueOf(TempUser.id), this);
    }

    private List<SortModel> filledData(String[] date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();
        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(NimUserInfoCache.getInstance().getUserDisplayName(date[i]));
            sortModel.setAccount(date[i]);
            Friend friend = FriendDataCache.getInstance().getFriendByAccount(date[i]);
            if (friend.getExtension() != null && friend.getExtension().containsKey(NS.MARK) && (boolean) friend.getExtension().get(NS.MARK)) {
                    sortModel.setSortLetters("@");
            } else {
                //汉字转换成拼音
                String pinyin = characterParser.getSelling(sortModel.getName());
                String sortString = pinyin.substring(0, 1).toUpperCase();
                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    sortModel.setSortLetters(sortString.toUpperCase());
                } else {
                    sortModel.setSortLetters("#");
                }
            }
            mSortList.add(sortModel);
        }
        return mSortList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerObserver(true);
        setCount();
        refreshData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        registerObserver(false);
    }

    private void setCount() {
        group_count.setText("" + TeamDataCache.getInstance().getAllTeams().size());
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
            NimUserInfoCache.getInstance().getUserInfoFromRemote(accounts, null);
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
