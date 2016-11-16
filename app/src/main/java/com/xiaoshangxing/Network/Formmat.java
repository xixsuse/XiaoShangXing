package com.xiaoshangxing.Network;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.xiaoshangxing.Network.netUtil.MultipartUtility;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.netUtil.SimpleCallBack;
import com.xiaoshangxing.utils.FileUtils;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.normalUtils.SPUtils;
import com.xiaoshangxing.yujian.ChatActivity.SendImageHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by FengChaoQun
 * on 2016/9/9
 */
public class Formmat {
    private Activity context;
    private IBaseView iBaseView;
    private String path;
    private MultipartUtility multipartUtility;
    private Handler handler;
    private SimpleCallBack simpleCallBack;
    private String successToast;
    private String Failmsg;

    public Formmat(final IBaseView iBaseView, final Activity context, String path) {
        this.iBaseView = iBaseView;
        this.context = context;
        this.path = path;
        handler = new Handler(context.getMainLooper()) {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        iBaseView.showLoadingDialog("上传中...");
                        break;
                    case 2:
                        if (simpleCallBack != null) {
                            simpleCallBack.onSuccess();
                        }
                        iBaseView.hideLoadingDialog();
                        if (successToast != null) {
                            iBaseView.showToast(successToast);
                        } else {
                            iBaseView.showToast("发布成功");
                        }

                        FileUtils.deleteFolderFile(FileUtils.getXSX_CameraPhotoPath(), false);
                        break;
                    case 3:
                        iBaseView.showToast("上传出现异常");
                        iBaseView.hideLoadingDialog();
                        break;
                    case 4:
                        if (Failmsg != null) {
                            iBaseView.showToast(Failmsg);
                        }
                        iBaseView.hideLoadingDialog();
                        break;
                    case 5:
                        iBaseView.showToast("数据解析异常");
                        iBaseView.hideLoadingDialog();
                    default:
                        iBaseView.hideLoadingDialog();
                        iBaseView.showToast("" + msg.what);
                        break;
                }
                super.handleMessage(msg);
            }
        };
        try {
            multipartUtility = new MultipartUtility(path, "UTF-8");
            multipartUtility.addHeaderField("User-Phone",
                    (String) SPUtils.get(context, SPUtils.PHONENUMNBER, SPUtils.DEFAULT_STRING));
            multipartUtility.addHeaderField("User-Digest",
                    (String) SPUtils.get(context, SPUtils.DIGEST, SPUtils.DEFAULT_STRING));

        } catch (IOException e) {
            e.printStackTrace();
            iBaseView.showToast("初始化上传组件失败");
        }
    }

    public Formmat addFormField(String name, String value) {
        multipartUtility.addFormField(name, value);
        return this;
    }

    public Formmat addFilePart(String fieldName, File uploadFile) throws IOException {
        multipartUtility.addFilePart(fieldName, uploadFile);
        return this;
    }

    public Formmat addFormField(Map<String, String> map) {
        for (String key : map.keySet()) {
            if (!TextUtils.isEmpty(map.get(key))) {
                addFormField(key, map.get(key));
            }
        }
        return this;
    }

    public Formmat addFilePart(List<String> list, Context context) throws IOException {
        if (list == null || list.size() < 1) {
            return this;
        }
        startLoading();

        for (String path : list) {
            if (!TextUtils.isEmpty(path)) {
                multipartUtility.addFilePart("images", SendImageHelper.getLittleImage(path, context));
            }
        }
        return this;
    }

    public Formmat addFilePart(List<String> list, String key) throws IOException {
        if (list == null || list.size() < 1) {
            return this;
        }
        startLoading();

        for (String path : list) {
            if (!TextUtils.isEmpty(path)) {
                multipartUtility.addFilePart(key, SendImageHelper.getLittleImage(path, context));
            }
        }
        return this;
    }

    public void doUpload() {
        try {
            startLoading();

            List<String> result = multipartUtility.finish();
            String result_String = result.toString();
            Log.d("result", "--" + result_String);

            JSONObject jsonObject = null;
            try {
                JSONArray jsonArray = new JSONArray(result_String);
                jsonObject = jsonArray.getJSONObject(0);
                switch (jsonObject.getInt(NS.CODE)) {
                    case 200:
                        endLoading(2);
                        break;
                    default:
                        Failmsg = jsonObject.getString(NS.MSG);
                        endLoading(4);
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                e.printStackTrace();
                endLoading(3);
            }

//            if (result_String.contains("200")) {
//                endLoading(2);
//            } else {
//
//                endLoading(4);
//            }
        } catch (IOException e) {
            e.printStackTrace();
            endLoading(3);
        }
    }

    private void startLoading() {
        Message start = new Message();
        start.what = 1;
        handler.sendMessage(start);
    }

    private void endLoading(int i) {
        Message end = new Message();
        end.what = i;
        handler.sendMessage(end);
    }

    public SimpleCallBack getSimpleCallBack() {
        return simpleCallBack;
    }

    public void setSimpleCallBack(SimpleCallBack simpleCallBack) {
        this.simpleCallBack = simpleCallBack;
    }

    public void setSuccessToast(String successToast) {
        this.successToast = successToast;
    }
}
