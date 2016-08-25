package com.xiaoshangxing.input_activity.Location;

/**
 * Created by tianyang on 2016/8/24.
 */
public class AddressBean {
    private String name;
    private String address;

    public AddressBean(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
