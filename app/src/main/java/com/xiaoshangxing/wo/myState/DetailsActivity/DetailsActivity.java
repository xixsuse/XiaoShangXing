package com.xiaoshangxing.wo.myState.DetailsActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.netUtil.OperateUtils;
import com.xiaoshangxing.Network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.CommentsBean;
import com.xiaoshangxing.data.PublishCache;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.UserCache;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmoticonsEditText;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.input_activity.InputBoxLayout;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.wo.WoFrafment.NoScrollGridView;
import com.xiaoshangxing.wo.WoFrafment.check_photo.ImagePagerActivity;
import com.xiaoshangxing.wo.myState.check_photo.myStateNoScrollGridAdapter;
import com.xiaoshangxing.wo.myState.myStateActivity;
import com.xiaoshangxing.wo.roll.rollActivity;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/7/9
 */
public class DetailsActivity extends BaseActivity implements DetailsContract.View {

    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.myState)
    TextView myState;
    @Bind(R.id.title)
    RelativeLayout title;
    @Bind(R.id.head_image)
    CirecleImage headImage;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.college)
    TextView college;
    @Bind(R.id.text)
    EmotinText text;
    @Bind(R.id.photos1)
    NoScrollGridView photos1;
    @Bind(R.id.just_one)
    ImageView justOne;
    @Bind(R.id.location)
    TextView location;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.permission)
    ImageView permission;
    @Bind(R.id.delete)
    TextView delete;
    @Bind(R.id.praise)
    CheckBox praise;
    @Bind(R.id.comment)
    ImageView comment;
    @Bind(R.id.right_layout)
    LinearLayout rightLayout;
    @Bind(R.id.jianjiao)
    ImageView jianjiao;
    @Bind(R.id.praise_people)
    NoScrollGridView praisePeople;
    @Bind(R.id.comments)
    LinearLayout comments;
    @Bind(R.id.comment_layout)
    LinearLayout commentLayout;
    @Bind(R.id.scrollview)
    ScrollView scrollView;
    @Bind(R.id.comment_input)
    EmoticonsEditText commentInput;
    @Bind(R.id.emotion)
    ImageView emotion;
    @Bind(R.id.input_layout)
    RelativeLayout inputLayout;
    @Bind(R.id.send)
    TextView send;
    @Bind(R.id.comment_input_layout)
    RelativeLayout commentInputLayout;
    @Bind(R.id.normal_emot)
    LinearLayout normalEmot;
    @Bind(R.id.favorite)
    LinearLayout favorite;
    @Bind(R.id.delete_emot)
    RelativeLayout deleteEmot;
    @Bind(R.id.emot_type)
    RelativeLayout emotType;
    @Bind(R.id.emot_lay)
    LinearLayout emotLay;
    @Bind(R.id.edit_and_emot)
    RelativeLayout editAndEmot;
    @Bind(R.id.checkbox_lay)
    LinearLayout checkboxLay;
    @Bind(R.id.comment_click)
    LinearLayout commentClick;

    private Handler handler = new Handler();
    private InputBoxLayout inputBoxLayout;
    private DetailsContract.Presenter mPresenter;
    private int published_id;
    private Published published;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setmPresenter(new DetailPresenter(this, this));
        initView();
    }

    private void initView() {

        if (!getIntent().hasExtra(IntentStatic.DATA)) {
            showToast("动态id出错");
            finish();
        }
        published_id = getIntent().getIntExtra(IntentStatic.DATA, -1);

        published = realm.where(Published.class).equalTo(NS.ID, published_id).findFirst();
        if (published == null) {
            showToast("获取动态信息出错");
            finish();
        }

        refreshPager(published);
    }

    private void refresh() {
        PublishCache.reload(String.valueOf(published_id), new PublishCache.publishedCallback() {
            @Override
            public void callback(Published published1) {
                refreshPager(published1);
            }
        });
    }

    private void refreshPager(Published published) {
        if (published == null) {
            return;
        }
        this.published = published;
        headImage.setIntent_type(CirecleImage.PERSON_STATE, String.valueOf(published.getUserId()));
        UserCache userCache = new UserCache(this, String.valueOf(published.getUserId()), realm);
        userCache.getHead(headImage);
        userCache.getName(name);
        userCache.getCollege(college);
        text.setText(TextUtils.isEmpty(published.getText()) ? "" : published.getText());

        if (!TextUtils.isEmpty(published.getImage())) {
            String[] urls2 = published.getImage().split(NS.SPLIT);
            final ArrayList<String> imageUrls = new ArrayList<>();
            for (String i : urls2) {
                imageUrls.add(i);
            }
            photos1.setAdapter(new myStateNoScrollGridAdapter(this, imageUrls));
            photos1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(DetailsActivity.this, ImagePagerActivity.class);
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageUrls);
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                    startActivity(intent);
                }
            });
        }

        location.setText(published.getLocation());
        time.setText(TimeUtil.getTimeShowString(published.getCreateTime(), false));

        if (TempUser.isMine(String.valueOf(published.getUserId()))) {
            permission.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
        } else {
            permission.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(published.getPraiseUserIds())) {
            parsePraise();
        }
        if (published.getComments() != null && published.getComments().size() > 0) {
            parseComment();
        }

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                inputBoxLayout.remainEdittext();
                return false;
            }
        });

        initInputBox();
    }
    private void parsePraise() {
        final ArrayList<String> imageUrls = new ArrayList<>();
        String[] splits = published.getPraiseUserIds().split(NS.SPLIT);
        for (String i : splits) {
            imageUrls.add(i);
        }

        praisePeople.setVisibility(View.VISIBLE);
        praisePeople.setAdapter(new DetailPraiseAdapter(this, imageUrls, realm));
    }

    private void parseComment() {
        comments.removeAllViews();
        List<CommentsBean> list = published.getComments();
        if (list == null || list.size() < 1) {
            return;
        }

        for (final CommentsBean i : list) {
            Comment_layout comment_layout = new Comment_layout(this, i, realm);
            comments.addView(comment_layout.getView());
            comment_layout.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputBoxLayout.showOrHideLayout(true);
                    inputBoxLayout.setCallBack(new InputBoxLayout.CallBack() {
                        @Override
                        public void callback(String text) {
                            sendComment(text, i.getId());
                        }
                    });
                    final View mv = v;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int[] xy = new int[2];
                            mv.getLocationOnScreen(xy);
                            int destination = xy[1] + mv.getHeight() - inputBoxLayout.getEdittext_height();
                            scrollView.smoothScrollBy(0, destination + 10);
                        }
                    }, 300);


                }
            });
        }

    }


    private void initInputBox() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.edit_and_emot);
        inputBoxLayout = new InputBoxLayout(this, relativeLayout, this);
        inputBoxLayout.setRootVisible(View.VISIBLE);
        inputBoxLayout.setEmotion_layVisible(View.GONE);
    }

    @Override
    public void refreshData() {

    }

    @Override
    public void gotoPermisson() {
        Intent intent = new Intent(this, rollActivity.class);
        intent.putExtra(rollActivity.TYPE, rollActivity.FORBIDDEN);
        intent.putExtra(IntentStatic.DATA, published_id);
        startActivity(intent);
    }

    @Override
    public void showSureDelete() {
        final DialogUtils.Dialog_Center center = new DialogUtils.Dialog_Center(this);
        center.Message("确定删除吗?");
        center.Button("删除", "取消");
        center.MbuttonOnClick(new DialogUtils.Dialog_Center.buttonOnClick() {
            @Override
            public void onButton1() {
//                mPresenter.delete(realm, published);
                OperateUtils.deleteOnePublished(published_id, DetailsActivity.this, DetailsActivity.this, new SimpleCallBack() {
                    @Override
                    public void onSuccess() {
                        finishPager();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onBackData(Object o) {

                    }
                });
                center.close();
            }

            @Override
            public void onButton2() {
                center.close();
            }
        });
        Dialog dialog = center.create();
        dialog.show();
        LocationUtil.setWidth(this, dialog,
                getResources().getDimensionPixelSize(R.dimen.x780));
    }

    @Override
    public void finishPager() {
        Intent intent = new Intent(this, myStateActivity.class);
        intent.putExtra(IntentStatic.EXTRA_ACCOUNT, String.valueOf(TempUser.id));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void setmPresenter(@Nullable DetailsContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @OnClick({R.id.back, R.id.head_image, R.id.delete, R.id.praise, R.id.comment,
            R.id.emotion, R.id.send, R.id.checkbox_lay, R.id.comment_click})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.head_image:
                break;
            case R.id.delete:
                showSureDelete();
                break;
            case R.id.praise:
                OperateUtils.operate(published_id, DetailsActivity.this, true, NS.PRAISE, new SimpleCallBack() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onBackData(Object o) {
                        refreshPager((Published) o);
                    }
                });
                mPresenter.praise();
                break;
            case R.id.comment:
                inputBoxLayout.showOrHideLayout(true);
                inputBoxLayout.setCallBack(new InputBoxLayout.CallBack() {
                    @Override
                    public void callback(String text) {
                        sendComment(text, -1);
                    }
                });
                break;
            case R.id.checkbox_lay:
                praise.performClick();
                break;
            case R.id.comment_click:
                comment.performClick();
                break;
        }
    }

    @OnClick(R.id.permission)
    public void onClick() {
        gotoPermisson();
    }

    private void sendComment(String text, int commenId) {
        OperateUtils.Comment(published.getId(), commenId, text, this, true, new SimpleCallBack() {
            @Override
            public void onSuccess() {
                inputBoxLayout.setCallBack(null);
            }

            @Override
            public void onError(Throwable e) {
                inputBoxLayout.setCallBack(null);
                showToast("评论失败");
            }

            @Override
            public void onBackData(Object o) {
                refreshPager((Published) o);
            }
        });
    }

}
