package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class loginResult {

	@SerializedName("AuthStatus")
	@Expose
	private String authStatus;
	@SerializedName("UserID")
	@Expose
	private String userID;
	@SerializedName("Level")
	@Expose
	private String level;
	@SerializedName("UserName")
	@Expose
	private String userName;
	@SerializedName("UserStatus")
	@Expose
	private String userStatus;
	@SerializedName("ResponseMsg")
	@Expose
	private String responseMsg;
	@SerializedName("TeamNo")
	@Expose
	private String teamNo;
	@SerializedName("Region")
	@Expose
	private String region;
	@SerializedName("APIToken")
	@Expose
	private String aPIToken;
	@SerializedName("InsightURL")
	@Expose
	private String insightURL;

	public String getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public String getTeamNo() {
		return teamNo;
	}

	public void setTeamNo(String teamNo) {
		this.teamNo = teamNo;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getAPIToken() {
		return aPIToken;
	}

	public void setAPIToken(String aPIToken) {
		this.aPIToken = aPIToken;
	}

	public String getInsightURL() {
		return insightURL;
	}

	public void setInsightURL(String insightURL) {
		this.insightURL = insightURL;
	}
}
