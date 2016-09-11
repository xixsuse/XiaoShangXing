package com.xiaoshangxing.wo.WoFrafment.WoViewHolder;

import android.view.View;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.layout.CirecleImage;

/**
 * Created by FengChaoQun
 * on 2016/9/10
 */
public class WoTrasnsmit extends WoBaseHolder {
    private CirecleImage head;
    private EmotinText text;

//    public WoTrasnsmit(Context context, Published published, BaseActivity activity, Realm realm, WoFragment woFragment) {
//        super(context, published, activity, realm, woFragment);
//    }

    @Override
    public void inflate() {
        View.inflate(view.getContext(), R.layout.wo_viewholder_transmit, content);
        head = (CirecleImage) view.findViewById(R.id.transmit_image);
        text = (EmotinText) view.findViewById(R.id.transmit_text);
    }

    @Override
    public void refresh(Published item) {
        setPublished(item);
        refreshBase();
    }
}
