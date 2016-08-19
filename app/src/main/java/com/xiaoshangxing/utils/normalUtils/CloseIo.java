package com.xiaoshangxing.utils.normalUtils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by quchwe on 2016/8/8 0008.
 */

public class CloseIo {

    public static void CloseIo(Closeable ...io) {
        for (Closeable i:io) {
         if (i!=null){
             try {
                 i.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
        }
    }
}
