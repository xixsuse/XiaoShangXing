package com.xiaoshangxing.xiaoshang;

import android.support.annotation.IntDef;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by quchwe on 2016/8/16 0016.
 */

public class RecyclerViewUtil {
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_NORMAL = 2;
    public static final int TYPE_FOOTER = 3;

    /** @hide */
    @IntDef({TYPE_FOOTER, TYPE_HEADER, TYPE_NORMAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ItemType {}

    public interface OnItemClickListener{
        void onItemClickListener(View v, int position);
        void onItemLongClickListener(View v, int position);
    }
}
