package com.xiaoshangxing.yujian.ChatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.media.record.AudioRecorder;
import com.netease.nimlib.sdk.media.record.IAudioRecordCallback;
import com.netease.nimlib.sdk.media.record.RecordType;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.CustomNotificationConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.xiaoshangxing.R;
import com.xiaoshangxing.input_activity.EmotAndPicture.DividerItemDecoration;
import com.xiaoshangxing.input_activity.EmotAndPicture.EmotionGrideViewAdapter;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmoticonsEditText;
import com.xiaoshangxing.input_activity.InputActivity;
import com.xiaoshangxing.input_activity.album.AlbumActivity;
import com.xiaoshangxing.input_activity.album.AlbumHelper;
import com.xiaoshangxing.input_activity.album.ImageBucket;
import com.xiaoshangxing.input_activity.album.ImageItem;
import com.xiaoshangxing.input_activity.check_photo.inputSelectPhotoPagerActivity;
import com.xiaoshangxing.setting.utils.headimg_set.CommonUtils;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.FileUtils;
import com.xiaoshangxing.utils.normalUtils.KeyBoardUtils;
import com.xiaoshangxing.yujian.IM.NimUIKit;
import com.xiaoshangxing.yujian.IM.kit.string.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 底部文本编辑，语音等模块
 * Created by hzxuwen on 2015/6/16.
 */
public class InputPanel implements IAudioRecordCallback, View.OnClickListener {

    private static final String TAG = "MsgSendLayout";
    //      延迟展示布局时间
    private static final int SHOW_LAYOUT_DELAY = 200;

    protected Container container;
    protected View view;
    protected Handler uiHandler;

    protected View audioAnimLayout; // 录音动画布局

    public final int PICK_IMAGE = 1;

    private View TextInputLay;//文字输入布局
    private EmoticonsEditText emoticonsEditText;//文字输入框
    private Button send;//发送按钮
    private Button AudioRecord; //语音录制按钮
    private ImageView SwitchToAudio;//切换到语音输入
    private ImageView SwitchToText;//切换到文字输入
    private ImageView picture;//图片按钮
    private ImageView camera;//拍照按钮
    private ImageView emotion;//表情按钮
    private View emotion_lay;//表情布局
    private ViewPager emotion_viewpager;//表情列表
    private View picture_lay;//图片布局
    private RecyclerView picture_recyclerview;//图片列表
    private View album;//相册按钮
    private View picture_count_lay;//完成选择图片
    private TextView select_picture_complete;
    private TextView picture_count;//选择的图片数目

    private GridView gridView;
    private EmotionGrideViewAdapter emotionGrideViewAdapter;
    private ChatPictureAdapter adapter;
    private List<String> iamgeurls = new ArrayList<String>();
    private List<String> Select_image_urls = new ArrayList<String>();
    // 语音
    protected AudioRecorder audioMessageHelper;
    private Chronometer time;
    private TextView timerTip;
    private LinearLayout timerTipContainer;
    private boolean started = false;
    private boolean cancelled = false;
    private boolean touched = false; // 是否按着
    private boolean isKeyboardShowed = true; // 是否显示键盘


    // state
    private boolean actionPanelBottomLayoutHasSetup = false;
    private boolean isTextAudioSwitchShow = true;


    // data
    private long typingTime = 0;

    public InputPanel(Container container, View view, boolean isTextAudioSwitchShow) {
        this.container = container;
        this.view = view;
        this.uiHandler = new Handler();
        this.isTextAudioSwitchShow = isTextAudioSwitchShow;
        init();
    }

    public void onPause() {
        // 停止录音
        if (audioMessageHelper != null) {
            onEndAudioRecord(true);
        }
    }

    //      收起输入面板
    public boolean collapse(boolean immediately) {
        boolean respond = (emotion_lay != null && emotion_lay.getVisibility() == View.VISIBLE
                || picture_lay != null && picture_lay.getVisibility() == View.VISIBLE);

        hideAllInputLayout(immediately);

        return respond;     //标志是否需要截取事件用来收起输入面板
    }

    private void init() {
        initViews();
        initTextEdit();
        initAudioRecordButton();
        restoreText(false);
    }

