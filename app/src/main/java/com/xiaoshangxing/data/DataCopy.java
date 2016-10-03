package com.xiaoshangxing.data;

/**
 * Created by FengChaoQun
 * on 2016/9/11
 */
public class DataCopy {

    public static User copyUser(User user) {
        User user1 = new User();
        user1.setId(user.getId());
        user1.setUsername(user.getUsername());
        user1.setPhone(user.getPhone());
        user1.setArea(user.getArea());
        user1.setHometown(user.getHometown());
        user1.setSignature(user.getSignature());
        user1.setPhotoCover(user.getPhotoCover());
        user1.setIsVip(user.getIsVip());
        user1.setIsActive(user.getIsActive());
        user1.setSex(user.getSex());
        user1.setUserImage(user.getUserImage());
        user1.setActiveStatus(user.getActiveStatus());
        user1.setEmail(user.getEmail());
        user1.setServerTime(user.getServerTime());
        user1.setIsClass(user.getIsClass());
        user1.setIsCollege(user.getIsCollege());
        user1.setIsGrade(user.getIsGrade());
        return user1;
    }

    public static Published copyPublished(Published published) {
        Published published1 = new Published();
        published1.setId(published.getId());
        published1.setUserId(published.getUserId());
        published1.setCreateTime(published.getCreateTime());
        published1.setText(published.getText());
        published1.setImage(published.getImage());
        published1.setLocation(published.getLocation());
        published1.setIsHeadline(published.getIsHeadline());
        published1.setPersonLimit(published.getPersonLimit());
        published1.setUpdateTime(published.getUpdateTime());
        published1.setClientTime(published.getClientTime());
        published1.setOriginalId(published.getOriginalId());
        published1.setIsTransimit(published.getIsTransimit());
        published1.setCategory(published.getCategory());
        published1.setSight(published.getSight());
        published1.setPrice(published.getPrice());
        published1.setDorm(published.getDorm());
        published1.setIsDelete(published.getIsDelete());
        published1.setValidationDate(published.getValidationDate());
        published1.setOprations(published.getOprations());
        published1.setSightUserIds(published.getSightUserIds());
        published1.setPraiseUserIds(published.getPraiseUserIds());
        published1.setJoinUserIds(published.getJoinUserIds());
        published1.setTransmitCount(published.getTransmitCount());
        published1.setUser(published.getUser());
        published1.setComments(published.getComments());
        published1.setCdto(published.getCdto());
        return published1;
    }
}
