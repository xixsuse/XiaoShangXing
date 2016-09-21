package com.xiaoshangxing.yujian.Serch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.search.model.MsgIndexRecord;
import com.xiaoshangxing.R;
import com.xiaoshangxing.yujian.ChatActivity.ChatActivity;
import com.xiaoshangxing.yujian.ChatActivity.GroupActivity;
import com.xiaoshangxing.yujian.IM.kit.string.StringUtil;
import com.xiaoshangxing.yujian.Serch.ViewHolder.ContactHolder;
import com.xiaoshangxing.yujian.Serch.ViewHolder.LabelHolder;
import com.xiaoshangxing.yujian.Serch.ViewHolder.MsgHolder;
import com.xiaoshangxing.yujian.WatchMessagePicture.ReflectionUtil;

import java.lang.reflect.Field;

/**
 * 全局搜索页面
 * 支持通讯录搜索、消息全文检索
 * <p/>
 * Created by huangjun on 2015/4/13.
 */
public class GlobalSearchActivity extends AppCompatActivity implements OnItemClickListener {

    private ContactDataAdapter adapter;

    private ListView lvContacts;

    private SearchView searchView;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.global_search_result);

        ToolBarOptions options = new ToolBarOptions();
        setToolBar(R.id.toolbar, options);

        lvContacts = (ListView) findViewById(R.id.searchResultList);
        lvContacts.setVisibility(View.GONE);
        SearchGroupStrategy searchGroupStrategy = new SearchGroupStrategy();
        IContactDataProvider dataProvider = new ContactDataProvider(ItemTypes.FRIEND, ItemTypes.TEAM, ItemTypes.MSG);

        adapter = new ContactDataAdapter(this, searchGroupStrategy, dataProvider);
        adapter.addViewHolder(ItemTypes.LABEL, LabelHolder.class);
        adapter.addViewHolder(ItemTypes.FRIEND, ContactHolder.class);
        adapter.addViewHolder(ItemTypes.TEAM, ContactHolder.class);
        adapter.addViewHolder(ItemTypes.MSG, MsgHolder.class);

        lvContacts.setAdapter(adapter);
        lvContacts.setOnItemClickListener(this);
        lvContacts.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                showKeyboard(false);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        findViewById(R.id.global_search_root).setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (searchView != null) {
            searchView.clearFocus();
        }
    }

    @Override
    protected void onDestroy() {
        showKeyboard(false);
        super.onDestroy();
    }

    public static final void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, GlobalSearchActivity.class);
        context.startActivity(intent);
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.global_search_menu, menu);
        final MenuItem item = menu.findItem(R.id.action_search);

        handler.post(new Runnable() {
            @Override
            public void run() {
                MenuItemCompat.expandActionView(item);
            }
        });

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                finish();
                return false;
            }
        });

        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setDividerDrawable(getResources().getDrawable(R.drawable.cursor_blue4));
        searchView.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                showKeyboard(false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (StringUtil.isEmpty(query)) {
                    lvContacts.setVisibility(View.GONE);
                } else {
                    lvContacts.setVisibility(View.VISIBLE);
                }
                adapter.query(query);
                return true;
            }
        });

        //修改光标
        //指定某个私有属性
