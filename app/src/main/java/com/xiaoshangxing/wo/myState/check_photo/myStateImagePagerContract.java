package com.xiaoshangxing.wo.myState.check_photo;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;

/**
 * Created by FengChaoQun
 * on 2016/7/13
 */
public class myStateImagePagerContract {

    public interface View extends IBaseView<Presenter>{
        /*
        **describe:设置时间
        */
        void setTime();
        /*
        **describe:点击右上角的菜单按钮弹出的菜单
        */
        void showMoreDialog();
        /*
        **describe:前往详细界面
        */
        void gotoDetails();
        /*
        **describe:设置文字内容
        */
        void setText();
        /*
        **describe:设置点赞和评论数目
        */
        void setPraiseandComment();
        /*
        **describe:发送给好友
        */
        void sendToFriend();
        /*
        **describe:弹出确认删除图片对话框
        */
        void showMakesureDialog();

    }

    public interface Presenter extends IBasePresenter{
        /*
        **describe:保存到本地
        */
        void save();
    }
}
