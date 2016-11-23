package com.xiaoshangxing.yujian.Schoolfellow.ItemBean;

/**
 * Created by FengChaoQun
 * on 2016/10/22
 */

public class MateItem extends BaseItemBean {
    private String userName;
    private String signature;
    private String userImage;

    public MateItem(String id, BaseItemBean parent, String userName, String userImage, String signature) {
        super(id, parent, BaseItemBean.PERSON);
        this.userName = userName;
        this.userImage = userImage;
        this.signature = signature;
    }

    @Override
    public String getImage() {
        return userImage;
    }

    @Override
    public String getShowName() {
        return userName;
    }

    @Override
    public String getExText() {
        return signature;
    }
}
