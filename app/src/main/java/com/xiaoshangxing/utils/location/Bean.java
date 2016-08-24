package com.xiaoshangxing.utils.location;

/**
 * Created by tianyang on 2016/8/24.
 */
public class Bean {
    private String name;
    private String address;

    public Bean(String name, String address) {
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
