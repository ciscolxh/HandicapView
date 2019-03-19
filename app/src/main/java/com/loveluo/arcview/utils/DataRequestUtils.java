package com.loveluo.arcview.utils;

import android.content.Context;


import java.io.InputStream;

/**
 * 模拟网络请求
 *
 * @author tifezh
 * @date 2017/7/3
 */

public class DataRequestUtils {


    public static String getStringFromAssert(Context context, String fileName) {
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            return new String(buffer, 0, buffer.length, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}


