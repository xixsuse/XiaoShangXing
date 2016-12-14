package com.xiaoshangxing.xiaoshang.Reward.RewardDetail;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.network.netUtil.OperateUtils;
import com.xiaoshangxing.network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.publicActivity.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.data.PublishCache;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.data.bean.Published;
import com.xiaoshangxing.utils.AppContracts;
import com.xiaoshangxing.utils.customView.EmotionEdittext.EmotinText;
import com.xiaoshangxing.publicActivity.inputActivity.InputActivity;
import com.xiaoshangxing.publicActivity.inputActivity.InputBoxLayout;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.utils.baseClass.BaseFragment;
import com.xiaoshangxing.utils.baseClass.IBaseView;
import com.xiaoshangxing.utils.customView.CirecleImage;
import com.xiaoshangxing.utils.customView.Name;
import com.xiaoshangxing.utils.customView.dialog.DialogLocationAndSize;
import com.xiaoshangxing.utils.customView.dialog.DialogUtils;
import com.xiaoshangxing.wo.WoFrafment.Published_Help;
import com.xiaoshangxing.xiaoshang.Help.HelpDetail.CommentListFrafment;
import com.xiaoshangxing.xiaoshang.Help.HelpDetail.DepthPageTransformer;
import com.xiaoshangxing.xiaoshang.Help.HelpDetail.FixedSpeedScroller;
import com.xiaoshangxing.xiaoshang.Help.HelpDetail.GetDataFromActivity;
import com.xiaoshangxing.xiaoshang.Help.HelpDetail.PraiseListFrafment;
import com.xiaoshangxing.xiaoshang.Help.HelpDetail.TransmitListFrafment;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

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
public class RewardDetailActivity extends BaseActivity implements RewardDetailContract.view, GetDataFromActivity {
    public static final String TAG = BaseFragment.TAG + "-RewardDetailActivity";
    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.head_image)
    CirecleImage headImage;
    @Bind(R.id.name)
    Name name;
    @Bind(R.id.college)
    TextView college;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.collect)
    CheckBox collect;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.text)
    EmotinText text;
    @Bind(R.id.button)
    ImageView button;
    @Bind(R.id.detail)
    LinearLayout detail;
    @Bind(R.id.transmit_text)
    TextView transmitText;
    @Bind(R.id.comment_text)
    TextView commentText;
    @Bind(R.id.praise_text)
    TextView praiseText;
    @Bind(R.id.cursor)
    View cursor;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.coordinator)
    CoordinatorLayout coordinator;
    @Bind(R.id.transmit)
    RelativeLayout transmit;
    @Bind(R.id.comment)
    RelativeLayout comment;
    @Bind(R.id.praiseOrCancel)
    TextView praiseOrCancel;
    @Bind(R.id.praise)
    RelativeLayout praise;

    private int currentItem = 2;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private Handler handler = new Handler();
    private RewardDetailContract.Presenter mPresenter;
    private int published_id;
    private Published published;
    private IBaseView iBaseView = this;
    private InputBoxLayout inputBoxLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reward_help_detail);
        ButterKnife.bind(this);
        setmPresenter(new RewardDetailPresenter(this, this));
        setEnableRightSlide(false);
        init();
        initInputBox();
        moveImediate(currentItem);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PublishCache.reload(String.valueOf(published_id), new SimpleCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onBackData(Object o) {
                published = (Published) o;
                refresh();
            }
        });
    }

    private void init() {
        if (!getIntent().hasExtra(IntentStatic.DATA)) {
            showToast("动态id错误");
            finish();
            return;
        }
        published_id = getIntent().getIntExtra(IntentStatic.DATA, -1);

        PublishCache.reloadWithLoading(String.valueOf(published_id), this, new SimpleCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Throwable e) {
                showToast("没有查询到该动态的消息");
                finish();
            }

            @Override
            public void onBackData(Object o) {
                published = (Published) o;
                refresh();
            }
        });
        viewpager.setPageTransformer(true, new DepthPageTransformer());
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                inputBoxLayout.showOrHideLayout(false);
            }

            @Override
            public void onPageSelected(int position) {
                moveToPosition(position + 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
            e.printStackTrace();
            Log.d("修改viewpager滑动速度", "失败");
        }
        title.setText("悬赏详情");
    }

    private void refresh() {
        fragments.clear();
        fragments.add(new TransmitListFrafment());
        fragments.add(new CommentListFrafment());
        fragments.add(new PraiseListFrafment());
        FragAdapter adapter = new FragAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(1);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveToPosition(2);
            }
        }, 300);
        String userId = String.valueOf(published.getUserId());
        UserInfoCache.getInstance().getHeadIntoImage(userId, headImage);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.USER_NAME, name);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.COLLEGE, college);
        headImage.setIntent_type(CirecleImage.PERSON_INFO, String.valueOf(published.getUserId()));
        time.setText(TimeUtil.getTimeShowString(published.getCreateTime(), false));
        text.setText(published.getText());
        price.setText(AppContracts.RMB + published.getPrice());
        praiseOrCancel.setText(Published_Help.isPraised(published) ? "取消" : "赞");
        setCount();

        collect.setChecked(published.isCollected());
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OperateUtils.operateWithLoad(published_id, RewardDetailActivity.this, true, NS.COLLECT, published.isCollected(),
                        RewardDetailActivity.this, new SimpleCallBack() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                collect.setChecked(published.isCollected());
                            }

                            @Override
                            public void onBackData(Object o) {
                                Published published1 = (Published) o;
                                collect.setChecked(published1.isCollected());
                                noticeDialog(collect.isChecked() ? "已收藏" : "已取消收藏");
                                published = published1;
                            }
                        });
            }
        });
    }

    private void initInputBox() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.edit_and_emot);
        inputBoxLayout = new InputBoxLayout(this, relativeLayout, this);
        inputBoxLayout.setEmotion_layVisible(View.GONE);
    }

    @Override
    public void gotoInput() {
//        Intent comment_input = new Intent(this, InputActivity.class);
//        comment_input.putExtra(InputActivity.EDIT_STATE, InputActivity.COMMENT);
//        comment_input.putExtra(InputActivity.MOMENTID, published.getId());
//        startActivity(comment_input);
        inputBoxLayout.showOrHideLayout(true);
        inputBoxLayout.setCallBack(new InputBoxLayout.CallBack() {
            @Override
            public void callback(String text) {
                sendComment(text, -1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveToPosition(currentItem);
            }
        }, 300);
        appBar.setExpanded(false);
    }

    private void sendComment(String text, int commenId) {
        OperateUtils.Comment(published.getId(), commenId, text, this, true, RewardDetailActivity.this,
                new SimpleCallBack() {
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
                        published = (Published) o;
                        refresh();
                    }
                });
    }

    @Override
    public void gotoSelectPeson() {
        Intent intent_selectperson = new Intent(RewardDetailActivity.this, SelectPersonActivity.class);
//        intent_selectperson.putExtra(SelectPersonActivity.LIMIT, 1);
        startActivityForResult(intent_selectperson, SelectPersonActivity.SELECT_PERSON_CODE);
    }

    @Override
    public void setPraise() {
        if (praiseOrCancel.getText().equals("赞")) {
            praiseOrCancel.setText(R.string.cancle);
        } else {
            praiseOrCancel.setText(R.string.praise);
        }
    }

    @Override
    public void setCount() {
        String transmit = published.getTransmitCount();
        int comment = published.getComments() == null ? 0 : published.getComments().size();
        int praise = TextUtils.isEmpty(published.getPraiseUserIds()) ? 0 : published.getPraiseUserIds().split(NS.SPLIT).length;
        transmitText.setText("转发" + transmit);
        commentText.setText("评论" + comment);
        praiseText.setText("赞" + praise);
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
        View share_school_circle = view.findViewById(R.id.share_school_circle);
        View share_friend = view.findViewById(R.id.share_friend);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        share_school_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OperateUtils.Share(RewardDetailActivity.this, InputActivity.SHOOL_REWARD, published_id);
                dialog.dismiss();
            }
        });
        share_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSelectPeson();
                dialog.dismiss();
            }
        });
        dialog.show();
        DialogLocationAndSize.bottom_FillWidth(this, dialog);
    }

    public void noticeDialog(String message) {
        DialogUtils.Dialog_No_Button dialog_no_button = new DialogUtils.Dialog_No_Button(this, message);
        final Dialog alertDialog = dialog_no_button.create();
        alertDialog.show();
        DialogLocationAndSize.setWidth(alertDialog, R.dimen.x420);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                alertDialog.dismiss();
            }
        }, 1000);
    }

    @Override
    public void setmPresenter(@Nullable RewardDetailContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    private void showTransmitDialog(final List<String> id) {
        final Dialog dialog = new Dialog(this, R.style.ActionSheetDialog);
        View dialogView = View.inflate(this, R.layout.school_help_transmit_dialog, null);
        dialog.setContentView(dialogView);

        TextView cancle = (TextView) dialogView.findViewById(R.id.cancel);
        TextView send = (TextView) dialogView.findViewById(R.id.send);
        CirecleImage head = (CirecleImage) dialogView.findViewById(R.id.head_image);
        TextView name = (TextView) dialogView.findViewById(R.id.name);
        TextView college = (TextView) dialogView.findViewById(R.id.college);
        TextView text = (TextView) dialogView.findViewById(R.id.text);
        final EditText input = (EditText) dialogView.findViewById(R.id.input);

        String userId = String.valueOf(published.getUserId());
        UserInfoCache.getInstance().getHeadIntoImage(userId, head);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.USER_NAME, name);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.COLLEGE, college);
        text.setText(published.getText());

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OperateUtils.Tranmit(published_id, NS.CATEGORY_REWARD, id, iBaseView, input.getText().toString(),
                        new SimpleCallBack() {
                            @Override
                            public void onSuccess() {
                                showTransmitSuccess();
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onBackData(Object o) {

                            }
                        });
                dialog.dismiss();
            }
        });
        dialog.show();
        DialogLocationAndSize.setWidth(dialog, R.dimen.x900);
    }

    @Override
    public void showTransmitSuccess() {
        DialogUtils.Dialog_No_Button dialog_no_button =
                new DialogUtils.Dialog_No_Button(RewardDetailActivity.this, "已分享");
        final Dialog notice_dialog = dialog_no_button.create();
        notice_dialog.show();
        DialogLocationAndSize.setWidth(notice_dialog, R.dimen.x420);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notice_dialog.dismiss();
            }
        }, 500);
    }

    @OnClick({R.id.back, R.id.more, R.id.collect, R.id.transmit_text, R.id.comment_text, R.id.praise_text, R.id.transmit, R.id.comment, R.id.praise})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.more:
                showShareDialog();
                break;
            case R.id.collect:
                mPresenter.collect();
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
                gotoSelectPeson();
                break;
            case R.id.comment:
                gotoInput();
                break;
            case R.id.praise:
                praise();
                break;
        }
    }

    private void praise() {
        OperateUtils.operateWithLoad(published_id, this, true, NS.PRAISE, Published_Help.isPraised(published),
                RewardDetailActivity.this, new SimpleCallBack() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onBackData(Object o) {
                        published = (Published) o;
                        refresh();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SelectPersonActivity.SELECT_PERSON_CODE) {
            if (data != null) {
                if (data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON).size() > 0) {
//                    List<String> list = data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON);
//                    showTransmitDialog(list.get(0));
                    List<String> list = data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON);
                    showTransmitDialog(list);
                } else {
                    Toast.makeText(RewardDetailActivity.this, "未选择联系人", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public Object getData() {
        return published;
    }

    @Override
    public InputBoxLayout getInputBox() {
        return inputBoxLayout;
    }

    @Override
    public void hideInputBox() {
        inputBoxLayout.showOrHideLayout(false);
        moveToPosition(currentItem);
    }

    @Override
    public void comment(final int id) {
        inputBoxLayout.showOrHideLayout(true);
        inputBoxLayout.setCallBack(new InputBoxLayout.CallBack() {
            @Override
            public void callback(String text) {
                sendComment(text, id);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveToPosition(2);
            }
        }, 300);
        appBar.setExpanded(false, false);
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
}