    private void initViews() {
        TextInputLay = view.findViewById(R.id.text_input_lay);
        emoticonsEditText = (EmoticonsEditText) view.findViewById(R.id.edittext);
        send = (Button) view.findViewById(R.id.send);
        send.setOnClickListener(this);
        AudioRecord = (Button) view.findViewById(R.id.audioRecord);
        audioAnimLayout = view.findViewById(R.id.layoutPlayAudio);
        time = (Chronometer) view.findViewById(R.id.timer);
        timerTip = (TextView) view.findViewById(R.id.timer_tip);
        timerTipContainer = (LinearLayout) view.findViewById(R.id.timer_tip_container);
        SwitchToAudio = (ImageView) view.findViewById(R.id.switch_to_audio);
        SwitchToAudio.setOnClickListener(this);
        SwitchToText = (ImageView) view.findViewById(R.id.switch_to_text);
        SwitchToText.setOnClickListener(this);
        picture = (ImageView) view.findViewById(R.id.picture);
        picture.setOnClickListener(this);
        camera = (ImageView) view.findViewById(R.id.camera);
        camera.setOnClickListener(this);
        emotion = (ImageView) view.findViewById(R.id.emotion);
        emotion.setOnClickListener(this);
        emotion_lay = view.findViewById(R.id.emot_lay);
        emotion_viewpager = (ViewPager) view.findViewById(R.id.scrollview);
        picture_lay = view.findViewById(R.id.picture_lay);
        picture_recyclerview = (RecyclerView) view.findViewById(R.id.recycleView);
        album = view.findViewById(R.id.album);
        album.setOnClickListener(this);
        picture_count_lay = view.findViewById(R.id.picture_count_lay);
        picture_count_lay.setOnClickListener(this);
        picture_lay.setOnClickListener(this);
        picture_count = (TextView) view.findViewById(R.id.picture_count);
        select_picture_complete = (TextView) view.findViewById(R.id.complete);
        select_picture_complete.setOnClickListener(this);


        switchToTextLayout(false);
        initEmotView();
        initPictureView();
    }

