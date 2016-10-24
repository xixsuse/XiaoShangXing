package com.xiaoshangxing.input_activity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.model.Team;
import com.xiaoshangxing.Network.Formmat;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.Network.PublishNetwork;
import com.xiaoshangxing.Network.netUtil.BaseUrl;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.netUtil.OperateUtils;
import com.xiaoshangxing.Network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.input_activity.EmotAndPicture.DividerItemDecoration;
import com.xiaoshangxing.input_activity.EmotAndPicture.EmotionGrideViewAdapter;
import com.xiaoshangxing.input_activity.EmotAndPicture.PictureAdapter;
import com.xiaoshangxing.input_activity.EmotAndPicture.ShowSelectPictureAdapter;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmoticonsEditText;
import com.xiaoshangxing.input_activity.Location.LocationActivity;
import com.xiaoshangxing.input_activity.album.AlbumActivity;
import com.xiaoshangxing.input_activity.album.AlbumHelper;
import com.xiaoshangxing.input_activity.album.Bimp;
import com.xiaoshangxing.input_activity.album.ImageBucket;
import com.xiaoshangxing.input_activity.album.ImageItem;
import com.xiaoshangxing.setting.utils.city_choosing.ArrayWheelAdapter;
import com.xiaoshangxing.setting.utils.city_choosing.OnWheelChangedListener;
import com.xiaoshangxing.setting.utils.city_choosing.WheelView;
import com.xiaoshangxing.setting.utils.headimg_set.CommonUtils;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.FileUtils;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.XSXApplication;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.normalUtils.Flog;
import com.xiaoshangxing.utils.normalUtils.KeyBoardUtils;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.yujian.IM.TeamCreateHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import okhttp3.ResponseBody;

/**
 * Created by FengChaoQun
 * on 2016/7/25
 */
public class InputActivity extends BaseActivity implements IBaseView {

    @Bind(R.id.plan_name)
    EmoticonsEditText planName;
    @Bind(R.id.plan_name_count)
    TextView planNameCount;
    @Bind(R.id.plan_name_lay)
    LinearLayout planNameLay;
    @Bind(R.id.emotion_edittext)
    EmoticonsEditText emotionEdittext;
    @Bind(R.id.show_select)
    GridView showSelect;
    @Bind(R.id.select_location_imag)
    ImageView selectLocationImag;
    @Bind(R.id.selected_location)
    TextView selectedLocation;
    @Bind(R.id.price)
    EditText price;
    @Bind(R.id.show_xianzhi_select)
    GridView showXianzhiSelect;
    @Bind(R.id.xianzhi_lay)
    RelativeLayout xianzhiLay;
    @Bind(R.id.people_limit)
    EditText peopleLimit;
    @Bind(R.id.time_limit)
    EditText timeLimit;
    @Bind(R.id.people_time_limit_lay)
    LinearLayout peopleTimeLimitLay;
    @Bind(R.id.transmit_type_image)
    CirecleImage transmitTypeImage;
    @Bind(R.id.transmit_type_text)
    TextView transmitTypeText;
    @Bind(R.id.transmit_content)
    TextView transmitContent;
    @Bind(R.id.transmit_lay)
    LinearLayout transmitLay;
    @Bind(R.id.reward_price)
    EditText rewardPrice;
    @Bind(R.id.reward_lay)
    LinearLayout rewardLay;
    @Bind(R.id.delete)
    ImageView delete;
    @Bind(R.id.emotion)
    ImageView emotion;
    @Bind(R.id.notice_someone)
    ImageView noticeSomeone;
    @Bind(R.id.forbid_someone)
    ImageView forbidSomeone;
    @Bind(R.id.location)
    ImageView location;
    @Bind(R.id.picture)
    ImageView picture;
    @Bind(R.id.camera)
    ImageView camera;
    @Bind(R.id.send)
    ImageButton send;
    @Bind(R.id.select_lay)
    LinearLayout selectLay;
    @Bind(R.id.scrollview)
    ViewPager viewPager;
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
    @Bind(R.id.recycleView)
    RecyclerView recycleView;
    @Bind(R.id.album)
    TextView album;
    @Bind(R.id.complete)
    TextView complete;
    @Bind(R.id.picture_count)
    TextView pictureCount;
    @Bind(R.id.picture_count_lay)
    FrameLayout pictureCountLay;
    @Bind(R.id.picture_lay)
    LinearLayout pictureLay;
    @Bind(R.id.select_location)
    WheelView selectLocation;
    @Bind(R.id.emot_picture)
    LinearLayout emotPicture;

