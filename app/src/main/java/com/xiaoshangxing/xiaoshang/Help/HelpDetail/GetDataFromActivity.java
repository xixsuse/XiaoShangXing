package com.xiaoshangxing.xiaoshang.Help.HelpDetail;

import com.xiaoshangxing.publicActivity.inputActivity.InputBoxLayout;

/**
 * Created by FengChaoQun
 * on 2016/9/12
 */
public interface GetDataFromActivity {
    Object getData();

    InputBoxLayout getInputBox();

    void hideInputBox();

    void comment(int id);
}
