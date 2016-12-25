package com.xiaoshangxing.network.netUtil;

/**
 * Created by FengChaoQun
 * on 2016/8/28
 * 和服务器交互的一些常用字段
 */
public class NS {

    /**
     * 2016/12/14 21:42
     * description:分隔符
     */
    public static final String SPLIT = "#";
    public static final String SPLIT2 = "\\$";
    public static final String SPLIT3 = "\\^";

    /**
     * 2016/12/14 21:44
     * description:服务器返回常用字段
     */
    public static final String CODE = "code";
    public static final String MSG = "msg";
    public static final String TOKEN = "token";
    public static final String TIMESTAMP = "timeStamp";
    public static final int CODE_200 = 200;
    public static final String REQUEST_SUCCESS = "200";

    /**
     * 2016/12/14 21:44
     * description:个人信息
     */
    public static final String ID = "id";
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "username";
    public static final String HOMETOWN = "hometown";
    public static final String COLLEGE = "isCollege";

    /**
     * 2016/12/14 21:44
     * description:动态常用字段
     */
    public static final String CATEGORY = "category";
    public static final String LOCATION = "location";
    public static final String TEXT = "text";
    public static final String MOMENTID = "momentId";
    public static final String COMMENTID = "commentId";
    public static final String CLIENTTIME = "clientTime";
    public static final String NOTICE = "notice";
    public static final String FOBIDDEN = "forbidden";
    public static final String LABEL = "label";
    public static final String OPPOSITE_UERID = "oppositeUserId";
    public static final String PRICE = "price";
    public static final String CREATETIME = "createTime";
    public static final String DORM = "dorm";
    public static final String PERSON_LIMIT = "personLimit";
    public static final String PLAN_NAME = "planName";
    public static final String PLAN_GROUP = "groupNo";
    public static final String STATU = "status";
    public static final String COLLECT_STATU = "collectStatus";

    /**
     * 2016/12/14 21:45
     * description:动态类型
     */
    public static final String CATEGORY_STATE = "1";
    public static final String CATEGORY_HELP = "2";
    public static final String CATEGORY_PLAN = "3";
    public static final String CATEGORY_CALENDAR = "4";
    public static final String CATEGORY_SALE = "5";
    public static final String CATEGORY_REWARD = "6";

    /**
     * 2016/12/14 21:45
     * description:收藏类型
     */
    public static final String COLLECT_REWARD = "4";
    public static final String COLLECT_SALE = "2";

    /**
     * 2016/12/14 21:48
     * description:动态的操作类型
     */
    public static final String APPLY_PLAN = "APPLY_PLAN";
    public static final String PRAISE = "0";
    public static final String INFO = "1";
    public static final String COLLECT = "2";
    public static final String JOIN = "3";

    /**
     * 2016/12/14 21:47
     * description:校历常用字段
     */
    public static final String DAY = "day";
    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String MONTH_C = "月";

    /**
     * 2016/12/14 21:49
     * description:校友圈权限
     */
    public static final String MY_BLOCK = "myBlock";//不看谁
    public static final String BLOCK = "block";//不给看谁
    public static final String MY_BLOCK_CODE = "0";//不看谁
    public static final String BLOCK_CODE = "1";//不让看我
    public static final String REMOVE_MY_BLOCK_CODE = "3";//去除不看谁
    public static final String REMOVE_BLOCK_CODE = "4";//去除不让看我

    /**
     * 2016/12/14 21:50
     * description:遇见有关
     */
    public static final String MARK = "MARK";//留心
    public static final String CREATOR = "creator";//公告发布者
    public static final String TIME = "time";//公告发布时间


    /**
     * 2016/12/14 21:50
     * description:推送有关
     */
    public static final String PUSH_TYPE = "pushType";
    public static final String OPERATION_TYPE = "operationType";
    public static final String PUSH_TIME = "pushTime";

    /**
     * 2016/12/14 21:53
     * description:其余常用字段
     */
    public static final String CONTENT = "content";
    public static final String TYPE = "type";

    /**
     * 2016/12/14 21:52
     * description:获取系统的当前时间
     */
    public static long currentTime() {
        return System.currentTimeMillis();
    }

}
