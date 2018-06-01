package com.dlvn.mcustomerportal.adapter.model;

/**
 * Model data of notification
 * @author nn.tai
 * @date Dec 18, 2017
 */
public class NotificationModel {

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
	

	public NotificationModel(String title, String createDate, boolean read) {
		super();
		this.title = title;
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

}
