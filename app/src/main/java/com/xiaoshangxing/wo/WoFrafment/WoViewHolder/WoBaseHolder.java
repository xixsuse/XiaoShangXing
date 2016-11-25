package com.xiaoshangxing.wo.WoFrafment.WoViewHolder;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.netUtil.OperateUtils;
import com.xiaoshangxing.Network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.CommentsBean;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.input_activity.InputBoxLayout;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.layout.LayoutHelp;
import com.xiaoshangxing.utils.layout.MoreTextView;
import com.xiaoshangxing.utils.layout.Name;
import com.xiaoshangxing.wo.Roll.RollActivity;
import com.xiaoshangxing.wo.WoFrafment.Item_Comment;
import com.xiaoshangxing.wo.WoFrafment.Published_Help;
import com.xiaoshangxing.wo.WoFrafment.WoFragment;
import com.xiaoshangxing.wo.WoFrafment.Wo_adpter_realm;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by FengChaoQun
 * on 2016/9/10
 */
public abstract class WoBaseHolder {

    public Published published;
    /**
     * context
     */
    protected Context context;

    protected BaseActivity activity;

    /**
     * list item view
     */
    protected View view;

    protected Realm realm;

    protected WoFragment woFragment;

    protected Handler handler;

    protected int position;

    protected Wo_adpter_realm adpter;

    protected CirecleImage headImage;
    protected Name name;
    protected TextView college;
    protected MoreTextView text;
    protected FrameLayout content;
    protected TextView location;
    protected TextView time;
    protected ImageView permission;
    protected TextView delete;
    protected CheckBox praise;
    protected LinearLayout praiseLay;
    protected ImageView comment;
    protected LinearLayout commentLay;
    protected LinearLayout praisePeople;
    protected LinearLayout comments;
    protected LinearLayout commentLayout;
    protected ImageView headline;
    protected View jianjiao;
    protected View verticalLine;
    protected LinearLayout commentsLay;
    protected View lineBetweenPraiseComment;

    private boolean isLoadComment;

    public WoBaseHolder() {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public BaseActivity getActivity() {
        return activity;
    }

    public void setActivity(BaseActivity activity) {
        this.activity = activity;
    }

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    public WoFragment getWoFragment() {
        return woFragment;
    }

    public void setWoFragment(WoFragment woFragment) {
        this.woFragment = woFragment;
    }

    public void setPublished(Published published) {
        this.published = published;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Wo_adpter_realm getAdpter() {
        return adpter;
    }

    public void setAdpter(Wo_adpter_realm adpter) {
        this.adpter = adpter;
    }

    public Published getPublished() {
        return published;
    }

    public View getView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.wo_base_viewholder, null);
        inflateBase();
        inflate();
        return view;
    }

    public abstract void inflate();

    public abstract void refresh(Published published);

    private void inflateBase() {
        headImage = (CirecleImage) view.findViewById(R.id.head_image);
        name = (Name) view.findViewById(R.id.name);
        college = (TextView) view.findViewById(R.id.college);
        text = (MoreTextView) view.findViewById(R.id.text);
        content = (FrameLayout) view.findViewById(R.id.content);
        location = (TextView) view.findViewById(R.id.location);
        time = (TextView) view.findViewById(R.id.time);
        permission = (ImageView) view.findViewById(R.id.permission);
        delete = (TextView) view.findViewById(R.id.delete);
        praise = (CheckBox) view.findViewById(R.id.praise);
        praiseLay = (LinearLayout) view.findViewById(R.id.praise_lay);
        comment = (ImageView) view.findViewById(R.id.comment);
        commentLay = (LinearLayout) view.findViewById(R.id.comment_lay);
        praisePeople = (LinearLayout) view.findViewById(R.id.praise_people);
        comments = (LinearLayout) view.findViewById(R.id.comments);
        commentLayout = (LinearLayout) view.findViewById(R.id.comment_layout);
        headline = (ImageView) view.findViewById(R.id.headline);
        jianjiao = view.findViewById(R.id.jianjiao);
        commentsLay = (LinearLayout) view.findViewById(R.id.comments_lay);
        lineBetweenPraiseComment = view.findViewById(R.id.line_between_praise_comment);
        verticalLine = view.findViewById(R.id.vertical_line);
    }

    protected void refreshBase() {
        initView();
        initOnclick();
    }

    private void initView() {

//      头像  姓名 学院
        String userId = String.valueOf(published.getUserId());
        UserInfoCache.getInstance().getHeadIntoImage(userId, headImage);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.USER_NAME, name);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.COLLEGE, college);

//        是否头条
        headline.setVisibility(published.getIsHeadline() == 1 ? View.VISIBLE : View.INVISIBLE);

//      文字内容
        text.setText(published.getText());

//      地点
        location.setText(published.getLocation());
        location.setVisibility(TextUtils.isEmpty(published.getLocation()) ? View.GONE : View.VISIBLE);

//        时间
        time.setText(TimeUtil.getSimplePublishedTime(published.getCreateTime()));

//        显示权限列表
        if (TempUser.isMine(String.valueOf(published.getUserId()))) {
            if (!TextUtils.isEmpty(published.getNotice())) {
                permission.setVisibility(View.VISIBLE);
            }
            delete.setVisibility(View.VISIBLE);
        } else {
            permission.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }

        //赞
        praise.setChecked(Published_Help.isPraised(published));
        Published_Help.buildPrasiPeople(published, praisePeople, context);

        parseComment(published);