    private List<View> viewlist = new ArrayList<View>();
    private List<String> iamgeurls = new ArrayList<String>();
    private PictureAdapter adapter;
    private ShowSelectPictureAdapter showSelectPictureAdapter;

    private GridView gridView;

    private int screenHeight;
    public static int EMOTION = 1;
    public static int PICTURE = 2;

    public static final int SELECT_PHOTO_ONE_BY_ONE = 10000;
    public static final int SELECT_PHOTO_FROM_ALBUM = 20000;
    public static final int TAKE_PHOTO = 30000;
    public static final int REVIEW_PHOTO = 40000;
    public static final int NOTICE = 50001;
    public static final int FOBIDDEN = 50002;
    public static final String SELECT_IMAGE_URLS = "select_image_urls";

    public static final String EDIT_STATE = "EDIT_STATE";
    public static final String LIMIT = "LIMIT";
    public int current_state;                 //当前处于的发布状态
    public static final int PUBLISH_STATE = 80001;    //发布动态
    public static final int SHOOLFELLOW_HELP = 80002; //发布校友互帮
    public static final int SHOOL_REWARD = 80003; //发布校内悬赏
    public static final int LANCH_PLAN = 80004; //发布计划
    public static final int XIANZHI = 80005; //发布闲置
    public static final int TRANSMIT = 80006; //转发
    public static final int COMMENT = 80007; //评论
    public static final String COMMENT_OBJECT = "COMMENT_OBJECT";

    public static final String TRANSMIT_TYPE = "TRANSMIT_TYPE";

    private int current;
    private Uri came_photo_path;
    private int limit;
    private EmotionGrideViewAdapter emotionGrideViewAdapter;
    private IBaseView iBaseView = this;

    private String selected_location;//选择的位置
    private List<String> notices = new ArrayList<>();//提醒的人
    private List<String> fobiddens = new ArrayList<>();//禁止的人
    private List<String> select_image_urls = new ArrayList<String>();//选择的图片

    public static final String MOMENTID = "MOMENTID";
    public static final String COMMENTID = "COMMENTID";
    private int momentId;     //评论的动态id  转发的动态id
    private int commentId;     //评论的评论id
    private Realm realm;

    private Handler handler = new Handler();
    private Runnable runnable;

