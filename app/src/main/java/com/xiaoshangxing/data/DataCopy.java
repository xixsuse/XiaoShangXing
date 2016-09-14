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
}
