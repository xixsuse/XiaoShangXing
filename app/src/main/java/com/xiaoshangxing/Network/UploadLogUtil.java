package com.xiaoshangxing.Network;

import android.util.Log;

import com.xiaoshangxing.Network.netUtil.BaseUrl;
import com.xiaoshangxing.Network.netUtil.MultipartUtility;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.utils.FileUtils;
import com.xiaoshangxing.utils.XSXApplication;
import com.xiaoshangxing.utils.XSXCrashHelper;
import com.xiaoshangxing.utils.normalUtils.NetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by FengChaoQun
 * on 2016/11/27
 */

public class UploadLogUtil {

    public static void doUpload() throws IOException {
        File file = new File(XSXCrashHelper.PATH);
        if (!file.exists()) {
            Log.d("uploadLog", "has no folder");
            return;
        }
        File[] files = file.listFiles();
        if (files == null) {
            Log.d("uploadLog", "has no log");
            return;
        }
        MultipartUtility multipartUtility = new MultipartUtility(BaseUrl.BASE_URL + BaseUrl.UPLOAD_LOG, "UTF-8");
        for (File i : files) {
            multipartUtility.addFilePart("logfiles", i);
        }

        List<String> result = multipartUtility.finish();
        String result_String = result.toString();
        Log.d("result", "--" + result_String);

        JSONObject jsonObject = null;
        try {
            JSONArray jsonArray = new JSONArray(result_String);
            jsonObject = jsonArray.getJSONObject(0);
            switch (jsonObject.getInt(NS.CODE)) {
                case 200:
                    FileUtils.deleteFolderFile(XSXCrashHelper.PATH, false);
                    Log.d("uploadLog", "success");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
