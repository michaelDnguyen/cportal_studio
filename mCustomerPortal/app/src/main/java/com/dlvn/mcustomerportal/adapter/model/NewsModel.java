package com.dlvn.mcustomerportal.adapter.model;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * @author nn.tai
 * @date Jan 8, 2018
 * @Do TODO
 */
public class NewsModel implements Parcelable {

	String title;
	String link;
	String date;

	public static final Creator<NewsModel> CREATOR = new Creator<NewsModel>() {

		@Override
		public NewsModel[] newArray(int size) {
			// TODO Auto-generated method stub
			return new NewsModel[size];
		}

		@Override
		public NewsModel createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new NewsModel(source);
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(title);
		dest.writeString(link);
		dest.writeString(date);
	}

	public NewsModel(Parcel in) {
		title = in.readString();
		link = in.readString();
		date = in.readString();
	}

	public NewsModel(String title, String link, String date) {
		super();
		this.title = title;
		this.link = link;
		this.date = date;
	}

	public NewsModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
