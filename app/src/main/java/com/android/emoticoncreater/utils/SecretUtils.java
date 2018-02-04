package com.android.emoticoncreater.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.android.emoticoncreater.model.SecretBean;

import java.io.File;
import java.util.List;

/**
 * 告诉你个秘密工具类
 */

public class SecretUtils {

    private static final int padding = 20;//内边距
    private static final int pictureWidth = 500;//图片宽度
    private static final int pictureHeight = 268;//图片高度
    private static final int textSize = 40;//字体大小
    private static final int backgroundColor = 0xffffffff;
    private static final int textColor = 0xff010101;

    public static File createSecret(Resources resources, final List<SecretBean> secretList, final String savePath) {
        final Paint paint = new Paint();
        paint.reset();
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setFlags(Paint.FAKE_BOLD_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        final int totalWidth = padding + pictureWidth + padding;
        int totalHeight = 0;
        for (SecretBean secret : secretList) {
            totalHeight += padding;
            totalHeight += pictureHeight;
            totalHeight += padding;
            final String text = secret.getTitle();
            totalHeight += getTextHeight(paint, text);
            totalHeight += padding;
        }

        paint.reset();
        paint.setColor(backgroundColor);
        paint.setStyle(Paint.Style.FILL);

        final Bitmap picture = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
        final Rect background = new Rect(0, 0, totalWidth, totalHeight);
        final Canvas canvas = new Canvas(picture);
        canvas.drawRect(background, paint);

        paint.reset();
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setFlags(Paint.FAKE_BOLD_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        totalHeight = 0;
        for (SecretBean secret : secretList) {
            final String text = secret.getTitle();
            final int resourceId = secret.getResourceId();

            totalHeight += padding;

            drawBitmap(resources, canvas, resourceId, totalHeight);

            totalHeight += pictureHeight;
            totalHeight += padding;

            final Rect textRect = new Rect();
            paint.getTextBounds(text, 0, text.length(), textRect);

            final int textWidth = textRect.right;
            final int maxTextWidth = pictureWidth - padding * 2;

            if (textWidth <= maxTextWidth) {
                final float textLeft = (pictureWidth - textWidth) / 2f + padding;
                final float textTop = totalHeight - textRect.top;
                canvas.drawText(text, textLeft, textTop, paint);
                totalHeight += textSize;
            } else {
                final float line = textWidth / (float) maxTextWidth;
                final int count = (int) (text.length() / line);
                final String text1 = text.substring(0, count);
                final String text2 = text.substring(count, text.length());

                drawText(canvas, paint, text1, totalHeight);
                totalHeight += textSize;

                drawText(canvas, paint, text2, totalHeight);
                totalHeight += padding / 2 + textSize;
            }

            totalHeight += padding;
        }

        final String imageName = System.currentTimeMillis() + ".jpg";
        final File newFile = ImageUtils.saveBitmapToJpg(picture, savePath, imageName);
        picture.recycle();

        return newFile;
    }

    private static void drawText(Canvas canvas, Paint paint, String text, int top) {
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

    private static int getTextHeight(Paint paint, String text) {
        final Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);
        final int textWidth = textRect.right;
        final int maxTextWidth = pictureWidth - padding * 2;
        if (textWidth <= maxTextWidth) {
            return textSize;
        } else {
            return 2 * textSize + padding / 2;
        }
    }
}
