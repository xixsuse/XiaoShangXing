package com.xiaoshangxing.wo.myState.MyStateHodler;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.wo.myState.DetailsActivity.DetailsActivity;

/**
 * Created by FengChaoQun
 * on 2016/9/11
 */
public class Mystate_onlytext extends MyStateHodlerBase {
    private TextView onlyText;

    @Override
    public void inflate() {
        View.inflate(view.getContext(), R.layout.mystate_hodler_onlytext, content);
        onlyText = (TextView) view.findViewById(R.id.only_text);
    }

    @Override
    public void refresh(final Published published) {

        setPublished(published);

        refreshBase();

        onlyText.setText(published.getText());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(IntentStatic.DATA, published.getId());
                context.startActivity(intent);
            }
        });
    }
}
