package com.dlvn.mcustomerportal.services.model.request;

import com.dlvn.mcustomerportal.services.model.JsonDataInput;
import com.dlvn.mcustomerportal.services.model.response.PaymentDetailModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CPSubmitFormRequest extends JsonDataInput {

    @SerializedName("Action")
    @Expose
    private String action;
    @SerializedName("Active")
    @Expose
    private String active;
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
    @SerializedName("APIToken")
    @Expose
    private String aPIToken;
    @SerializedName("HomePhone")
    @Expose
    private String homePhone;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("ContactType")
    @Expose
    private String contactType;
    @SerializedName("ContactContent")
    @Expose
    private String contactContent;

    /**
     * For Alteration Form PO Info
     *
     * @return
     */
    @SerializedName("ContactPhone")
    @Expose
    private String contactPhone;
    @SerializedName("HomeAddress")
    @Expose
    private String homeAddress;
    @SerializedName("BusinessAddress")
    @Expose
    private String businessAddress;
    @SerializedName("FullName")
    @Expose
    private String fullName;

    @SerializedName("lstClient")
    @Expose
    List<PaymentDetailModel> lstClient;

    /**
     * For Update Notification Settings
     *
     * @return
     */
    @SerializedName("MessageCode")
    @Expose
    private String messageCode;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

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

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getContactContent() {
        return contactContent;
    }

    public void setContactContent(String contactContent) {
        this.contactContent = contactContent;
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

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public List<PaymentDetailModel> getLstClient() {
        return lstClient;
    }

    public void setLstClient(List<PaymentDetailModel> lstClient) {
        this.lstClient = lstClient;
    }
}
