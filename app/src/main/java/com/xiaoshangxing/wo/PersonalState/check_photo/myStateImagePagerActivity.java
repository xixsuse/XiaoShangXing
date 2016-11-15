package com.xiaoshangxing.wo.PersonalState.check_photo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.netUtil.OperateUtils;
import com.xiaoshangxing.Network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.FileUtils;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.LoadingDialog;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.image.SaveImageTask;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.wo.PersonalState.PersonalStateActivity;
import com.xiaoshangxing.wo.StateDetailsActivity.DetailsActivity;
import com.xiaoshangxing.wo.WoFrafment.check_photo.HackyViewPager;
import com.xiaoshangxing.wo.WoFrafment.check_photo.ImageDetailFragment;
import com.xiaoshangxing.yujian.ChatActivity.SendImageHelper;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * 图片查看器
 */
public class myStateImagePagerActivity extends FragmentActivity implements View.OnClickListener, myStateImagePagerContract.View {
    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    @Bind(R.id.praise_people_count)
    TextView praisePeopleCount;
    @Bind(R.id.comment_count)
    TextView commentCount;
    @Bind(R.id.comment_image)
    ImageView commentImage;

    private HackyViewPager mPager;
    private int pagerPosition;
    private TextView indicator;

    private View back, more, detail, backgroud;
    private TextView time;
    private EmotinText text;

