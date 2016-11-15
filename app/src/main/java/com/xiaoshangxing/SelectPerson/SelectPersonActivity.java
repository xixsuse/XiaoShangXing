package com.xiaoshangxing.SelectPerson;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.team.model.Team;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.input_activity.InputActivity;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.yujian.IM.cache.FriendDataCache;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;
import com.xiaoshangxing.yujian.IM.cache.TeamDataCache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by FengChaoQun
 * on 2016/7/28
 */
public class SelectPersonActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = BaseActivity.TAG + "-SelectPersonActivity";
    public static final String SELECT_PERSON="SELECT_PERSON";
    public static final String LOCKED="LOCKED";
    public static final String REQUSET_CODE="REQUSET_CODE";
    public static final String LIMIT="limit";
    public static final int SELECT_PERSON_CODE=9000;
    public static final int MY_FRIEND = 1;
    public static final int GROUP = 2;
    private int group_id;
    private ArrayList<String> locked_account;

    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;
    private PinyinComparator pinyinComparator;

    private RecyclerView recyclerView;
    private SelectedPersonAdapter selectedPersonAdapter;
    private List<String> selectPerson=new ArrayList<String>();
    private int noLimit = 1000;
    private int limit;
    private boolean isGroup;


    private TextView cancel, title;
    private TextView sure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_person);
        limit = getIntent().getIntExtra("limit", noLimit);
        initView();
        refreshCount();
    }

    private void initView(){

        isGroup = getIntent().getIntExtra(IntentStatic.TYPE, MY_FRIEND) == GROUP;

        sortListView =(ListView)findViewById(R.id.listview);
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        //初始化标题
        title = (TextView) findViewById(R.id.myState);
        if (getIntent().getIntExtra(REQUSET_CODE, -1) == InputActivity.NOTICE) {
            title.setText("提醒谁看");
        } else if (getIntent().getIntExtra(REQUSET_CODE, -1) == InputActivity.FOBIDDEN) {
            title.setText("不给谁看");
        }

        if (getIntent().hasExtra(IntentStatic.DATA)) {
            selectPerson = getIntent().getStringArrayListExtra(IntentStatic.DATA);
        }

        cancel=(TextView)findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        sure=(TextView)findViewById(R.id.sure);
        sure.setOnClickListener(this);

        recyclerView=(RecyclerView)findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        selectedPersonAdapter = new SelectedPersonAdapter(this, selectPerson, this, isGroup);
        recyclerView.setAdapter(selectedPersonAdapter);
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                if (s.contains("☆")){
                    sortListView.setSelection(0);
                    return;
                }
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        parseData();

        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(this, SourceDateList, limit, this, locked_account, isGroup);
        sortListView.setAdapter(adapter);

    }

    private void parseData() {
        if (getIntent().hasExtra(LOCKED)){
            locked_account=getIntent().getStringArrayListExtra(LOCKED);
        }
        if (getIntent().getIntExtra(IntentStatic.TYPE, MY_FRIEND) == MY_FRIEND) {
            List<String> friends = FriendDataCache.getInstance().getMyFriendAccounts();
            String[] friendss = new String[friends.size()];
            for (int i = 0; i < friends.size(); i++) {
                friendss[i] = friends.get(i);
            }
            SourceDateList = filledData(friendss);

        }else if (getIntent().getIntExtra(IntentStatic.TYPE, MY_FRIEND) == GROUP){
            List<Team> teams= TeamDataCache.getInstance().getAllTeams();
            SourceDateList=filledGroup(teams);
            isGroup = true;
        }
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

    private List<SortModel> filledGroup(List<Team> teams) {
        List<SortModel> mSortList = new ArrayList<SortModel>();
        for (int i = 0; i < teams.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(teams.get(i).getName());
            sortModel.setAccount(teams.get(i).getId());
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
        return mSortList;
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.toUpperCase().indexOf(
                        filterStr.toString().toUpperCase()) != -1
                        || characterParser.getSelling(name).toUpperCase()
                        .startsWith(filterStr.toString().toUpperCase())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    public List<String> getSelectPerson() {
        return selectPerson;
    }

    public void setSelectPerson(List<String> selectPerson) {
        this.selectPerson = selectPerson;
        selectedPersonAdapter.setSelectPerson(this.selectPerson);
        selectedPersonAdapter.notifyDataSetChanged();
    }

    public void addSelectPerson(String name){
        this.selectPerson.add(name);
        selectedPersonAdapter.setSelectPerson(this.selectPerson);
        selectedPersonAdapter.notifyDataSetChanged();
        if (selectedPersonAdapter.getSelectCount()>8){
            recyclerView.smoothScrollToPosition(selectedPersonAdapter.getSelectCount());
        }
        refreshCount();
    }

    public void reduceSelectPerson(String name){
        if (selectPerson.contains(name)){
            selectPerson.remove(name);
            selectedPersonAdapter.setSelectPerson(this.selectPerson);
            selectedPersonAdapter.notifyDataSetChanged();
            refreshCount();
        }
    }

    public void showOverLimit(){
        Toast.makeText(SelectPersonActivity.this, "最多只能选择"+limit+"人", Toast.LENGTH_SHORT).show();
    }

    public void refreshCount(){
        if (limit == noLimit) {
            sure.setText("确认(" + selectPerson.size() + ")");
        } else {
            sure.setText("确认(" + selectPerson.size() + "/" + limit + ")");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                finish();
                break;
            case R.id.sure:
                sure();
                break;
        }
    }

    private void sure(){
        Intent intent=new Intent();
        intent.putStringArrayListExtra(SELECT_PERSON,(ArrayList<String>) selectPerson);
        setResult(RESULT_OK, intent);
        finish();
    }
}
