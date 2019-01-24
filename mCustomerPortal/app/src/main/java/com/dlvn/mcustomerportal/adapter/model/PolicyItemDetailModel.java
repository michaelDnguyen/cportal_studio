package com.dlvn.mcustomerportal.adapter.model;

/**
 * Model data item Home Page
 * @author nn.tai
 * @date Dec 18, 2017
 */
public class PolicyItemDetailModel {

	String _title;
	String _content;
	String imgPath;

	//for policy pending
	String actionPolicy = "";
	String issueCreDt, issueUpDt;

	public PolicyItemDetailModel(String _title, String _content, String imgPath) {
		super();
		this._title = _title;
		this._content = _content;
		this.imgPath = imgPath;
		this.actionPolicy = "";
		this.issueCreDt = null;
		this.issueUpDt = null;
	}

	public PolicyItemDetailModel(String title, String content, String action, String creDt, String upDt){
		this._title = title;
		this._content = content;
		this.imgPath = null;
		this.actionPolicy = action;
		this.issueCreDt = creDt;
		this.issueUpDt = upDt;
	}

	public PolicyItemDetailModel() {
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

	/**
	 * Policy Pending
	 * @return
	 */
	public String getActionPolicy() {
		return actionPolicy;
	}

	public void setActionPolicy(String actionPolicy) {
		this.actionPolicy = actionPolicy;
	}

	public String getIssueCreDt() {
		return issueCreDt;
	}

	public void setIssueCreDt(String issueCreDt) {
		this.issueCreDt = issueCreDt;
	}

	public String getIssueUpDt() {
		return issueUpDt;
	}

	public void setIssueUpDt(String issueUpDt) {
		this.issueUpDt = issueUpDt;
	}
}