    private boolean isOrig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        initPictureView();
        initState();
        initEmotView();
        initKeyboard();
        initLocation();
        initShowSelect();
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }

    private void initState() {
        Intent intent = getIntent();
        if (!intent.hasExtra(EDIT_STATE)) {
            showToast("跳转意图不明");
            finish();
        }
        if (getIntent().getIntExtra(LIMIT,0)!=0){
            limit=getIntent().getIntExtra(LIMIT,9);
        }
        current_state = intent.getIntExtra(EDIT_STATE, PUBLISH_STATE);
        switch (current_state) {
            case PUBLISH_STATE:
                break;
            case SHOOLFELLOW_HELP:
                location.setVisibility(View.GONE);
                picture.setVisibility(View.GONE);
                camera.setVisibility(View.GONE);
                emotionEdittext.setHint("输入互帮内容...");
                break;
            case SHOOL_REWARD:
                emotionEdittext.setHint("输入悬赏内容...");
                rewardLay.setVisibility(View.VISIBLE);
                location.setVisibility(View.GONE);
                picture.setVisibility(View.GONE);
                camera.setVisibility(View.GONE);
                break;
            case LANCH_PLAN:
                initLanchPlan();
                break;
            case XIANZHI:
                initSale();
                break;
            case TRANSMIT:
                initTransmit();
                break;
            case COMMENT:
                initComment();
                break;
        }
    }

    private void initEmotView() {
        gridView = (GridView) View.inflate(this, R.layout.gridelayout, null);
        emotionGrideViewAdapter = new EmotionGrideViewAdapter(this/*, this*/);
        emotionGrideViewAdapter.setMcallBack(new EmotionGrideViewAdapter.callBack() {
            @Override
            public void callback(String emot) {
                inputEmot(emot);
            }
        });
        gridView.setAdapter(emotionGrideViewAdapter);
        viewlist.add(gridView);

        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return 1;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewlist.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewlist.get(position));
                return viewlist.get(position);
            }
        };

        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SendState();
            }
        };

        emotionEdittext.addTextChangedListener(textWatcher);
        price.addTextChangedListener(textWatcher);
        rewardPrice.addTextChangedListener(textWatcher);
        price.addTextChangedListener(textWatcher);
        timeLimit.addTextChangedListener(textWatcher);

    }

    private void initPictureView() {
        recycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL_LIST));
        getDatas();
        adapter = new PictureAdapter(this, iamgeurls, this);
        recycleView.setAdapter(adapter);
        setSelectCount(0);
    }

    private void initKeyboard() {

        //监听键盘高度  让输入框保持在键盘上面
        screenHeight = ScreenUtils.getTotalHeight(this);
        KeyBoardUtils.observeSoftKeyboard(this, new KeyBoardUtils.OnSoftKeyboardChangeListener() {
            @Override
            public void onSoftKeyBoardChange(int softKeybardHeight, boolean visible) {

                if (softKeybardHeight > getResources().getDimensionPixelSize(R.dimen.y684)) {
                    selectLay.layout(0, screenHeight - softKeybardHeight - selectLay.getHeight(),
                            selectLay.getWidth(),
                            screenHeight - softKeybardHeight);
                }
            }
        });

        //输入框自获取焦点 并弹出输入键盘
        emotionEdittext.setFocusable(true);
        emotionEdittext.setFocusableInTouchMode(true);
        emotionEdittext.requestFocus();
        KeyBoardUtils.openKeybord(emotionEdittext, this);
    }

    private void initShowSelect() {
        showSelectPictureAdapter = new ShowSelectPictureAdapter(this, this);
        showSelect.setAdapter(showSelectPictureAdapter);
        showXianzhiSelect.setAdapter(showSelectPictureAdapter);
        reset();
    }

    private void initLanchPlan() {
        emotionEdittext.setHint("输入计划内容...");
        planNameLay.setVisibility(View.VISIBLE);
        peopleTimeLimitLay.setVisibility(View.VISIBLE);
        location.setVisibility(View.GONE);
        picture.setVisibility(View.GONE);
        camera.setVisibility(View.GONE);
        planName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int i = s.length();
                planNameCount.setText("" + (10 - i));
            }
        });
        planName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planName.setFocusable(true);
                planName.setFocusableInTouchMode(true);
                planName.requestFocus();
                KeyBoardUtils.openKeybord(planName, InputActivity.this);
            }
        });

    }

    private void initLocation() {
        final String[] location = new String[10];
        for (int i = 0; i < 10; i++) {
            location[i] = "园区" + i;
        }
        selectLocation.setViewAdapter(new ArrayWheelAdapter<String>(this, location));
        selectLocation.setVisibleItems(6);
        selectLocation.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                selectedLocation.setText(location[newValue]);
            }
        });
        selectedLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLocationImag.performClick();
            }
        });
    }

    private void initTransmit() {
        momentId = getIntent().getIntExtra(MOMENTID, -1);
        Published published = realm.where(Published.class).equalTo(NS.ID, momentId).findFirst();
        if (published == null) {
            showToast("动态信息异常");
            return;
        }
        emotionEdittext.setHint("顺便说点什么...");
        location.setVisibility(View.GONE);
        picture.setVisibility(View.GONE);
        camera.setVisibility(View.GONE);
        transmitLay.setVisibility(View.VISIBLE);
        int type = getIntent().getIntExtra(TRANSMIT_TYPE, 0);
        switch (type) {
            case SHOOLFELLOW_HELP:
                transmitTypeImage.setImageResource(R.mipmap.shool_help_log);
                transmitTypeText.setText("校友互帮|");
                break;
            case SHOOL_REWARD:
                transmitTypeImage.setImageResource(R.mipmap.school_reward_log);
                transmitTypeText.setText("校内悬赏|");
                break;
            case LANCH_PLAN:
                transmitTypeImage.setImageResource(R.mipmap.launch_plan_log);
                transmitTypeText.setText("计划发起|");
                break;
            case XIANZHI:
                transmitTypeImage.setImageResource(R.mipmap.xianzhi_log);
                transmitTypeText.setText("闲置出售|");
                break;
        }
        transmitContent.setText(published.getText());
    }

    private void initComment() {
        emotionEdittext.setHint("输入评论内容...");
        location.setVisibility(View.GONE);
        picture.setVisibility(View.GONE);
        camera.setVisibility(View.GONE);
        noticeSomeone.setVisibility(View.GONE);
        forbidSomeone.setVisibility(View.GONE);
        String text = getIntent().getStringExtra(COMMENT_OBJECT);
        momentId = getIntent().getIntExtra(MOMENTID, -1);
        commentId = getIntent().getIntExtra(COMMENTID, -1);
        if (!TextUtils.isEmpty(text)) {
            emotionEdittext.setHint("回复" + text);
        }
    }

    private void initSale() {
        if (!getIntent().hasExtra(SELECT_IMAGE_URLS)) {
            showToast("没有图片信息");
            return;
        } else {
            setSelect_image_urls(getIntent().getStringArrayListExtra(SELECT_IMAGE_URLS));
        }
        emotionEdittext.setHint("输入闲置内容...");
        showSelect.setVisibility(View.GONE);
        xianzhiLay.setVisibility(View.VISIBLE);
        location.setVisibility(View.GONE);
    }

    private void selectItem(int position) {
        switch (position) {
            case 0:
                normalEmot.setBackgroundColor(getResources().getColor(R.color.w2));
                favorite.setBackgroundColor(getResources().getColor(R.color.w1));
                break;
            case 1:
                normalEmot.setBackgroundColor(getResources().getColor(R.color.w1));
                favorite.setBackgroundColor(getResources().getColor(R.color.w2));
                break;
        }
    }

    public void inputEmot(String emot) {
//        emotionEdittext.append(emot);
        Editable mEditable = emotionEdittext.getText();
        //在光标位置插入图片标志
        int start = emotionEdittext.getSelectionStart();
        int end = emotionEdittext.getSelectionEnd();
        start = (start < 0 ? 0 : start);
        end = (start < 0 ? 0 : end);
        mEditable.replace(start, end, emot);
    }

    public void setSendState(boolean is) {
        if (is) {
            send.setAlpha(1f);
            send.setEnabled(true);
        } else {
            send.setEnabled(false);
            send.setAlpha(0.3f);
        }
    }

    private void SendState() {
        if (TextUtils.isEmpty(emotionEdittext.getText().toString())) {
            setSendState(false);
            return;
        }

        switch (current_state) {
            case SHOOL_REWARD:
                if (TextUtils.isEmpty(rewardPrice.getText().toString())) {
                    setSendState(false);
                } else {
                    setSendState(true);
                }
                break;
            case LANCH_PLAN:
                if (TextUtils.isEmpty(timeLimit.getText().toString())) {
                    setSendState(false);
                } else {
                    setSendState(true);
                }
                break;
            case XIANZHI:
                if (TextUtils.isEmpty(price.getText().toString()) || select_image_urls.isEmpty()
                        || selectedLocation.getText().toString().equals("未选")) {
                    setSendState(false);
                } else {
                    setSendState(true);
                }
                break;
            default:
                setSendState(true);
        }
    }

    public void getDatas() {
        AlbumHelper albumHelper=AlbumHelper.getHelper();
        albumHelper.init(XSXApplication.getInstance());
        ImageBucket imageBucket= albumHelper.getTotalImage(false);
        ArrayList<ImageItem> imageItems=(ArrayList<ImageItem>) imageBucket.imageList;
        for (ImageItem i:imageItems){
            iamgeurls.add(i.imagePath);
        }

    }

    private void showEmot(int position) {
        switch (position) {
            case 1:
                if (current == EMOTION) {
                    KeyBoardUtils.openKeybord(emotionEdittext, this);
                    emotion.setSelected(false);
                    picture.setSelected(false);
                    selectLocation.setVisibility(View.GONE);
                    current = 0;
                } else {
                    pictureLay.setVisibility(View.GONE);
                    emotLay.setVisibility(View.VISIBLE);
                    emotion.setSelected(true);
                    picture.setSelected(false);
                    selectLocation.setVisibility(View.GONE);
                    KeyBoardUtils.closeKeybord(emotionEdittext, this);
                    current = 1;
                }
                break;
            case 2:
                if (current == PICTURE) {
                    KeyBoardUtils.openKeybord(emotionEdittext, this);
                    picture.setSelected(false);
                    emotion.setSelected(false);
                    selectLocation.setVisibility(View.GONE);
                    current = 0;
                } else {
                    pictureLay.setVisibility(View.VISIBLE);
                    emotLay.setVisibility(View.GONE);
                    emotion.setSelected(false);
                    picture.setSelected(true);
                    selectLocation.setVisibility(View.GONE);
                    KeyBoardUtils.closeKeybord(emotionEdittext, this);
                    current = 2;
                }
                break;
        }
    }

    private void reset() {
        emotion.setSelected(false);
        picture.setSelected(false);
        initKeyboard();
    }

    public void setSelectCount(int count) {
        if (count == 0) {
            pictureCountLay.setVisibility(View.GONE);
        } else {
            pictureCountLay.setVisibility(View.VISIBLE);
            pictureCount.setText("" + count);
        }
    }

    public List<String> getSelect_image_urls() {
        return select_image_urls;
    }

    public void setSelect_image_urls(List<String> list) {
        this.select_image_urls = list;
        adapter.setSelect_image_urls(list);
        initShowSelect();
        addToBitm(list);
        Flog.logList("select_image_urls", select_image_urls);
    }

    @OnClick({R.id.emotion_edittext, R.id.delete, R.id.emotion, R.id.notice_someone,
            R.id.forbid_someone, R.id.location, R.id.picture, R.id.camera, R.id.send,
            R.id.normal_emot, R.id.favorite, R.id.delete_emot, R.id.album, R.id.complete,
            R.id.select_location_imag})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.emotion_edittext:
                reset();
                break;
            case R.id.delete:
                showSureDialog();
                break;
            case R.id.emotion:
                showEmot(EMOTION);
                break;
            case R.id.notice_someone:
                gotoSelectPerson(NOTICE);
                break;
            case R.id.forbid_someone:
                gotoSelectPerson(FOBIDDEN);
                break;
            case R.id.location:
                Intent location_intent = new Intent(InputActivity.this, LocationActivity.class);
                startActivityForResult(location_intent, LocationActivity.LOCATION);
                break;
            case R.id.picture:
                showEmot(PICTURE);
                break;
            case R.id.camera:
                openCamera();
                break;
            case R.id.send:
                send();
                break;
            case R.id.normal_emot:
                viewPager.setCurrentItem(0);
                break;
            case R.id.favorite:
                viewPager.setCurrentItem(1);
                break;
            case R.id.delete_emot:
                emotionEdittext.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                break;
            case R.id.album:
                Intent album_intent = new Intent(InputActivity.this, AlbumActivity.class);
                if (limit!=0){
                    album_intent.putExtra(AlbumActivity.LIMIT,limit);
                }else {
                    album_intent.putExtra(AlbumActivity.LIMIT,9);
                }
                album_intent.putExtra(AlbumActivity.SELECTED,(ArrayList<String>)select_image_urls);
                startActivityForResult(album_intent, SELECT_PHOTO_FROM_ALBUM);
                break;
            case R.id.complete:
                setSelect_image_urls(adapter.getSelect_image_urls());
                break;
            case R.id.select_location_imag:
                pictureLay.setVisibility(View.GONE);
                emotLay.setVisibility(View.GONE);
                selectLocation.setVisibility(View.VISIBLE);
                emotion.setSelected(false);
                picture.setSelected(false);
                KeyBoardUtils.closeKeybord(emotionEdittext, this);
                break;
        }
    }


    private void send(){
        switch (current_state) {
            case PUBLISH_STATE:
                publishState();
                break;
            case SHOOL_REWARD:
                publishReward();
                break;
            case SHOOLFELLOW_HELP:
                publishHelp();
                break;
            case COMMENT:
                publishComment();
                break;
            case TRANSMIT:
                publishTransmit();
                break;
            case LANCH_PLAN:
                publishPlan();
                break;
            case XIANZHI:
                publishSale();
        }
    }

    private void publish(final Map<String, String> map) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Formmat formmat = new Formmat(iBaseView, InputActivity.this, BaseUrl.BASE_URL + BaseUrl.PUBLISH);
                formmat.setSimpleCallBack(new SimpleCallBack() {
                    @Override
                    public void onSuccess() {
                        setResult(IntentStatic.PUBLISH_SUCCESS);
                        KeyBoardUtils.closeKeybord(emotionEdittext, InputActivity.this);
                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        };
                        handler.postDelayed(runnable, 250);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onBackData(Object o) {

                    }
                });
                try {
                    formmat.addFormField(map)
                            .addFilePart(select_image_urls, InputActivity.this)
                            .doUpload();
                } catch (IOException e) {
                    e.printStackTrace();
                    showToast("图片出错");
                }
            }
        });
        thread.start();
    }

    private void publishState() {
        Map<String, String> map = new HashMap<>();
        map.put(NS.USER_ID, String.valueOf(TempUser.getID(InputActivity.this)));
        map.put(NS.TEXT, emotionEdittext.getText().toString());
        map.put(NS.LOCATION, selected_location);
        map.put(NS.CLIENTTIME, String.valueOf(NS.currentTime()));
        map.put(NS.CATEGORY, NS.CATEGORY_STATE);
        map.put(NS.TIMESTAMP, String.valueOf(NS.currentTime()));
        NS.getPermissionString(NS.NOTICE, notices, map);
        NS.getPermissionString(NS.FOBIDDEN, fobiddens, map);
        publish(map);
    }

    private void publishReward() {
        Map<String, String> map = new HashMap<>();
        map.put(NS.USER_ID, String.valueOf(TempUser.getID(InputActivity.this)));
        map.put(NS.TEXT, emotionEdittext.getText().toString());
        map.put(NS.CLIENTTIME, String.valueOf(NS.currentTime()));
        map.put(NS.CATEGORY, NS.CATEGORY_REWARD);
        map.put(NS.TIMESTAMP, String.valueOf(NS.currentTime()));
        map.put(NS.PRICE, rewardPrice.getText().toString());
        NS.getPermissionString(NS.NOTICE, notices, map);
        NS.getPermissionString(NS.FOBIDDEN, fobiddens, map);
        publish(map);
    }

    public void publishHelp() {
        Map<String, String> map = new HashMap<>();
        map.put(NS.USER_ID, String.valueOf(TempUser.getID(InputActivity.this)));
        map.put(NS.TEXT, emotionEdittext.getText().toString());
        map.put(NS.CLIENTTIME, String.valueOf(NS.currentTime()));
        map.put(NS.CATEGORY, NS.CATEGORY_HELP);
        map.put(NS.TIMESTAMP, String.valueOf(NS.currentTime()));
        NS.getPermissionString(NS.NOTICE, notices, map);
        NS.getPermissionString(NS.FOBIDDEN, fobiddens, map);
        publish(map);
    }

    private void publishTransmit() {
        ProgressSubscriberOnNext<ResponseBody> next = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(e.string());
                    switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
                        case 8001:
                            showToast("转发成功");
                            finish();
                            break;
                        default:
                            showToast(jsonObject.getString(NS.MSG));
                            break;
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };
        ProgressSubsciber<ResponseBody> progressSubsciber = new ProgressSubsciber<>(next, this);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.USER_ID, TempUser.id);
        jsonObject.addProperty(NS.MOMENTID, momentId);
        jsonObject.addProperty(NS.TEXT, emotionEdittext.getText().toString());
        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());
        PublishNetwork.getInstance().transmit(progressSubsciber, jsonObject, this);
    }

    private void publishComment() {
        OperateUtils.Comment(momentId, commentId, emotionEdittext.getText().toString(), this, true,
                new SimpleCallBack() {
            @Override
            public void onSuccess() {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                };
                handler.postDelayed(runnable, 250);
            }

                    @Override
                    public void onError(Throwable e) {
                        showToast("评论失败");
                    }

                    @Override
                    public void onBackData(Object o) {

                    }
                });
    }

    private void publishPlan() {

        showLoadingDialog("");
        TeamCreateHelper.createPlanTeam(planName.getText().toString(), new RequestCallback<Team>() {
            @Override
            public void onSuccess(Team team) {

                hideLoadingDialog();

                Map<String, String> map = new HashMap<>();
                map.put(NS.USER_ID, String.valueOf(TempUser.id));
                map.put(NS.TEXT, emotionEdittext.getText().toString());
                map.put(NS.CLIENTTIME, String.valueOf(NS.currentTime()));
                map.put(NS.CATEGORY, NS.CATEGORY_PLAN);
                map.put(NS.TIMESTAMP, String.valueOf(NS.currentTime()));
                map.put(NS.PERSON_LIMIT, peopleLimit.getText().toString());
                map.put(NS.PLAN_NAME, planName.getText().toString());
                map.put(NS.DAY, timeLimit.getText().toString());
                map.put(NS.PLAN_GROUP, String.valueOf(team.getId()));
                Log.d("plan_group", "--" + team.getId());
                NS.getPermissionString(NS.NOTICE, notices, map);
                NS.getPermissionString(NS.FOBIDDEN, fobiddens, map);
                publish(map);
            }

            @Override
            public void onFailed(int i) {
                showToast("创建计划群失败：" + i);
                hideLoadingDialog();
            }

            @Override
            public void onException(Throwable throwable) {
                showToast("创建计划群失败：异常");
                throwable.printStackTrace();
                hideLoadingDialog();
            }
        });


    }

    private void publishSale() {
        Map<String, String> map = new HashMap<>();
        map.put(NS.USER_ID, String.valueOf(TempUser.id));
        map.put(NS.TEXT, emotionEdittext.getText().toString());
        map.put(NS.CLIENTTIME, String.valueOf(NS.currentTime()));
        map.put(NS.CATEGORY, NS.CATEGORY_SALE);
        map.put(NS.TIMESTAMP, String.valueOf(NS.currentTime()));
        map.put(NS.PRICE, price.getText().toString());
        map.put(NS.DORM, selectedLocation.getText().toString());
        NS.getPermissionString(NS.NOTICE, notices, map);
        NS.getPermissionString(NS.FOBIDDEN, fobiddens, map);
        publish(map);
    }

    public void showSureDialog() {
        if (emotionEdittext.getText().toString().isEmpty()){
            finish();
        }else {
            final DialogUtils.Dialog_Center center = new DialogUtils.Dialog_Center(this);
            center.Message("退出此次编辑?");
            center.Button("退出", "继续编辑");
            center.MbuttonOnClick(new DialogUtils.Dialog_Center.buttonOnClick() {
                @Override
                public void onButton1() {
                    finish();
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

    }

    public void gotoSelectPerson(int requestcode) {
        Intent intent = new Intent(InputActivity.this, SelectPersonActivity.class);
        intent.putExtra(SelectPersonActivity.REQUSET_CODE, requestcode);
        startActivityForResult(intent, requestcode);
    }

    private void openCamera() {
        if (CommonUtils.isExistCamera(this)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用android自带的照相机
            came_photo_path = FileUtils.newPhotoPath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, came_photo_path);
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            startActivityForResult(intent, TAKE_PHOTO);
        } else {
            Toast.makeText(this,
                    getResources().getString(R.string.user_no_camera),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public List<String> merge2list(List<String> host, List<String> client) {
        for (int i = 0; i < client.size(); i++) {
            if (!host.contains(client.get(i))) {
                host.add(client.get(i));
            }
        }
        return host;
    }

    public void addToBitm(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            ImageItem imageItem = new ImageItem();
            imageItem.setImagePath(list.get(i));
            imageItem.setSelected(true);
            if (!Bimp.tempSelectBitmap.contains(imageItem)) {
                Bimp.tempSelectBitmap.add(imageItem);
            }
        }
    }

    /**
     * 解决小米手机上获取图片路径为null的情况
     *
     * @param intent
     * @return
     */
    public Uri geturi(Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }

    public int getCurrent_state() {
        return current_state;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case SELECT_PHOTO_ONE_BY_ONE:
                setSelect_image_urls(data.getStringArrayListExtra(SELECT_IMAGE_URLS));
                isOrig=data.getBooleanExtra(IntentStatic.IS_ORIG,false);
                break;
            case SELECT_PHOTO_FROM_ALBUM:
                setSelect_image_urls(data.getStringArrayListExtra(SELECT_IMAGE_URLS));
                isOrig=data.getBooleanExtra(IntentStatic.IS_ORIG,false);
                break;
            case REVIEW_PHOTO:
                setSelect_image_urls(data.getStringArrayListExtra(SELECT_IMAGE_URLS));
                break;
            case TAKE_PHOTO:
                List<String> temp = new ArrayList<String>();
                temp.add(came_photo_path.getPath());
                Log.d("came_photo_path", came_photo_path.toString());
                setSelect_image_urls(merge2list(select_image_urls, temp));
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(came_photo_path);
                sendBroadcast(intent);
                break;
            case LocationActivity.LOCATION:
                selected_location = data.getStringExtra(LocationActivity.SELECTED);
                Log.d("select_location", selected_location);
                break;
            case FOBIDDEN:
                fobiddens = data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON);
                Flog.logList("fobiddens", fobiddens);
                break;
            case NOTICE:
                notices = data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON);
                Flog.logList("notices", notices);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        Bimp.tempSelectBitmap.clear();
        KeyBoardUtils.closeKeybord(emotionEdittext, this);
        realm.close();
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        KeyBoardUtils.closeKeybord(emotionEdittext, this);
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showSureDialog();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @OnClick(R.id.select_location_imag)
    public void onClick() {
    }

}



