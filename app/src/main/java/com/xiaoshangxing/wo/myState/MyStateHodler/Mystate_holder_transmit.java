package com.xiaoshangxing.wo.myState.MyStateHodler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.utils.layout.CirecleImage;

/**
 * Created by FengChaoQun
 * on 2016/9/11
 */
public class Mystate_holder_transmit extends MyStateHodlerBase {
    private TextView shareText, text;
    private ImageView good;
    private CirecleImage head;

    @Override
    public void inflate() {
        View.inflate(view.getContext(), R.layout.mystae_holder_transmit, content);
        shareText = (TextView) view.findViewById(R.id.share_text);
        text = (TextView) view.findViewById(R.id.good_describe);
        good = (ImageView) view.findViewById(R.id.good_image);
        head = (CirecleImage) view.findViewById(R.id.person_image);
    }

    @Override
    public void refresh(Published published) {
        refreshBase();
        shareText.setText(published.getText());
    }
}
