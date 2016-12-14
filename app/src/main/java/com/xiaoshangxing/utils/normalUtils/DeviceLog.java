package com.xiaoshangxing.utils.normalUtils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.utils.XSXCrashHelper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by FengChaoQun
 * on 2016/7/12
 */
public class DeviceLog {
    private Activity activity;
    private TelephonyManager telephonyManager;

    public DeviceLog(Activity activity) {
        this.activity = activity;
        telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public void saveDevieceLog() {
        //如果SD卡不存在或无法使用，则无法把异常信息写入SD卡
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.w("saveDeviceLog", "sdcard unmounted,skip dump exception");
        }

        File dir = new File(XSXCrashHelper.PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
        //以当前时间创建log文件
        File file = new File(XSXCrashHelper.PATH + "DeviceLog" + TempUser.getId() + "_" + time + ".txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            //导出时间
            pw.println(time);
            //导出设备信息
            pw.println(getUid());
            pw.println(getProid());
            pw.println(getVersion());
            pw.println(getSystemInfo());
            pw.println(getVendor());
            pw.println(getDeviceModel());
            pw.println(getABI());
            pw.println(getPX());
            pw.println(getNetType());
            pw.println(getIMSI());
            pw.println(getIMEI());
            pw.close();
            Log.d("saveDeviceLog", "ok");
        } catch (Exception e) {
            Log.e("saveDeviceLog", "dump crash info failed");
            e.printStackTrace();
        }
    }

    //    获取分辨率
    public String getPX() {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int SCREEN_WIDTH = dm.widthPixels;
        int SCREEN_HEIGHT = dm.heightPixels;
        StringBuilder stringBuilder = new StringBuilder("Resolution:");
        stringBuilder.append(SCREEN_WIDTH).append("*").append(SCREEN_HEIGHT).append(";");
        return stringBuilder.toString();
    }

    //获取设备型号
    public String getDeviceModel() {
        String device_model = Build.MODEL;
        return "Model:" + device_model + ";";
    }

    //获取设备制造商
    public String getVendor() {
        return "Vendor:" + Build.MANUFACTURER + ";";
    }

    public String getABI() {
        return "CPU_ABI:" + Build.CPU_ABI;
    }

    //获取系统版本
    public String getSystemInfo() {
        String version_release = Build.VERSION.RELEASE;
        return "OSVersion:" + version_release + "_" + Build.VERSION.SDK_INT + ";";
    }

    //获取手机网络信息
    public String getNetType() {
        String strNetworkType = null;
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        strNetworkType = "4G";
                        break;
                    default:
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }
                        break;
                }

            }
        }
        return "Network:" + strNetworkType + ";";
    }


    //获取IMSI
    public String getIMSI() {
        String IMSI = telephonyManager.getSubscriberId();
        return "IMSI:" + IMSI + ";";
    }

    //获取IMEI
    public String getIMEI() {
        String IMEI = telephonyManager.getDeviceId();
        return "IMEI:" + IMEI + ";";
    }

    //获取软件版本
    public String getVersion() {
        try {
            PackageManager manager = activity.getPackageManager();
            PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
            String version = info.versionName + "_" + info.versionCode;
            return "AppVersion:" + version + ";";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //获取客户端产品编号
    public String getProid() {
        String proid = "android";
        return "Proid:" + proid + ";";
    }

    //获取用户编号
    public String getUid() {
        return "UserId:" + TempUser.getId() + ";";
    }


}
