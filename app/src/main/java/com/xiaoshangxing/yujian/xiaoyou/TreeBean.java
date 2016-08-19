package com.xiaoshangxing.yujian.xiaoyou;


import android.graphics.Bitmap;

import com.xiaoshangxing.yujian.xiaoyou.tree.TreeNodeBitmap;
import com.xiaoshangxing.yujian.xiaoyou.tree.TreeNodeId;
import com.xiaoshangxing.yujian.xiaoyou.tree.TreeNodeLabel2;
import com.xiaoshangxing.yujian.xiaoyou.tree.TreeNodeLabel1;
import com.xiaoshangxing.yujian.xiaoyou.tree.TreeNodePid;

public class TreeBean {
    @TreeNodeId
    private int _id;
    @TreeNodePid
    private int parentId;
    @TreeNodeLabel1
    private String name1;
    @TreeNodeLabel2
    private String name2;
    @TreeNodeBitmap
    private Bitmap bitmap;

    public TreeBean(int _id, int parentId, String name1, String name2, Bitmap bitmap) {
        super();
        this._id = _id;
        this.parentId = parentId;
        this.name1 = name1;
        this.name2 = name2;
        this.bitmap = bitmap;
    }

    public TreeBean(int _id, int parentId, String name1) {
        super();
        this._id = _id;
        this.parentId = parentId;
        this.name1 = name1;
    }

}
