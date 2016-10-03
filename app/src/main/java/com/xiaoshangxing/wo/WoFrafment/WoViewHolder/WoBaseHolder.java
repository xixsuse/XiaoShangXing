package com.xiaoshangxing.wo.WoFrafment.WoViewHolder;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
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
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.data.UserCache;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.input_activity.InputBoxLayout;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.layout.MoreTextView;
import com.xiaoshangxing.utils.layout.Name;
import com.xiaoshangxing.utils.school_circle.Item_Comment;
import com.xiaoshangxing.wo.WoFrafment.Published_Help;
import com.xiaoshangxing.wo.WoFrafment.WoFragment;
import com.xiaoshangxing.wo.WoFrafment.Wo_listview_adpter;
import com.xiaoshangxing.wo.roll.rollActivity;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    protected Wo_listview_adpter adpter;

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
    protected FrameLayout comments_fragment;

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

    public Wo_listview_adpter getAdpter() {
        return adpter;
    }

    public void setAdpter(Wo_listview_adpter adpter) {
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
        comments_fragment = (FrameLayout) view.findViewById(R.id.comment_fragment);
    }

    protected void refreshBase() {
        initView();
        initOnclick();
    }

    private void initView() {

//      头像  姓名 学院
        UserInfoCache.getInstance().getHead(headImage, published.getUserId(), context);
        UserInfoCache.getInstance().getName(name, published.getUserId());
        UserInfoCache.getInstance().getCollege(college, published.getUserId());
//        name.setText(published.getUser().getUsername());
//        college.setText(published.getUser().getIsCollege());

//        是否头条
        headline.setVisibility(published.getIsHeadline() == 1 ? View.VISIBLE : View.INVISIBLE);

//      文字内容
        text.setText(published.getText());

//      地点
        location.setText(published.getLocation());
        location.setVisibility(TextUtils.isEmpty(published.getLocation()) ? View.GONE : View.VISIBLE);

//        时间
        time.setText(TimeUtil.getTimeShowString(published.getCreateTime(), false));

//        显示权限列表
        if (TempUser.isMine(String.valueOf(published.getUserId()))) {
            permission.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
        } else {
            permission.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }

        //赞
        praise.setChecked(Published_Help.isPraised(published));
        Published_Help.buildPrasiPeople(published, praisePeople, context);


//         评论
//        if (published.getComments() != null && published.getComments().size() > 0) {
//            simpleParse();
//        } else {
//            comments.removeAllViews();
//            comments_fragment.removeAllViews();
//        }

        parseComment(published);

//        隐藏尖角
        if (TextUtils.isEmpty(published.getPraiseUserIds()) &&
                (published.getComments() == null || published.getComments().size() < 1)) {
            jianjiao.setVisibility(View.GONE);
        } else {
            jianjiao.setVisibility(View.VISIBLE);
        }
    }

    public void parseComment(int publish_id) {

        final int[] ints = new int[1];
        ints[0] = publish_id;

        comments.removeAllViews();
        comments_fragment.removeAllViews();

        Observable<List<TextView>> observable = Observable.create(new Observable.OnSubscribe<List<TextView>>() {
            @Override
            public void call(Subscriber<? super List<TextView>> subscriber) {

                Realm realm1 = Realm.getDefaultInstance();
                List<TextView> list = new ArrayList<>();

                Published a_pulished = realm1.where(Published.class).equalTo(NS.ID, ints[0]).findFirst();

                List<CommentsBean> commentsBeen = a_pulished.getComments();
                final List<Integer> commentIds = new ArrayList<Integer>();

                for (int j = 0; j < commentsBeen.size(); j++) {
                    CommentsBean i = commentsBeen.get(j);
                    commentIds.add(i.getId());
                    User user = realm1.where(User.class).equalTo(NS.ID, i.getUserId()).findFirst();
                    if (user == null) {
                        user = UserCache.getUserByBlock(String.valueOf(i.getUserId()), context);
                    }
                    Item_Comment item_comment = new Item_Comment(context, user.getUsername(),
                            i.getText(), String.valueOf(i.getId()));
                    TextView textView = item_comment.getTextView();
                    list.add(textView);
                }

                for (int i = 0; i < commentIds.size(); i++) {
                    TextView textView = list.get(i);
                    final int finalI = i;
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showEdittext(v);
                            woFragment.setEditCallback(new InputBoxLayout.CallBack() {
                                @Override
                                public void callback(String text) {
//                                    JsonObject jsonObject = new JsonObject();
//                                    jsonObject.addProperty(NS.USER_ID, TempUser.id);
//                                    jsonObject.addProperty("momentId", published.getId());
//                                    jsonObject.addProperty(NS.CONTENT, text);
//                                    jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());
//                                    jsonObject.addProperty("commentId", commentIds.get(finalI));
//                                    sendComment(jsonObject);
                                }
                            });
                        }
                    });
                }

                realm1.close();

                subscriber.onNext(list);
            }
        });

        Subscriber<List<TextView>> subscriber = new Subscriber<List<TextView>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                TextView textView = new TextView(context);
                textView.setText(e.toString());
                comments.addView(textView);
                e.printStackTrace();
            }

            @Override
            public void onNext(List<TextView> textViews) {
                int jh = 0;
                LinearLayout linearLayout = (LinearLayout) View.inflate(context, R.layout.util_comment_linearlayout, null);
                for (TextView i : textViews) {
//                    comments.addView(i);
                    linearLayout.addView(i);
                    Log.d("add textview", "" + jh++);
                }
                comments_fragment.addView(linearLayout);
            }
        };

        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

//    public void

    public void simpleParse() {
        comments.removeAllViews();
        comments_fragment.removeAllViews();

        RealmList<CommentsBean> realmResults = published.getComments();
        for (final CommentsBean i : realmResults) {
            Item_Comment item_comment = new Item_Comment(context, String.valueOf(i.getUserId()),
                    i.getText(), String.valueOf(i.getId()));
            comments.addView(item_comment.getTextView());
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

    public void parseComment(Published published) {

        comments.removeAllViews();

        if (published.getComments().size() < 1) {
            return;
        }

        RealmList<CommentsBean> realmResults = published.getComments();
        for (final CommentsBean i : realmResults) {
            Item_Comment item_comment;

            if (i.isReply()) {
                item_comment = new Item_Comment(context, i.getUserName(), i.getObjectName(),
                        i.getText(), String.valueOf(i.getId()), String.valueOf(i.getObejectId()));
            } else {
                item_comment = new Item_Comment(context, i.getUserName(),
                        i.getText(), String.valueOf(i.getId()));
            }

            comments.addView(item_comment.getTextView());
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
                Intent intent = new Intent(context, rollActivity.class);
                intent.putExtra(rollActivity.TYPE, rollActivity.NOTICE);
                context.startActivity(intent);
            }
        });

        //       评论
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                woFragment.setEditCallback(new InputBoxLayout.CallBack() {
                    @Override
                    public void callback(String text) {
                        sendComment(text, -1);
                    }
                });
                showEdittext(v);
            }
        });

        praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
