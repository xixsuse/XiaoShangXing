package com.xiaoshangxing.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.DataSetting;

/**
 * Created by 15828 on 2016/7/25.
 */
public class ImageButtonText extends LinearLayout implements View.OnClickListener {

    private ImageView imgView;
    private TextView textView;
    private boolean checked = false;
    private OnImageButtonTextClickListener mOnImageButtonTextClickListener;

    public ImageButtonText(Context context) {
        super(context, null);
    }

    public ImageButtonText(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.button_img_text, this, true);

        this.imgView = (ImageView) findViewById(R.id.imgview);
        this.textView = (TextView) findViewById(R.id.textview);

        this.setClickable(true);
        this.setFocusable(true);
        checked = DataSetting.IsFocused(context);
        if (!checked) {
            imgView.setImageResource(R.mipmap.icon_liuxin);
        } else {
            imgView.setImageResource(R.mipmap.icon_liuxin_select);
        }
        this.setOnClickListener(this);

//        this.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checked) {
//                    checked = true;
//                    imgView.setImageResource(R.mipmap.icon_liuxin_select);
//                    SPUtils.put(context, "focus", true);
//
//                    DialogUtils.Dialog_Linxin dialog_no_button =
//                            new DialogUtils.Dialog_Linxin((Activity)context, "已添加到我关注的人");
//                    final Dialog notice_dialog = dialog_no_button.create();
//                    notice_dialog.show();
//                    LocationUtil.setWidth((Activity)context, notice_dialog,
//                            getResources().getDimensionPixelSize(R.dimen.x420));
//
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            notice_dialog.dismiss();
//                        }
//                    }, 500);
//                } else {
//
//                    final DialogUtils.Dialog_Center2 dialogUtils = new DialogUtils.Dialog_Center2(context);
//                    final Dialog alertDialog = dialogUtils.Message("确定不再留心？")
//                            .Button("取消", "确定").MbuttonOnClick(new DialogUtils.Dialog_Center2.buttonOnClick() {
//                                @Override
//                                public void onButton1() {
//                                    dialogUtils.close();
//                                }
//
//                                @Override
//                                public void onButton2() {
//                                    checked = false;
//                                    imgView.setImageResource(R.mipmap.icon_liuxin);
//                                    SPUtils.put(context, "focus", false);
//                                    dialogUtils.close();
//                                }
//                            }).create();
//                    alertDialog.show();
//                    LocationUtil.setWidth((Activity)context, alertDialog,
//                            getResources().getDimensionPixelSize(R.dimen.x780));
//                }
//            }
//        });


    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setImgView(ImageView imgView){
        this.imgView = imgView;
    }

    public ImageView getImgView() {
        return imgView;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    @Override
    public void onClick(View v) {
        mOnImageButtonTextClickListener.OnImageButtonTextClick();
    }


    public interface OnImageButtonTextClickListener {
        void OnImageButtonTextClick();
    }

    public void setmOnImageButtonTextClickListener(OnImageButtonTextClickListener mOnImageButtonTextClickListener) {
        this.mOnImageButtonTextClickListener = mOnImageButtonTextClickListener;
    }
}
