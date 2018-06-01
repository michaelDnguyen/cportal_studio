package com.dlvn.mcustomerportal.adapter.model;

/**
 * Model data Chuong trinh diem thuong
 * @author nn.tai
 * @date Dec 18, 2017
 */
public class BonusItemModel {

	String _title;
	String _content;
	int resourceID;

	public BonusItemModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BonusItemModel(String _title, String _content, int resourceID) {
		super();
		this._title = _title;
		this._content = _content;
		this.resourceID = resourceID;
	}

	public String get_title() {
		return _title;
	}

	public void set_title(String _title) {
		this._title = _title;
	}

	public String get_content() {
		return _content;
	}

	public void set_content(String _content) {
		this._content = _content;
	}

	public int getResourceID() {
		return resourceID;
	}

	public void setResourceID(int resourceID) {
		this.resourceID = resourceID;
	}

}
