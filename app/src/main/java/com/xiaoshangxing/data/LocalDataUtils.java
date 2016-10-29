package com.xiaoshangxing.data;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/10/27
 */

public class LocalDataUtils {

    public static void saveBackgroud(final String account, final String image, final boolean isAll, final Context context) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                /*
                **describe:设置全局背景
                */
                if (isAll) {
                    BackGround backGround = new BackGround();
                    backGround.setId(BackGround.DEFAULT);
                    backGround.setObjectId(BackGround.DEFAULT);
                    backGround.setSelfId(TempUser.getId());
                    backGround.setImgae(image);
                    realm.copyToRealmOrUpdate(backGround);

                    List<BackGround> backGrounds = realm.where(BackGround.class).findAll();
                    for (BackGround i : backGrounds) {
                        i.setImgae(image);
                    }
                } else {
                     /*
                    **describe:设置单个背景
                    */
                    BackGround backGround = new BackGround();
                    if (TextUtils.isEmpty(account)) {
                        backGround.setId(BackGround.DEFAULT);
                        backGround.setObjectId(BackGround.DEFAULT);
                    } else {
                        backGround.setId(TempUser.getId() + account);
                        backGround.setObjectId(account);
                    }
                    backGround.setSelfId(TempUser.getId());
                    backGround.setImgae(image);
                    realm.copyToRealmOrUpdate(backGround);
                }
                Toast.makeText(context, "应用成功", Toast.LENGTH_SHORT).show();
            }
        });

        realm.close();
    }

}
