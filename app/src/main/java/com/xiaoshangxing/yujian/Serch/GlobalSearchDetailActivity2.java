package com.xiaoshangxing.yujian.Serch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.search.model.MsgIndexRecord;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.customView.AutoRefreshListView;
import com.xiaoshangxing.yujian.ChatActivity.ChatActivity;
import com.xiaoshangxing.yujian.ChatActivity.GroupActivity;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;
import com.xiaoshangxing.yujian.IM.cache.TeamDataCache;
import com.xiaoshangxing.yujian.Serch.ViewHolder.LabelHolder;
import com.xiaoshangxing.yujian.Serch.ViewHolder.MsgHolder;
import com.xiaoshangxing.yujian.WatchMessagePicture.ReflectionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息全文检索详细页面
 * Created by huangjun on 2016/6/28.
 */
public class GlobalSearchDetailActivity2 extends AppCompatActivity implements OnItemClickListener {

    private static final String EXTRA_SESSION_TYPE = "EXTRA_SESSION_TYPE";

    private static final String EXTRA_SESSION_ID = "EXTRA_SESSION_ID";

    private static final String EXTRA_QUERY = "EXTRA_QUERY";

    private static final String EXTRA_RESULT_COUNT = "EXTRA_RESULT_COUNT";

    private ContactDataAdapter adapter;

    private AutoRefreshListView lvContacts;

    private String sessionId;

    private SessionTypeEnum sessionType;

    private String query;

    private int resultCount;
    private Toolbar toolbar;
    private List<AbsContactItem> dataList;

    public static final void start(Context context, MsgIndexRecord record) {
        Intent intent = new Intent();
        intent.setClass(context, GlobalSearchDetailActivity2.class);
        intent.putExtra(EXTRA_SESSION_TYPE, record.getSessionType().getValue());
        intent.putExtra(EXTRA_SESSION_ID, record.getSessionId());
        intent.putExtra(EXTRA_QUERY, record.getQuery());
        intent.putExtra(EXTRA_RESULT_COUNT, record.getCount());

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parseIntent();

        setContentView(R.layout.global_search_detail);

        // title name
        ToolBarOptions options = new ToolBarOptions();
        if (sessionType == SessionTypeEnum.P2P) {
            options.titleString = NimUserInfoCache.getInstance().getUserDisplayName(sessionId);
        } else if (sessionType == SessionTypeEnum.Team) {
            options.titleString = TeamDataCache.getInstance().getTeamName(sessionId);
        }
        setToolBar(R.id.toolbar, options);

        // textView tip
        String tip = String.format("共%d条与\"%s\"相关的聊天记录", resultCount, query);
        TextView tipTextView = findView(R.id.search_result_tip);
        tipTextView.setText(tip);

        // listView adapter
        lvContacts = findView(R.id.search_result_list);
        IContactDataProvider dataProvider = new ContactDataProviderSearch(new ArrayList<AbsContactItem>(), ItemTypes.MSG);

        adapter = new ContactDataAdapter(this, null, dataProvider) {
            @Override
            protected void onPostLoad(boolean empty, String query, boolean all) {
                super.onPostLoad(empty, query, all);
                lvContacts.onRefreshComplete();
            }
        };
        adapter.addViewHolder(ItemTypes.LABEL, LabelHolder.class);
        adapter.addViewHolder(ItemTypes.MSG, MsgHolder.class);

        lvContacts.setMode(AutoRefreshListView.Mode.END);
        lvContacts.setAdapter(adapter);
        lvContacts.setOnItemClickListener(this);
        lvContacts.setOnRefreshListener(new AutoRefreshListView.OnRefreshListener() {
            @Override
            public void onRefreshFromStart() {
            }

            @Override
            public void onRefreshFromEnd() {
                // query data
                if (dataList != null && dataList.size() < resultCount) {
                    TextQuery textQuery = new TextQuery(query);
                    textQuery.extra = new Object[]{sessionType, sessionId, ((MsgItem) (dataList.get(dataList.size() - 1))).getRecord()};

                    adapter.query(textQuery);
                } else {
                    lvContacts.onRefreshComplete();
                }
            }
        });
        // query data
        TextQuery textQuery = new TextQuery(query);
        textQuery.extra = new Object[]{sessionType, sessionId, new MsgIndexRecord(null, query)};
        adapter.query(textQuery);

    }

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

    protected <T extends View> T findView(int resId) {
        return (T) (findViewById(resId));
    }

    private void parseIntent() {
        sessionType = SessionTypeEnum.typeOfValue(getIntent().getIntExtra(EXTRA_SESSION_TYPE, 0));
        sessionId = getIntent().getStringExtra(EXTRA_SESSION_ID);
        query = getIntent().getStringExtra(EXTRA_QUERY);
        resultCount = getIntent().getIntExtra(EXTRA_RESULT_COUNT, 0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AbsContactItem item = (AbsContactItem) adapter.getItem(position - lvContacts.getHeaderViewsCount());
        switch (item.getItemType()) {
            case ItemTypes.MSG: {
                MsgIndexRecord msgIndexRecord = ((MsgItem) item).getRecord();
                if (msgIndexRecord.getSessionType() == SessionTypeEnum.P2P) {
                    ChatActivity.start(this, msgIndexRecord.getSessionId(), msgIndexRecord.getMessage(), null);
                } else if (msgIndexRecord.getSessionType() == SessionTypeEnum.Team) {
                    GroupActivity.start(this, msgIndexRecord.getSessionId(), msgIndexRecord.getMessage(), null);
                }
                break;
            }
            default:
                break;
        }
    }

    private class ContactDataProviderSearch extends ContactDataProvider {

        public ContactDataProviderSearch(List<AbsContactItem> data, int... itemTypes) {
            super(itemTypes);
            dataList = data;
        }

        @Override
        public List<AbsContactItem> provide(TextQuery query) {
            dataList.addAll(MsgDataProvider.provide(query));
            return dataList;
        }
    }

}
