package com.xiaoshangxing.wo.WoFrafment.WoViewHolder;

import com.xiaoshangxing.data.bean.Published;

/**
 * Created by FengChaoQun
 * on 2016/9/10
 */
public class WoNoImages extends WoBaseHolder {

//    public WoNoImages(Context context, Published published, BaseActivity activity, Realm realm, WoFragment woFragment) {
//        super(context, published, activity, realm, woFragment);
//    }

    @Override
    public void inflate() {
    }

    @Override
    public void refresh(Published item) {
        setPublished(item);
        refreshBase();
    }

}
