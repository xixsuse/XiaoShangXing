package com.xiaoshangxing.xiaoshang.Sale.SaleDetail;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.netUtil.OperateUtils;
import com.xiaoshangxing.Network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.PublishCache;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.input_activity.InputActivity;
import com.xiaoshangxing.input_activity.InputBoxLayout;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.layout.Name;
import com.xiaoshangxing.wo.WoFrafment.NoScrollGridView;
import com.xiaoshangxing.wo.WoFrafment.check_photo.ImagePagerActivity;
import com.xiaoshangxing.yujian.ChatActivity.ChatActivity;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/9/28
 */
public class SaleDetailsActivity extends BaseActivity implements IBaseView {
    public static final String TAG = BaseFragment.TAG + "-SaleDetailsActivity";
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
    @Bind(R.id.text)
    EmotinText text;
    @Bind(R.id.tv_dormitory)
    TextView tvDormitory;
    @Bind(R.id.dorm)
    LinearLayout dorm;
    @Bind(R.id.complete)
    ImageView complete;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.pictures)
    NoScrollGridView pictures;
    @Bind(R.id.scrollView)
    ScrollView scrollView;

    private int published_id;
    private Published published;
    private InputBoxLayout inputBoxLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        title.setText("闲置详情");
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
                initInputBox();
                refreshPager();
            }
        });
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                inputBoxLayout.remainEdittext();
                return false;
            }
        });
    }

    private void refreshPager() {
        String userId = String.valueOf(published.getUserId());
        UserInfoCache.getInstance().getHeadIntoImage(userId, headImage);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.USER_NAME, name);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.COLLEGE, college);
        time.setText(TimeUtil.getTimeShowString(published.getCreateTime(), false));
        text.setText(published.getText());
        price.setText(NS.RMB + published.getPrice());
        tvDormitory.setText(TextUtils.isEmpty(published.getDorm()) ? "未选" : published.getDorm());
        collect.setChecked(published.isCollected());
        complete.setVisibility(published.isAlive() ? View.GONE : View.VISIBLE);
        headImage.setIntent_type(CirecleImage.PERSON_INFO, userId);

        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OperateUtils.operateWithLoad(published_id, SaleDetailsActivity.this, true, NS.COLLECT, published.isCollected(),
                        SaleDetailsActivity.this, new SimpleCallBack() {
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

        if (TextUtils.isEmpty(published.getImage())) {
            return;
        }
        final ArrayList<String> imageUrls = new ArrayList<>();
        for (String i : published.getImage().split(NS.SPLIT)) {
            imageUrls.add(i);
        }
        pictures.setAdapter(new SaleDetailGridAdapter(this, imageUrls));
        pictures.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SaleDetailsActivity.this, ImagePagerActivity.class);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageUrls);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                startActivity(intent);
            }
        });
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
                OperateUtils.Share(SaleDetailsActivity.this, InputActivity.XIANZHI, published_id);
                dialog.dismiss();
            }
        });
        dialog.show();
        LocationUtil.bottom_FillWidth(this, dialog);
    }

    public void noticeDialog(String message) {
        DialogUtils.Dialog_No_Button dialog_no_button = new DialogUtils.Dialog_No_Button(this, message);
        final Dialog alertDialog = dialog_no_button.create();
        alertDialog.show();
        LocationUtil.setWidth(this, alertDialog,
                getResources().getDimensionPixelSize(R.dimen.x420));

        new Handler().postDelayed(new Runnable() {
            public void run() {
                alertDialog.dismiss();
            }
        }, 1000);
    }

    private void initInputBox() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.edit_and_emot);
        inputBoxLayout = new InputBoxLayout(this, relativeLayout, this);
        inputBoxLayout.setRemainEdittext(true);
        inputBoxLayout.setRootVisible(View.VISIBLE);
        inputBoxLayout.setEmotion_layVisible(View.GONE);
        inputBoxLayout.setSend("私聊");
        inputBoxLayout.setCallBack(new InputBoxLayout.CallBack() {
            @Override
            public void callback(String text) {
                ArrayList<String> arrayList = new ArrayList<String>();
                arrayList.add(String.valueOf(published.getUserId()));
                OperateUtils.Tranmit(published.getId(), NS.CATEGORY_SALE, arrayList, SaleDetailsActivity.this,
                        text, new SimpleCallBack() {
                            @Override
                            public void onSuccess() {
                                ChatActivity.start(SaleDetailsActivity.this, String.valueOf(published.getUserId()), null, SessionTypeEnum.P2P);
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onBackData(Object o) {

                            }
                        });
            }
        });
    }

    @OnClick({R.id.back, R.id.more, R.id.dorm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.more:
                showShareDialog();
                break;
            case R.id.dorm:
                break;
        }
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }
}
