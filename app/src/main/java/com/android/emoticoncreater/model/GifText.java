package com.android.emoticoncreater.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Gif 文字类
 */
public class GifText implements Parcelable {

    private long id;//文字ID
    private String hint;//提示内容
    private String text;//文字内容
    private int startFrame;//开始帧
    private int endFrame;//结束帧

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getStartFrame() {
        return startFrame;
    }

    public void setStartFrame(int startFrame) {
        this.startFrame = startFrame;
    }

    public int getEndFrame() {
        return endFrame;
    }

    public void setEndFrame(int endFrame) {
        this.endFrame = endFrame;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.hint);
        dest.writeString(this.text);
        dest.writeInt(this.startFrame);
        dest.writeInt(this.endFrame);
    }

    public GifText() {
    }

    protected GifText(Parcel in) {
        this.id = in.readLong();
        this.hint = in.readString();
        this.text = in.readString();
        this.startFrame = in.readInt();
        this.endFrame = in.readInt();
    }

    public static final Creator<GifText> CREATOR = new Creator<GifText>() {
        @Override
        public GifText createFromParcel(Parcel source) {
            return new GifText(source);
        }

        @Override
        public GifText[] newArray(int size) {
            return new GifText[size];
        }
    };
}
