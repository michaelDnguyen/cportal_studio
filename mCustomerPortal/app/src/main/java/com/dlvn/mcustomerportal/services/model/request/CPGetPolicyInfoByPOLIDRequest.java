package com.dlvn.mcustomerportal.services.model.request;

import com.dlvn.mcustomerportal.services.model.JsonDataInput;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CPGetPolicyInfoByPOLIDRequest extends JsonDataInput {

    @SerializedName("Project")
    @Expose
    private String project;
    @SerializedName("APIToken")
    @Expose
    private String aPIToken;
    @SerializedName("Authentication")
    @Expose
    private String authentication;
    @SerializedName("DeviceId")
    @Expose
    private String deviceId;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("Action")
    @Expose
    private String action;
    @SerializedName("DeviceToken")
    @Expose
    private String deviceToken;
    @SerializedName("OS")
    @Expose
    private String OS;
    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("PolID")
    @Expose
    private String polID;
    @SerializedName("POID")
    @Expose
    private String poID;
    @SerializedName("DOB")
    @Expose
    private String doB;


    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getAPIToken() {
        return aPIToken;
    }

    public void setAPIToken(String aPIToken) {
        this.aPIToken = aPIToken;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPolID() {
        return polID;
    }

    public void setPolID(String polID) {
        this.polID = polID;
    }

    public String getOS() {
        return OS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }

    public String getPoID() {
        return poID;
    }

    public void setPoID(String poID) {
        this.poID = poID;
    }

    public String getDoB() {
        return doB;
    }

    public void setDoB(String doB) {
        this.doB = doB;
    }
}
