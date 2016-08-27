package com.xiaoshangxing.input_activity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.Network.PublishNetwork;
import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.data.CommentsBean;
import com.xiaoshangxing.data.Published;
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
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.normalUtils.KeyBoardUtils;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    private List<String> select_image_urls = new ArrayList<String>();
    private PictureAdapter adapter;
    private ShowSelectPictureAdapter showSelectPictureAdapter;

    private GridView gridView;

    private int screenHeight;
    public static int EMOTION = 1;
    public static int PICTURE = 2;

    public static int SELECT_PHOTO_RESULT_1 = 10000;
    public static int SELECT_PHOTO_FROM_ALBUM = 20000;
    public static int TAKE_PHOTO = 30000;
    public static int REVIEW_PHOTO = 40000;
    public static String SELECT_IMAGE_URLS = "select_image_urls";

    public static String EDIT_STATE = "EDIT_STATE";
    public static String LIMIT = "LIMIT";
    public int current_state;                 //当前处于的发布状态
    public static final int PUBLISH_STATE = 80001;    //发布动态
    public static final int SHOOLFELLOW_HELP = 80002; //发布校友互帮
    public static final int SHOOL_REWARD = 80003; //发布校内悬赏
    public static final int LANCH_PLAN = 80004; //发布计划
    public static final int XIANZHI = 80005; //发布闲置
    public static final int TRANSMIT = 80006; //转发
    public static final int COMMENT = 80007; //评论
    public static final String COMMENT_OBJECT = "COMMENT_OBJECT";

    public static String TRANSMIT_TYPE = "TRANSMIT_TYPE";

    private int current;
    private Uri came_photo_path;
    private int limit;
    private EmotionGrideViewAdapter emotionGrideViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        ButterKnife.bind(this);
        initState();
        initEmotView();
        initPictureView();
        initKeyboard();
        initLocation();
        initShowSelect();
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }

    private void initState() {
        Intent intent = getIntent();
        if (getIntent().getIntExtra(LIMIT,0)!=0){
            limit=getIntent().getIntExtra(LIMIT,9);
        }
        current_state = intent.getIntExtra(EDIT_STATE, 80001);
        switch (intent.getIntExtra(EDIT_STATE, 80001)) {
            case PUBLISH_STATE:
                break;
            case SHOOLFELLOW_HELP:
                location.setVisibility(View.INVISIBLE);
                picture.setVisibility(View.INVISIBLE);
                camera.setVisibility(View.INVISIBLE);
                emotionEdittext.setHint("输入互帮内容...");
                break;
            case SHOOL_REWARD:
                emotionEdittext.setHint("输入悬赏内容...");
                rewardLay.setVisibility(View.VISIBLE);
                location.setVisibility(View.INVISIBLE);
                picture.setVisibility(View.INVISIBLE);
                camera.setVisibility(View.INVISIBLE);
                break;
            case LANCH_PLAN:
                initLanchPlan();
                break;
            case XIANZHI:
                emotionEdittext.setHint("输入闲置内容...");
                showSelect.setVisibility(View.GONE);
                xianzhiLay.setVisibility(View.VISIBLE);
                location.setVisibility(View.GONE);
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
        TextView textView = new TextView(this);
        textView.setText("55555");
        viewlist.add(gridView);
        viewlist.add(textView);

        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return 2;
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

        emotionEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    setSendState(true);
                } else {
                    setSendState(false);
                }
            }
        });

    }

    private void initPictureView() {
        recycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL_LIST));
        getDatas();
        adapter = new PictureAdapter(this, iamgeurls, this);
        recycleView.setAdapter(adapter);
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
        location.setVisibility(View.INVISIBLE);
        picture.setVisibility(View.INVISIBLE);
        camera.setVisibility(View.INVISIBLE);
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

