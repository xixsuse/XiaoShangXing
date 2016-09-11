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

import com.xiaoshangxing.Network.NS;
import com.xiaoshangxing.R;
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
import com.xiaoshangxing.wo.roll.rollActivity;

import java.util.ArrayList;

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
        initInputBox();
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

        UserCache userCache = new UserCache(this, String.valueOf(published.getUserId()), realm);
        userCache.getHead(headImage);
        userCache.getName(name);
        userCache.getCollege(college);
        text.setText(published.getText());

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

//        for (int i = 0; i <= 20; i++) {
//            CirecleImage cirecleImage = new CirecleImage(this);
//            cirecleImage.setImageResource(R.mipmap.cirecleimage_default);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    getResources().getDimensionPixelSize(R.dimen.x96),
//                    getResources().getDimensionPixelSize(R.dimen.y96));
//            cirecleImage.setLayoutParams(params);
//            cirecleImage.setPadding(getResources().getDimensionPixelSize(R.dimen.x20), 0, 0, 0);
//
//            cirecleImage.setIntent_type(CirecleImage.PERSON_STATE, null);
//
//            praisePeople.addView(cirecleImage);
//
//            final Comment_layout comment_layout = new Comment_layout(this);
//            comments.addView(comment_layout.getView());
//            comment_layout.getView().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    inputBoxLayout.showOrHideLayout(true);
//                    final View mv = v;
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            int[] xy = new int[2];
//                            mv.getLocationOnScreen(xy);
//                            int destination = xy[1] + mv.getHeight() - inputBoxLayout.getEdittext_height();
//                            scrollView.smoothScrollBy(0, destination + 10);
//                        }
//                    }, 300);
//
//                }
//            });
//
//            CirecleImage cirecleImage1 = (CirecleImage) comment_layout.getView().findViewById(R.id.head_image);
//            cirecleImage1.setIntent_type(CirecleImage.PERSON_STATE, null);
//        }

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    inputBoxLayout.remainEdittext();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    inputBoxLayout.remainEdittext();
                }
                return false;
            }
        });
    }

    private void parsePraise() {
//        for (String i : published.getPraiseUserIds().split(NS.SPLIT)) {
//
//            CirecleImage cirecleImage = new CirecleImage(this);
//            cirecleImage.setImageResource(R.mipmap.cirecleimage_default);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    getResources().getDimensionPixelSize(R.dimen.x96),
//                    getResources().getDimensionPixelSize(R.dimen.y96));
//            cirecleImage.setLayoutParams(params);
//            cirecleImage.setPadding(getResources().getDimensionPixelSize(R.dimen.x20), 0, 0, 0);
//
//            CirecleImage cirecleImage1 = (CirecleImage) View.inflate(this, R.layout.util_circle_image_96, null).findViewById(R.id.head_image);
//
//            cirecleImage1.setIntent_type(CirecleImage.PERSON_STATE, i);
//
//            UserCache userCache = new UserCache(this, i, realm);
//            userCache.getHead(cirecleImage1);
//
//            praisePeople.addView(cirecleImage1);
//
//        }

        final ArrayList<String> imageUrls = new ArrayList<>();
        String[] splits = published.getPraiseUserIds().split(NS.SPLIT);
        for (String i : splits) {
            imageUrls.add(i);
        }

        praisePeople.setVisibility(View.VISIBLE);
        praisePeople.setAdapter(new DetailPraiseAdapter(this, imageUrls,realm));
//        praisePeople.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(DetailsActivity.this, ImagePagerActivity.class);
//                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageUrls);
//                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
//                startActivity(intent);
//            }
//        });
    }

    private void parseComment() {
        final Comment_layout comment_layout = new Comment_layout(this);
        comments.addView(comment_layout.getView());
        comment_layout.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputBoxLayout.showOrHideLayout(true);
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

        CirecleImage cirecleImage1 = (CirecleImage) comment_layout.getView().findViewById(R.id.head_image);
        cirecleImage1.setIntent_type(CirecleImage.PERSON_STATE, null);

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
        intent.putExtra(rollActivity.TYPE, rollActivity.NOTICE);
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
                mPresenter.delete();
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
    public void setmPresenter(@Nullable DetailsContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @OnClick({R.id.back, R.id.head_image, R.id.delete, R.id.praise, R.id.comment, R.id.emotion, R.id.send})
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
                mPresenter.praise();
                break;
            case R.id.comment:
                inputBoxLayout.showOrHideLayout(true);
                break;
        }
    }

    @OnClick(R.id.permission)
    public void onClick() {
        gotoPermisson();
    }
}
