package com.xiaoshangxing.Network.netUtil;

/**
 * Created by FengChaoQun
 * on 2016/8/3
 */
public class BaseUrl {
    public static final String BASE_URL = "http://114.55.96.241:8080/xsx/v1/";
//    登录注册
    public static final String LOGIN = "base/login";
    public static final String SEND_CODE = "base/sendCode";
    public static final String CHECK_CODE = "base/checkCode";
    public static final String REGISTER = "base/setPassword";
    public static final String CHECK_PHONE = "base/chkExist";

    public static final String BIND_EMAIL = "base/bindEmail";
    public static final String UNBIND_EMAIL = "base/unBindEmail";
    public static final String PUBLISH = "moment/releaseMoment";
    public static final String SET_IMAGE = "base/setUserImage";
    public static final String USER = "base/getUserInfo";
    public static final String GET_PUBLISHED ="moment/catRelease";
    public static final String GET_ALLPUBLISHED = "moment/catMoments";
    public static final String GET_TRANSMIT_INFO = "moment/catTransmitInfo";
    public static final String REFRESH_PUBLISHED = "moment/catReleaseUpdate";
    public static final String TRANSMIT = "moment/transmit";
    public static final String DELETE_PUBLISHED = "moment/deleteMoment";
    public static final String MODIFT_INFO ="base/user";

    public static final String CHECH_PASSWORD ="base/chkPassword";
    public static final String MODIFY_PASSWORD ="base/modifyPassword";

    public static final String COMMENT = "commentary/comment";
    public static final String OPERATE = "operation/operate";
    public static final String CANCLE_OPERATE = "operation/cancelOperate";
    public static final String CHANGE_PUBLISHED_STATU = "moment/changeMomentStatus";
    public static final String GET_COLLECT = "moment/catCollect";

    public static final String GET_CALENDAR = "moment/getSchoolCalendar";


    public static final String UPDATE = "operation/updateVersion";
    public static final String SUGGESTION = "suggestion/suggest";
    public static final String GET_CALENDAR_INPUTER = "base/getLeaderInfo";
    public static final String FAVOR = "friend/favor";

}
