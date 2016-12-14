package com.xiaoshangxing.data.bean;

import android.text.TextUtils;

import com.xiaoshangxing.network.netUtil.NS;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by FengChaoQun
 * on 2016/9/9
 */
public class Published extends RealmObject {

    /**
     * id : 2
     * userId : 1
     * createTime : 1471491953000
     * text : hello
     * image :
     * location : wuxi
     * isHeadline : 0
     * personLimit :
     * updateTime :
     * clientTime : 1471491952000
     * originalId :
     * isTransimit : 0
     * category : 1
     * sight : 0
     * price :
     * dorm :
     * isDelete : 0
     * validationDate :
     * oprations :
     * comments : [{"id":2,"userId":2,"isDelete":0,"clientTime":1471542075000,"createTime":1471542075000,"updateTime":"","text":"啊哈哈哈哈哈","isReply":0,"lastId":"","momentId":2},{"id":3,"userId":2,"isDelete":0,"clientTime":1471578086000,"createTime":1471578087000,"updateTime":"","text":"啊哈哈哈哈哈","isReply":0,"lastId":"","momentId":2},{"id":4,"userId":2,"isDelete":0,"clientTime":1471578288000,"createTime":1471578289000,"updateTime":null,"text":"啊哈哈哈哈哈","isReply":0,"lastId":null,"momentId":2},{"id":5,"userId":2,"isDelete":0,"clientTime":1471579461000,"createTime":1471579461000,"updateTime":null,"text":"啊哈哈哈哈哈","isReply":0,"lastId":null,"momentId":2},{"id":6,"userId":2,"isDelete":0,"clientTime":1471579519000,"createTime":1471579519000,"updateTime":null,"text":"啊哈哈哈哈哈","isReply":0,"lastId":null,"momentId":2},{"id":7,"userId":2,"isDelete":0,"clientTime":1471582231000,"createTime":1471582232000,"updateTime":null,"text":"啊哈哈哈哈哈","isReply":0,"lastId":null,"momentId":2},{"id":9,"userId":2,"isDelete":0,"clientTime":1471582666000,"createTime":1471582667000,"updateTime":null,"text":"啊哈哈哈哈哈","isReply":0,"lastId":null,"momentId":2},{"id":10,"userId":2,"isDelete":0,"clientTime":1471583889000,"createTime":1471583890000,"updateTime":null,"text":"啊哈哈哈哈哈","isReply":0,"lastId":null,"momentId":2},{"id":11,"userId":2,"isDelete":0,"clientTime":1471584354000,"createTime":1471584355000,"updateTime":null,"text":"啊哈哈哈哈哈","isReply":0,"lastId":null,"momentId":2},{"id":12,"userId":2,"isDelete":0,"clientTime":1471584644000,"createTime":1471584645000,"updateTime":null,"text":"啊哈哈哈哈哈","isReply":0,"lastId":null,"momentId":2}]
     * sightUserIds : null
     * praiseUserIds :
     * joinUserIds : null
     * transmitCount : 0
     * user : null
     * notice :
     * forbidden : null
     */
    @PrimaryKey
    private int id;
    private int userId;
    private long createTime;
    private String text;
    private String image;
    private String location;
    private int isHeadline;
    private String personLimit;
    private String updateTime;
    private long clientTime;
    private String originalId;
    private int isTransimit;
    private int category;
    private Integer sight;
    private String price;
    private String dorm;
    private int isDelete;
    private String validationDate;
    private String oprations;
    private String sightUserIds;
    private String praiseUserIds;
    private String joinUserIds;
    private String transmitCount;
    private User user;
    private String notice;
    private String forbidden;
    private long serverTime;
    private String planName;
    private String collectStatus;
    private Integer status;
    private String groupNo;
    /**
     * id : 2
     * userId : 2
     * isDelete : 0
     * clientTime : 1471542075000
     * createTime : 1471542075000
     * updateTime :
     * text : 啊哈哈哈哈哈
     * isReply : 0
     * lastId :
     * momentId : 2
     */

