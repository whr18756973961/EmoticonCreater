package com.android.emoticoncreater.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;

import com.android.emoticoncreater.model.PictureBean;

import java.io.File;

/**
 * 一个表情图标编辑帮助类
 */
public class OneEmoticonHelper {

    private static final int padding = 20;//内边距
    private static final int pictureWidth = 300;//图片宽度
    private static final int pictureHeight = 300;//图片高度
    private static final int textSize = 30;//字体大小
    private static final int backgroundColor = 0xffffffff;
    private static final int textColor = 0xff010101;

    public static File create(Resources resources, final PictureBean emoticon, final String savePath) {
        final String text = emoticon.getTitle();
        final int resourceId = emoticon.getResourceId();

        final Paint paint = new Paint();
        paint.reset();
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setFlags(Paint.FAKE_BOLD_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        final Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);
        final int maxTextWidth = pictureWidth;
        final int textWidth = textRect.right;
        final int textHeight = TextUtils.isEmpty(text) ? 0
                : (textWidth <= maxTextWidth ? textSize : 2 * textSize + padding / 2);

        final int totalWidth = padding + pictureWidth + padding;
        final int totalHeight = padding + pictureHeight + textHeight + padding;

        paint.reset();
        paint.setColor(backgroundColor);
        paint.setStyle(Paint.Style.FILL);

        final Bitmap picture = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
        final Rect background = new Rect(0, 0, totalWidth, totalHeight);
        final Canvas canvas = new Canvas(picture);
        canvas.drawRect(background, paint);

        drawBitmap(resources, canvas, resourceId, padding);

        if (textHeight > 0) {
            if (textWidth <= maxTextWidth) {
                drawText(canvas, paint, text, padding + pictureHeight);
            } else {
                final float line = textWidth / (float) maxTextWidth;
                final int count = (int) (text.length() / line);
                final String text1 = text.substring(0, count);
                final String text2 = text.substring(count, text.length());

                drawText(canvas, paint, text1, padding + pictureHeight);
                drawText(canvas, paint, text2, padding + pictureHeight + textSize + padding / 2);
            }
        }

        final String imageName = System.currentTimeMillis() + ".jpg";
        final File newFile = ImageUtils.saveBitmapToJpg(picture, savePath, imageName);
        picture.recycle();

        return newFile;
    }

    private static void drawText(Canvas canvas, Paint paint, String text, int top) {
        paint.reset();
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setFlags(Paint.FAKE_BOLD_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        final Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);
        final int textWidth = textRect.right;

        final float textTop = top - textRect.top;
        final float textLeft = (pictureWidth - textWidth) / 2f + padding;
        canvas.drawText(text, textLeft, textTop, paint);
    }

    private static void drawBitmap(Resources resources, Canvas canvas, int resourceId, int top) {
        final Bitmap bitmap = getBitmapByResourcesId(resources, resourceId);
        final Rect pictureRect = new Rect(0, 0, pictureWidth, pictureHeight);
        final RectF dst = new RectF(padding, top, pictureWidth + padding, top + pictureHeight);
        canvas.drawBitmap(bitmap, pictureRect, dst, null);
        bitmap.recycle();
    }

    private static Bitmap getBitmapByResourcesId(Resources resources, int resourceId) {
        final Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);
        final int bitmapWidth = bitmap.getWidth();
        final int bitmapHeight = bitmap.getHeight();

        if (bitmapWidth != pictureWidth || bitmapHeight != pictureHeight) {
            float scaleWidth = ((float) pictureWidth) / bitmapWidth;
            float scaleHeight = ((float) pictureHeight) / bitmapHeight;

            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);

            bitmap.recycle();

            return resizedBitmap;
        }

        return bitmap;
    }
}
