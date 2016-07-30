package com.xiaoshangxing;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.input_activity.EmotAndPicture.EmotionGrideViewAdapter;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmoticonsEditText;
import com.xiaoshangxing.input_activity.InputBoxLayout;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.wo.WoFragment;
import com.xiaoshangxing.xiaoshang.XiaoShangFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/6/21
 */
public class MainActivity extends BaseActivity {
    @Bind(R.id.image_xiaoshang)
    ImageView imageXiaoshang;
    @Bind(R.id.xiaoshang)
    TextView xiaoshang;
    @Bind(R.id.xiaoshang_lay)
    RelativeLayout xiaoshangLay;
    @Bind(R.id.image_yujian)
    ImageView imageYujian;
    @Bind(R.id.yujian)
    TextView yujian;
    @Bind(R.id.yujian_lay)
    RelativeLayout yujianLay;
    @Bind(R.id.image_wo)
    ImageView imageWo;
    @Bind(R.id.wo)
    TextView wo;
    @Bind(R.id.wolay)
    RelativeLayout wolay;
    @Bind(R.id.radio_layout)
    LinearLayout radioLayout;

    private int current;

    private WoFragment woFragment;
    private XiaoShangFragment xiaoShangFragment;

    private GridView gridView;
    private List<View> viewlist = new ArrayList<View>();
    private ViewPager viewPager;
    private LinearLayout emotion_lay;
    private EmotionGrideViewAdapter adapter;

    private RelativeLayout comment_input_layout;
    private EmoticonsEditText emoticonsEditText;
    private TextView send;
    private ImageView emot;
    private View normal_emot, favorite,delete_emot;
    private RelativeLayout edit_and_emot;
    private int screenHeight;


    private InputBoxLayout inputBoxLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);
//        initWoInput();
        initInputBox();
        initAllFragments();
    }

    private void initInputBox(){
        RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.edit_and_emot);
        inputBoxLayout=new InputBoxLayout(this,relativeLayout,this);
    }

