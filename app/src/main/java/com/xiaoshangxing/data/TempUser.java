package com.xiaoshangxing.data;

import android.content.Context;

import com.xiaoshangxing.utils.normalUtils.SPUtils;

/**
 * Created by FengChaoQun
 * on 2016/9/8
 * 获取当前账号信息便捷入口
 */

public class TempUser {

    public static String phone;
    public static int id;
    public static boolean isRealName;

    //    获取当前账号注册号码
    public static String getPhone(Context context) {
        String account = (String) SPUtils.get(context, SPUtils.PHONENUMNBER, SPUtils.DEFAULT_STRING);
        return account.equals(SPUtils.DEFAULT_STRING) ? null : account;
    }

    //     获取当前账号id
    public static Integer getID(Context context) {
        Integer ID = (Integer) SPUtils.get(context, SPUtils.ID, SPUtils.DEFAULT_int);
        return ID == SPUtils.DEFAULT_int ? -1 : ID;
    }

    public static boolean isIsRealName(Context context) {
        return (boolean) SPUtils.get(context, SPUtils.IS_REAL_NAME, false);
    }

    public static boolean isMine(String id) {
        return String.valueOf(TempUser.id).equals(id);
    }

    public static String getId() {
        return String.valueOf(id);
    }

    public static void initTempUser(Context context) {
        TempUser.phone = TempUser.getPhone(context);
        TempUser.id = TempUser.getID(context);
        TempUser.isRealName = TempUser.isIsRealName(context);
    }
}