//        隐藏尖角
        if (TextUtils.isEmpty(published.getPraiseUserIds()) &&
                (published.getComments() == null || published.getComments().size() < 1)) {
            jianjiao.setVisibility(View.GONE);
            commentLayout.setVisibility(View.GONE);
        } else {
            jianjiao.setVisibility(View.VISIBLE);
            commentLayout.setVisibility(View.VISIBLE);
        }

//        隐藏评论与赞之间的分割线
        if (!TextUtils.isEmpty(published.getPraiseUserIds()) &&
                !(published.getComments() == null || published.getComments().size() < 1)) {
            lineBetweenPraiseComment.setVisibility(View.VISIBLE);
        } else {
            lineBetweenPraiseComment.setVisibility(View.GONE);
        }

        if (adpter.getCount() == position + 1) {
            verticalLine.setVisibility(View.GONE);
        } else {
            verticalLine.setVisibility(View.VISIBLE);
        }

    }

    private void parseComment(Published published) {

        comments.removeAllViews();

        if (published.getComments().size() < 1) {
            commentsLay.setVisibility(View.GONE);
            return;
        } else {
            commentsLay.setVisibility(View.VISIBLE);
        }

        RealmList<CommentsBean> realmResults = published.getComments();
        for (final CommentsBean i : realmResults) {
            Item_Comment item_comment;

            if (i.isReply()) {
                item_comment = new Item_Comment(context, i.getUserName(), i.getObjectName(),
                        i.getText(), String.valueOf(i.getUserId()), String.valueOf(i.getObejectId()));
            } else {
                item_comment = new Item_Comment(context, i.getUserName(),
                        i.getText(), String.valueOf(i.getUserId()));
            }

            comments.addView(item_comment.getTextView());

            if (!TempUser.isRealName) {
                continue;
            }

            item_comment.getTextView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEdittext(v);
                    woFragment.setEditCallback(new InputBoxLayout.CallBack() {
                        @Override
                        public void callback(String text) {
                            sendComment(text, i.getId());
                        }
                    });
                }
            });
        }
    }

    private void initOnclick() {
        commentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.performClick();
            }
        });
        praiseLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                praise.performClick();
            }
        });

        //头像监听
        headImage.setIntent_type(CirecleImage.PERSON_STATE, String.valueOf(published.getUserId()));
//        删除
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogUtils.Dialog_Center center = new DialogUtils.Dialog_Center(context);
                center.Message("确定删除吗?");
                center.Button("删除", "取消");
                center.MbuttonOnClick(new DialogUtils.Dialog_Center.buttonOnClick() {
                    @Override
                    public void onButton1() {
                        delete();
                        center.close();
                    }

                    @Override
                    public void onButton2() {
                        center.close();
                    }
                });
                Dialog dialog = center.create();
                dialog.show();
                LocationUtil.setWidth(activity, dialog,
                        context.getResources().getDimensionPixelSize(R.dimen.x780));
            }
        });

        permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RollActivity.class);
                intent.putExtra(RollActivity.TYPE, RollActivity.NOTICE);
                intent.putExtra(IntentStatic.DATA, published.getId());
                context.startActivity(intent);
            }
        });

        //       评论
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                LayoutHelp.PermissionClick(activity, new LayoutHelp.PermisionMethod() {
                    @Override
                    public void doSomething() {
                        woFragment.setEditCallback(new InputBoxLayout.CallBack() {
                            @Override
                            public void callback(String text) {
                                sendComment(text, -1);
                            }
                        });
                        showEdittext(v);
                    }
                });
            }
        });

        praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutHelp.PermissionClick(activity, new LayoutHelp.PermisionMethod() {
                    @Override
                    public void doSomething() {
                        OperateUtils.operate(published.getId(), context, true, NS.PRAISE, Published_Help.isPraised(published),
                                new SimpleCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        woFragment.showToast("操作成功");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
//                            praise.setChecked(!praise.isChecked());
                                    }

                                    @Override
                                    public void onBackData(Object o) {
                                        if (o != null) {
                                            refresh((Published) o);
                                        }
                                    }
                                });
                    }
                });

            }
        });
    }

    private void delete() {
        OperateUtils.deleteOnePublished(published.getId(), context, woFragment, new SimpleCallBack() {
            @Override
            public void onSuccess() {
                woFragment.deleteOne(position);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onBackData(Object o) {
            }
        });
    }

    private void showEdittext(View v) {
        handler = new Handler();
        woFragment.showEdittext(context);
        final int[] xy = new int[2];
        v.getLocationOnScreen(xy);
        final View mv = v;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int editextLocation = woFragment.get_Editext_Height();
                int destination = xy[1] + mv.getHeight() - editextLocation;
                woFragment.moveListview(destination);
            }
        }, 300);
    }

    private void sendComment(String text, int commenId) {
        OperateUtils.Comment(published.getId(), commenId, text, context, true, new SimpleCallBack() {
            @Override
            public void onSuccess() {
                woFragment.setEditCallback(null);
            }

            @Override
            public void onError(Throwable e) {
                woFragment.setEditCallback(null);
                activity.showToast("评论失败");
            }

            @Override
            public void onBackData(Object o) {
                if (o != null) {
                    refresh((Published) o);
                }
            }
        });
    }

}
