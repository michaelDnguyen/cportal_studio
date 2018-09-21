package com.dlvn.mcustomerportal.services.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author nn.tai
 * @create Nov 6, 2017
 */
public class User implements Parcelable {

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
    private String email;
    private String gender;
    private String address;
    private String fullname;
    private String dayOfbirth;
    private String phone;

    private String linkFacebook;
    private String linkGmail;

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
     * @return The aPIToken
     */
    public String getAPIToken() {
        return aPIToken;
    }

    /**
     * @param aPIToken The APIToken
     */
    public void setAPIToken(String aPIToken) {
        this.aPIToken = aPIToken;
    }

    /**
     * @return The authStatus
     */
    public String getAuthStatus() {
        return authStatus;
    }

    /**
     * @param authStatus The AuthStatus
     */
    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    /**
     * @return The insightURL
     */
    public String getInsightURL() {
        return insightURL;
    }

    /**
     * @param insightURL The InsightURL
     */
    public void setInsightURL(String insightURL) {
        this.insightURL = insightURL;
    }

    /**
     * @return The level
     */
    public String getLevel() {
        return level;
    }

    /**
     * @param level The Level
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * @return The region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region The Region
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return The responseMsg
     */
    public String getResponseMsg() {
        return responseMsg;
    }

    /**
     * @param responseMsg The ResponseMsg
     */
    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    /**
     * @return The teamNo
     */
    public String getTeamNo() {
        return teamNo;
    }

    /**
     * @param teamNo The TeamNo
     */
    public void setTeamNo(String teamNo) {
        this.teamNo = teamNo;
    }

    /**
     * @return The userID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * @param userID The UserID
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * @return The userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName The UserName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return The userStatus
     */
    public String getUserStatus() {
        return userStatus;
    }

    /**
     * @param userStatus The UserStatus
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDayOfbirth() {
        return dayOfbirth;
    }

    public void setDayOfbirth(String dayOfbirth) {
        this.dayOfbirth = dayOfbirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLinkFacebook() {
        return linkFacebook;
    }

    public void setLinkFacebook(String linkFacebook) {
        this.linkFacebook = linkFacebook;
    }

    public String getLinkGmail() {
        return linkGmail;
    }

    public void setLinkGmail(String linkGmail) {
        this.linkGmail = linkGmail;
    }

    public User() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userID);
        dest.writeString(userName);
        dest.writeString(password);
        dest.writeString(email);
        dest.writeString(fullname);
        dest.writeString(gender);
        dest.writeString(dayOfbirth);
        dest.writeString(phone);

        dest.writeString(linkFacebook);
        dest.writeString(linkGmail);
    }

    public User(Parcel in) {
        userID = in.readString();
        userName = in.readString();
        password = in.readString();
        email = in.readString();
        fullname = in.readString();
        gender = in.readString();
        dayOfbirth = in.readString();
        phone = in.readString();

        linkFacebook = in.readString();
        linkGmail = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
