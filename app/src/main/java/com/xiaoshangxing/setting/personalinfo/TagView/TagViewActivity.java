package com.xiaoshangxing.setting.personalinfo.TagView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.uinfo.UserServiceObserve;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xiaoshangxing.Network.InfoNetwork;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.setting.utils.TagView.Tag;
import com.xiaoshangxing.setting.utils.TagView.TagListView;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

/**
 * Created by tianyang on 2016/8/20.
 */
public class TagViewActivity extends BaseActivity implements View.OnClickListener,
        TagListView.OnTagClickListener, TagListView.OnTagLongClickListener, IBaseView {
    @Bind(R.id.tagView_restView)
    View tagViewRestView;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_text)
    TextView save;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.tagView_text)
    TextView tagViewText;
    @Bind(R.id.tagView_editText)
    EditText tagViewEditText;
    @Bind(R.id.tagview_linear)
    LinearLayout tagviewLinear;
    @Bind(R.id.tag_title)
    TextView tagTitle;
    @Bind(R.id.tagview)
    TagListView mTagListView;
    private View restView;
    private final List<Tag> mTags = new ArrayList<Tag>();
    private String tagString;
    private EditText editText;
    private boolean flag = false;
    private boolean isbacked = false;
    private NimUserInfo user;
    private Observer<List<NimUserInfo>> observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_setting_personalinfo_tagview);
        ButterKnife.bind(this);
        title.setText("标签");
        save.setTextColor(getResources().getColor(R.color.green1));
        save.setAlpha(0.5f);
        save.setText("保存");

        restView = findViewById(R.id.tagView_restView);
        editText = (EditText) findViewById(R.id.tagView_editText);

        user = NimUserInfoCache.getInstance().getUserInfo(TempUser.getId());
        refreshList();

        back.setOnClickListener(this);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    save();
                    return true;
                }
                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    save.setAlpha(1);
                    save.setClickable(true);
                } else {
                    save.setAlpha((float) 0.5);
                    save.setClickable(false);
                }
            }
        });
        save.setOnClickListener(this);
        restView.setOnClickListener(this);
        mTagListView.setOnTagClickListener(this);
        mTagListView.setOnTagLongClickListener(this);

        oberverUserInfo(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        oberverUserInfo(false);
    }

    private void refreshList() {
        if (user != null) {
            tagString = (String) user.getExtensionMap().get(NS.LABEL);
            if (!TextUtils.isEmpty(tagString)) {
                mTags.clear();
                String tags[] = tagString.split(NS.SPLIT);
                for (String i : tags) {
                    Tag tag = new Tag();
                    tag.setTitle(i);
                    mTags.add(tag);
                }
                mTagListView.setTags(mTags);
            }
        }
    }

    private void oberverUserInfo(boolean is) {
        if (observer == null) {
            observer = new Observer<List<NimUserInfo>>() {
                @Override
                public void onEvent(List<NimUserInfo> nimUserInfos) {
                    for (NimUserInfo userInfo : nimUserInfos) {
                        if (userInfo.getAccount().equals(String.valueOf(TempUser.id))) {
                            user = userInfo;
                            refreshList();
                        }
                    }
                }
            };
        }
        NIMClient.getService(UserServiceObserve.class).observeUserInfoUpdate(observer, is);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_text:
                save();
                break;
            case R.id.tagView_restView:
                flag = false;
                mTagListView.setDeleteMode(flag);
                mTagListView.setTags(mTags);
                isbacked = false;
                break;
            case R.id.back:
                if (!editText.getText().toString().equals("")) {
                    final Tag tag = new Tag();
                    tag.setId(0);
                    tag.setChecked(true);
                    tag.setTitle(editText.getText().toString());
                    final DialogUtils.Dialog_Center2 dialogUtils = new DialogUtils.Dialog_Center2(this);
                    final Dialog alertDialog = dialogUtils.Message("保存本次编辑？")
                            .Button("不保存", "保存").MbuttonOnClick(new DialogUtils.Dialog_Center2.buttonOnClick() {
                                @Override
                                public void onButton1() {
                                    dialogUtils.close();
                                    finish();
                                }

                                @Override
                                public void onButton2() {
                                    mTags.add(tag);
                                    mTagListView.addTag(tag);
                                    dialogUtils.close();
                                    finish();
                                }
                            }).create();
                    alertDialog.show();
                    LocationUtil.setWidth(this, alertDialog,
                            getResources().getDimensionPixelSize(R.dimen.x780));
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onTagClick(TextView tagView, Tag tag) {
        if (flag) {
            mTags.remove(tag);
            mTagListView.removeTag(tag);
            change();
            if (mTags.size() == 0) {
                flag = false;
                mTagListView.setDeleteMode(false);
                isbacked = false;
            }
        }
    }

    @Override
    public void onTagLongClick(TextView tagView, Tag tag) {
        flag = true;
        mTagListView.setDeleteMode(flag);
        mTagListView.setTags(mTags);
        isbacked = true;
    }

    public void save() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        if (!editText.getText().toString().equals("")) {
            Tag tag = new Tag();
            tag.setId(0);
            tag.setChecked(true);
            tag.setTitle(editText.getText().toString());

            if (Same(editText.getText().toString()))
                Toast.makeText(this, "标签名不能相同", Toast.LENGTH_SHORT).show();
            else {
                mTags.add(tag);
                mTagListView.addTag(tag);
                change();
            }
            editText.setText("");
        }
    }

    private void change() {
        String temp = "";
        for (Tag i : mTagListView.getTags()) {
            temp += i.getTitle() + NS.SPLIT;
        }
        ProgressSubscriberOnNext<ResponseBody> next = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) {
                try {
                    JSONObject jsonObject = new JSONObject(e.string());
                    if (jsonObject.getString(NS.CODE).equals("200")) {

                    } else {
                        showToast(jsonObject.getString(NS.MSG));
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    showToast("上传异常");
                }
            }
        };

        ProgressSubsciber<ResponseBody> progressSubsciber = new ProgressSubsciber<>(next, this);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.ID, TempUser.getId());
        jsonObject.addProperty(NS.LABEL, temp);
        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());

        InfoNetwork.getInstance().ModifyInfo(progressSubsciber, jsonObject, this);
    }


    public boolean Same(String title) {
        int num = mTags.size();
        for (int i = 0; i < num; i++) {
            if (mTags.get(i).getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isbacked) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (flag) {
                        flag = false;
                        mTagListView.setDeleteMode(flag);
                        mTagListView.setTags(mTags);
                    }//后退
                }
            }
            isbacked = false;
            return true;
        } else if (!editText.getText().toString().equals("")) {
            final Tag tag = new Tag();
            tag.setId(0);
            tag.setChecked(true);
            tag.setTitle(editText.getText().toString());
            final DialogUtils.Dialog_Center2 dialogUtils = new DialogUtils.Dialog_Center2(this);
            final Dialog alertDialog = dialogUtils.Message("保存本次编辑？")
                    .Button("不保存", "保存").MbuttonOnClick(new DialogUtils.Dialog_Center2.buttonOnClick() {
                        @Override
                        public void onButton1() {
                            dialogUtils.close();
                            finish();
                        }

                        @Override
                        public void onButton2() {
                            mTags.add(tag);
                            mTagListView.addTag(tag);
                            dialogUtils.close();
                            finish();
                        }
                    }).create();
            alertDialog.show();
            LocationUtil.setWidth(this, alertDialog,
                    getResources().getDimensionPixelSize(R.dimen.x780));

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }
}
