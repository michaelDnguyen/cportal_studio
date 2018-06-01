package com.dlvn.mcustomerportal.services.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author nn.tai
 * @create Nov 6, 2017
 */
public class User {

	public static final String USER_NAME = "USER_NAME";
	public static final String USER_ID = "USER_ID";
	public static final String PASSWORD = "PASSWORD";
	public static final String TOKEN_LOGIN = "TOKEN_LOGIN";

	public static final String USER_CONTRACT = "USER_CONTRACT";
	public static final String USER_POINT = "USER_POINT";
	public static final String USER_AMOUNT = "USER_AMOUNT";
	public static final String USER_PROPOSAL = "USER_PROPOSAL";

	@SerializedName("APIToken")
	@Expose
	private String aPIToken;

	@SerializedName("AuthStatus")
	@Expose
	private String authStatus;

	@SerializedName("InsightURL")
	@Expose
	private String insightURL;

	@SerializedName("Level")
	@Expose
	private String level;

	@SerializedName("Region")
	@Expose
	private String region;

	@SerializedName("ResponseMsg")
	@Expose
	private String responseMsg;

	@SerializedName("TeamNo")
	@Expose
	private String teamNo;

	@SerializedName("UserID")
	@Expose
	private String userID;

	@SerializedName("UserName")
	@Expose
	private String userName;

	@SerializedName("UserStatus")
	@Expose
	private String userStatus;

	private String password;

	private String proposalNo;
	private int numberContract;
	private long amountContract;
	private int point;

	public String getProposalNo() {
		return proposalNo;
	}

	public void setProposalNo(String proposalNo) {
		this.proposalNo = proposalNo;
	}

	public int getNumberContract() {
		return numberContract;
	}

	public void setNumberContract(int numberContract) {
		this.numberContract = numberContract;
	}

	public long getAmountContract() {
		return amountContract;
	}

	public void setAmountContract(long amountContract) {
		this.amountContract = amountContract;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 
	 * @return The aPIToken
	 */
	public String getAPIToken() {
		return aPIToken;
	}

	/**
	 * 
	 * @param aPIToken
	 *            The APIToken
	 */
	public void setAPIToken(String aPIToken) {
		this.aPIToken = aPIToken;
	}

	/**
	 * 
	 * @return The authStatus
	 */
	public String getAuthStatus() {
		return authStatus;
	}

	/**
	 * 
	 * @param authStatus
	 *            The AuthStatus
	 */
	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

	/**
	 * 
	 * @return The insightURL
	 */
	public String getInsightURL() {
		return insightURL;
	}

	/**
	 * 
	 * @param insightURL
	 *            The InsightURL
	 */
	public void setInsightURL(String insightURL) {
		this.insightURL = insightURL;
	}

	/**
	 * 
	 * @return The level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * 
	 * @param level
	 *            The Level
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * 
	 * @return The region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * 
	 * @param region
	 *            The Region
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * 
	 * @return The responseMsg
	 */
	public String getResponseMsg() {
		return responseMsg;
	}

	/**
	 * 
	 * @param responseMsg
	 *            The ResponseMsg
	 */
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	/**
	 * 
	 * @return The teamNo
	 */
	public String getTeamNo() {
		return teamNo;
	}

	/**
	 * 
	 * @param teamNo
	 *            The TeamNo
	 */
	public void setTeamNo(String teamNo) {
		this.teamNo = teamNo;
	}

	/**
	 * 
	 * @return The userID
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * 
	 * @param userID
	 *            The UserID
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * 
	 * @return The userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 
	 * @param userName
	 *            The UserName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 
	 * @return The userStatus
	 */
	public String getUserStatus() {
		return userStatus;
	}

	/**
	 * 
	 * @param userStatus
	 *            The UserStatus
	 */
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	@Override
	public String toString() {
		return "User [aPIToken=" + aPIToken + ", authStatus=" + authStatus + ", insightURL=" + insightURL + ", level="
				+ level + ", region=" + region + ", responseMsg=" + responseMsg + ", teamNo=" + teamNo + ", userID="
				+ userID + ", userName=" + userName + ", userStatus=" + userStatus + "]";
	}

}
