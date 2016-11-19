package com.xiaoshangxing.xiaoshang.MessageNotice.NoticeViewHodler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Notice;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.layout.Name;
import com.xiaoshangxing.xiaoshang.MessageNotice.NoticeAdpter;

/**
 * Created by FengChaoQun
 * on 2016/11/16
 */

public abstract class NoticeBaseViewHolder {
    protected NoticeAdpter adpter;
    protected int position;
    protected Context context;
    protected View view;
    protected Notice notice;

    protected CirecleImage headImage;
    protected Name name;
    protected TextView time;
    protected View responseLay;
    protected FrameLayout content;
    protected EmotinText publishedText;


    public View getView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.notice_base_vh, null);
        inflateBase();
        inflateChild();
        return view;
    }


    //初始化基础
    private void inflateBase() {
        headImage = (CirecleImage) view.findViewById(R.id.head_image);
        name = (Name) view.findViewById(R.id.name);
        time = (TextView) view.findViewById(R.id.time);
        responseLay = view.findViewById(R.id.response);
        content = (FrameLayout) view.findViewById(R.id.content);
        publishedText = (EmotinText) view.findViewById(R.id.published_text);
    }

    //初始化子类
    public abstract void inflateChild();

    public void refresh(Notice notice) {
        setNotice(notice);
        refreshBase();
        refreshChild();
    }

    //刷新基础
    protected void refreshBase() {
        initView();
        initOnclick();
    }

    //刷新子类
    protected abstract void refreshChild();

    private void initView() {

    }

    private void initOnclick() {

    }

    public NoticeAdpter getAdpter() {
        return adpter;
    }

    public void setAdpter(NoticeAdpter adpter) {
        this.adpter = adpter;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}
