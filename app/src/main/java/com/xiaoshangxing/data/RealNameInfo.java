package com.xiaoshangxing.data;

import android.util.Log;

import com.xiaoshangxing.Network.netUtil.NS;

import java.util.Arrays;

/**
 * Created by FengChaoQun
 * on 2016/11/7
 */

public class RealNameInfo {

    /**
     * id : 18
     * serverTime : 1478532980105
     * userId : 76
     * name : "张涛"^1
     * sex : "1"^1
     * studentNum : "111111111"^1
     * schoolName : "江南大学"^1
     * college : "物联网"^1
     * profession : "计算机"^1
     * clientTime : 2016-11-07
     * admissionYear : "2013年"^1
     * degree : "本科"^1
     * sidImages : http://114.55.96.241:8080/images/cc5b1d07afdd427a9e01ed38339b25ef.jpg^1
     * isAuth : 1
     */

    private int id;
    private long serverTime;
    private String userId;
    private String name;
    private String sex;
    private String studentNum;
    private String schoolName;
    private String college;
    private String profession;
    private String clientTime;
    private String admissionYear;
    private String degree;
    private String sidImages;
    private String isAuth;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getClientTime() {
        return clientTime;
    }

    public void setClientTime(String clientTime) {
        this.clientTime = clientTime;
    }

    public String getAdmissionYear() {
        return admissionYear;
    }

    public void setAdmissionYear(String admissionYear) {
        this.admissionYear = admissionYear;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getSidImages() {
        return sidImages;
    }

    public void setSidImages(String sidImages) {
        this.sidImages = sidImages;
    }

    public String getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(String isAuth) {
        this.isAuth = isAuth;
    }

    //筛选字符
    public static String getOnlyString(String s) {
        String[] result = s.split(NS.SPLIT3);
        Log.d("String", result[0]);
        Log.d("Strings", Arrays.toString(result));
        return result[0];
    }

    //    判断是否某项是否认证成功
    public static boolean isPass(String s) {
        String[] result = s.split(NS.SPLIT3);
        if (result.length >= 2 && result[1].equals("1")) {
            return true;
        }
        return false;
    }
}
