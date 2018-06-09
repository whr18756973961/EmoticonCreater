package com.android.emoticoncreater.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Gif 主题类型
 */
public class GifTheme implements Parcelable {

    private long id;//主题ID
    private String name;//主题名称
    private String fileName;//主题文件名
    private List<GifText> textList;//文字列表

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<GifText> getTextList() {
        return textList;
    }

    public void setTextList(List<GifText> textList) {
        this.textList = textList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.fileName);
        dest.writeTypedList(this.textList);
    }

    public GifTheme() {
    }

    protected GifTheme(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.fileName = in.readString();
        this.textList = in.createTypedArrayList(GifText.CREATOR);
    }

    public static final Creator<GifTheme> CREATOR = new Creator<GifTheme>() {
        @Override
        public GifTheme createFromParcel(Parcel source) {
            return new GifTheme(source);
        }

        @Override
        public GifTheme[] newArray(int size) {
            return new GifTheme[size];
        }
    };
}
