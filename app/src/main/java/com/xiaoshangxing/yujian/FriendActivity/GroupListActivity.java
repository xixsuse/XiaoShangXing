package com.xiaoshangxing.yujian.FriendActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamMemberType;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.layout.CustomSwipeListView;
import com.xiaoshangxing.utils.layout.SwipeItemView;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.yujian.ChatActivity.GroupActivity;
import com.xiaoshangxing.yujian.IM.NimUIKit;
import com.xiaoshangxing.yujian.IM.TeamCreateHelper;
import com.xiaoshangxing.yujian.IM.cache.SimpleCallback;
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
    CustomSwipeListView listview;
    @Bind(R.id.count)
    TextView count;
    private Handler handler = new Handler();

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }

    private BaseAdapter baseAdapter;
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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initView();
            }
        }, 100);
    }

    @Override
    protected void onPause() {
        super.onPause();
        registerTeamUpdateObserver(false);
    }

    private void initView() {
        title.setText("群聊");
        more.setImageResource(R.mipmap.group_list_more);
        more.setPadding(0, 0, ScreenUtils.getAdapterPx(R.dimen.width_48, this), 0);
        teams.clear();
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
                        deleGroup(teams.get(position));
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
                GroupActivity.start(GroupListActivity.this, teams.get(position).getId(), null, SessionTypeEnum.Team);
            }
        });
    }

    private void deleGroup(final Team team) {
        TeamDataCache.getInstance().fetchTeamMember(team.getId(), NimUIKit.getAccount(), new SimpleCallback<TeamMember>() {
            @Override
            public void onResult(boolean success, TeamMember result) {
                if (team.getType() == TeamTypeEnum.Advanced && result.getType() == TeamMemberType.Owner) {
                    NIMClient.getService(TeamService.class).dismissTeam(team.getId()).setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast("解散成功");
                        }

                        @Override
                        public void onFailed(int i) {
                            showToast("操作失败:" + i);
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            showToast("操作失败:异常");
                            throwable.printStackTrace();
                        }
                    });
                } else {
                    NIMClient.getService(TeamService.class).quitTeam(team.getId()).setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast("退出成功");
                        }

                        @Override
                        public void onFailed(int i) {
                            showToast("操作失败:" + i);
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            showToast("操作失败:异常");
                            throwable.printStackTrace();
                        }
                    });
                }
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
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initView();
                }
            }, 100);
        }

        @Override
        public void onRemoveTeam(Team team) {
            Log.d("team", "remove");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initView();
                }
            }, 100);
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
                return;
            }
            ArrayList<String> arrayList = data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON);
            if (arrayList != null && arrayList.size() != 0) {
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
