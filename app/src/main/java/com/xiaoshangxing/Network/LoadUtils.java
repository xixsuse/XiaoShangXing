package com.xiaoshangxing.Network;

import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.XSXApplication;
import com.xiaoshangxing.utils.normalUtils.SPUtils;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.ResponseBody;

/**
 * Created by FengChaoQun
 * on 2016/9/12
 */
public class LoadUtils {

    public static final String TIME_LOAD_STATE = "TIME_LOAD_STATE";
    public static final String TIME_LOAD_SELFSTATE = "TIME_LOAD_SELFSTATE";
    public static final String TIME_LOAD_HELP = "TIME_LOAD_HELP";
    public static final String TIME_LOAD_SELFHELP = "TIME_LOAD_SELFHELP";

    public static boolean needRefresh(String type) {
        return (NS.currentTime() - (long) SPUtils.get(XSXApplication.getInstance(), type,
                SPUtils.DEFAULT_LONG) > 5 * TimeUtil.MINUTE);
    }

    public static void refreshTime(String type) {
        SPUtils.put(XSXApplication.getInstance(), type, NS.currentTime());
    }

    public static void parseData(ResponseBody responseBody, Realm realm, IBaseView iBaseView) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject(responseBody.string());
        switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
            case 200:
                final JSONArray jsonArray = jsonObject.getJSONObject(NS.MSG).getJSONArray("moments");

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.createOrUpdateAllFromJson(Published.class, jsonArray);
                    }
                });
                break;
            default:
                iBaseView.showToast(jsonObject.getString(NS.MSG));
                break;
        }
    }

}