//        try {
//            Field mQueryTextView = null;
//            mQueryTextView = searchView.getClass().getDeclaredField("mQueryTextView");
//            mQueryTextView.setAccessible(true);
//            Class<?> mTextViewClass = mQueryTextView.get(searchView).getClass().getSuperclass().getSuperclass().getSuperclass();
//            //mCursorDrawableRes光标图片Id的属性 这个属性是TextView的属性，所以要用mQueryTextView（SearchAutoComplete）
//            //的父类（AutoCompleteTextView）的父  类( EditText）的父类(TextView)
//            Field mCursorDrawableRes = mTextViewClass.getDeclaredField("mCursorDrawableRes");
//            //setAccessible 它是用来设置是否有权限访问反射类中的私有属性的，只有设置为true时才可以访问，默认为false
//            mCursorDrawableRes.setAccessible(true);
//            //注意第一个参数持有这个属性(mQueryTextView)的对象(mSearchView) 光标必须是一张图片不能是颜色，因为光标有两张图片，
//            //一张是第一次获得焦点的时候的闪烁的图片，一张是后边有内容时候的图片，如果用颜色填充的话，就会失去闪烁的那张图片，
//            //颜色填充的会缩短文字和光标的距离（某些字母会背光标覆盖一部分）。
//            mCursorDrawableRes.set(mQueryTextView.get(searchView), R.drawable.cursor_blue4);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }

        setSearchViewTextCusor(searchView);


        return true;
    }

    private void setSearchViewTextCusor(SearchView view) {
        try {
            Class<?> mTextViewClass = view.getClass().getSuperclass()
                    .getSuperclass().getSuperclass();
            //mCursorDrawableRes光标图片Id的属性 这个属性是TextView的属性，所以要用view（SearchAutoComplete）
            //的父类（AutoCompleteTextView）的父  类( EditText）的父类(TextView)
            // Although these fields are specific to editable text, they are not added to Editor because
            // they are defined by the TextView's style and are theme-dependent.
            Field mCursorDrawableRes = mTextViewClass.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            //注意第一个参数持有这个属性的对象 光标必须是一张图片不能是颜色，因为光标有两张图片，第二张必须是一张图片 我这里是一张白色的图片
            mCursorDrawableRes.set(view, R.drawable.cursor_blue4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showKeyboard(boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            if (getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(getCurrentFocus(), 0);
            }
        } else {
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private Toolbar toolbar;

    public void setToolBar(int toolBarId, ToolBarOptions options) {
        toolbar = (Toolbar) findViewById(toolBarId);
        if (options.titleId != 0) {
            toolbar.setTitle(options.titleId);
        }
        if (!TextUtils.isEmpty(options.titleString)) {
            toolbar.setTitle(options.titleString);
        }
        if (options.logoId != 0) {
            toolbar.setLogo(options.logoId);
        }
        setSupportActionBar(toolbar);

        if (options.isNeedNavigate) {
            toolbar.setNavigationIcon(options.navigateId);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getSupportFragmentManager();
                    ReflectionUtil.invokeMethod(fm, "noteStateNotSaved", null);
                }
            });
        }
    }


    private static class SearchGroupStrategy extends ContactGroupStrategy {
        public static final String GROUP_FRIEND = "FRIEND";
        public static final String GROUP_TEAM = "TEAM";
        public static final String GROUP_MSG = "MSG";

        SearchGroupStrategy() {
            add(ContactGroupStrategy.GROUP_NULL, 0, "");
            add(GROUP_TEAM, 1, "群组");
            add(GROUP_FRIEND, 2, "好友");
            add(GROUP_MSG, 3, "聊天记录");
        }

        @Override
        public String belongs(AbsContactItem item) {
            switch (item.getItemType()) {
                case ItemTypes.FRIEND:
                    return GROUP_FRIEND;
                case ItemTypes.TEAM:
                    return GROUP_TEAM;
                case ItemTypes.MSG:
                    return GROUP_MSG;
                default:
                    return null;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AbsContactItem item = (AbsContactItem) adapter.getItem(position);
        switch (item.getItemType()) {
            case ItemTypes.TEAM: {
//                SessionHelper.startTeamSession(this, ((ContactItem) item).getContact().getContactId());
                GroupActivity.start(GlobalSearchActivity.this, ((ContactItem) item).getContact().getContactId(), null, null);
                break;
            }

            case ItemTypes.FRIEND: {
//                SessionHelper.startP2PSession(this, ((ContactItem) item).getContact().getContactId());
                ChatActivity.start(GlobalSearchActivity.this, ((ContactItem) item).getContact().getContactId(), null, null);
                break;
            }

            case ItemTypes.MSG: {
                MsgIndexRecord msgIndexRecord = ((MsgItem) item).getRecord();
                if (msgIndexRecord.getCount() > 1) {
                    GlobalSearchDetailActivity2.start(this, msgIndexRecord);
                } else {
                    if (msgIndexRecord.getSessionType() == SessionTypeEnum.P2P) {
                        ChatActivity.start(GlobalSearchActivity.this, msgIndexRecord.getSessionId(), msgIndexRecord.getMessage(), null);
//                        SessionHelper.startP2PSession(this, msgIndexRecord.getSessionId(), msgIndexRecord.getMessage());
                    } else if (msgIndexRecord.getSessionType() == SessionTypeEnum.Team) {
                        GroupActivity.start(GlobalSearchActivity.this, msgIndexRecord.getSessionId(), msgIndexRecord.getMessage(), null);
//                        SessionHelper.startTeamSession(this, msgIndexRecord.getSessionId(), msgIndexRecord.getMessage());
                    }
                }
                break;
            }

            default:
                break;
        }
    }

}
