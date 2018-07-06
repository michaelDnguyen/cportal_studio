package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author nn.tai
 * @see 05-07-2018 contain info of user client
 */
public class ClientProfile {

    @SerializedName("ClientID")
    @Expose
    private String clientID;
    @SerializedName("LoginName")
    @Expose
    private String loginName;
    @SerializedName("DOB")
    @Expose
    private String dOB;
    @SerializedName("POID")
    @Expose
    private String pOID;
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
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("LinkFaceBook")
    @Expose
    private String linkFaceBook;
    @SerializedName("LinkGmail")
    @Expose
    private String linkGmail;

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getDOB() {
        return dOB;
    }

    public void setDOB(String dOB) {
        this.dOB = dOB;
    }

    public String getPOID() {
        return pOID;
    }

    public void setPOID(String pOID) {
        this.pOID = pOID;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLinkFaceBook() {
        return linkFaceBook;
    }

    public void setLinkFaceBook(String linkFaceBook) {
        this.linkFaceBook = linkFaceBook;
    }

    public String getLinkGmail() {
        return linkGmail;
    }

    public void setLinkGmail(String linkGmail) {
        this.linkGmail = linkGmail;
    }
}
