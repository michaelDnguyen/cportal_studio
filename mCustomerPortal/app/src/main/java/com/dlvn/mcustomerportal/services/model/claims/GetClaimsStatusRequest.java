package com.dlvn.mcustomerportal.services.model.claims;

import com.dlvn.mcustomerportal.services.model.JsonDataInput;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetClaimsStatusRequest extends JsonDataInput {

    @SerializedName("APIToken")
    @Expose
    private String aPIToken;
    @SerializedName("Action")
    @Expose
    private String action;
    @SerializedName("Authentication")
    @Expose
    private String authentication;
    @SerializedName("DeviceId")
    @Expose
    private String deviceId;
    @SerializedName("Project")
    @Expose
    private String project;
    @SerializedName("OS")
    @Expose
    private String os;
    @SerializedName("UserLogin")
    @Expose
    private String userLogin;
    @SerializedName("ClientID")
    @Expose
    private String clientID;
    @SerializedName("SubmissionID")
    @Expose
    private String submissionID;

    public String getAPIToken() {
        return aPIToken;
    }

    public void setAPIToken(String aPIToken) {
        this.aPIToken = aPIToken;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
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

    public String getSubmissionID() {
        return submissionID;
    }

    public void setSubmissionID(String submissionID) {
        this.submissionID = submissionID;
    }
}
