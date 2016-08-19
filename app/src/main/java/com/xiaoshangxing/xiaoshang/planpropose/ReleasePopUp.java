package com.xiaoshangxing.xiaoshang.planpropose;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.xiaoshangxing.R;

/**
 * Created by quchwe on 2016/7/23 0023.
 */
public class ReleasePopUp {

    OnPopClickListener mListener;
    private PopupWindow mPopup;

    public interface OnPopClickListener{
        void onReadyRelease();
        void onReleased();
        void onJoined();
    }
    public void popRelease(Context context, View moreBtnView, int resourceId,final OnPopClickListener listener){
        final LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = View.inflate(context,resourceId, null);
        if (mPopup == null) {

            mPopup = new PopupWindow(content, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout readyRelease= (LinearLayout) content.findViewById(R.id.publish);
            LinearLayout released= (LinearLayout) content.findViewById(R.id.published);
            LinearLayout joined= (LinearLayout) content.findViewById(R.id.collect);
            readyRelease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onReadyRelease();
                    mPopup.dismiss();
                }
            });

            released.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onReleased();
                    mPopup.dismiss();
                }
            });
            joined.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onJoined();
                    mPopup.dismiss();
                }
            });

        }
        mPopup.setBackgroundDrawable(new BitmapDrawable());
        mPopup.setFocusable(true);
        mPopup.update();

        content.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        View parent = mPopup.getContentView();



        WindowManager m = (WindowManager)context. getSystemService(Context.WINDOW_SERVICE);


        mPopup.showAsDropDown(moreBtnView, -context.getResources().getDimensionPixelSize(R.dimen.x243),
                context.getResources().getDimensionPixelSize(R.dimen.y50));


        mPopup.setAnimationStyle(R.style.popwindow_anim);

        content.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int mShowMorePopupWindowWidth = content.getMeasuredWidth();
        int mShowMorePopupWindowHeight = content.getMeasuredHeight();
        int heightMoreBtnView = moreBtnView.getHeight();

        mPopup.setOutsideTouchable(true);
        mPopup.setTouchable(true);

        mPopup.showAsDropDown(moreBtnView, -mShowMorePopupWindowWidth - context.getResources().getDimensionPixelSize(R.dimen.x6),
                -(mShowMorePopupWindowHeight + heightMoreBtnView) / 2);
    }


    void setmListener(OnPopClickListener listener){
        this.mListener = listener;
    }
}


