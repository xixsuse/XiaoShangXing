package com.xiaoshangxing.Network.netUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by FengChaoQun
 * on 2016/8/28
 */
public class NS {

    public static final String SPLIT = "#";

    public static final String CODE="code";
    public static final String MSG="msg";
    public static final String TOKEN="token";
    public static final String TIMESTAMP = "timeStamp";

    public static final String ID = "id";
    public static final String USER_ID = "userId";


    //    个人信息
    public static final String USER_NAME = "username";
    public static final String USER_IMAGE = "userImage";

    //    动态
    public static final String CATEGORY = "category";
    public static final String LOCATION = "location";
    public static final String TEXT = "text";
    public static final String MOMENTID = "momentId";
    public static final String CLIENTTIME = "clientTime";
    public static final String NOTICE = "notice";
    public static final String FOBIDDEN = "forbidden";
    public static final String CATEGORY_STATE = "1";
    public static final String CATEGORY_HELP = "2";
    public static final String CATEGORY_PLAN = "3";
    public static final String CATEGORY_CALENDAR = "4";
    public static final String CATEGORY_SALE = "5";
    public static final String PRICE = "price";


    public static final String CATEGORY_REWARD = "6";

    //    好友
    public static final String MARK = "MARK";//留心
    public static final String RMB="¥";



    public static long currentTime(){
        return System.currentTimeMillis();
    }


    public static final int CODE_200 = 200;

    public static final String CONTENT="content";

    public static void getPermissionString(String key, List<String> list, Map<String, String> map) {
        if (list != null && list.size() > 0) {
            String result = "";
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    result += list.get(i);
                } else {
                    result += "#" + list.get(i);
                }
            }
            map.put(key, result);
        }
    }
}
