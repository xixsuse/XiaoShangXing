package com.xiaoshangxing.network.AutoUpdate;

/**
 * Created by FengChaoQun
 * on 2016/9/24
 * 更新相关
 */

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.xiaoshangxing.network.AppNetwork;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.XSXApplication;
import com.xiaoshangxing.utils.normalUtils.FileUtils;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.ResponseBody;
import rx.Subscriber;

public class UpdateManager {

    /* 下载包安装路径 */
    private static final String savePath = FileUtils.XSX_PATH;
    private static final String saveFileName = savePath + "UpdateRelease.apk";
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private Context mContext;
    //提示语
    private String updateMsg;
    //返回的安装包url
    private String apkUrl;
    //版本号
    private String version;
    private Dialog noticeDialog;
    private Dialog downloadDialog;
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;
    //    无更新时是否需要通知用户
    private boolean needNotice;
    private int progress;

    private Thread downLoadThread;

    private boolean interceptFlag = false;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:

                    installApk();
                    break;
                default:
                    break;
            }
        }

        ;
    };
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    //更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        //下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);//点击取消就停止下载.

                fos.close();
                is.close();
                downloadDialog.dismiss();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    public UpdateManager(Context context, String version, boolean needNotice) {
        this.mContext = context;
        this.version = version;
        this.needNotice = needNotice;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    //外部接口让主Activity调用
    public void checkUpdateInfo() {
        update();
    }

    private void showNoticeDialog() {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle("软件版本更新");
        builder.setMessage(updateMsg);
        builder.setPositiveButton("下载", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
            }
        });
        builder.setNegativeButton("以后再说", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();

//        final Dialog dialog = new Dialog(mContext, R.style.ActionSheetDialog);
//        LinearLayout linearLayout = (LinearLayout) View
//                .inflateChild(mContext, R.layout.dialog_update, null);
//
//        TextView message = (TextView) linearLayout.findViewById(R.id.dialog_message);
//        message.setText(updateMsg);
//        Button cancle = (Button) linearLayout.findViewById(R.id.cancel);
//        Button ok = (Button) linearLayout.findViewById(R.id.ok);
//        cancle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                showDownloadDialog();
//            }
//        });
//        dialog.setCancelable(false);
//        dialog.show();
    }

    private void showDownloadDialog() {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle("软件版本更新");

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.progressbar, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);

        builder.setView(v);
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
            }
        });
        downloadDialog = builder.create();
        downloadDialog.show();
        downloadDialog.setCancelable(false);

        downloadApk();
    }

    /**
     * 下载apk
     *
     * @param
     */

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 安装apk
     *
     * @param
     */
    private void installApk() {
        //标记需要展示引导页
        SPUtils.put(XSXApplication.getInstance(), SPUtils.IS_NEED_GUIDE, true);

        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void update() {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("version", "" + version);

        Subscriber<ResponseBody> subscriber = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    JSONObject jsonObject1 = new JSONObject(responseBody.string());
                    if (jsonObject1.getString(NS.CODE).equals("900010006")) {
                        apkUrl = jsonObject1.getJSONObject(NS.MSG).getString("path");
                        updateMsg = jsonObject1.getJSONObject(NS.MSG).getString("updateContent");
                        if (!TextUtils.isEmpty(apkUrl)) {
                            showNoticeDialog();
                        } else {
                            Toast.makeText(mContext, "解析下载路径出错", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (needNotice) {
                            Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        AppNetwork.getInstance().Update(subscriber, jsonObject, mContext);
    }
}
