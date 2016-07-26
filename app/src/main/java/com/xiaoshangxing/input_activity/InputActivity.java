package com.xiaoshangxing.input_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.input_activity.EmotAndPicture.DividerItemDecoration;
import com.xiaoshangxing.input_activity.EmotAndPicture.GrideViewAdapter;
import com.xiaoshangxing.input_activity.EmotAndPicture.PictureAdapter;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmoticonsEditText;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.normalUtils.KeyBoardUtils;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/7/25
 */
public class InputActivity extends BaseActivity {
    @Bind(R.id.emotion_edittext)
    EmoticonsEditText emotionEdittext;
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
    @Bind(R.id.select_lay)
    LinearLayout selectLay;
    @Bind(R.id.scrollView)
    ViewPager viewPager;
    @Bind(R.id.normal_emot)
    LinearLayout normalEmot;
    @Bind(R.id.favorite)
    LinearLayout favorite;
    @Bind(R.id.delete_emot)
    RelativeLayout deleteEmot;
    @Bind(R.id.emot_type)
    RelativeLayout emotType;
    @Bind(R.id.emot_picture)
    LinearLayout emotPicture;
    @Bind(R.id.send)
    ImageButton send;
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
    private List<View> viewlist = new ArrayList<View>();
    private List<String> iamgeurls = new ArrayList<String>();
    private List<String> select_image_urls = new ArrayList<String>();
    private PictureAdapter adapter;

    private GridView gridView;

    private int screenHeight;
    public static int EMOTION = 1;
    public static int PICTURE = 2;

    public static int SELECT_PHOTO_RESULT;
    public static String SELECT_IMAGE_URLS = "select_image_urls";
    public static String SELECT_IMAGE_POSITION = "select_image_position";

    private int current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        ButterKnife.bind(this);
        initEmotView();
        initPictureView();
        initKeyboard();
    }

    private void initEmotView() {
        gridView = (GridView) View.inflate(this, R.layout.gridelayout, null);
        final GrideViewAdapter adapter = new GrideViewAdapter(this, this);
        gridView.setAdapter(adapter);
        TextView textView = new TextView(this);
        textView.setText("55555");
        viewlist.add(gridView);
        viewlist.add(textView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

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
        screenHeight = ScreenUtils.getScreenHeight(this);
        KeyBoardUtils.observeSoftKeyboard(this, new KeyBoardUtils.OnSoftKeyboardChangeListener() {
            @Override
            public void onSoftKeyBoardChange(int softKeybardHeight, boolean visible) {

                if (softKeybardHeight > getResources().getDimensionPixelSize(R.dimen.y684)) {
                    selectLay.layout(0, screenHeight - softKeybardHeight - selectLay.getHeight(),
                            selectLay.getWidth(),
                            screenHeight - softKeybardHeight);
                    Log.d("keyboard", "" + softKeybardHeight);
                    Log.d("height", "" + screenHeight);
                }
            }
        });

        //输入框自获取焦点 并弹出输入键盘
        emotionEdittext.setFocusable(true);
        emotionEdittext.setFocusableInTouchMode(true);
        emotionEdittext.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
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
        String path = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/Camera";
        Log.d("CameraPath", path);
        File file = new File(path);
        File[] files = file.listFiles();
        Log.d("ddd", String.valueOf(files.length));
        Matrix m = new Matrix();
        m.postRotate(90);
        for (File mfile : files) {
            iamgeurls.add(mfile.getPath());
        }

    }

    private void showEmot(int position) {
        switch (position) {
            case 1:
                if (current == 1) {
                    KeyBoardUtils.openKeybord(emotionEdittext, this);
                    emotion.setSelected(false);
                    picture.setSelected(false);
                    current = 0;
                } else {
                    pictureLay.setVisibility(View.GONE);
                    emotLay.setVisibility(View.VISIBLE);
                    emotion.setSelected(true);
                    picture.setSelected(false);
                    KeyBoardUtils.closeKeybord(emotionEdittext, this);
                    current = 1;
                }
                break;
            case 2:
                if (current == 2) {
                    KeyBoardUtils.openKeybord(emotionEdittext, this);
                    picture.setSelected(false);
                    emotion.setSelected(false);
                    current = 0;
                } else {
                    pictureLay.setVisibility(View.VISIBLE);
                    emotLay.setVisibility(View.GONE);
                    emotion.setSelected(false);
                    picture.setSelected(true);
                    KeyBoardUtils.closeKeybord(emotionEdittext, this);
                    current = 2;
                }
                break;
        }
    }

    private void reset() {
        emotion.setSelected(false);
        picture.setSelected(false);
    }

    public void setSelectCount(int count) {
        pictureCount.setText("" + count);
    }

    @OnClick({R.id.emotion_edittext, R.id.delete, R.id.emotion, R.id.notice_someone,
            R.id.forbid_someone, R.id.location, R.id.picture, R.id.camera, R.id.send,
            R.id.normal_emot, R.id.favorite, R.id.delete_emot, R.id.album, R.id.complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.emotion_edittext:
                reset();
                break;
            case R.id.delete:
                break;
            case R.id.emotion:
                showEmot(EMOTION);
                break;
            case R.id.notice_someone:
                break;
            case R.id.forbid_someone:
                break;
            case R.id.location:
                break;
            case R.id.picture:
                showEmot(PICTURE);
                break;
            case R.id.camera:
                break;
            case R.id.send:
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
//                Intent album_intent=new Intent(InputActivity.this, PhotoChoosingActivity.class);
//                startActivity(album_intent);
                break;
            case R.id.complete:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PHOTO_RESULT) {
            select_image_urls = data.getStringArrayListExtra(SELECT_IMAGE_URLS);
            adapter.setSelectPosition(data.getIntegerArrayListExtra(SELECT_IMAGE_POSITION));
            Log.d("select_image", "" + select_image_urls.toString());
        }
    }
}

