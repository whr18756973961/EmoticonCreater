package com.android.emoticoncreater.utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.android.emoticoncreater.model.GifText;
import com.android.emoticoncreater.model.GifTheme;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;

/**
 * Gif生成帮助类
 */
public class GifHelper {

    private static final int padding = 5;//内边距
    private static final int textColor = 0xfffafafa;

    public static File create(AssetManager assetManager, GifTheme theme, String savePath) {
        try {
            final String gifFileName = theme.getFileName();
            final float textSize = theme.getTextSize();
            final int duration = theme.getDuration();
            final List<GifText> textList = theme.getTextList();

            final GifDrawable drawable = new GifDrawable(assetManager, gifFileName);
            final int frames = drawable.getNumberOfFrames();
            final AnimatedGifEncoder encoder = new AnimatedGifEncoder();
            final ByteArrayOutputStream os = new ByteArrayOutputStream();

            final Paint paint = new Paint();

            encoder.setRepeat(0);
            encoder.setDelay(duration);
            encoder.start(os);

            for (int i = 0; i < frames; i++) {
                final Bitmap bitmap = drawable.seekToFrameAndGet(i);
                if (textList != null && !textList.isEmpty()) {
                    for (GifText gifText : textList) {
                        final String text = gifText.getText();
                        final int startFrame = gifText.getStartFrame();
                        final int endFrame = gifText.getEndFrame();

                        if (i >= startFrame && i < endFrame) {
                            final Canvas canvas = new Canvas(bitmap);
                            final float textTop = bitmap.getHeight() - padding - textSize;
                            final int maxWidth = bitmap.getWidth() - padding * 2;

                            paint.reset();
                            paint.setColor(0xff000000);
                            paint.setTextSize(textSize + 0.2f);
                            paint.setFlags(Paint.FAKE_BOLD_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                            drawText(canvas, paint, text, textTop, maxWidth);
                            paint.reset();
                            paint.setColor(textColor);
                            paint.setTextSize(textSize);
                            paint.setFlags(Paint.FAKE_BOLD_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                            drawText(canvas, paint, text, textTop, maxWidth);
                            break;
                        }
                    }
                }

                encoder.addFrame(bitmap);
            }

            encoder.finish();
            drawable.recycle();

            final String fileName = System.currentTimeMillis() + ".gif";

            return ImageUtils.saveToGif(os, savePath, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void drawText(Canvas canvas, Paint paint, String text, float top, int maxWidth) {
        final Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);
        final int textWidth = textRect.right;

        final float textTop = top - textRect.top;
        final float textLeft = (maxWidth - textWidth) / 2f;
        canvas.drawText(text, textLeft, textTop, paint);
    }
}
