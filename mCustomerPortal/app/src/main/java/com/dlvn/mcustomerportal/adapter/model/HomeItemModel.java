package com.dlvn.mcustomerportal.adapter.model;

/**
 * Model data item Home Page
 * @author nn.tai
 * @date Dec 18, 2017
 */
public class HomeItemModel {

	String _title;
	String _content;
	String imgPath;

	public HomeItemModel(String _title, String _content, String imgPath) {
		super();
		this._title = _title;
		this._content = _content;
		this.imgPath = imgPath;
	}

	public HomeItemModel() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

}
