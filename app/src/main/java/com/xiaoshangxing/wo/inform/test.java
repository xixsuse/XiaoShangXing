package com.xiaoshangxing.wo.inform;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.school_circle.Item_Comment;
import com.xiaoshangxing.utils.school_circle.PraisePeople;

/**
 * Created by FengChaoQun
 * on 2016/7/1
 */
public class test extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        LinearLayout myViewGroup=(LinearLayout) findViewById(R.id.comments);

        PraisePeople praisePeople=new PraisePeople(this);
        praisePeople.addName("吴天阳");
        praisePeople.addName("吴天阳");
        praisePeople.addName("吴天阳");
        praisePeople.addName("吴天阳");
        praisePeople.addName("吴天阳");
        praisePeople.addName("吴天阳");

        Item_Comment item_comment=new Item_Comment(this,"冯超群","吴天阳","啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦");


        myViewGroup.addView(praisePeople.getTextView());
        myViewGroup.addView(item_comment.getTextView());

    }


}
