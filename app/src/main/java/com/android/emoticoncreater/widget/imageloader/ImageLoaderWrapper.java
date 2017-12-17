package com.android.emoticoncreater.widget.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * 加载图片接口
 */

public interface ImageLoaderWrapper {

    void loadImage(Context context, ImageView imageView, String url);

    void loadImage(Context context, ImageView imageView, String url, int placeholder, int errorImage);

    void loadImage(Context context, ImageView imageView, String url, BitmapTransformation transformation);

    void loadImage(Context context, ImageView imageView, String url, int placeholder, int errorImage, BitmapTransformation transformation);

    void loadImageFitCenter(Context context, ImageView imageView, String url);

    void loadImageFitCenter(Context context, ImageView imageView, String url, int placeholder, int errorImage);

    void loadImageFitCenter(Context context, ImageView imageView, String url, BitmapTransformation transformation);

    void loadImageFitCenter(Context context, ImageView imageView, String url, int placeholder, int errorImage, BitmapTransformation transformation);

    void loadImageCenterCrop(Context context, ImageView imageView, String url);

    void loadImageCenterCrop(Context context, ImageView imageView, String url, int placeholder, int errorImage);

    void loadImageCenterCrop(Context context, ImageView imageView, String url, BitmapTransformation transformation);

    void loadImageCenterCrop(Context context, ImageView imageView, String url, int placeholder, int errorImage, BitmapTransformation transformation);
}
