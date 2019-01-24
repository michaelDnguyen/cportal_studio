package com.dlvn.mcustomerportal.adapter.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetailModel implements Parcelable {

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("ThumbnailURL")
    @Expose
    private String thumbnailURL;
    @SerializedName("ContentURL")
    @Expose
    private String contentURL;
    @SerializedName("PublishDate")
    @Expose
    private String publishDate;
    @SerializedName("Description")
    @Expose
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getContentURL() {
        return contentURL;
    }

    public void setContentURL(String contentURL) {
        this.contentURL = contentURL;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    protected ProductDetailModel(Parcel in) {
        title = in.readString();
        thumbnailURL = in.readString();
        contentURL = in.readString();
        publishDate = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(thumbnailURL);
        dest.writeString(contentURL);
        dest.writeString(publishDate);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductDetailModel> CREATOR = new Creator<ProductDetailModel>() {
        @Override
        public ProductDetailModel createFromParcel(Parcel in) {
            return new ProductDetailModel(in);
        }

        @Override
        public ProductDetailModel[] newArray(int size) {
            return new ProductDetailModel[size];
        }
    };
}