    private ArrayList<String> urls = new ArrayList<>();
    private myStateImagePagerContract.Presenter mPresenter;
    private int published_id;
    private Published published;
    private Realm realm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_mystate);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        setmPresenter(new myStateImagePresenter(this, this));
        initView();

        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
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

        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        more = findViewById(R.id.more);
        more.setOnClickListener(this);
        detail = findViewById(R.id.details_entry_layout);
        detail.setOnClickListener(this);
        time = (TextView) findViewById(R.id.time);
        text = (EmotinText) findViewById(R.id.text);

        backgroud = findViewById(R.id.background);

        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        String[] urls2 = published.getImage().split(NS.SPLIT);

        for (String i : urls2) {
            urls.add(i);
        }

        mPager = (HackyViewPager) findViewById(R.id.pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
        mPager.setAdapter(mAdapter);
        indicator = (TextView) findViewById(R.id.indicator);

        CharSequence text1 = getString(R.string.viewpager_indicator, 1, mPager.getAdapter().getCount());
        indicator.setText(text1);

        backgroud.setMinimumHeight(text.getHeight());

        // 更新下标
        mPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = getString(R.string.viewpager_indicator, arg0 + 1, mPager.getAdapter().getCount());
                indicator.setText(text);
                pagerPosition = arg0;
            }

        });

        mPager.setCurrentItem(pagerPosition);

        setTime();
        setText();
        setPraiseandComment();
    }

    @Override
    public void setTime() {
        time.setText(TimeUtil.getTimeShowString(published.getCreateTime(), false));
    }

    @Override
    public void showMoreDialog() {
        DialogUtils.DialogMenu2 menu = new DialogUtils.DialogMenu2(myStateImagePagerActivity.this);
        menu.addMenuItem("发送给好友");
        menu.addMenuItem("保存到本地");
        menu.addMenuItem("删除");
        menu.initView();
        menu.show();
        LocationUtil.bottom_FillWidth(this, menu);

        menu.setMenuListener(new DialogUtils.DialogMenu2.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                switch (position) {
                    case 0:
                        mPresenter.sendToFriend();
                        Intent selectPerson = new Intent(myStateImagePagerActivity.this, SelectPersonActivity.class);
                        startActivityForResult(selectPerson, SelectPersonActivity.SELECT_PERSON_CODE);
                        break;
                    case 1:
                        mPresenter.saveImage(urls.get(pagerPosition));
                        break;
                    case 2:
                        showMakesureDialog();
                        break;
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void gotoDetails() {
        Intent intent = new Intent(myStateImagePagerActivity.this, DetailsActivity.class);
        intent.putExtra(IntentStatic.DATA, published_id);
        startActivity(intent);
    }

    @Override
    public void setText() {
        text.setText(TextUtils.isEmpty(published.getText()) ? "" : published.getText());
    }

    @Override
    public void setPraiseandComment() {
        int praise = published.getPraiseCount();
        int comment = published.getComments().size();
        if (praise == 0) {
            praisePeopleCount.setVisibility(View.GONE);
        } else {
            praisePeopleCount.setText(String.valueOf(praise));
        }
        if (comment == 0) {
            commentCount.setVisibility(View.GONE);
            commentImage.setPadding(0, 0, ScreenUtils.getAdapterPx(R.dimen.x32, this), 0);
        } else {
            commentCount.setText(String.valueOf(comment));
            commentImage.setPadding(0, 0, ScreenUtils.getAdapterPx(R.dimen.x12, this), 0);
        }
    }

    @Override
    public void showMakesureDialog() {
        final DialogUtils.Dialog_Center dialog_center = new DialogUtils.Dialog_Center(this);
        if (urls.size() > 1) {
            dialog_center.Message("与这张照片同时发布的一组照片都会被删除。");
            dialog_center.Button("全部删除", "取消");
        } else {
            dialog_center.Message("确认删除吗?");
            dialog_center.Button("删除", "取消");
        }
        dialog_center.MbuttonOnClick(new DialogUtils.Dialog_Center.buttonOnClick() {
            @Override
            public void onButton1() {
                OperateUtils.deleteOnePublished(published_id, myStateImagePagerActivity.this, myStateImagePagerActivity.this, new SimpleCallBack() {
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
                dialog_center.close();
            }

            @Override
            public void onButton2() {
                dialog_center.close();
            }
        });
        Dialog dialog = dialog_center.create();
        dialog.show();
        LocationUtil.setWidth(this, dialog,
                getResources().getDimensionPixelSize(R.dimen.x780));
    }

    public void finishPager() {
        Intent intent = new Intent(this, PersonalStateActivity.class);
        intent.putExtra(IntentStatic.EXTRA_ACCOUNT, String.valueOf(TempUser.id));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void sendImageToFriend(final List<String> ids) {
        if (ids == null || ids.size() == 0) {
            Log.d("sendToFriend", "size:0");
            return;
        }

        SaveImageTask s = new SaveImageTask(this, FileUtils.getTempImage(), null, null);
        s.setSimpleCallBack(new SimpleCallBack() {
            @Override
            public void onSuccess() {
                final ArrayList<String> arrayList = new ArrayList<>();
                List<String> list = new ArrayList<>();
                list.add(FileUtils.TEMP_IMAGE);
                for (final String id : ids) {
                    SendImageHelper.sendImageAfterSelfImagePicker(myStateImagePagerActivity.this, false, list, new SendImageHelper.Callback() {
                        @Override
                        public void sendImage(File file, boolean isOrig) {
                            IMMessage message = MessageBuilder.createImageMessage(id, SessionTypeEnum.P2P, file, file.getName());
                            NIMClient.getService(MsgService.class).sendMessage(message, false).setCallback(new RequestCallback<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    arrayList.add("1");
                                    if (arrayList.size() == ids.size()) {
                                        showToast("转发成功");
                                    }
                                }

                                @Override
                                public void onFailed(int i) {
                                    showToast("转发失败:" + i);
                                }

                                @Override
                                public void onException(Throwable throwable) {
                                    showToast("转发异常");
                                    throwable.printStackTrace();
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onBackData(Object o) {

            }
        });
        s.execute(urls.get(pagerPosition));
    }

    @Override
    public void setmPresenter(@Nullable myStateImagePagerContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more:
                showMoreDialog();
                break;
            case R.id.details_entry_layout:
                gotoDetails();
                break;
            case R.id.back:
                finish();
                break;
        }

    }

    @Override
    public void showLoadingDialog(String text) {

    }

    @Override
    public void hideLoadingDialog() {

    }

    @Override
    public void setonDismiss(LoadingDialog.onDismiss on) {

    }

    @Override
    public void showToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SelectPersonActivity.SELECT_PERSON_CODE) {
            if (data != null) {
                if (data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON).size() > 0) {
                    List<String> list = data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON);
                    sendImageToFriend(list);
                } else {
                    Toast.makeText(myStateImagePagerActivity.this, "未选择联系人", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ArrayList<String> fileList;

        public ImagePagerAdapter(FragmentManager fm, ArrayList<String> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList.get(position);
            return ImageDetailFragment.newInstance(url);
        }

    }
}
