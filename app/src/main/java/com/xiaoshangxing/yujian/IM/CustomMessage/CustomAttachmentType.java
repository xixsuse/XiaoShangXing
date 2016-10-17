package com.xiaoshangxing.yujian.IM.CustomMessage;

/**
 * Created by zhoujianghua on 2015/4/9.
 */
public interface CustomAttachmentType {
    /**
     * description:自定义消息类型，多端保持统一
     */
    int Reward = 1;             //校内悬赏
    int Help = 2;               //校友互帮
    int Plan = 3;               //计划发起
    int Sale = 4;               //闲置出售
    int ApplyPlan = 5;          //申请加入计划
    int CustomNotification = 6; //通知
}