    private RealmList<CommentsBean> comments;
    private RealmList<CommentsBean> cdto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getText() {
        return TextUtils.isEmpty(text) ? "" : text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getIsHeadline() {
        return isHeadline;
    }

    public void setIsHeadline(int isHeadline) {
        this.isHeadline = isHeadline;
    }

    public String getPersonLimit() {
        if ((!TextUtils.isEmpty(personLimit)) && personLimit.equals("0")) {
            return null;
        }
        return personLimit;
    }

    public void setPersonLimit(String personLimit) {
        this.personLimit = personLimit;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public long getClientTime() {
        return clientTime;
    }

    public void setClientTime(long clientTime) {
        this.clientTime = clientTime;
    }

    public String getOriginalId() {
        return originalId;
    }

    public void setOriginalId(String originalId) {
        this.originalId = originalId;
    }

    public int getIsTransimit() {
        return isTransimit;
    }

    public void setIsTransimit(int isTransimit) {
        this.isTransimit = isTransimit;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public Integer getSight() {
        return sight;
    }

    public void setSight(Integer sight) {
        this.sight = sight;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDorm() {
        return dorm;
    }

    public void setDorm(String dorm) {
        this.dorm = dorm;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(String validationDate) {
        this.validationDate = validationDate;
    }

    public String getOprations() {
        return oprations;
    }

    public void setOprations(String oprations) {
        this.oprations = oprations;
    }

    public String getSightUserIds() {
        return sightUserIds;
    }

    public void setSightUserIds(String sightUserIds) {
        this.sightUserIds = sightUserIds;
    }

    public String getPraiseUserIds() {
        return praiseUserIds;
    }

    public void setPraiseUserIds(String praiseUserIds) {
        this.praiseUserIds = praiseUserIds;
    }

    public String getJoinUserIds() {
        return joinUserIds;
    }

    public void setJoinUserIds(String joinUserIds) {
        this.joinUserIds = joinUserIds;
    }

    public String getTransmitCount() {
        return TextUtils.isEmpty(transmitCount) ? "0" : transmitCount;
    }

    public void setTransmitCount(String transmitCount) {
        this.transmitCount = transmitCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getForbidden() {
        return forbidden;
    }

    public void setForbidden(String forbidden) {
        this.forbidden = forbidden;
    }

    public RealmList<CommentsBean> getComments() {
        return cdto;
    }

    public void setComments(RealmList<CommentsBean> comments) {
        this.comments = comments;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public String getPlanName() {
        return TextUtils.isEmpty(planName) ? "" : planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getCollectStatus() {
        return collectStatus;
    }

    public void setCollectStatus(String collectStatus) {
        this.collectStatus = collectStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public RealmList<CommentsBean> getCdto() {
        return cdto;
    }

    public void setCdto(RealmList<CommentsBean> cdto) {
        this.cdto = cdto;
    }

    public ArrayList<String> getPraisePeopleOnlyIds() {
        return getOnlyIds(getPraiseUserIds());
    }

    public ArrayList<String> getJoinPeopleOnlyIds() {
        return getOnlyIds(getJoinUserIds());
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    /*筛选出id*/
    public ArrayList<String> getOnlyIds(String id_names) {
        if (TextUtils.isEmpty(id_names)) {
            return null;
        } else {
            ArrayList<String> resluts = new ArrayList<>();
            for (String i : id_names.split(NS.SPLIT)) {
                String[] id_name = i.split(NS.SPLIT2);
                if (id_name.length >= 2) {
                    resluts.add(id_name[0]);
                }
            }
            return resluts;
        }
    }

    //获取加入计划的人数
    public int getJoinCount() {
        if (TextUtils.isEmpty(getJoinUserIds())) {
            return 0;
        } else {
            return getJoinPeopleOnlyIds().size();
        }
    }

    //获取点赞人数
    public int getPraiseCount() {
        if (TextUtils.isEmpty(getPraiseUserIds())) {
            return 0;
        } else {
            return getPraisePeopleOnlyIds().size();
        }
    }

    //是否收藏
    public boolean isCollected() {
        if (TextUtils.isEmpty(getCollectStatus())) {
            return false;
        } else {
            return getCollectStatus().equals("1");
        }
    }

    //是否未结束
    public boolean isAlive() {
        if (getStatus() == null) {
            return false;
        }
        return getStatus().equals(1);
    }
}
