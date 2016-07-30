package com.xiaoshangxing.xiaoshang.ShoolReward.RewardDetail;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.normalUtils.KeyBoardUtils;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.xiaoshang.ShoolfellowHelp.HelpDetail.CommentListFrafment;
import com.xiaoshangxing.xiaoshang.ShoolfellowHelp.HelpDetail.DepthPageTransformer;
import com.xiaoshangxing.xiaoshang.ShoolfellowHelp.HelpDetail.FixedSpeedScroller;
import com.xiaoshangxing.xiaoshang.ShoolfellowHelp.HelpDetail.PraiseListFrafment;
import com.xiaoshangxing.xiaoshang.ShoolfellowHelp.HelpDetail.TransmitListFrafment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/7/20
 */
public class RewardDetailActivity extends BaseActivity implements RewardContract.view {
    public static final String TAG = BaseFragment.TAG + "-HelpDetailActivity";
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.head_image)
    CirecleImage headImage;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.college)
    TextView college;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.button)
    ImageView button;
    @Bind(R.id.transmit_text)
    TextView transmitText;
    @Bind(R.id.comment_text)
    TextView commentText;
    @Bind(R.id.praise_text)
    TextView praiseText;
    @Bind(R.id.cursor)
    View cursor;
    @Bind(R.id.scrollview)
    ViewPager viewpager;
    @Bind(R.id.coordinator)
    CoordinatorLayout coordinator;
    @Bind(R.id.praiseOrCancel)
    TextView praiseOrCancel;
    @Bind(R.id.comment_input)
    EditText commentInput;
    @Bind(R.id.emotion)
    ImageView emotion;
    @Bind(R.id.send)
    TextView send;
    @Bind(R.id.comment_input_layout)
    RelativeLayout commentInputLayout;
    @Bind(R.id.detail)
    LinearLayout detail;
    @Bind(R.id.transmit)
    RelativeLayout transmit;
    @Bind(R.id.comment)
    RelativeLayout comment;
    @Bind(R.id.praise)
    RelativeLayout praise;

    private int currentItem = 2;
    private List<Fragment> fragments = new ArrayList<Fragment>();

    private int screenHeight;
    private boolean isInputBox;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_detail);
        ButterKnife.bind(this);
        init();
        moveImediate(currentItem);
    }

    private void init() {
        fragments.add(new TransmitListFrafment());
        CommentListFrafment commentListFrafment = new CommentListFrafment();
        commentListFrafment.setCollect(true);
        fragments.add(commentListFrafment);
        fragments.add(new PraiseListFrafment());
        FragAdapter adapter = new FragAdapter(getSupportFragmentManager(), fragments);

        viewpager.setAdapter(adapter);
        viewpager.setPageTransformer(true, new DepthPageTransformer());
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                showInputBox(false);
            }

            @Override
            public void onPageSelected(int position) {
                moveToPosition(position + 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setCurrentItem(1);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveToPosition(2);
            }
        }, 300);


        /*
        **describe:通过反射修改viewpager的滑动速度
        */
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewpager.getContext(),
                    new LinearInterpolator());
            field.set(viewpager, scroller);
            scroller.setmDuration(300);
        } catch (Exception e) {

        }

        //监听键盘高度  让输入框保持在键盘上面
        screenHeight = ScreenUtils.getScreenHeight(this);
        KeyBoardUtils.observeSoftKeyboard(this, new KeyBoardUtils.OnSoftKeyboardChangeListener() {
            @Override
            public void onSoftKeyBoardChange(int softKeybardHeight, boolean visible) {

                if (softKeybardHeight > 100) {
                    commentInputLayout.layout(0, screenHeight - softKeybardHeight - commentInputLayout.getHeight(),
                            commentInputLayout.getWidth(),
                            screenHeight - softKeybardHeight);
                    Log.d("keyboard", "" + softKeybardHeight);
                    Log.d("height", "" + screenHeight);
                }
//                moveImediate(currentItem);
            }
        });

        //监听输入框内容
        commentInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (commentInput.getText().length() > 0) {
                    send.setBackground(getResources().getDrawable(R.drawable.btn_circular_green1));
                    send.setEnabled(true);
                } else {
                    send.setBackground(getResources().getDrawable(R.drawable.btn_circular_g1));
                    send.setEnabled(false);
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputBox(false);
            }
        });
    }

    @OnClick({R.id.back, R.id.more, R.id.name, R.id.button,
            R.id.comment_input, R.id.emotion, R.id.send, R.id.transmit_text,
            R.id.comment_text, R.id.praise_text, R.id.transmit, R.id.comment,
            R.id.praise, R.id.detail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.more:
                showShareDialog();
                break;
            case R.id.name:
                break;
            case R.id.button:
                break;
            case R.id.transmit_text:
                viewpager.setCurrentItem(0);
                break;
            case R.id.comment_text:
                viewpager.setCurrentItem(1);
                break;
            case R.id.praise_text:
                viewpager.setCurrentItem(2);
                break;
            case R.id.transmit:
                break;
            case R.id.comment:
                showInputBox(true);
                break;
            case R.id.praise:
                PraiseOrCancel();
                break;
            case R.id.comment_input:
                break;
            case R.id.emotion:
                break;
            case R.id.send:
                break;
            case R.id.detail:
                showInputBox(false);
                break;
        }
    }


    public class FragAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;

        public FragAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            // TODO Auto-generated constructor stub
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            return mFragments.get(arg0);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mFragments.size();
        }

    }

    //    控制滑块滑动到指定位置
    public void moveToPosition(int position) {
        int[] xy = new int[2];
        switch (position) {
            case 1:
                transmitText.getLocationOnScreen(xy);
                break;
            case 2:
                commentText.getLocationOnScreen(xy);
                break;
            case 3:
                praiseText.getLocationOnScreen(xy);
                break;
        }

        ValueAnimator animator = ValueAnimator.ofInt(cursor.getLeft(), xy[0]);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                cursor.layout((int) animation.getAnimatedValue(), cursor.getTop(),
                        cursor.getWidth() + (int) animation.getAnimatedValue(), cursor.getBottom());
            }
        });
        animator.setDuration(300);
        animator.start();
        Log.d("x", "" + xy[0]);
    }

    public void moveImediate(int position) {
        int[] xy = new int[2];
        switch (position) {
            case 1:
                transmitText.getLocationOnScreen(xy);
                break;
            case 2:
                commentText.getLocationOnScreen(xy);
                break;
            case 3:
                praiseText.getLocationOnScreen(xy);
                break;
        }
        cursor.layout(xy[0], cursor.getTop(),
                cursor.getWidth() + xy[0], cursor.getBottom());
    }

    public void showShareDialog() {
        final Dialog dialog = new Dialog(this, R.style.ActionSheetDialog);
        View view = View.inflate(this, R.layout.util_help_share_dialog, null);
        dialog.setContentView(view);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        Button button = (Button) view.findViewById(R.id.cancel);
        View xsx = view.findViewById(R.id.xsx);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        xsx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RewardDetailActivity.this, "分享到xsx", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
        LocationUtil.bottom_FillWidth(this, dialog);
    }

    private void PraiseOrCancel() {
        if (praiseOrCancel.getText().equals("赞")) {
            praiseOrCancel.setText("取消");
        } else {
            praiseOrCancel.setText("赞");
        }
    }

    public void showInputBox(boolean is) {
        if (is) {
            commentInputLayout.setVisibility(View.VISIBLE);
            commentInputLayout.setFocusable(true);
            //输入框自获取焦点 并弹出输入键盘
            commentInput.setFocusable(true);
            commentInput.setFocusableInTouchMode(true);
            commentInput.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            isInputBox = true;
        } else {
            commentInputLayout.setVisibility(View.GONE);
            KeyBoardUtils.closeKeybord(commentInput, this);
            isInputBox = false;
            commentInput.setText("");
        }
    }

    public void showOrHideInputBox() {
        if (isInputBox) {
            showInputBox(false);
        } else {
            showInputBox(true);
        }
    }

    @Override
    public void setmPresenter(@Nullable RewardContract.Presenter presenter) {

    }
}