//        emotionGrideViewAdapter.setMcallBack(new EmotionGrideViewAdapter.callBack() {
//            @Override
//            public void callback(String emot) {
//                planName.append(emot);
//            }
//        });
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
        emotionEdittext.setHint("顺便说点什么...");
        location.setVisibility(View.INVISIBLE);
        picture.setVisibility(View.INVISIBLE);
        camera.setVisibility(View.INVISIBLE);
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
    }

    private void initComment() {
        emotionEdittext.setHint("输入评论内容...");
        location.setVisibility(View.INVISIBLE);
        picture.setVisibility(View.INVISIBLE);
        camera.setVisibility(View.INVISIBLE);
        noticeSomeone.setVisibility(View.INVISIBLE);
        forbidSomeone.setVisibility(View.INVISIBLE);
        String text = getIntent().getStringExtra(COMMENT_OBJECT);
        if (!TextUtils.isEmpty(text)) {
            emotionEdittext.setHint("回复" + text);
        }
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
        emotionEdittext.append(emot);
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

    public void getDatas() {
//        String path = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/Camera";
//        Log.d("CameraPath", path);
//        File file = new File(path);
//        File[] files = file.listFiles();
//        Log.d("ddd", String.valueOf(files.length));
//        Arrays.sort(files, new TimeComparator());
//        Matrix m = new Matrix();
//        m.postRotate(90);
//        for (File mfile : files) {
//            iamgeurls.add(mfile.getPath());
//        }
        AlbumHelper albumHelper=AlbumHelper.getHelper();
        albumHelper.init(this);
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
        pictureCount.setText("" + count);
    }

    public List<String> getSelect_image_urls() {
        return select_image_urls;
    }

    public void setSelect_image_urls(List<String> list) {
        this.select_image_urls = list;
        adapter.setSelect_image_urls(list);
        initShowSelect();
        addToBitm(list);
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
                gotoSelectPerson();
                break;
            case R.id.forbid_someone:
                gotoSelectPerson();
                break;
            case R.id.location:
                Intent location_intent = new Intent(InputActivity.this, LocationActivity.class);
                startActivityForResult(location_intent, IntentStatic.CODE);
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

//    test
    private void send(){
        ProgressSubscriberOnNext<ResponseBody> onNext1=new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(final ResponseBody e) {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        try {
                            String string = e.string();
                            Log.d("ttttt", string);
                            JSONObject jsonObject = new JSONObject(string);
                            Log.d("jsonObject", jsonObject.toString());
                            JSONObject msg = jsonObject.getJSONObject("msg");
                            Log.d("msg", msg.toString());
                            JSONArray moments = msg.getJSONArray("moments");
                            Log.d("moments", moments.toString());
                            realm.createOrUpdateAllFromJson(Published.class, moments);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                    }
                });


                RealmResults<Published> publisheds = realm.where(Published.class).findAll();
                RealmList<CommentsBean> commentsBeen = publisheds.where().findFirst().getComments();
                Log.d("RealmResults", publisheds.toString());
                for (int i = 0; i < commentsBeen.size(); i++) {
                    Log.d("commentsBeen", commentsBeen.get(i).toString());
                }
                realm.close();

            }
        };



        ProgressSubsciber<ResponseBody> progressSubsciber=new ProgressSubsciber<>(onNext1,this);
//        LoginNetwork.getInstance().bindEmail(progressSubsciber,bindEmai,this);

        String path = "/sdcard/XSX/test.png";

        File file = new File(path);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
//        LoginNetwork.getInstance().setUserImage(progressSubsciber,42,body,11,this);

//        SetUserImage1 setUserImage1= Network.getRetrofitWithHeader(this).create(SetUserImage1.class);
//        Call<ResponseBody> call=setUserImage1.setUserImage(42,path,11);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    Log.d("ResponseBody",response.body().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
//        Publish publish=new Publish();
//        publish.setUserId(1);
//        publish.setText("''''''''i");
//        File file=new File("file:///android_asset/emoji/default/emoji_00.png");
//        publish.getImages().add(new File("file:///android_asset/emoji/default/emoji_00.png"));
//        LoginNetwork.getInstance().Publish(progressSubsciber,publish,this);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", 1);
        jsonObject.addProperty("category", 1);
        jsonObject.addProperty("timeStamp", System.currentTimeMillis());

//        Realm realm=Realm.getDefaultInstance();
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                User user=realm.createObject(User.class);
//                user.setUsername("'''''''fcq");
//            }
//        });
//
//        User user=realm.where(User.class).findFirst();
//        Log.d("user",user.toString());
        PublishNetwork.getInstance().getPublished(progressSubsciber, jsonObject, this);
//        LoginNetwork.getInstance().GetUser(progressSubsciber,jsonObject);
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

    public void gotoSelectPerson() {
        Intent intent = new Intent(InputActivity.this, SelectPersonActivity.class);
        startActivity(intent);
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
        if (requestCode == SELECT_PHOTO_RESULT_1) {
            setSelect_image_urls(data.getStringArrayListExtra(SELECT_IMAGE_URLS));
        } else if (requestCode == SELECT_PHOTO_FROM_ALBUM) {
//            ArrayList<String> selectPicture = new ArrayList<String>();
//            for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
//                selectPicture.add(Bimp.tempSelectBitmap.get(i).imagePath);
//            }
//            setSelect_image_urls(selectPicture);
            setSelect_image_urls(data.getStringArrayListExtra(SELECT_IMAGE_URLS));

        } else if (requestCode == REVIEW_PHOTO) {
            setSelect_image_urls(data.getStringArrayListExtra(SELECT_IMAGE_URLS));
        } else if (requestCode == TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                List<String> temp = new ArrayList<String>();
                temp.add(came_photo_path.toString());
                Log.d("came_photo_path", came_photo_path.toString());
                setSelect_image_urls(merge2list(select_image_urls, temp));
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(came_photo_path);
                sendBroadcast(intent);
            }
        } else if (requestCode == IntentStatic.CODE) {
            String select_location = data.getStringExtra(LocationActivity.SELECTED);
            Log.d("select_location", select_location);
        }
    }

    @Override
    protected void onDestroy() {
        Bimp.tempSelectBitmap.clear();
        KeyBoardUtils.closeKeybord(emotionEdittext, this);
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

