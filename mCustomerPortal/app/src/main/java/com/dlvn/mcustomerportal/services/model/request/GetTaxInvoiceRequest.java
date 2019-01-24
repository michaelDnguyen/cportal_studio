package com.dlvn.mcustomerportal.services.model.request;

import com.dlvn.mcustomerportal.services.model.JsonDataInput;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTaxInvoiceRequest extends JsonDataInput {

    @SerializedName("APIToken")
    @Expose
    private String aPIToken;
    @SerializedName("Action")
    @Expose
    private String action;
    @SerializedName("Authentication")
    @Expose
    private String authentication;
    @SerializedName("ClientID")
    @Expose
    private String clientID;
    @SerializedName("DeviceId")
    @Expose
    private String deviceID;
    @SerializedName("DeviceToken")
    @Expose
    private String deviceToken;
    @SerializedName("OS")
    @Expose
    private String OS;
    @SerializedName("Project")
    @Expose
    private String project;
    @SerializedName("UserLogin")
    @Expose
    private String userLogin;
    @SerializedName("PolicyNo")
    @Expose
    private String PolicyNo;

    @SerializedName("InvoiceID")
    @Expose
    private String InvoiceID;
    @SerializedName("InvoiceSign")
    @Expose
    private String InvoiceSign;

    public String getAPIToken() {
        return aPIToken;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
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

    public String getOS() {
        return OS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }

    public String getPolicyNo() {
        return PolicyNo;
    }

    public void setPolicyNo(String policyNo) {
        PolicyNo = policyNo;
    }

    public String getInvoiceID() {
        return InvoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        InvoiceID = invoiceID;
    }

    public String getInvoiceSign() {
        return InvoiceSign;
    }

    public void setInvoiceSign(String invoiceSign) {
        InvoiceSign = invoiceSign;
    }
}