//    private void initWoInput() {
//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        emotion_lay = (LinearLayout) findViewById(R.id.emot_lay);
//
//        comment_input_layout = (RelativeLayout) findViewById(R.id.comment_input_layout);
//        emoticonsEditText = (EmoticonsEditText) findViewById(R.id.comment_input);
//        send = (TextView) findViewById(R.id.send);
//        emot = (ImageView) findViewById(R.id.emotion);
//        edit_and_emot = (RelativeLayout) findViewById(R.id.edit_and_emot);
//        normal_emot = findViewById(R.id.normal_emot);
//        favorite = findViewById(R.id.favorite);
//        delete_emot=findViewById(R.id.delete_emot);
//        //监听键盘高度  让输入框保持在键盘上面
//        screenHeight = ScreenUtils.getScreenHeight(this);
//        KeyBoardUtils.observeSoftKeyboard(this, new KeyBoardUtils.OnSoftKeyboardChangeListener() {
//            @Override
//            public void onSoftKeyBoardChange(int softKeybardHeight, boolean visible) {
//
//                if (softKeybardHeight > 100) {
//                    comment_input_layout.layout(0, screenHeight - softKeybardHeight - comment_input_layout.getHeight(),
//                            comment_input_layout.getWidth(),
//                            screenHeight - softKeybardHeight);
//                    Log.d("keyboard", "" + softKeybardHeight);
//                    Log.d("height", "" + screenHeight);
//                }
//            }
//        });
//
//        //监听输入框内容
//        emoticonsEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (emoticonsEditText.getText().length() > 0) {
//                    send.setBackground(getResources().getDrawable(R.drawable.btn_circular_green1));
//                    send.setEnabled(true);
//                } else {
//                    send.setBackground(getResources().getDrawable(R.drawable.btn_circular_g1));
//                    send.setEnabled(false);
//                }
//            }
//        });
//
//        gridView = (GridView) View.inflate(this, R.layout.gridelayout, null);
//        adapter = new EmotionGrideViewAdapter(this);
//        adapter.setMcallBack(new EmotionGrideViewAdapter.callBack() {
//            @Override
//            public void callback(String emot) {
//                emoticonsEditText.append(emot);
//            }
//        });
//        gridView.setAdapter(adapter);
//        TextView textView = new TextView(this);
//        textView.setText("55555");
//        viewlist.add(gridView);
//        viewlist.add(textView);
//
//        PagerAdapter pagerAdapter = new PagerAdapter() {
//
//            @Override
//            public boolean isViewFromObject(View arg0, Object arg1) {
//                // TODO Auto-generated method stub
//                return arg0 == arg1;
//            }
//
//            @Override
//            public int getCount() {
//                // TODO Auto-generated method stub
//                return 2;
//            }
//
//            @Override
//            public void destroyItem(ViewGroup container, int position,
//                                    Object object) {
//                // TODO Auto-generated method stub
//                container.removeView(viewlist.get(position));
//            }
//
//            @Override
//            public Object instantiateItem(ViewGroup container, int position) {
//                // TODO Auto-generated method stub
//                container.addView(viewlist.get(position));
//
//
//                return viewlist.get(position);
//            }
//        };
//
//        viewPager.setAdapter(pagerAdapter);
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                selectItem(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//        viewPager.setCurrentItem(0);
//        selectItem(0);
//
//    }
//
//    private void selectItem(int position) {
//        switch (position) {
//            case 0:
//                normal_emot.setBackgroundColor(getResources().getColor(R.color.w2));
//                favorite.setBackgroundColor(getResources().getColor(R.color.w1));
//                break;
//            case 1:
//                normal_emot.setBackgroundColor(getResources().getColor(R.color.w1));
//                favorite.setBackgroundColor(getResources().getColor(R.color.w2));
//                break;
//        }
//    }
//
//    public void showOrHideLayout(boolean is) {
////        if (is) {
////            edit_and_emot.setVisibility(View.VISIBLE);
////            comment_input_layout.setVisibility(View.VISIBLE);
////            //输入框自获取焦点 并弹出输入键盘
////            emoticonsEditText.setFocusable(true);
////            emoticonsEditText.setFocusableInTouchMode(true);
////            emoticonsEditText.requestFocus();
////            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
////            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
////        } else {
////            Log.d("hideedit", "ok");
////            edit_and_emot.setVisibility(View.GONE);
////            comment_input_layout.setVisibility(View.INVISIBLE);
////            KeyBoardUtils.closeKeybord(emoticonsEditText, this);
////        }
//        inputBoxLayout.showOrHideLayout(is);
//    }
//
//    public int getEdittext_height() {
////        int xy[] = new int[2];
////        comment_input_layout.getLocationOnScreen(xy);
////        return xy[1];
//       return inputBoxLayout.getEdittext_height();
//    }
//
    private void initAllFragments() {
        Fragment frag;
        frag = mFragmentManager.findFragmentByTag(WoFragment.TAG);
        woFragment = (frag == null) ? WoFragment.newInstance() : (WoFragment) frag;

        frag = mFragmentManager.findFragmentByTag(XiaoShangFragment.TAG);
        xiaoShangFragment = (frag == null) ? XiaoShangFragment.newInstance() : (XiaoShangFragment) frag;

        if (!xiaoShangFragment.isAdded()){
            mFragmentManager.beginTransaction().add(R.id.main_fragment,xiaoShangFragment,XiaoShangFragment.TAG)
                    .commit();
        }
        if (!woFragment.isAdded()){
            mFragmentManager.beginTransaction().add(R.id.main_fragment,woFragment,WoFragment.TAG)
                    .commit();
        }


//        mFragmentManager.beginTransaction().show(xiaoShangFragment).hide(woFragment).commit();

        setXiaoshang(true);
    }

    @OnClick({R.id.xiaoshang_lay, R.id.yujian_lay, R.id.wolay, R.id.emotion, R.id.normal_emot, R.id.favorite
            , R.id.send,R.id.delete_emot})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.xiaoshang_lay:
                if (current == 1) {
                    return;
                } else {
                    setXiaoshang(true);
                    setYUjian(false);
                    setWo(false);
                }

                break;
            case R.id.yujian_lay:
                if (current == 2) {
                    return;
                } else {
                    setXiaoshang(false);
                    setYUjian(true);
                    setWo(false);
                }
                break;
            case R.id.wolay:
                if (current == 3) {
                    return;
                } else {
                    setXiaoshang(false);
                    setYUjian(false);
                    setWo(true);
                }
                break;
            case R.id.emotion:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.normal_emot:
                viewPager.setCurrentItem(0);
                break;
            case R.id.favorite:
                viewPager.setCurrentItem(1);
                break;
            case R.id.send:
//                showOrHideLayout(false);
                inputBoxLayout.showOrHideLayout(false);
                emoticonsEditText.setText("");
                break;
            case R.id.delete_emot:
                emoticonsEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                break;
        }
    }

    private void setWo(boolean is) {
        if (is) {
            imageWo.setImageResource(R.mipmap.wo_on);
            wo.setTextColor(getResources().getColor(R.color.green1));

//            mFragmentManager.beginTransaction().replace(R.id.main_fragment,
//                    woFragment, XiaoShangFragment.TAG).commit();
            mFragmentManager.beginTransaction().hide(xiaoShangFragment).show(woFragment)
                    .commit();
            current = 3;
        } else {
            imageWo.setImageResource(R.mipmap.wo_off);
            wo.setTextColor(getResources().getColor(R.color.g0));
        }

    }

    private void setYUjian(boolean is) {
        if (is) {
            imageYujian.setImageResource(R.mipmap.yujian_on);
            yujian.setTextColor(getResources().getColor(R.color.green1));
            current = 2;
        } else {
            imageYujian.setImageResource(R.mipmap.yujian_off);
            yujian.setTextColor(getResources().getColor(R.color.g0));
        }

    }

    private void setXiaoshang(boolean is) {
        if (is) {
            imageXiaoshang.setImageResource(R.mipmap.xiaoshang_on);
            xiaoshang.setTextColor(getResources().getColor(R.color.green1));

//            mFragmentManager.beginTransaction().replace(R.id.main_fragment,
//                    xiaoShangFragment, XiaoShangFragment.TAG).commit();
            mFragmentManager.beginTransaction().hide(woFragment).show(xiaoShangFragment)
                    .commit();
            current = 1;
        } else {
            imageXiaoshang.setImageResource(R.mipmap.xiaoshang_off);
            xiaoshang.setTextColor(getResources().getColor(R.color.g0));
        }
    }

    public InputBoxLayout getInputBoxLayout() {
        return inputBoxLayout;
    }
}
