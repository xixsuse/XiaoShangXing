package com.xiaoshangxing.wo.WoFrafment.WoViewHolder;

import com.xiaoshangxing.data.bean.Published;

/**
 * Created by FengChaoQun
 * on 2016/9/10
 */
public class WoNoImages extends WoBaseHolder {
    @Override
    public void inflate() {
    }

    @Override
    public void refresh(Published item) {
        setPublished(item);
        refreshBase();
    }

}
