package com.dlvn.mcustomerportal.services.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CPPolicyInfo {
    /**
     * Action POINFO_ACTION_POLICYPRODUCT
     */
    @SerializedName("PolicyLIName")
    @Expose
    private String policyLIName;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("FaceAmount")
    @Expose
    private String faceAmount;
    @SerializedName("PolExpiryDate")
    @Expose
    private String polExpiryDate;
    @SerializedName("PolIssEffDate")
    @Expose
    private String polIssEffDate;

    /**
     * Action POINFO_ACTION_POLICYBENE, POINFO_ACTION_POLICYLIFEINSURED
     */
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
    @SerializedName("Note")
    @Expose
    private String note;

    /**
     * Action POINFO_ACTION_POLICYDETAIL
     */
    @SerializedName("PolicyID")
    @Expose
    private String policyID;
    @SerializedName("Frequency")
    @Expose
    private String frequency;
    @SerializedName("PolicyStatus")
    @Expose
    private String policyStatus;
    @SerializedName("PolMPremAmt")
    @Expose
    private String polMPremAmt;
    @SerializedName("PolSndryAmt")
    @Expose
    private String polSndryAmt;

    /**
     * Action POLICY_PAYMENT
     */
    @SerializedName("TotalDeposit")
    @Expose
    private String totalDeposit;
    @SerializedName("BasicPrem")
    @Expose
    private String basicPrem;
    @SerializedName("ExcessPrem")
    @Expose
    private String excessPrem;

    /**
     * Action POLICY_ANN
     */
    @SerializedName("PolAccountValue")
    @Expose
    private String polAccountValue;
    @SerializedName("AllocateMPrem")
    @Expose
    private String allocateMPrem;
    @SerializedName("ACFIP")
    @Expose
    private String aCFIP;
    @SerializedName("LoyaltyBonus")
    @Expose
    private String loyaltyBonus;
    @SerializedName("TotalDeduct")
    @Expose
    private String totalDeduct;
    @SerializedName("NetWithDrawal")
    @Expose
    private String netWithDrawal;
    @SerializedName("WdChargeAmt")
    @Expose
    private String wdChargeAmt;
    @SerializedName("PrincIntAmt")
    @Expose
    private String princIntAmt;

    /**
     * Action Agent
     */
    @SerializedName("PrimaryAgent")
    @Expose
    private String primaryAgent;
    @SerializedName("ServAgent")
    @Expose
    private String servAgent;
    @SerializedName("ContactEmail")
    @Expose
    private String contactEmail;

    /**
     * Action Policy Client
     */
    @SerializedName("ClientID")
    @Expose
    private String clientID;

    /**
     * For Action POINFO_ACTION_POLICYPRODUCT
     *
     * @return
     */
    public String getPolicyLIName() {
        return policyLIName;
    }

    public void setPolicyLIName(String policyLIName) {
        this.policyLIName = policyLIName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getFaceAmount() {
        return faceAmount;
    }

    public void setFaceAmount(String faceAmount) {
        this.faceAmount = faceAmount;
    }

    public String getPolExpiryDate() {
        return polExpiryDate;
    }

    public void setPolExpiryDate(String polExpiryDate) {
        this.polExpiryDate = polExpiryDate;
    }

    public String getPolIssEffDate() {
        return polIssEffDate;
    }

    public void setPolIssEffDate(String polIssEffDate) {
        this.polIssEffDate = polIssEffDate;
    }

    /**
     * For Action Policy Info in @{@link com.dlvn.mcustomerportal.common.Constant}
     * POINFO_ACTION_POLICYBENE, POINFO_ACTION_POLICYLIFEINSURED
     *
     * @return
     */
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Action POLICY_DETAIL
     */
    public String getPolicyID() {
        return policyID;
    }

    public void setPolicyID(String policyID) {
        this.policyID = policyID;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getPolicyStatus() {
        return policyStatus;
    }

    public void setPolicyStatus(String policyStatus) {
        this.policyStatus = policyStatus;
    }

    public String getPolMPremAmt() {
        return polMPremAmt;
    }

    public void setPolMPremAmt(String polMPremAmt) {
        this.polMPremAmt = polMPremAmt;
    }

    public String getPolSndryAmt() {
        return polSndryAmt;
    }

    public void setPolSndryAmt(String polSndryAmt) {
        this.polSndryAmt = polSndryAmt;
    }

    /**
     * Action POLICY_PAYMENT
     */
    public String getTotalDeposit() {
        return totalDeposit;
    }

    public void setTotalDeposit(String totalDeposit) {
        this.totalDeposit = totalDeposit;
    }

    public String getBasicPrem() {
        return basicPrem;
    }

    public void setBasicPrem(String basicPrem) {
        this.basicPrem = basicPrem;
    }

    public String getExcessPrem() {
        return excessPrem;
    }

    public void setExcessPrem(String excessPrem) {
        this.excessPrem = excessPrem;
    }

    /**
     * Action for POLICY_ANN
     */
    public String getPolAccountValue() {
        return polAccountValue;
    }

    public void setPolAccountValue(String polAccountValue) {
        this.polAccountValue = polAccountValue;
    }

    public String getAllocateMPrem() {
        return allocateMPrem;
    }

    public void setAllocateMPrem(String allocateMPrem) {
        this.allocateMPrem = allocateMPrem;
    }

    public String getACFIP() {
        return aCFIP;
    }

    public void setACFIP(String aCFIP) {
        this.aCFIP = aCFIP;
    }

    public String getLoyaltyBonus() {
        return loyaltyBonus;
    }

    public void setLoyaltyBonus(String loyaltyBonus) {
        this.loyaltyBonus = loyaltyBonus;
    }

    public String getTotalDeduct() {
        return totalDeduct;
    }

    public void setTotalDeduct(String totalDeduct) {
        this.totalDeduct = totalDeduct;
    }

    public String getNetWithDrawal() {
        return netWithDrawal;
    }

    public void setNetWithDrawal(String netWithDrawal) {
        this.netWithDrawal = netWithDrawal;
    }

    public String getWdChargeAmt() {
        return wdChargeAmt;
    }

    public void setWdChargeAmt(String wdChargeAmt) {
        this.wdChargeAmt = wdChargeAmt;
    }

    public String getPrincIntAmt() {
        return princIntAmt;
    }

    public void setPrincIntAmt(String princIntAmt) {
        this.princIntAmt = princIntAmt;
    }

    /**
     * Action Agent
     */
    public String getPrimaryAgent() {
        return primaryAgent;
    }

    public void setPrimaryAgent(String primaryAgent) {
        this.primaryAgent = primaryAgent;
    }

    public String getServAgent() {
        return servAgent;
    }

    public void setServAgent(String servAgent) {
        this.servAgent = servAgent;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
     * Action Policy Client
     */
    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }
}
