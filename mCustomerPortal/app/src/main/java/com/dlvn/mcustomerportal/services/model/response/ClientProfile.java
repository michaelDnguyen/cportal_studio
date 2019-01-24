package com.dlvn.mcustomerportal.services.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.dlvn.mcustomerportal.services.model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author nn.tai
 * @see 05-07-2018 contain info of user client
 */
public class ClientProfile implements Parcelable {

    public static final String USER_NAME = "USER_NAME";
    public static final String USER_ID = "USER_ID";
    public static final String PASSWORD = "PASSWORD";
    public static final String TOKEN_LOGIN = "TOKEN_LOGIN";

    public static final String USER_POINT = "USER_POINT";
    public static final String USER_RANK = "USER_RANK";
    public static final String USER_AMOUNT = "USER_AMOUNT";

    public static final String FULLNAME = "USER_FULLNAME";
    public static final String GENDER = "USER_GENDER";
    public static final String ADDRESS = "USER_ADDRESS";
    public static final String EMAIL = "USER_EMAIL";
    public static final String PHONE_NUMBER = "USER_PHONE_NUMBER";
    public static final String FACEBOOK = "USER_FACEBOOK";
    public static final String GOOGLE = "USER_GOOGLE";
    public static final String DAYOFBIRTH = "USER_DOB";
    public static final String USER_POID = "USER_POID";
    public static final String USER_PHOTO = "USER_PHOTO";

    public static final String VERYFY_CELLPHONE = "VERYFY_CELLPHONE";
    public static final String VERYFY_EMAIL = "VERYFY_EMAIL";
    public static final String VERYFY_TWOFA = "VERYFY_TWOFA";

    @SerializedName("ClientID")
    @Expose
    private String clientID;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("APIToken")
    @Expose
    private String aPIToken;
    @SerializedName("AuthStatus")
    @Expose
    private String authStatus;
    @SerializedName("LoginName")
    @Expose
    private String loginName;

    @SerializedName("DOB")
    @Expose
    private String dOB;

    @SerializedName("POID")
    @Expose
    private String pOID;
    @SerializedName("PODOB")
    @Expose
    private String poDOB;
    @SerializedName("PolicyNo")
    @Expose
    private String policyNo;

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

    @SerializedName("PasswordModifiedDate")
    @Expose
    private String passwordModifyDate;
    @SerializedName("MaritalStatus")
    @Expose
    private String maritalStatus;
    @SerializedName("AgreedToTermsOfUse")
    @Expose
    private String agreeToTerms;

    @SerializedName("VerifyCellPhone")
    @Expose
    private String verifyCellPhone;
    @SerializedName("VerifyEmail")
    @Expose
    private String verifyEmail;
    @SerializedName("TwoFA")
    @Expose
    private String verifyTwoFA;

    /**
     * Password encrypt
     */
    @SerializedName("PasswordEncrypted")
    @Expose
    private String passwordEncrypted;

    /**
     * Linked DLVN Account
     */
    @SerializedName("UserName")
    @Expose
    private String userName;

    private String profilePhoto = "";

    /**
     * For Claims
     * @return
     */
    @SerializedName("ClaimType")
    @Expose
    private String claimsType;
    @SerializedName("ClaimName")
    @Expose
    private String claimsName;
    @SerializedName("PolicyID")
    @Expose
    private String policyID;
    @SerializedName("ProductCode")
    @Expose
    private String productCode;

    public String getClientID() {
        return clientID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getPoDOB() {
        return poDOB;
    }

    public void setPoDOB(String poDOB) {
        this.poDOB = poDOB;
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

    public String getaPIToken() {
        return aPIToken;
    }

    public void setaPIToken(String aPIToken) {
        this.aPIToken = aPIToken;
    }

    public String getPasswordModifyDate() {
        return passwordModifyDate;
    }

    public void setPasswordModifyDate(String passwordModifyDate) {
        this.passwordModifyDate = passwordModifyDate;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getAgreeToTerms() {
        return agreeToTerms;
    }

    public void setAgreeToTerms(String agreeToTerms) {
        this.agreeToTerms = agreeToTerms;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    /**
     * For get notification/mail box
     *
     * @return
     */
    @SerializedName("UserLogin")
    @Expose
    private String userLogin;
    @SerializedName("Subject")
    @Expose
    private String subject;
    @SerializedName("Content")
    @Expose
    private String content;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * End notifications/mailbox
     */

    public String getVerifyCellPhone() {
        return verifyCellPhone;
    }

    public void setVerifyCellPhone(String verifyCellPhone) {
        this.verifyCellPhone = verifyCellPhone;
    }

    public String getVerifyEmail() {
        return verifyEmail;
    }

    public void setVerifyEmail(String verifyEmail) {
        this.verifyEmail = verifyEmail;
    }

    public String getVerifyTwoFA() {
        return verifyTwoFA;
    }

    public void setVerifyTwoFA(String verifyTwoFA) {
        this.verifyTwoFA = verifyTwoFA;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClaimsType() {
        return claimsType;
    }

    public void setClaimsType(String claimsType) {
        this.claimsType = claimsType;
    }

    public String getClaimsName() {
        return claimsName;
    }

    public void setClaimsName(String claimsName) {
        this.claimsName = claimsName;
    }

    public String getPolicyID() {
        return policyID;
    }

    public void setPolicyID(String policyID) {
        this.policyID = policyID;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public ClientProfile() {

    }

    /**
     * Implements method of Parcelable
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(clientID);
        dest.writeString(userName);
        dest.writeString(password);
        dest.writeString(aPIToken);
        dest.writeString(loginName);
        dest.writeString(gender);
        dest.writeString(fullName);
        dest.writeString(address);
        dest.writeString(cellPhone);
        dest.writeString(email);
        dest.writeString(dOB);
        dest.writeString(linkFaceBook);
        dest.writeString(linkGmail);
        dest.writeString(passwordModifyDate);
        dest.writeString(maritalStatus);
        dest.writeString(pOID);
        dest.writeString(poDOB);
        dest.writeString(policyNo);
        dest.writeString(authStatus);
        dest.writeString(verifyCellPhone);
        dest.writeString(verifyEmail);
        dest.writeString(verifyTwoFA);

        dest.writeString(claimsType);
        dest.writeString(claimsName);
        dest.writeString(policyID);
        dest.writeString(productCode);
    }

    public ClientProfile(Parcel in) {

        clientID = in.readString();
        userName = in.readString();
        password = in.readString();
        aPIToken = in.readString();
        loginName = in.readString();
        gender = in.readString();
        fullName = in.readString();
        address = in.readString();
        cellPhone = in.readString();
        email = in.readString();
        dOB = in.readString();
        linkFaceBook = in.readString();
        linkGmail = in.readString();
        passwordModifyDate = in.readString();
        maritalStatus = in.readString();
        pOID = in.readString();
        poDOB = in.readString();
        policyNo = in.readString();
        authStatus = in.readString();
        verifyCellPhone = in.readString();
        verifyEmail = in.readString();
        verifyTwoFA = in.readString();

        claimsType = in.readString();
        claimsName = in.readString();
        policyID = in.readString();
        productCode = in.readString();
    }

    public static final Creator<ClientProfile> CREATOR = new Creator<ClientProfile>() {
        @Override
        public ClientProfile createFromParcel(Parcel source) {
            return new ClientProfile(source);
        }

        @Override
        public ClientProfile[] newArray(int size) {
            return new ClientProfile[size];
        }
    };

}
