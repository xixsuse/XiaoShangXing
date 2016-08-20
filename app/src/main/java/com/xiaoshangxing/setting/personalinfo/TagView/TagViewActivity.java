package com.xiaoshangxing.setting.personalinfo.TagView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.utils.TagView.Tag;
import com.xiaoshangxing.setting.utils.TagView.TagListView;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianyang on 2016/8/20.
 */
public class TagViewActivity extends BaseActivity implements View.OnClickListener,
        TagListView.OnTagClickListener, TagListView.OnTagLongClickListener {
    private View restView;
    private TagListView mTagListView;
    private final List<Tag> mTags = new ArrayList<Tag>();
    private EditText editText;
    private TextView save, back;
    private boolean flag = false;
    private boolean isbacked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_setting_personalinfo_tagview);
        mTagListView = (TagListView) findViewById(R.id.tagview);
        restView = findViewById(R.id.tagView_restView);
        mTagListView.setTags(mTags);
        save = (TextView) findViewById(R.id.tagView_save);
        back = (TextView) findViewById(R.id.tagview_back);
        back.setOnClickListener(this);

        editText = (EditText) findViewById(R.id.tagView_editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tagView_save:
                save();
                break;
            case R.id.tagView_restView:
                flag = false;
                mTagListView.setDeleteMode(flag);
                mTagListView.setTags(mTags);
                isbacked = false;
                break;
            case R.id.tagview_back:
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
            if (mTags.size() == 0) {
                Log.d("qqq", "   " + mTags.size());
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
            Log.d("qqq", editText.getText().toString());

            if (Same(editText.getText().toString()))
                Toast.makeText(this, "标签名不能相同", Toast.LENGTH_SHORT).show();
            else {
                mTags.add(tag);
                mTagListView.addTag(tag);
            }
            editText.setText("");
//                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
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
                    Log.d("qqq", "back----" + flag);//表示按返回键 时的操作
                    if (flag) {
                        flag = false;
                        mTagListView.setDeleteMode(flag);
                        mTagListView.setTags(mTags);
                    }//后退
                }
            }
            isbacked = false;
            return true;
        }else if (!editText.getText().toString().equals("")) {
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

}
