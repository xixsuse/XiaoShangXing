package com.xiaoshangxing.yujian.FriendActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.layout.CustomSwipeListView;
import com.xiaoshangxing.utils.layout.SwipeItemView;
import com.xiaoshangxing.yujian.ChatActivity.GroupActivity;
import com.xiaoshangxing.yujian.IM.NimUIKit;
import com.xiaoshangxing.yujian.IM.TeamCreateHelper;
import com.xiaoshangxing.yujian.IM.cache.TeamDataCache;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/9/5
 */
public class GroupListActivity extends BaseActivity implements IBaseView {
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.title)
    RelativeLayout title;
    @Bind(R.id.listview)
    CustomSwipeListView listview;
    @Bind(R.id.count)
    TextView count;

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }

    private BaseAdapter baseAdapter;
    //    private List<String> data = new ArrayList<>();
    private List<Team> teams = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerTeamUpdateObserver(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        registerTeamUpdateObserver(false);
    }

    private void initView() {
        teams = TeamDataCache.getInstance().getAllTeams();

        baseAdapter = new BaseAdapter() {
            SwipeItemView mLastSlideViewWithStatusOn;

            @Override
            public int getCount() {
                return teams.size();
            }

            @Override
            public Team getItem(int position) {
                return teams.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                SwipeItemView slideView = (SwipeItemView) convertView;
                if (slideView == null) {
                    View itemView = getLayoutInflater().inflate(R.layout.item_grouplist, null);

                    slideView = new SwipeItemView(GroupListActivity.this);
                    slideView.setContentView(itemView);

                    holder = new ViewHolder(slideView);
                    slideView.setOnSlideListener(new SwipeItemView.OnSlideListener() {

                        @Override
                        public void onSlide(View view, int status) {
                            // TODO Auto-generated method stub
                            if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
                                mLastSlideViewWithStatusOn.shrink();
                            }

                            if (status == SLIDE_STATUS_ON) {
                                mLastSlideViewWithStatusOn = (SwipeItemView) view;
                            }
                        }
                    });
                    slideView.setTag(holder);
                } else {
                    holder = (ViewHolder) slideView.getTag();
                }

                if (CustomSwipeListView.mFocusedItemView != null) {
                    CustomSwipeListView.mFocusedItemView.shrink();
                }

                holder.name.setText(teams.get(position).getName());
                Glide.with(GroupListActivity.this)
                        .load(teams.get(position).getIcon())
                        .placeholder(R.mipmap.greyblock)
                        .error(R.mipmap.cirecleimage_default)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.image);

                holder.deleteHolder.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
//                        data.remove(position);
//                        notifyDataSetChanged();
//                        count.setText(data.size()+"个群聊");
                    }
                });
                return slideView;
            }
        };

        listview.setAdapter(baseAdapter);
        count.setText(teams.size() + "个群聊");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupActivity.start(GroupListActivity.this,teams.get(position).getId(),null, SessionTypeEnum.Team);
            }
        });
    }

    private static class ViewHolder {
        public ImageView image;
        public TextView name;
        public ViewGroup deleteHolder;

        ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.head_image);
            name = (TextView) view.findViewById(R.id.name);
            deleteHolder = (ViewGroup) view.findViewById(R.id.holder);
        }
    }

    private void registerTeamUpdateObserver(boolean register) {
        if (register) {
            TeamDataCache.getInstance().registerTeamDataChangedObserver(teamDataChangedObserver);
        } else {
            TeamDataCache.getInstance().unregisterTeamDataChangedObserver(teamDataChangedObserver);
        }
    }

    TeamDataCache.TeamDataChangedObserver teamDataChangedObserver = new TeamDataCache.TeamDataChangedObserver() {
        @Override
        public void onUpdateTeams(List<Team> teams) {
            initView();
        }

        @Override
        public void onRemoveTeam(Team team) {
            initView();
        }
    };

    @OnClick({R.id.back, R.id.more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.more:
                Intent intent = new Intent(GroupListActivity.this, SelectPersonActivity.class);
                intent.putExtra(IntentStatic.TYPE, SelectPersonActivity.MY_FRIEND);
                ArrayList<String> locked = new ArrayList<String>();
                locked.add(NimUIKit.getAccount());
                intent.putExtra(SelectPersonActivity.LOCKED, locked);
                startActivityForResult(intent, SelectPersonActivity.SELECT_PERSON_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SelectPersonActivity.SELECT_PERSON_CODE) {
            if (data == null) {
                Toast.makeText(GroupListActivity.this, "没有选择联系人", Toast.LENGTH_SHORT).show();
                return;
            }
            ArrayList<String> arrayList = data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON);
            if (arrayList == null || arrayList.size() == 0) {
                Toast.makeText(GroupListActivity.this, "没有选择联系人", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("select account", arrayList.toString());
                TeamCreateHelper.createAdvancedTeam(GroupListActivity.this,
                        arrayList, this, new RequestCallback<Team>() {
                            @Override
                            public void onSuccess(Team team) {

                            }

                            @Override
                            public void onFailed(int i) {

                            }

                            @Override
                            public void onException(Throwable throwable) {

                            }
                        });
            }
        }
    }
}
