package com.android.emoticoncreater.utils;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 图片工具类
 */

public class ImageUtils {

    public static File saveBitmapToJpg(Bitmap bitmap, String path, String bitName) {
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(path, bitName);
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

}
