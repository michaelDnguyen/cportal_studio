package com.dlvn.mcustomerportal.services.model.claims;

import com.dlvn.mcustomerportal.services.model.JsonDataInput;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CPGetClaimIDRequest extends JsonDataInput {

    @SerializedName("Project")
    @Expose
    private String project;
    @SerializedName("Authentication")
    @Expose
    private String authentication;
    @SerializedName("DeviceId")
    @Expose
    private String deviceId;
    @SerializedName("DeviceToken")
    @Expose
    private String deviceToken;
    @SerializedName("OS")
    @Expose
    private String OS;
    @SerializedName("UserLogin")
    @Expose
    private String userLogin;
    @SerializedName("ClientID")
    @Expose
    private String clientID;
    @SerializedName("PolicyNo")
    @Expose
    private String policyNo;
    @SerializedName("ClaimType")
    @Expose
    private String claimType;
    @SerializedName("ClaimSubType")
    @Expose
    private String claimSubType;
    @SerializedName("ClaimID")
    @Expose
    private String claimID;

    @SerializedName("SubmissionID")
    @Expose
    private String submissionID;
    @SerializedName("APIToken")
    @Expose
    private String aPIToken;

    @SerializedName("DocProcessID")
    @Expose
    private String docProcessID;

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getAPIToken() {
        return aPIToken;
    }

    public void setAPIToken(String aPIToken) {
        this.aPIToken = aPIToken;
    }

    public String getOS() {
        return OS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public String getClaimID() {
        return claimID;
    }

    public void setClaimID(String claimID) {
        this.claimID = claimID;
    }

    public String getClaimSubType() {
        return claimSubType;
    }

    public void setClaimSubType(String claimSubType) {
        this.claimSubType = claimSubType;
    }

    public String getDocProcessID() {
        return docProcessID;
    }

    public void setDocProcessID(String docProcessID) {
        this.docProcessID = docProcessID;
    }

    public String getSubmissionID() {
        return submissionID;
    }

    public void setSubmissionID(String submissionID) {
        this.submissionID = submissionID;
    }
}
