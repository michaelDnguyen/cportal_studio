package com.dlvn.mcustomerportal.services.model.claims;

import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.services.model.JsonDataInput;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadClaimImageRequest extends JsonDataInput {

    @SerializedName("APIToken")
    @Expose
    private String aPIToken;
    @SerializedName("DeviceID")
    @Expose
    private String deviceID;
    @SerializedName("DeviceName")
    @Expose
    private String deviceName;
    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("ProposalID")
    @Expose
    private String proposalID;
    @SerializedName("SubmissionID")
    @Expose
    private String SubmissionID;
    @SerializedName("DocProcessID")
    @Expose
    private String docProcessID;
    @SerializedName("DocTypeID")
    @Expose
    private String docTypeID;
    @SerializedName("DocTypeName")
    @Expose
    private String docTypeName;
    @SerializedName("DocNumber")
    @Expose
    private String docNumber;
    @SerializedName("NumberOfPage")
    @Expose
    private String NumberOfPage;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("Project")
    @Expose
    private String project = Constant.Project_ID;
    @SerializedName("Authentication")
    @Expose
    private String authentication;
    @SerializedName("OS")
    @Expose
    private String OS;
    @SerializedName("ClientID")
    @Expose
    private String clientID;

    public String getAPIToken() {
        return aPIToken;
    }

    public void setAPIToken(String aPIToken) {
        this.aPIToken = aPIToken;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProposalID() {
        return proposalID;
    }

    public void setProposalID(String proposalID) {
        this.proposalID = proposalID;
    }

    public String getDocProcessID() {
        return docProcessID;
    }

    public void setDocProcessID(String docProcessID) {
        this.docProcessID = docProcessID;
    }

    public String getDocTypeID() {
        return docTypeID;
    }

    public void setDocTypeID(String docTypeID) {
        this.docTypeID = docTypeID;
    }

    public String getDocTypeName() {
        return docTypeName;
    }

    public void setDocTypeName(String docTypeName) {
        this.docTypeName = docTypeName;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public String getNumberOfPage() {
        return NumberOfPage;
    }

    public void setNumberOfPage(String numberOfPage) {
        NumberOfPage = numberOfPage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSubmissionID() {
        return SubmissionID;
    }

    public void setSubmissionID(String submissionID) {
        SubmissionID = submissionID;
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
}
