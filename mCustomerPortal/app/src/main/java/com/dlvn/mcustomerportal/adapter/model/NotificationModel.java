package com.dlvn.mcustomerportal.adapter.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model data of notification
 *
 * @author nn.tai
 * @date Dec 18, 2017
 */
public class NotificationModel implements Parcelable {

    int ID;
    String title;
    String content;
    String createDate;
    String readDate;
    boolean isRead;

    public NotificationModel() {
        super();
        // TODO Auto-generated constructor stub
    }


    public NotificationModel(String title, String content, String createDate, boolean read) {
        super();
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.isRead = read;
    }


    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getReadDate() {
        return readDate;
    }

    public void setReadDate(String readDate) {
        this.readDate = readDate;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public NotificationModel(Parcel in) {
        this.ID = in.readInt();
        this.title = in.readString();
        this.content = in.readString();
        this.createDate = in.readString();
        this.readDate = in.readString();
        this.isRead = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(createDate);
        dest.writeString(readDate);
        dest.writeByte((byte) (isRead ? 1 : 0));
    }

    public static final Creator<NotificationModel> CREATOR = new Creator<NotificationModel>() {
        @Override
        public NotificationModel createFromParcel(Parcel source) {
            return new NotificationModel(source);
        }

        @Override
        public NotificationModel[] newArray(int size) {
            return new NotificationModel[size];
        }
    };
}
