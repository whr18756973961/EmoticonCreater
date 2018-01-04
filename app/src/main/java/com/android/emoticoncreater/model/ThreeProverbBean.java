package com.android.emoticoncreater.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * 怼人语录
 */

@Table("ThreeProverb")
public class ThreeProverbBean implements Parcelable{

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private long id;
    private String title;
    private String firstProverb;
    private String secondProverb;
    private String thirdProverb;
    private long useTimes = 0;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstProverb() {
        return firstProverb;
    }

    public void setFirstProverb(String firstProverb) {
        this.firstProverb = firstProverb;
    }

    public String getSecondProverb() {
        return secondProverb;
    }

    public void setSecondProverb(String secondProverb) {
        this.secondProverb = secondProverb;
    }

    public String getThirdProverb() {
        return thirdProverb;
    }

    public void setThirdProverb(String thirdProverb) {
        this.thirdProverb = thirdProverb;
    }

    public long getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(long useTimes) {
        this.useTimes = useTimes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.firstProverb);
        dest.writeString(this.secondProverb);
        dest.writeString(this.thirdProverb);
        dest.writeLong(this.useTimes);
    }

    public ThreeProverbBean() {
    }

    protected ThreeProverbBean(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.firstProverb = in.readString();
        this.secondProverb = in.readString();
        this.thirdProverb = in.readString();
        this.useTimes = in.readLong();
    }

    public static final Creator<ThreeProverbBean> CREATOR = new Creator<ThreeProverbBean>() {
        @Override
        public ThreeProverbBean createFromParcel(Parcel source) {
            return new ThreeProverbBean(source);
        }

        @Override
        public ThreeProverbBean[] newArray(int size) {
            return new ThreeProverbBean[size];
        }
    };
}