    private void initEmotView() {
        gridView = (GridView) View.inflate(container.activity, R.layout.gridelayout, null);
        emotionGrideViewAdapter = new EmotionGrideViewAdapter(container.activity);
        emotionGrideViewAdapter.setMcallBack(new EmotionGrideViewAdapter.callBack() {
            @Override
            public void callback(String emot) {
//                inputEmot(emot);
                Editable mEditable = emoticonsEditText.getText();
                //在光标位置插入图片标志
                int start = emoticonsEditText.getSelectionStart();
                int end = emoticonsEditText.getSelectionEnd();
                start = (start < 0 ? 0 : start);
                end = (start < 0 ? 0 : end);
                mEditable.replace(start, end, emot);
            }
        });
        gridView.setAdapter(emotionGrideViewAdapter);

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
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(gridView);

                return gridView;
            }
        };

        emotion_viewpager.setAdapter(pagerAdapter);
        emotion_viewpager.setCurrentItem(0);
    }

    private void initPictureView() {
        picture_recyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        picture_recyclerview.setItemAnimator(new DefaultItemAnimator());
        picture_recyclerview.addItemDecoration(new DividerItemDecoration(container.activity, DividerItemDecoration.HORIZONTAL_LIST));
        getDatas();
        adapter = new ChatPictureAdapter(container.activity, iamgeurls, this);
        picture_recyclerview.setAdapter(adapter);
    }

    public void getDatas() {
        AlbumHelper albumHelper = AlbumHelper.getHelper();
        albumHelper.init(container.activity);
        ImageBucket imageBucket = albumHelper.getTotalImage(false);
        ArrayList<ImageItem> imageItems = (ArrayList<ImageItem>) imageBucket.imageList;
        for (ImageItem i : imageItems) {
            iamgeurls.add(i.imagePath);
        }
    }

    public List<String> getSelect_image_urls() {
        return Select_image_urls;
    }

    public void setSelect_image_urls(List<String> select_image_urls) {
        Select_image_urls = select_image_urls;
    }

    public Activity getActivity() {
        return container.activity;
    }

    public void setSelectCount(int count) {
        picture_count.setText("" + count);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_to_text:
                switchToTextLayout(true);
                break;
            case R.id.switch_to_audio:
                switchToAudioLayout();
                break;
            case R.id.send:
                onTextMessageSendButtonPressed();
                break;
            case R.id.emotion:
                toggleEmojiLayout();
                break;
            case R.id.picture:
                ClickOnPicture();
                break;
            case R.id.picture_count_lay:
                Select_image_urls = adapter.getSelect_image_urls();
                sendImage();
                break;
            case R.id.complete:
                picture_count_lay.performClick();
                break;
            case R.id.album:
                Intent album_intent = new Intent(container.activity, AlbumActivity.class);
                album_intent.putExtra(AlbumActivity.LIMIT, 9);
                album_intent.putExtra(AlbumActivity.SELECTED, (ArrayList<String>) Select_image_urls);
                container.activity.startActivityForResult(album_intent, AlbumActivity.SELECT_PHOTO_FROM_ALBUM);
                break;
            case R.id.camera:
                openCamera();
                break;
        }
    }

    private Uri came_photo_path;
    public static final int TAKE_PHOTO = 30000;

    private void openCamera() {
        if (CommonUtils.isExistCamera(container.activity)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用android自带的照相机
            came_photo_path = FileUtils.newPhotoPath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, came_photo_path);
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            container.activity.startActivityForResult(intent, TAKE_PHOTO);
        } else {
            Toast.makeText(container.activity,
                    "卧槽,没找到你手机上的相机你敢信?!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /*
    **describe:初始化文字输入框
    * 点击展示文字输入布局
    * 文字变化监听 判断是否显示发送按钮
    * 替换文字为表情
    * 输入时发送正在输入通知
    */
    private void initTextEdit() {

        emoticonsEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        emoticonsEditText.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    switchToTextLayout(true);
                }
                return false;
            }
        });

        emoticonsEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                emoticonsEditText.setHint("");
                checkSendButtonEnable(emoticonsEditText);
            }
        });

        emoticonsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sendTypingCommand();
                checkSendButtonEnable(emoticonsEditText);
            }
        });

    }


    /**
     * 发送“正在输入”通知
     */
    private void sendTypingCommand() {
        if (container.account.equals(NimUIKit.getAccount())) {
            return;
        }

        if (container.sessionType == SessionTypeEnum.Team || container.sessionType == SessionTypeEnum.ChatRoom) {
            return;
        }

        if (System.currentTimeMillis() - typingTime > 5000L) {
            typingTime = System.currentTimeMillis();
            CustomNotification command = new CustomNotification();
            command.setSessionId(container.account);
            command.setSessionType(container.sessionType);
            CustomNotificationConfig config = new CustomNotificationConfig();
            config.enablePush = false;      //不推送
            config.enableUnreadCount = false;   //不计入未读信息
            command.setConfig(config);

            JSONObject json = new JSONObject();
            try {
                json.put("id", "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            command.setContent(json.toString());

            NIMClient.getService(MsgService.class).sendCustomNotification(command);
        }
    }

    /**
     * ************************* 键盘布局切换 *******************************
     */

//    private View.OnClickListener clickListener = new View.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            if (v == switchToTextButtonInInputBar) {
//                switchToTextLayout(true);// 显示文本发送的布局
//            } else if (v == sendMessageButtonInInputBar) {
//                onTextMessageSendButtonPressed();//发送消息
//            } else if (v == switchToAudioButtonInInputBar) {
//                switchToAudioLayout();//显示发送语音消息布局
//            } else if (v == moreFuntionButtonInInputBar) {
//                toggleActionPanelLayout();//显示更多布局
//            } else if (v == emojiButtonInInputBar) {
//                toggleEmojiLayout();//显示表情输入布局
//            }
//        }
//    };

    // 点击edittext，切换键盘和更多布局
    private void switchToTextLayout(boolean needShowInput) {
        hideEmojiLayout();
        showPictureLay(false);
        AudioRecord.setVisibility(View.GONE);
        emoticonsEditText.setVisibility(View.VISIBLE);
        SwitchToText.setVisibility(View.GONE);
        SwitchToAudio.setVisibility(View.VISIBLE);
        if (needShowInput) {
            uiHandler.postDelayed(showTextRunnable, SHOW_LAYOUT_DELAY);
        } else {
            hideInputMethod();
        }
    }

    // 发送文本消息
    private void onTextMessageSendButtonPressed() {
        String text = emoticonsEditText.getText().toString();
        IMMessage textMessage = createTextMessage(text);

        if (container.proxy.sendMessage(textMessage)) {
            restoreText(true);
        }
    }

    private void ClickOnPicture() {
        if (picture_lay.getVisibility() == View.VISIBLE) {
            showPictureLay(false);
        } else {
            showPictureLay(true);
        }
        emotion_lay.setVisibility(View.GONE);
        hideInputMethod();
    }

    private void showPictureLay(boolean is) {
        if (is) {
            picture_lay.setVisibility(View.VISIBLE);
        } else {
            picture_lay.setVisibility(View.GONE);
        }
    }

    protected IMMessage createTextMessage(String text) {
        return MessageBuilder.createTextMessage(container.account, container.sessionType, text);
    }

    // 切换成音频，收起键盘，按钮切换成键盘
    private void switchToAudioLayout() {
        emoticonsEditText.setVisibility(View.GONE);
        AudioRecord.setVisibility(View.VISIBLE);
        hideInputMethod();
        hideEmojiLayout();
        showPictureLay(false);
        SwitchToAudio.setVisibility(View.GONE);
        SwitchToText.setVisibility(View.VISIBLE);
    }


    // 点击表情，切换到表情布局
    private void toggleEmojiLayout() {
        if (emotion_lay.getVisibility() == View.GONE) {
            showEmojiLayout();
        } else {
            hideEmojiLayout();
        }
        showPictureLay(false);
    }

    // 隐藏表情布局
    private void hideEmojiLayout() {
        uiHandler.removeCallbacks(showEmojiRunnable);
        if (emotion_lay != null) {
            emotion_lay.setVisibility(View.GONE);
        }
    }


    // 隐藏键盘布局
    private void hideInputMethod() {
        isKeyboardShowed = false;
        uiHandler.removeCallbacks(showTextRunnable);
        InputMethodManager imm = (InputMethodManager) container.activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(emoticonsEditText.getWindowToken(), 0);
        emoticonsEditText.clearFocus();
    }

    // 隐藏语音布局
    private void hideAudioLayout() {
        AudioRecord.setVisibility(View.GONE);
        emoticonsEditText.setVisibility(View.VISIBLE);
        SwitchToText.setVisibility(View.VISIBLE);
        SwitchToAudio.setVisibility(View.GONE);
    }

    // 显示表情布局
    private void showEmojiLayout() {
        hideInputMethod();
        hideAudioLayout();
        emoticonsEditText.requestFocus();
        uiHandler.postDelayed(showEmojiRunnable, 200);
        emotion_lay.setVisibility(View.VISIBLE);
    }


    // 显示键盘布局
    private void showInputMethod(EditText editTextMessage) {
        editTextMessage.requestFocus();
        //如果已经显示,则继续操作时不需要把光标定位到最后
        if (!isKeyboardShowed) {
            editTextMessage.setSelection(editTextMessage.getText().length());
            isKeyboardShowed = true;
        }

        InputMethodManager imm = (InputMethodManager) container.activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editTextMessage, 0);

        //输入框自获取焦点 并弹出输入键盘
        emoticonsEditText.setFocusable(true);
        emoticonsEditText.setFocusableInTouchMode(true);
        emoticonsEditText.requestFocus();
        KeyBoardUtils.openKeybord(emoticonsEditText, container.activity);

        container.proxy.onInputPanelExpand();
    }


    private Runnable showEmojiRunnable = new Runnable() {
        @Override
        public void run() {
            emotion_lay.setVisibility(View.VISIBLE);
        }
    };

    private Runnable showTextRunnable = new Runnable() {
        @Override
        public void run() {
            showInputMethod(emoticonsEditText);
        }
    };

    private void restoreText(boolean clearText) {
        if (clearText) {
            emoticonsEditText.setText("");
        }

        checkSendButtonEnable(emoticonsEditText);
    }

    /**
     * 显示发送或更多
     *
     * @param editText
     */
    private void checkSendButtonEnable(EditText editText) {
        String textMessage = editText.getText().toString();
        if (!TextUtils.isEmpty(StringUtil.removeBlanks(textMessage)) && editText.hasFocus()) {
            send.setBackgroundResource(R.drawable.circular_15_green);
            send.setEnabled(true);
        } else {
            send.setBackgroundResource(R.drawable.circular_15_g2);
            send.setEnabled(false);
        }
    }


    private Runnable hideAllInputLayoutRunnable;


    /**
     * 隐藏所有输入布局
     */
    private void hideAllInputLayout(boolean immediately) {
        if (hideAllInputLayoutRunnable == null) {
            hideAllInputLayoutRunnable = new Runnable() {

                @Override
                public void run() {
                    hideInputMethod();
                    hideEmojiLayout();
                    showPictureLay(false);
                }
            };
        }
        long delay = immediately ? 0 : ViewConfiguration.getDoubleTapTimeout();
        uiHandler.postDelayed(hideAllInputLayoutRunnable, delay);
    }

    /**
     * ****************************** 语音 ***********************************
     */
    private void initAudioRecordButton() {
        AudioRecord.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touched = true;
                    initAudioRecord();
                    onStartAudioRecord();
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL
                        || event.getAction() == MotionEvent.ACTION_UP) {
                    touched = false;
                    onEndAudioRecord(isCancelled(v, event));
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    touched = false;
                    cancelAudioRecord(isCancelled(v, event));
                }

                return false;
            }
        });
    }

    // 上滑取消录音判断
    private static boolean isCancelled(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        if (event.getRawX() < location[0] || event.getRawX() > location[0] + view.getWidth()
                || event.getRawY() < location[1] - 40) {
            return true;
        }

        return false;
    }

    /**
     * 初始化AudioRecord
     */
    private void initAudioRecord() {
        if (audioMessageHelper == null) {
            audioMessageHelper = new AudioRecorder(container.activity, RecordType.AAC, AudioRecorder.DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND, this);
        }
    }

    /**
     * 开始语音录制
     */
    private void onStartAudioRecord() {
        container.activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        started = audioMessageHelper.startRecord();
        cancelled = false;
        if (started == false) {
            Toast.makeText(container.activity, "初始化录音组件失败", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!touched) {
            return;
        }

        AudioRecord.setText("松开 结束");
        AudioRecord.setBackgroundResource(R.drawable.circular_15_g1);

        updateTimerTip(false); // 初始化语音动画状态
        playAudioRecordAnim();
    }

    /**
     * 结束语音录制
     *
     * @param cancel
     */
    private void onEndAudioRecord(boolean cancel) {
        container.activity.getWindow().setFlags(0, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        audioMessageHelper.completeRecord(cancel);
        AudioRecord.setText("按住 说话");
        AudioRecord.setBackgroundResource(R.drawable.circular_15_w0);
        stopAudioRecordAnim();
    }

    /**
     * 取消语音录制
     *
     * @param cancel
     */
    private void cancelAudioRecord(boolean cancel) {
        // reject
        if (!started) {
            return;
        }
        // no change
        if (cancelled == cancel) {
            return;
        }

        cancelled = cancel;
        updateTimerTip(cancel);
    }

    /**
     * 正在进行语音录制和取消语音录制，界面展示
     *
     * @param cancel
     */
    private void updateTimerTip(boolean cancel) {
        if (cancel) {
            timerTip.setText("松开手指,取消发送");
            timerTipContainer.setBackgroundResource(R.drawable.nim_cancel_record_red_bg);
        } else {
            timerTip.setText("手指上滑,取消发送");
            timerTipContainer.setBackgroundResource(0);
        }
    }

    /**
     * 开始语音录制动画
     */
    private void playAudioRecordAnim() {
        audioAnimLayout.setVisibility(View.VISIBLE);
        time.setBase(SystemClock.elapsedRealtime());
        time.start();
    }

    /**
     * 结束语音录制动画
     */
    private void stopAudioRecordAnim() {
        audioAnimLayout.setVisibility(View.GONE);
        time.stop();
        time.setBase(SystemClock.elapsedRealtime());
    }

    // 录音状态回调
    @Override
    public void onRecordReady() {

    }

    @Override
    public void onRecordStart(File audioFile, RecordType recordType) {

    }

    //      录音成功后发送语音消息
    @Override
    public void onRecordSuccess(File audioFile, long audioLength, RecordType recordType) {
        IMMessage audioMessage = MessageBuilder.createAudioMessage(container.account, container.sessionType, audioFile, audioLength);
        container.proxy.sendMessage(audioMessage);
    }

    @Override
    public void onRecordFail() {

    }

    @Override
    public void onRecordCancel() {

    }

    //      录音时间达到最大
    @Override
    public void onRecordReachedMaxTime(final int maxTime) {
        stopAudioRecordAnim();
        final DialogUtils.Dialog_Center dialog_center = new DialogUtils.Dialog_Center(container.activity);
        dialog_center.Button("发送", "取消");
        dialog_center.Message("录音达到最大时间,是否发送?");
        dialog_center.MbuttonOnClick(new DialogUtils.Dialog_Center.buttonOnClick() {
            @Override
            public void onButton1() {
                audioMessageHelper.handleEndRecord(true, maxTime);
                dialog_center.close();
            }

            @Override
            public void onButton2() {
                dialog_center.close();
            }
        });
        Dialog dialog = dialog_center.create();
        dialog.show();
    }

    public boolean isRecording() {
        return audioMessageHelper != null && audioMessageHelper.isRecording();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AlbumActivity.SELECT_PHOTO_FROM_ALBUM:
                setSelect_image_urls(data.getStringArrayListExtra(InputActivity.SELECT_IMAGE_URLS));
                sendImage();
                break;
            case inputSelectPhotoPagerActivity.REQUEST_CODE:
                setSelect_image_urls(data.getStringArrayListExtra(InputActivity.SELECT_IMAGE_URLS));
                if (data.getIntExtra(inputSelectPhotoPagerActivity.BACK_STATE, 1) == 0) {
                    sendImage();
                } else {
                    adapter.setSelect_image_urls(Select_image_urls);
                    adapter.notifyDataSetChanged();
                }
                break;
            case TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
//                    List<String> temp = new ArrayList<String>();
//                    temp.add("///storage/emulated/0/XSX/XSX_Camera/7ef5cd5d-3f26-422c-9c74-39df99fd5c1d.jpg");
                    final String path = came_photo_path.getPath();
//                    temp.add(path);
//                    setSelect_image_urls(temp);
                    Select_image_urls.add(path);
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(came_photo_path);
                    container.activity.sendBroadcast(intent);
                    sendImage();
//                    uiHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            sendImage();
//                            File fi=new File(path);
//                            if (fi.exists()){
//                                Log.d("image","exists");
//                            }else {
//                                Log.d("image","noexists");
//                            }
//                        }
//                    },1000);

                } else {
                    Toast.makeText(container.activity, "相片获取失败", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void sendImage() {
        if (Select_image_urls.size() == 0) {
            Toast.makeText(container.activity, "没有图片", Toast.LENGTH_SHORT).show();
            return;
        }
        SendImageHelper.sendImageAfterSelfImagePicker(getActivity(), Select_image_urls, new SendImageHelper.Callback() {
            @Override
            public void sendImage(File file, boolean isOrig) {
                IMMessage message = MessageBuilder.createImageMessage(container.account, container.sessionType, file, file.getName());
                container.proxy.sendMessage(message);

                Select_image_urls.clear();
                adapter.setSelect_image_urls(Select_image_urls);
                adapter.notifyDataSetChanged();
                showPictureLay(false);
            }
        });
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
                ContentResolver cr = container.activity.getContentResolver();
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

    private String pathFromResult(Intent data) {
        String outPath = came_photo_path.toString();
        if (data == null || data.getData() == null) {
            Log.d("image", "data null");
            return outPath;
        }

        Uri uri = data.getData();
        Cursor cursor = container.activity.getContentResolver()
                .query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
        if (cursor == null) {
            // miui 2.3 有可能为null
            return uri.getPath();
        } else {
            if (uri.toString().contains("content://com.android.providers.media.documents/document/image")) { // htc 某些手机
                // 获取图片地址
                String _id = null;
                String uridecode = uri.decode(uri.toString());
                int id_index = uridecode.lastIndexOf(":");
                _id = uridecode.substring(id_index + 1);
                Cursor mcursor = container.activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, " _id = " + _id,
                        null, null);
                mcursor.moveToFirst();
                int column_index = mcursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                outPath = mcursor.getString(column_index);
                if (!mcursor.isClosed()) {
                    mcursor.close();
                }
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                return outPath;

            } else {
                cursor.moveToFirst();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                outPath = cursor.getString(column_index);
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                return outPath;
            }
        }
    }
}
