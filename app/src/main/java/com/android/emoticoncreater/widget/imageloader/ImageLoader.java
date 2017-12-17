package com.android.emoticoncreater.widget.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.utils.GlideApp;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * 加载图片实现
 */

public class ImageLoader implements ImageLoaderWrapper {

    private final int mPlaceHolder = R.mipmap.ic_launcher;
    private final int mErrorImage = R.mipmap.ic_launcher;

    @Override
    public void loadImage(Context context, ImageView imageView, String url) {
        loadImage(context, imageView, url, mPlaceHolder, mErrorImage);
    }

    @Override
    public void loadImage(Context context, ImageView imageView, String url, int placeholder, int errorImage) {
        loadImage(context, imageView, url, placeholder, errorImage, null);
    }

    @Override
    public void loadImage(Context context, ImageView imageView, String url, BitmapTransformation transformation) {
        loadImage(context, imageView, url, mPlaceHolder, mErrorImage, transformation);
    }

    @Override
    public void loadImage(Context context, ImageView imageView, String url, int placeholder, int errorImage, BitmapTransformation transformation) {
        GlideApp.with(context)
                .load(url)
                .placeholder(placeholder)
                .error(errorImage)
                .dontAnimate()
                .into(imageView);
    }

    @Override
    public void loadImageFitCenter(Context context, ImageView imageView, String url) {
        loadImageFitCenter(context, imageView, url, mPlaceHolder, mErrorImage);
    }

    @Override
    public void loadImageFitCenter(Context context, ImageView imageView, String url, int placeholder, int errorImage) {
        loadImageFitCenter(context, imageView, url, placeholder, errorImage, null);
    }

    @Override
    public void loadImageFitCenter(Context context, ImageView imageView, String url, BitmapTransformation transformation) {
        loadImageFitCenter(context, imageView, url, mPlaceHolder, mErrorImage, transformation);
    }

    @Override
    public void loadImageFitCenter(Context context, ImageView imageView, String url, int placeholder, int errorImage, BitmapTransformation transformation) {
        GlideApp.with(context)
                .load(url)
                .placeholder(placeholder)
                .error(errorImage)
                .fitCenter()
                .dontAnimate()
                .into(imageView);
    }

    @Override
    public void loadImageCenterCrop(Context context, ImageView imageView, String url) {
        loadImageCenterCrop(context, imageView, url, mPlaceHolder, mErrorImage);
    }

    @Override
    public void loadImageCenterCrop(Context context, ImageView imageView, String url, int placeholder, int errorImage) {
        loadImageCenterCrop(context, imageView, url, placeholder, errorImage, null);
    }

    @Override
    public void loadImageCenterCrop(Context context, ImageView imageView, String url, BitmapTransformation transformation) {
        loadImageCenterCrop(context, imageView, url, mPlaceHolder, mErrorImage, transformation);
    }

    @Override
    public void loadImageCenterCrop(Context context, ImageView imageView, String url, int placeholder, int errorImage, BitmapTransformation transformation) {
        GlideApp.with(context)
                .load(url)
                .placeholder(placeholder)
                .error(errorImage)
                .centerCrop()
                .dontAnimate()
                .into(imageView);
    }
}
