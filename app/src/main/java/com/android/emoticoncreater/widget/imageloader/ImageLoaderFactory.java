package com.android.emoticoncreater.widget.imageloader;

/**
 * 加载图片工厂类
 */

public class ImageLoaderFactory {

    private static volatile ImageLoaderWrapper sInstance;

    private ImageLoaderFactory() {

    }

    /**
     * 获取图片加载器
     */
    public static ImageLoaderWrapper getLoader() {
        if (sInstance == null) {
            synchronized (ImageLoaderFactory.class) {
                if (sInstance == null) {
                    sInstance = new ImageLoader();
                }
            }
        }
        return sInstance;
    }

}
