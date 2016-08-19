package com.xiaoshangxing.xiaoshang.schoolcalender.addEntranceFragment;

/**
 * Created by quchwe on 2016/7/22 0022.
 */

import android.content.Context;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Bean;
import com.xiaoshangxing.utils.normalUtils.ImageTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quchwe on 2016/7/10 0010.
 */
public class Presenter implements AddEntryceContract.Presenter {
    Context context;

    public Presenter(Context context, Bean b){
        this.context = context;
    }

    List<ManagerInfo> managerInfoList = new ArrayList<>();
    @Override
    public List<ManagerInfo> getManagerList() {

        ManagerInfo managerInfo=new ManagerInfo();
        managerInfo.setBitmap(ImageTools.drawableToBitmap(context.getResources().getDrawable(R.mipmap.cirecleimage_default)));
        managerInfo.setName("孙路阳");
        managerInfo.setPosion("学生会主席");
        managerInfoList.add(managerInfo);
        return managerInfoList;
    }

}
