package com.android.emoticoncreater.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.io.File;

/**
 * 图片旋转处理工具类
 */

public class EmotionCreateUtils {

    private static final int backgroundWidth = 640;
    private static final int backgroundHeight = 300;
    private static final int backgroundColor = 0xffffffff;
    private static final int textColor = 0xff0B0B0B;
    private static final int textSize = 30;
    private static final int pictureWidth = 200;
    private static final int pictureHeight = 200;
    private static final int textHeight = 50;
    private static final int padding = 10;

    public static File createExpression(final String title,
                                        final String path1,
                                        final String path2,
                                        final String path3,
                                        final String name1,
                                        final String name2,
                                        final String name3,
                                        final String savePath) {

        final Paint paint = new Paint();
        paint.setColor(backgroundColor);
        paint.setStyle(Paint.Style.FILL);

        final Bitmap picture = Bitmap.createBitmap(backgroundWidth, backgroundHeight, Bitmap.Config.ARGB_8888);
        final Rect background = new Rect(0, 0, backgroundWidth, backgroundHeight);

        final Canvas canvas = new Canvas(picture);
        canvas.drawRect(background, paint);

        final int left1 = padding;
        final int left2 = pictureWidth + padding * 2;
        final int left3 = (pictureWidth + padding) * 2 + padding;

        final boolean ignoreSecond = path1.equals(path2);
        final boolean ignoreThird = path1.equals(path3);

        final Bitmap bitmap1 = getBitmapByFilePath(path1);
        drawBitmap(canvas, bitmap1, left1, left1 + pictureWidth);
        if (ignoreSecond) {
            drawBitmap(canvas, bitmap1, left2, left2 + pictureWidth);
        }
        if (ignoreThird) {
            drawBitmap(canvas, bitmap1, left3, left3 + pictureWidth);
        }
        bitmap1.recycle();

        if (!ignoreSecond) {
            final Bitmap bitmap2 = getBitmapByFilePath(path2);
            drawBitmap(canvas, bitmap2, left2, left2 + pictureWidth);
            bitmap2.recycle();
        }
        if (!ignoreThird) {
            final Bitmap bitmap3 = getBitmapByFilePath(path3);
            drawBitmap(canvas, bitmap3, left3, left3 + pictureWidth);
            bitmap3.recycle();
        }

        paint.reset();
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setFlags(Paint.FAKE_BOLD_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        final Rect titleRect = new Rect();
        paint.getTextBounds(title, 0, title.length(), titleRect);
        final float titleLeft = (backgroundWidth - titleRect.right) / 2f;
        final float titleTop = (textHeight - textSize) / 2f - titleRect.top;
        canvas.drawText(title, titleLeft, titleTop, paint);

        drawText(canvas, paint, name1, left1);
        drawText(canvas, paint, name2, left2);
        drawText(canvas, paint, name3, left3);

        final String imageName = System.currentTimeMillis() + ".jpg";
        final File newFile = ImageUtils.saveBitmapToJpg(picture, savePath, imageName);
        picture.recycle();

        return newFile;
    }

    private static Bitmap getBitmapByFilePath(String path) {
        final Bitmap bitmap = BitmapFactory.decodeFile(path);
        final int bitmapWidth = bitmap.getWidth();
        final int bitmapHeight = bitmap.getHeight();

        if (bitmapWidth < pictureWidth || bitmapHeight < pictureHeight) {
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

    private static void drawBitmap(Canvas canvas, Bitmap bitmap, int left, int right) {
        final Rect pictureRect = new Rect(0, 0, pictureWidth, pictureHeight);
        final RectF dst = new RectF(left, textHeight, right, pictureHeight + textHeight);
        canvas.drawBitmap(bitmap, pictureRect, dst, null);
    }

    private static void drawText(Canvas canvas, Paint paint, String text, int left) {
        final Rect nameRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), nameRect);
        final float nameTop = textHeight + pictureHeight + (textHeight - textSize) / 2f - nameRect.top;
        final float nameLeft1 = (pictureWidth - nameRect.right) / 2f + left;
        canvas.drawText(text, nameLeft1, nameTop, paint);
    }
}
