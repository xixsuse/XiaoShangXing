package com.xiaoshangxing.Network;

import android.util.Log;

import java.util.List;
import java.util.Map;

/**
 * Created by FengChaoQun
 * on 2016/8/28
 */
public class NS {
    public static final String CODE="code";
    public static final String MSG="msg";
    public static final String TOKEN="token";
    public static final String TIMESTAMP = "timeStamp";

    public static final String ID = "id";
    public static final String USER_ID = "userId";

    public static long currentTime(){
        return System.currentTimeMillis();
    }

    public static final String NOTICE = "3";
    public static final String FOBIDDEN = "0";
    public static final String NOTICE_FOBIDDEN = "4";
    public static final String NO_PERMISSON = "1";

    public static final String sight(List<String> notice, List<String> fobidden) {
        boolean is_notice = !(notice == null || notice.size() == 0);
        boolean is_fobidden = !(fobidden == null || fobidden.size() == 0);

        if (!is_notice && !is_fobidden) {
            return NO_PERMISSON;
        }

        if (is_notice && !is_fobidden) {
            return NOTICE;
        }

        if (!is_notice && is_fobidden) {
            return FOBIDDEN;
        }
        return NOTICE_FOBIDDEN;

    }

    public static final String sightUserids(List<String> notice, List<String> fobidden) {
//        String sight = sight(notice, fobidden);
        StringBuilder result = new StringBuilder();
//        if (sight.equals(NO_PERMISSON)){
//            return  null;
//        }else if (sight.equals(NOTICE)){
//            for (int i=0;i<notice.size();i++){
//
//            }
//        }

        if (notice != null && notice.size() > 0) {
            for (int i = 0; i < notice.size(); i++) {
                if (i == 0) {
                    result.append(notice.get(i));
                } else {
                    result.append("#" + notice.get(i));
                }

            }
        }

        if (fobidden != null && fobidden.size() > 0) {
            for (int i = 0; i < fobidden.size(); i++) {
                if (i == 0) {
                    result.append(fobidden.get(i));
                } else {
                    result.append("$" + fobidden.get(i));
                }
            }
        }

        Log.d("sightUserids", "--" + result.toString());
        return result.toString();
    }

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
