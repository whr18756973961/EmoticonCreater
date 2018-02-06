package com.android.emoticoncreater.config;

import java.io.File;

/**
 * 常量
 */

public class Constants {

    private static final String PATH_DCIM = File.separator + "DCIM" + File.separator;
    public static final String PATH_BASE = PATH_DCIM + "表情包生成器" + File.separator;
    public static final String PATH_TRIPLE_SEND = PATH_BASE + "表情三连发" + File.separator;
    public static final String PATH_SECRET = PATH_BASE + "告诉你个秘密" + File.separator;
    public static final String PATH_USED_PICTURE = PATH_BASE + "缓存" + File.separator;

    public static final String KEY_RETURN_DATA = "key_return_data";//ActivityResult

}
