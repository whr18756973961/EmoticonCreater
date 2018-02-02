package com.android.emoticoncreater.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 告诉你个秘密
 */

public class SecretBean implements Parcelable {

    private int resourceId;
    private String title;

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.resourceId);
        dest.writeString(this.title);
    }

    public SecretBean() {
    }

    protected SecretBean(Parcel in) {
        this.resourceId = in.readInt();
        this.title = in.readString();
    }

    public static final Creator<SecretBean> CREATOR = new Creator<SecretBean>() {
        @Override
        public SecretBean createFromParcel(Parcel source) {
            return new SecretBean(source);
        }

        @Override
        public SecretBean[] newArray(int size) {
            return new SecretBean[size];
        }
    };
}
