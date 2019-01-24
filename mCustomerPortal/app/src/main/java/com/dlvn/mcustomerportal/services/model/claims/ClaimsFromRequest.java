package com.dlvn.mcustomerportal.services.model.claims;

import android.os.Parcel;
import android.os.Parcelable;

import com.dlvn.mcustomerportal.services.model.JsonDataInput;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClaimsFromRequest extends JsonDataInput implements Parcelable {

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
    @SerializedName("ClaimType")
    @Expose
    private String claimType;
    @SerializedName("ClaimSubType")
    @Expose
    private String claimSubType;
    @SerializedName("ClaimName")
    @Expose
    private String claimName;
    @SerializedName("UserLogin")
    @Expose
    private String userLogin;
    @SerializedName("ClientID")
    @Expose
    private String clientID;
    @SerializedName("LIClientID")
    @Expose
    private String liClientID;
    @SerializedName("SubmissionID")
    @Expose
    private String submissionID;
    @SerializedName("PolicyNo")
    @Expose
    private String policyNo;
    @SerializedName("FullName")
    @Expose
    private String fullName;
    @SerializedName("Relationship")
    @Expose
    private String relationship;
    @SerializedName("IDNum")
    @Expose
    private String iDNum;
    @SerializedName("LIIDNum")
    @Expose
    private String LIiDNum;
    @SerializedName("DateIssueOfIDNum")
    @Expose
    private String dateIssueOfIDNum;
    @SerializedName("PlaceIssueOfIDNum")
    @Expose
    private String placeIssueOfIDNum;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("HomePhone")
    @Expose
    private String homePhone;
    @SerializedName("CellPhone")
    @Expose
    private String cellPhone;
    @SerializedName("PaymentMethod")
    @Expose
    private String paymentMethod;
    @SerializedName("BankName")
    @Expose
    private String bankName;
    @SerializedName("BankBranch")
    @Expose
    private String bankBranch;
    @SerializedName("AccountName")
    @Expose
    private String accountName;
    @SerializedName("AccountNumber")
    @Expose
    private String accountNumber;
    @SerializedName("LIFullName")
    @Expose
    private String lIFullName;
    @SerializedName("AliasName")
    @Expose
    private String aliasName;
    @SerializedName("PermanentAddress")
    @Expose
    private String permanentAddress;
    @SerializedName("WorkPlace")
    @Expose
    private String workPlace;
    @SerializedName("Occupation")
    @Expose
    private String occupation;
    @SerializedName("MedicInsurance")
    @Expose
    private String medicInsurance;
    @SerializedName("RegistrationPlace")
    @Expose
    private String registrationPlace;
    @SerializedName("OtherClaim")
    @Expose
    private String otherClaim;
    /**
     * Add field
     */
    @SerializedName("LifeInsuredID")
    @Expose
    private String lifeInsuredID;
    @SerializedName("HCClaimAmt")
    @Expose
    private String hCClaimAmt;
    @SerializedName("HCClaimAmtChar")
    @Expose
    private String hCClaimAmtChar;
    @SerializedName("PaymentPolicy")
    @Expose
    private String paymentPolicy;
    @SerializedName("PayIssueID")
    @Expose
    private String payIssueID;
    @SerializedName("PayDateIssue")
    @Expose
    private String payDateIssue;
    @SerializedName("PayPlaceIsssue")
    @Expose
    private String payPlaceIsssue;
    /**
     * End Add field
     */
    @SerializedName("ClaimDeath")
    @Expose
    private List<ClaimsItem> claimDeath = null;
    @SerializedName("TreatmentHistorys")
    @Expose
    private List<TreatmentHistory> treatmentHistorys = null;

    /**
     * for Supplement
     *
     * @return
     */
    @SerializedName("DocTypeComments")
    @Expose
    private List<DocTypeComment> docTypeComments = null;

    /**
     * Additional for check duplicate
     *
     * @return
     */
    @SerializedName("LossDate")
    @Expose
    private String dateLoss;

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

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public String getClaimSubType() {
        return claimSubType;
    }

    public void setClaimSubType(String claimSubType) {
        this.claimSubType = claimSubType;
    }

    public String getClaimName() {
        return claimName;
    }

    public void setClaimName(String claimName) {
        this.claimName = claimName;
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

    public String getLiClientID() {
        return liClientID;
    }

    public void setLiClientID(String liClientID) {
        this.liClientID = liClientID;
    }

    public String getSubmissionID() {
        return submissionID;
    }

    public void setSubmissionID(String submissionID) {
        this.submissionID = submissionID;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getIDNum() {
        return iDNum;
    }

    public void setIDNum(String iDNum) {
        this.iDNum = iDNum;
    }

    public String getLIiDNum() {
        return LIiDNum;
    }

    public void setLIiDNum(String LIiDNum) {
        this.LIiDNum = LIiDNum;
    }

    public String getDateIssueOfIDNum() {
        return dateIssueOfIDNum;
    }

    public void setDateIssueOfIDNum(String dateIssueOfIDNum) {
        this.dateIssueOfIDNum = dateIssueOfIDNum;
    }

    public String getPlaceIssueOfIDNum() {
        return placeIssueOfIDNum;
    }

    public void setPlaceIssueOfIDNum(String placeIssueOfIDNum) {
        this.placeIssueOfIDNum = placeIssueOfIDNum;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getLIFullName() {
        return lIFullName;
    }

    public void setLIFullName(String lIFullName) {
        this.lIFullName = lIFullName;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getMedicInsurance() {
        return medicInsurance;
    }

    public void setMedicInsurance(String medicInsurance) {
        this.medicInsurance = medicInsurance;
    }

    public String getRegistrationPlace() {
        return registrationPlace;
    }

    public void setRegistrationPlace(String registrationPlace) {
        this.registrationPlace = registrationPlace;
    }

    public String getOtherClaim() {
        return otherClaim;
    }

    public void setOtherClaim(String otherClaim) {
        this.otherClaim = otherClaim;
    }

    public String getLifeInsuredID() {
        return lifeInsuredID;
    }

    public void setLifeInsuredID(String lifeInsuredID) {
        this.lifeInsuredID = lifeInsuredID;
    }

    public String getHCClaimAmt() {
        return hCClaimAmt;
    }

    public void setHCClaimAmt(String hCClaimAmt) {
        this.hCClaimAmt = hCClaimAmt;
    }

    public String getHCClaimAmtChar() {
        return hCClaimAmtChar;
    }

    public void setHCClaimAmtChar(String hCClaimAmtChar) {
        this.hCClaimAmtChar = hCClaimAmtChar;
    }

    public String getPaymentPolicy() {
        return paymentPolicy;
    }

    public void setPaymentPolicy(String paymentPolicy) {
        this.paymentPolicy = paymentPolicy;
    }

    public String getPayIssueID() {
        return payIssueID;
    }

    public void setPayIssueID(String payIssueID) {
        this.payIssueID = payIssueID;
    }

    public String getPayDateIssue() {
        return payDateIssue;
    }

    public void setPayDateIssue(String payDateIssue) {
        this.payDateIssue = payDateIssue;
    }

    public String getPayPlaceIsssue() {
        return payPlaceIsssue;
    }

    public void setPayPlaceIsssue(String payPlaceIsssue) {
        this.payPlaceIsssue = payPlaceIsssue;
    }

    public List<ClaimsItem> getClaimDeath() {
        return claimDeath;
    }

    public void setClaimDeath(List<ClaimsItem> claimDeath) {
        this.claimDeath = claimDeath;
    }

    public List<TreatmentHistory> getTreatmentHistorys() {
        return treatmentHistorys;
    }

    public void setTreatmentHistorys(List<TreatmentHistory> treatmentHistorys) {
        this.treatmentHistorys = treatmentHistorys;
    }

    public String getDateLoss() {
        return dateLoss;
    }

    public void setDateLoss(String dateLoss) {
        this.dateLoss = dateLoss;
    }

    /**
     * For Supplement Claim Hold
     */
    public List<DocTypeComment> getDocTypeComments() {
        return docTypeComments;
    }

    public void setDocTypeComments(List<DocTypeComment> docTypeComments) {
        this.docTypeComments = docTypeComments;
    }

    /**
     * Contructor for new Objective
     */
    public ClaimsFromRequest() {
        super();
        claimDeath = Collections.emptyList();
        treatmentHistorys = Collections.emptyList();
    }

    public ClaimsFromRequest(Parcel in) {
        claimType = in.readString();
        claimName = in.readString();
        submissionID = in.readString();
        policyNo = in.readString();

        clientID = in.readString();
        liClientID = in.readString();
        fullName = in.readString();
        lIFullName = in.readString();
        iDNum = in.readString();
        LIiDNum = in.readString();
        relationship = in.readString();
        dateIssueOfIDNum = in.readString();
        placeIssueOfIDNum = in.readString();
        address = in.readString();
        homePhone = in.readString();
        cellPhone = in.readString();
        email = in.readString();

        paymentMethod = in.readString();
        bankName = in.readString();
        bankBranch = in.readString();
        accountName = in.readString();
        accountNumber = in.readString();
        aliasName = in.readString();

        permanentAddress = in.readString();
        workPlace = in.readString();
        occupation = in.readString();
        medicInsurance = in.readString();
        registrationPlace = in.readString();
        otherClaim = in.readString();

        lifeInsuredID = in.readString();
        hCClaimAmt = in.readString();
        hCClaimAmtChar = in.readString();
        paymentPolicy = in.readString();
        payIssueID = in.readString();
        payDateIssue = in.readString();
        payPlaceIsssue = in.readString();

        claimDeath = new ArrayList<>();
        in.readList(claimDeath, ClaimsItem.class.getClassLoader());
        treatmentHistorys = new ArrayList<>();
        in.readList(treatmentHistorys, TreatmentHistory.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(claimType);
        dest.writeString(claimName);
        dest.writeString(submissionID);
        dest.writeString(policyNo);

        dest.writeString(clientID);
        dest.writeString(liClientID);
        dest.writeString(fullName);
        dest.writeString(lIFullName);
        dest.writeString(iDNum);
        dest.writeString(LIiDNum);
        dest.writeString(relationship);
        dest.writeString(dateIssueOfIDNum);
        dest.writeString(placeIssueOfIDNum);
        dest.writeString(address);
        dest.writeString(homePhone);
        dest.writeString(cellPhone);
        dest.writeString(email);

        dest.writeString(paymentMethod);
        dest.writeString(bankName);
        dest.writeString(bankBranch);
        dest.writeString(accountName);
        dest.writeString(accountNumber);
        dest.writeString(aliasName);

        dest.writeString(permanentAddress);
        dest.writeString(workPlace);
        dest.writeString(occupation);
        dest.writeString(medicInsurance);
        dest.writeString(registrationPlace);
        dest.writeString(otherClaim);

        dest.writeString(lifeInsuredID);
        dest.writeString(hCClaimAmt);
        dest.writeString(hCClaimAmtChar);
        dest.writeString(paymentPolicy);
        dest.writeString(payIssueID);
        dest.writeString(payDateIssue);
        dest.writeString(payPlaceIsssue);

        dest.writeList(claimDeath);
        dest.writeList(treatmentHistorys);
    }

    public static final Creator<ClaimsFromRequest> CREATOR = new Creator<ClaimsFromRequest>() {
        @Override
        public ClaimsFromRequest createFromParcel(Parcel source) {
            return new ClaimsFromRequest(source);
        }

        @Override
        public ClaimsFromRequest[] newArray(int size) {
            return new ClaimsFromRequest[size];
        }
    };
}
