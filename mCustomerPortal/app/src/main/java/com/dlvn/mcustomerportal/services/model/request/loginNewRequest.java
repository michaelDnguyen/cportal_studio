package com.dlvn.mcustomerportal.services.model.request;

import com.dlvn.mcustomerportal.services.model.JsonDataInput;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class loginNewRequest extends JsonDataInput {

    @SerializedName("Action")
    @Expose
    private String action;
    @SerializedName("UserLogin")
    @Expose
    private String userLogin;
    @SerializedName("ClientID")
    @Expose
    private String clientID;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("DeviceId")
    @Expose
    private String deviceID;
    @SerializedName("DeviceToken")
    @Expose
    private String deviceToken;
    @SerializedName("APIToken")
    @Expose
    private String apiToken;
    @SerializedName("OS")
    @Expose
    private String oS;
    @SerializedName("Project")
    @Expose
    private String project;
    @SerializedName("POID")
    @Expose
    private String pOID;
    @SerializedName("PODOB")
    @Expose
    private String pODOB;
    @SerializedName("PolicyNo")
    @Expose
    private String policyNo;
    @SerializedName("ProposalNo")
    @Expose
    private String proposalNo;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("FullName")
    @Expose
    private String fullName;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("HomePhone")
    @Expose
    private String homePhone;
    @SerializedName("CellPhone")
    @Expose
    private String cellPhone;
    @SerializedName("BusinessPhone")
    @Expose
    private String businessPhone;
    @SerializedName("LinkFB")
    @Expose
    private String linkFB;
    @SerializedName("LinkGmail")
    @Expose
    private String linkGMail;
    @SerializedName("Authentication")
    @Expose
    private String authentication;
    @SerializedName("OTP")
    @Expose
    private String otp;

    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("2FA")
    @Expose
    private String twoFA;
    @SerializedName("UserName")
    @Expose
    private String userName;


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getOS() {
        return oS;
    }

    public void setOS(String oS) {
        this.oS = oS;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getPOID() {
        return pOID;
    }

    public void setPOID(String pOID) {
        this.pOID = pOID;
    }

    public String getPODOB() {
        return pODOB;
    }

    public void setPODOB(String pODOB) {
        this.pODOB = pODOB;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public String getLinkFB() {
        return linkFB;
    }

    public void setLinkFB(String linkFB) {
        this.linkFB = linkFB;
    }

    public String getLinkGMail() {
        return linkGMail;
    }

    public void setLinkGMail(String linkGMail) {
        this.linkGMail = linkGMail;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProposalNo() {
        return proposalNo;
    }

    public void setProposalNo(String proposalNo) {
        this.proposalNo = proposalNo;
    }

    public String getTwoFA() {
        return twoFA;
    }

    public void setTwoFA(String twoFA) {
        this.twoFA = twoFA;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
