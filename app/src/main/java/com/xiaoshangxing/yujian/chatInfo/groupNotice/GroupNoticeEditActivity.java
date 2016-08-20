package com.xiaoshangxing.yujian.chatInfo.groupNotice;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/8/12.
 */
public class GroupNoticeEditActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.groupNoticeEdit_back)
    TextView back;
    @Bind(R.id.groupNoticeEdit_finish)
    TextView finish;
    @Bind(R.id.groupNotice_editText)
    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_groupnotice_edit);
        ButterKnife.bind(this);
        finish.setEnabled(false);
        back.setOnClickListener(this);
        finish.setOnClickListener(this);

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
                    finish.setAlpha(1);
                    finish.setEnabled(true);
                } else {
                    finish.setAlpha((float) 0.5);
                    finish.setEnabled(false);
                }
            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.groupNoticeEdit_back:
                if (!editText.getText().toString().equals("")) {
                    final DialogUtils.Dialog_Center2 dialogUtils = new DialogUtils.Dialog_Center2(this);
                    final Dialog alertDialog = dialogUtils.Message("退出本次编辑？")
                            .Button("继续编辑", "退出").MbuttonOnClick(new DialogUtils.Dialog_Center2.buttonOnClick() {
                                @Override
                                public void onButton1() {
                                    dialogUtils.close();
                                }

                                @Override
                                public void onButton2() {
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
            case R.id.groupNoticeEdit_finish:
                final DialogUtils.Dialog_Center2 dialogUtils = new DialogUtils.Dialog_Center2(this);
                final Dialog alertDialog = dialogUtils.Message("该公告会通知全部群成员，是否\n发布？")
                        .Button("取消", "发布").MbuttonOnClick(new DialogUtils.Dialog_Center2.buttonOnClick() {
                            @Override
                            public void onButton1() {
                                dialogUtils.close();
                            }

                            @Override
                            public void onButton2() {
                                finish();
                            }
                        }).create();
                alertDialog.show();
                LocationUtil.setWidth(this, alertDialog,
                        getResources().getDimensionPixelSize(R.dimen.x780));
                break;
        }
    }





}
